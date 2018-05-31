
import evaluation.EvaluationVisitor;
import evaluation.EventVisitor;
import extractor.APISentenceTree;

public class EventVisitorTestHelper {

    public static EventVisitorTestVerifier accept(EventVisitor node, APISentenceTree context) {
        return new EventVisitorTestVerifier(node, context);
    }

    public static EventVisitorTestVerifier accept(EvaluationVisitor node, APISentenceTree context) {
        return new EventVisitorTestVerifier(node, context);
    }
}
