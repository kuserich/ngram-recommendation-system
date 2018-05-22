package evaluation;

import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.IProposal;
import cc.kave.commons.model.events.completionevents.IProposalSelection;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.utils.io.ReadingArchive;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.datastructures.Tuple;
import com.google.common.collect.Lists;
import ngram.NgramRecommenderClient;
import opennlp.tools.util.StringList;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class NgramRecommenderEvaluation {
    private static String modelsDir = "models/";
    private static Set<String> inputFiles = new Directory(modelsDir).findFiles(s -> s.endsWith(".xml"));
    private static int NameSpaceCoutner = 0;
    private static int possibleNameSpaceCounter = 0;

    private static int correctlyPredicted = 0;
    private static int allPredictions = 0;


    private static List<String> findAllUsers(String inputDirectory) {
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
            }
            ra.close();
        }
        System.out.println("************ Number of found NameSpaces ************");
        System.out.println("[INFO] Possible Namespaces " + possibleNameSpaceCounter);
        System.out.println("[INFO] Hitted Namespaces " + NameSpaceCoutner);
        System.out.println("[INFO] #Correctly Predicted " + correctlyPredicted);
        System.out.println("[INFO] All Predictions " + allPredictions);
        System.out.println("[INFO] Precision " + correctlyPredicted / allPredictions);
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

    /**
     * 4: Processing events
     */
    private static void process(IIDEEvent event) throws IOException {
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

                    String selected = selections.get(selections.size() - 1).getProposal().getName().getIdentifier();

                    // Remove the events that dont matter from my view
                    if (!selected.contains("LocalVariableName") && !selected.contains("???")) {
                        if (ce.context.getTypeShape().getMethodHierarchies().iterator().hasNext()) {

                            IMemberHierarchy<IMethodName> entry = ce.context.getTypeShape().getMethodHierarchies().iterator().next();
                            String identifier = entry.getElement().getDeclaringType().getNamespace().getIdentifier();

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
                                    testWithModel(ns, type, operation, tokens.toString());


                                } else {

                                    // If not, the contents of the model output and proposal are string compared

                                    System.out.println("************ Actuall Output ************");
                                    System.out.println("[INFO] Unparsed: " + selected);
                                    testWithModel(ns, type, operation, selected);

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

    /**
     * Compare the selected with the model
     */
    private static void testWithModel(Set<String> ns, String type, String operation, String selected) throws IOException {

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

    /**
     * Check if namespace exists
     *
     * @param s
     * @return
     */
    private static boolean namespaceExists(String s) {
        boolean found = false;
        for (String entry : inputFiles) {
            if (s.length() > 0 && entry.contains(s)) {
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
