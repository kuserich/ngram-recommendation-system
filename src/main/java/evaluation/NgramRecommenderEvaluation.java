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
import com.google.common.collect.Lists;
import ngram.NgramRecommenderClient;
import opennlp.tools.util.StringList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class NgramRecommenderEvaluation {
    private static String modelsDir = "models/";
    private static Set<String> inputFiles = new Directory(modelsDir).findFiles(s -> s.endsWith(".xml"));
    private static int coutner = 0;


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
                //System.out.println("Proposals: "+proposals.size());

                if (selections.size() > 0) {
                    // Last of selection is selected event

                    String name = selections.get(selections.size() - 1).getProposal().getName().toString();


                    if (!name.contains("LocalVariableName")) {
                        /*if (ce.context.getTypeShape().getMethodHierarchies().iterator().hasNext()) {

                            IMemberHierarchy<IMethodName> entry = ce.context.getTypeShape().getMethodHierarchies().iterator().next();
                            String identifier = entry.getElement().getDeclaringType().getNamespace().getIdentifier();


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
                                testWithModel(ns, type, operation);
                                System.out.println("\n");
                                System.out.println("************************************************************************");
                            }
                        }*/

                        System.out.println("************ Selection Event from the User ************");

                        String selection = selections.get(selections.size() - 1).getProposal().getName().getIdentifier();
                        System.out.println(selection);

                        System.out.printf("getname %s%n", selections.get(selections.size() - 1).getProposal().getName().getIdentifier());
                        System.out.printf("getname %s%n", selections.get(selections.size() - 1).getProposal().getName().getIdentifier());

                        System.out.println("==================================================================");

                    }
                    //System.out.println("Kontext " +ce.context.getSST().getEntryPoints());


                    //System.out.println("Events: " + ce.context.getSST().getEvents());
//                    APISentenceTree asp = new APISentenceTree();

                    //for (IMethodDeclaration method : ce.context.getSST().getMethods()) {
                    //method.accept(new APIVisitor(), asp);

                    //if (asp.getTokens().size() > 0 || asp.getBranches().size() > 0) {
                    //  System.out.println(asp);
                    //}
                    // }
                }
            }
            // ...and access the special context for this kind of event
//            System.out.println(ce.context);
        } else {
            // there a many different event types to process, it is recommended
            // that you browse the package to see all types and consult the
            // website for the documentation of the semantics of each event...
        }
    }

    private static void testWithModel(Set<String> ns, String type, String operation) throws IOException {

        for (String s : ns) {
            NgramRecommenderClient nrc = new NgramRecommenderClient(s);

            System.out.println("************Model Predicts Next Tokens ************");
            try {
                System.out.println("[INFO] " + nrc.query(new StringList(type + "," + operation)));

            } catch (IndexOutOfBoundsException e) {
                System.out.println(e);
            }
        }

    }

    private static boolean namespaceExists(String s) {
        boolean found = false;
        for (String entry : inputFiles) {
            if (s.length() > 0 && entry.contains(s)) {
                found = true;
            }
        }
        return found;
    }

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
