
import evaluation.EventVisitor;
import extractor.APISentenceTree;

public class EventVisitorTestHelper {

    public static EventVisitorTestVerifier accept(EventVisitor node, APISentenceTree context) {
        return new EventVisitorTestVerifier(node, context);
    }
}
