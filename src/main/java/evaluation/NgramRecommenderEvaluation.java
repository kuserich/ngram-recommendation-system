package evaluation;

import cc.kave.commons.model.events.CommandEvent;
import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.IProposal;
import cc.kave.commons.model.events.completionevents.IProposalSelection;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.utils.io.ReadingArchive;
import cc.kave.commons.utils.io.json.JsonUtils;
import com.google.common.collect.Lists;
import extractor.APISentenceTree;
import extractor.APIVisitor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

public class NgramRecommenderEvaluation {

    private static List<String> findAllUsers(String inputDirectory) {
        // This step is straight forward, as events are grouped by user. Each
        // .zip file in the dataset corresponds to one user.

        List<String> zips = Lists.newLinkedList();
        for (File f : FileUtils.listFiles(new File(inputDirectory), new String[] { "zip" }, true)) {
            zips.add(f.getAbsolutePath());
        }
        return zips;
    }

    public static void readAllEvents(String inputDirectory) {
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
    public static void readPlainEvents(String inputDirectory) {
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
    private static void process(IIDEEvent event) {
        // once you have access to the instantiated event you can dispatch the
        // type. As the events are not nested, we did not implement the visitor
        // pattern, but resorted to instanceof checks.
        if(event instanceof CompletionEvent) {
            // if the correct type is identified, you can cast it...
            CompletionEvent ce = (CompletionEvent) event;
            List<IProposal> proposals = ce.proposalCollection;
            List<IProposalSelection> selections = ce.selections;
            
            if(proposals.size() > 0) {
                System.out.println("Proposals: "+proposals.size());
                
                if(selections.size() > 0) {
                    System.out.println("Selections: "+selections.size());
                    
                    System.out.println("Events: "+ce.context.getSST().getEvents());
                    APISentenceTree asp = new APISentenceTree();
                    for(IMethodDeclaration method : ce.context.getSST().getMethods()) {
                        method.accept(new APIVisitor(), asp);
                        System.out.println(asp.toString(2));
                        if(asp.getTokens().size() > 0 || asp.getBranches().size() > 0) {
                            System.out.println("here i guess");
                        }
                    }
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
    
}
