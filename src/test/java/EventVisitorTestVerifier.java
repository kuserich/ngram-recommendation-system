import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import evaluation.EventVisitor;
import extractor.APISentenceTree;

import static org.junit.Assert.assertEquals;

public class EventVisitorTestVerifier {
    private EventVisitor _node;
    private APISentenceTree _context;

    public EventVisitorTestVerifier(EventVisitor node, APISentenceTree context) {
        _node = node;
        _context = context;
    }

    public void verify(Object obj) {
        EventVisitor ev = new EventVisitor();
        //EventVisitor visitor = mock(EventVisitor.class);
        //_node.visit
        //_node.accept(visitor, _context);
        if (obj instanceof ICompletionExpression) {
            ev.visit((ICompletionExpression) obj, _context);
            assertEquals(ev.hasEventFired(), true);
        } else if (obj instanceof InvocationExpression) {
            System.out.println("invocation Expression");
        } else
            throw new RuntimeException();

    }
}
