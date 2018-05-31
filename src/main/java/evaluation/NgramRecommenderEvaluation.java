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
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.utils.io.ReadingArchive;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.datastructures.Tuple;
import com.google.common.collect.Lists;
import extractor.APISentenceTree;
import extractor.APIToken;
import extractor.Invocation;
import ngram.NgramRecommenderClient;
import opennlp.tools.util.StringList;
import org.apache.commons.io.FileUtils;
import util.IoHelper;
import util.Utilities;

import javax.xml.bind.SchemaOutputResolver;
import java.io.File;
import java.io.IOException;
import java.util.*;


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

    /**
     * 3: Reading the plain JSON representation
     */
    public static void readPlainEvents(String inputDirectory) throws IOException {
        // the example is basically the same as before, but...
        List<String> userZips = findAllUsers(inputDirectory);

        for (String user : userZips) {
            ReadingArchive ra = new ReadingArchive(new File(user));
            while (ra.hasNext()) {
                // ... sometimes it is easier to just read the JSON...
                String json = ra.getNextPlain();
                // .. and call the deserializer yourself.
                IIDEEvent e = JsonUtils.fromJson(json, IIDEEvent.class);
                process(e);

                // Not all event bindings are very stable already, reading the
                // JSON helps debugging possible bugs in the bindings
            }
            ra.close();
        }
    }
    
    private static void process(IIDEEvent event) throws IOException {
        // once you have access to the instantiated event you can dispatch the
        // type. As the events are not nested, we did not implement the visitor
        // pattern, but resorted to instanceof checks.
        if(event instanceof CompletionEvent) {
            // if the correct type is identified, you can cast it...
            CompletionEvent ce = (CompletionEvent) event;
            List<IProposal> proposals = ce.proposalCollection;
            List<IProposalSelection> selections = ce.selections; // positive: last

            // if there was no selection made, we can simply skip this event

            // we can skip processing if there are no proposals
            // as we compare our proposals against these
            if(selections.size() == 0 || proposals.size() == 0) {
                return;
            }
            
            // Last of selection is selected event

            // TODO: we should be able to move this to after traversal
            IProposalSelection selected = selections.get(selections.size() - 1);
            IName selectionName = selected.getProposal().getName();
            
            if(!(selectionName instanceof MethodName)) {
                return;
            }


            IMethodName methodName = (IMethodName) selectionName;
            
            if((methodName.getDeclaringType().getAssembly().isLocalProject()
                    || Utilities.hasIllegalMethodName(methodName))) {
                return;
            }

//                System.out.println("thats what we're looking for");

            String operation;
            Invocation invocation;

            if(methodName.isConstructor()) {
                operation = "new";
                invocation = Invocation.CLASS_CONSTRUCTOR;
            } else {
                operation = methodName.getName();
                if(methodName.getIdentifier().startsWith("static")) {
                    invocation = Invocation.STATIC_OPERATION;
                } else {
                    invocation = Invocation.INSTANCE_OPERATION;
                }
            }

            APIToken selectedAPIToken = new APIToken();
            selectedAPIToken.setOperation(operation);
            selectedAPIToken.setInvocation(invocation);
            selectedAPIToken.setType(methodName.getDeclaringType().getFullName());
            selectedAPIToken.setNamespace(methodName.getDeclaringType().getNamespace().getIdentifier());

            String modelFile;
            if(selectedAPIToken.getNamespace().equals("System.Collections.Generic") 
                    || (modelFile = getModelForNamespace(selectedAPIToken.getNamespace())) == null) {
//                System.out.println("[INFO]\tNo model found for "+selectedAPIToken.getNamespace());
                return;
            }
//            System.out.println("[INFO]\tModel found for " + modelFile + ".");
//            System.out.println("[INFO]\t...Processing context");
            
            ISST sst = ce.context.getSST();

            for(IMethodDeclaration md : sst.getMethods()) {
                EventVisitor visitor = new EventVisitor(selectedAPIToken);
                APISentenceTree sentenceTree = new APISentenceTree();
                md.accept(visitor, sentenceTree);
                // we only process the sentence tree if there was a CompletionExpression (hasEventFired)
                // and if there is at least one other token next to the prediction token
                if(sentenceTree.size() > 1 && visitor.hasEventFired()) {
                    List<List<APIToken>> sentences = sentenceTree.flatten();
                    for(List<APIToken> sentence : sentences) {
                        System.out.println("[INFO]\t"+sentences.size()+" sentences");
                        if(sentence.get(sentence.size()-1).getType().equals(selectedAPIToken.getType())) {
                            sentence.remove(sentence.size()-1);
                            
                            System.out.println(selectedAPIToken.toString());
                            System.out.println("\t"+sentence.toString());
                            
                            testWithModel(selectedAPIToken,
                                    sentence.subList(
                                            Math.max(0, sentence.size()-MAX_PROXIMITY),
                                            sentence.size()
                                    ));
                            break;
                        }
                    }
                    break;
                } else {
//                    System.out.println("[INFO]\tNo CompletionExpression and/or APITokens found.");
                }
            }

            System.out.println("[INFO]\tPredictions: "+correctlyPredicted + "/" + allPredictions + "  - (correct/all)");
        }
    }
    
    
    
    /**
     * 4: Processing events
     */
    private static void process_old(IIDEEvent event) throws IOException {
        // once you have access to the instantiated event you can dispatch the
        // type. As the events are not nested, we did not implement the visitor
        // pattern, but resorted to instanceof checks.
        if (event instanceof CompletionEvent) {
            // if the correct type is identified, you can cast it...
            CompletionEvent ce = (CompletionEvent) event;
            List<IProposal> proposals = ce.proposalCollection;
            List<IProposalSelection> selections = ce.selections; // positive: last

            if (proposals.size() > 0) {

                if (selections.size() > 0) {
                    // Last of selection is selected event

                    // TODO: we should be able to move this to after traversal
                    String selected = selections.get(selections.size() - 1).getProposal().getName().getIdentifier();

                    // Remove the events that dont matter from my view
                    // TODO: what does this LocalVariableName thing do?
                    if (!selected.contains("LocalVariableName") && !selected.contains("???")) {
                        // TODO: why typeShape and methodHierarchies?
                        if(ce.context.getTypeShape().getMethodHierarchies().iterator().hasNext()) {

                            // TODO: this is almost certainly wrong
                            IMemberHierarchy<IMethodName> entry = ce.context.getTypeShape().getMethodHierarchies().iterator().next();
                            String identifier = entry.getElement().getDeclaringType().getNamespace().getIdentifier();
//                            identifier = entry.getElement().getDeclaringType().getNamespace().toString();

                            possibleNameSpaceCounter = possibleNameSpaceCounter + 1;

                            if (namespaceExists(identifier)) {
                                Set<String> ns = getNamespaces(identifier);

                                String operation = ce.context.getSST().getEnclosingType().getName();
                                String type = ce.context.getSST().getEnclosingType().getFullName();

                                System.out.println("==================================================================");
                                System.out.println("************ Active Document, could be API Name ************");
                                System.out.println("[INFO] XML NAMESPACE / S " + ns.toString() + " EXIST!!");
                                System.out.println("==================================================================");
                                System.out.println("************ EnclosingType: typename of the type under edit ************");
                                System.out.println("[INFO] Type,operation: " + type + "," + operation);
                                System.out.println("\n");

                                ProposalParser bunchOfTokens = new ProposalParser(selected);
                                StringList tokens = bunchOfTokens.getTokens();

                                if (tokens != null) {

                                    //if a parseing token is present, the tokens are compared

                                    System.out.println("************ Actuall Output ************");
                                    System.out.println("[INFO] Uncleaned: " + selected);
                                    System.out.println("[INFO] Parsed Token: " + tokens);
                                    testWithModel_old(ns, type, operation, tokens.toString());


                                } else {

                                    // If not, the contents of the model output and proposal are string compared

                                    System.out.println("************ Actuall Output ************");
                                    System.out.println("[INFO] Unparsed: " + selected);
                                    testWithModel_old(ns, type, operation, selected);

                                }
                                NameSpaceCoutner = NameSpaceCoutner + 1;
                                System.out.println(NameSpaceCoutner);

                                System.out.println("\n");
                                System.out.println("\n");
                                System.out.println("************************************************************************");
                            }
                        }
                    }
                }
            }
        }
    }

    private static void testWithModel(APIToken expected, List<APIToken> sentence) {
        try {
            NgramRecommenderClient nrc = new NgramRecommenderClient(modelsDir+getModelForNamespace(expected.getNamespace()));
            Set<Tuple<IMethodName, Double>> predictions = nrc.query(Utilities.apiSentenceToStringList(sentence));
            
            if(predictions.size() == 0) {
//                System.out.println("[INFO]\tNo prediction found.");
                return;
            }
            
            System.out.println("============= PREDICTION =============");
            System.out.println(predictions);
            System.out.println("============= ACTUAL =============");
            System.out.println(expected.toString());

            IoHelper.appendClassificationToFile("evaluation.txt", 
                    expected, 
                    (APIToken) predictions.iterator().next().getFirst());
            
            if(compareStrings(predictions.toString(), expected.getType()+","+expected.getOperation())) {
                correctlyPredicted = correctlyPredicted + 1;
                allPredictions = allPredictions + 1;
                System.out.println("IS CORRECT");
            } else {
                allPredictions = allPredictions + 1;
                System.out.println("IS NOT CORRECT");
            }

            
        } catch(IOException e) {
            System.out.println("No model found for "+expected.getNamespace());
//            e.printStackTrace();
        }
    }

        /**
         * Compare the selected with the model
         */
    private static void testWithModel_old(Set<String> ns, String type, String operation, String selected) throws IOException {

        //TODO: only return the one with highest proba

        for (String s : ns) {
            NgramRecommenderClient nrc = new NgramRecommenderClient(s);

            try {
                System.out.println("[INFO] " + nrc.query(new StringList(type + "," + operation)));
                Set<Tuple<IMethodName, Double>> predictions = nrc.query(new StringList(type + "," + operation));


                if (compareStrings(predictions.toString(), selected)) {
                    correctlyPredicted = correctlyPredicted + 1;
                    allPredictions = allPredictions + 1;
                } else {
                    allPredictions = allPredictions + 1;
                }


            } catch (IndexOutOfBoundsException e) {
                System.out.println(e);
            }
        }
    }
    
    

    private static boolean compareStrings(String one, String two) {

        return one.contains(two) || two.contains(one);
    }

    private static String getModelForNamespace(String namespace) {
        for(String entry : inputFiles) {
            if(entry.toLowerCase().substring(0, entry.length()-4).equals(namespace.toLowerCase())) {
                return entry;
            }
        }
        return null;
    }
    
    private static boolean modelForNamespaceExists(String namespace) {
        return getModelForNamespace(namespace) != null;
    }
    
    /**
     * Check if namespace exists
     *
     * @param s
     * @return
     */
    private static boolean namespaceExists(String s) {
        boolean found = false;
        for (String entry : inputFiles) {
            if (s.length() > 0 && entry.toLowerCase().contains(s.toLowerCase())) {
                found = true;
            }
        }
        return found;
    }

    /**
     * Get Namespaces for each entry
     *
     * @param s
     * @return
     */
    private static Set<String> getNamespaces(String s) {
        Set<String> namespaces = new HashSet<String>();

        for (String entry : inputFiles) {
            if (s.length() > 0 && entry.contains(s)) {
                namespaces.add("models/" + entry);


            }
        }
        return namespaces;
    }

}
