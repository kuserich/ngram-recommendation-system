package evaluation;

import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.IProposal;
import cc.kave.commons.model.events.completionevents.IProposalSelection;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.utils.io.ReadingArchive;
import cc.kave.rsse.calls.datastructures.Tuple;
import com.google.common.collect.Lists;
import extractor.APISentenceTree;
import extractor.APIToken;
import ngram.NgramRecommenderClient;
import org.apache.commons.io.FileUtils;
import util.IoHelper;
import util.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class NgramRecommenderEvaluation {

    private static String modelsDir = "models/";
    private static Set<String> inputFiles = new Directory(modelsDir).findFiles(s -> s.endsWith(".xml"));
    private static int NameSpaceCoutner = 0;
    private static int possibleNameSpaceCounter = 0;

    private static final int MAX_PROXIMITY = 5;

    private static int correctlyPredicted = 0;
    private static int allPredictions = 0;
    private static int numberOfEvents = 0;


    public static List<String> findAllUsers(String inputDirectory) {
        // This step is straight forward, as events are grouped by user. Each
        // .zip file in the dataset corresponds to one user.

        List<String> zips = Lists.newLinkedList();
        for (File f : FileUtils.listFiles(new File(inputDirectory), new String[]{"zip"}, true)) {
            zips.add(f.getAbsolutePath());
        }
        return zips;
    }

    public static void readAllEvents(String inputDirectory) throws IOException {
        // each .zip file corresponds to a user
        List<String> userZips = findAllUsers(inputDirectory);

        for (String user : userZips) {
            // you can use our helper to open a file...
            ReadingArchive ra = new ReadingArchive(new File(user));
            // ...iterate over it...
            while (ra.hasNext()) {
                // ... and desrialize the IDE event.
                IIDEEvent e = ra.getNext(IIDEEvent.class);
                // afterwards, you can process it as a Java object
                process(e);
                numberOfEvents++;
            }
            ra.close();
        }
        System.out.println("************ Number of found NameSpaces ************");
        System.out.println("[INFO] Possible Namespaces " + possibleNameSpaceCounter);
        System.out.println("[INFO] Hitted Namespaces " + NameSpaceCoutner);
        System.out.println("[INFO] #Correctly Predicted " + correctlyPredicted);
        System.out.println("[INFO] All Predictions " + allPredictions);
        System.out.println("[INFO] Precision " + (double) correctlyPredicted / allPredictions);

        IoHelper.writeEvaluationResultsToFile(numberOfEvents, correctlyPredicted, allPredictions);
    }

    private static void process(IIDEEvent event) throws IOException {
        // once you have access to the instantiated event you can dispatch the
        // type. As the events are not nested, we did not implement the visitor
        // pattern, but resorted to instanceof checks.
        if (event instanceof CompletionEvent) {
            // if the correct type is identified, you can cast it...
            CompletionEvent ce = (CompletionEvent) event;
            List<IProposal> proposals = ce.proposalCollection;
            List<IProposalSelection> selections = ce.selections; // positive: last

            // if there was no selection made, we can simply skip this event

            // we can skip processing if there are no proposals
            // as we compare our proposals against these
            if (selections.size() == 0 || proposals.size() == 0) {
                return;
            }

            // Last of selection is selected event

            // TODO: we should be able to move this to after traversal
            IProposalSelection selected = selections.get(selections.size() - 1);
            IName selectionName = selected.getProposal().getName();

            if (!(selectionName instanceof MethodName)) {
                return;
            }

            IMethodName methodName = (IMethodName) selectionName;

            if ((methodName.getDeclaringType().getAssembly().isLocalProject()
                    || Utilities.hasIllegalMethodName(methodName))) {
                return;
            }

            APIToken selectedAPIToken = new APIToken(methodName.getIdentifier());

            String modelFile;
            if ((modelFile = getModelForNamespace(selectedAPIToken.getNamespace())) == null) {
//                System.out.println("[INFO]\tNo model found for "+selectedAPIToken.getNamespace());
                return;
            }
//            System.out.println("[INFO]\tModel found for " + modelFile + ".");
//            System.out.println("[INFO]\t...Processing context");

            ISST sst = ce.context.getSST();

            for (IMethodDeclaration md : sst.getMethods()) {
                EventVisitor visitor = new EventVisitor(selectedAPIToken);
                APISentenceTree sentenceTree = new APISentenceTree();
                md.accept(visitor, sentenceTree);
                // we only process the sentence tree if there was a CompletionExpression (hasEventFired)
                // and if there is at least one other token next to the prediction token
                if (sentenceTree.size() > 1 && visitor.hasEventFired()) {
                    List<List<APIToken>> sentences = sentenceTree.flatten();
                    List<List<APIToken>> sentencesWithExpectedToken = new ArrayList<>();
                    for (List<APIToken> sentence : sentences) {
//                        System.out.println("[INFO]\t"+sentences.size()+" sentences");
                        if (sentence.get(sentence.size() - 1).getType().equals(selectedAPIToken.getType())) {
                            sentence.remove(sentence.size() - 1);
                            sentencesWithExpectedToken.add(sentence);

//                            System.out.println(selectedAPIToken.toString());
//                            System.out.println("\t"+sentence.toString());


                            break;
                        }
                    }

                    if (sentencesWithExpectedToken.size() > 0) {
                        List<List<APIToken>> positives = testWithModel(modelsDir + modelFile, selectedAPIToken, sentencesWithExpectedToken);
                        System.out.println("\tfound in " + positives.size() + "/" + sentencesWithExpectedToken.size() + " (" + sentences.size() + ")");
                        for (List<APIToken> pos : positives) {
                            System.out.println("\t\t" + pos.size());
                        }
                        System.out.println("[INFO]\tPredictions: " + correctlyPredicted + "/" + allPredictions + "  - (correct/all)");
                    }

                    break;
                } else {
//                    System.out.println("[INFO]\tNo CompletionExpression and/or APITokens found.");
                }
            }
        }
    }

    private static List<List<APIToken>> testWithModel(String modelFile, APIToken expected, List<List<APIToken>> sentences) {
        try {
            NgramRecommenderClient nrc = new NgramRecommenderClient(modelFile);
            List<List<APIToken>> found = new ArrayList<>();
            for (List<APIToken> sentence : sentences) {
                Set<Tuple<IMethodName, Double>> predictions = nrc.query(
                        Utilities.apiSentenceToStringList(
                                sentence.subList(
                                        Math.max(0, sentence.size() - MAX_PROXIMITY),
                                        sentence.size())));
                if (predictions.size() > 0) {

                    IoHelper.appendPredictionToFile("evaluation.txt",
                            expected,
                            (APIToken) predictions.iterator().next().getFirst());

                    if (((APIToken) predictions.iterator().next().getFirst()).toString().equals(expected.toString())) {
                        found.add(sentence);
                    }
                }
            }
            if (found.size() > 0) {
                correctlyPredicted++;
            }

            allPredictions++;
            return found;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * Return the file path of the matching model for the given namespace.
     *
     * @param namespace namespace from an {@link IName}
     * @return model that fits the namespace in {@link #inputFiles} from {@link #modelsDir}
     */
    private static String getModelForNamespace(String namespace) {
        for (String entry : inputFiles) {
            if (entry.toLowerCase().substring(0, entry.length() - 4).equals(namespace.toLowerCase())) {
                return entry;
            }
        }
        return null;
    }
}
