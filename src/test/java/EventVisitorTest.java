import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import evaluation.EventVisitor;
import extractor.APISentenceTree;
import extractor.APIToken;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventVisitorTest {

    @Test
    public void EventVisitorConstructor() {
        APIToken t1 = new APIToken();
        t1.setNamespace("System");
        t1.setOperation("M1");
        EventVisitor ev = new EventVisitor();
        ev.setSelection(t1);
        assertEquals(ev.getSelection(), t1);
    }

    @Test
    public void EventVisitorGetSelection() {
        APIToken t1 = new APIToken();
        t1.setNamespace("System");
        t1.setOperation("M1");
        EventVisitor ev = new EventVisitor(t1);
        assertEquals(ev.getSelection(), t1);
    }


    @Test
    public void EventVisitorCompletionExpressionHasEventFired() {
        EventVisitor sut = new EventVisitor();
        APISentenceTree asp = new APISentenceTree();
        APIToken t1 = new APIToken();
        t1.setNamespace("System");
        t1.setOperation("M1");
        asp.addToken(t1);

        EventVisitorTestHelper.accept(sut, asp).verify(new CompletionExpression());
    }

    @Test
    public void EventVisitorHasIInvocationExpression() {
        EventVisitor sut = new EventVisitor();
        APISentenceTree asp = new APISentenceTree();
        APIToken t1 = new APIToken();
        t1.setNamespace("System");
        t1.setOperation("M1");
        asp.addToken(t1);

        EventVisitorTestHelper.accept(sut, asp).verify(new InvocationExpression());
    }

}
