import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.impl.blocks.*;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.*;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.statements.*;
import evaluation.EvaluationVisitor;
import evaluation.EventVisitor;
import extractor.APISentenceTree;
import extractor.APIToken;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventVisitorTest {

    @Test
    public void EventVisitorGetSelection() {
        APIToken t1 = new APIToken("[?] [Some.Namespace.Type, Some.Namespace].Operation1()");
        EventVisitor ev = new EventVisitor();
        ev.setSelection(t1);
        assertEquals(ev.getSelection(), t1);
    }

    @Test
    public void EventVisitorConstructor() {
        APIToken t1 = new APIToken("[?] [Some.Namespace.Type, Some.Namespace].Operation1()");
        EventVisitor ev = new EventVisitor(t1);
        assertEquals(ev.getSelection(), t1);
    }

    @Test
    public void EventVisitorSetSelection () {
        APIToken t1 = new APIToken("[?] [Some.Namespace.Type, Some.Namespace].Operation1()");
        EventVisitor ev1 = new EventVisitor(t1);
        EventVisitor ev2 = new EventVisitor();
        ev2.setSelection(t1);
        assertEquals(ev1.getSelection(), ev2.getSelection());
    }

    @Test
    public void EventVisitorHasEventFired() {
        EventVisitor ev1 = new EventVisitor();
        assertEquals(ev1.hasEventFired(), false);

    }


    @Test
    public void EventVisitorCompletionExpressionHasEventFired() {
        EventVisitor sut = new EventVisitor();


        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new CompletionExpression());
    }

    @Test
    public void EventVisitorHasIInvocationExpression() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new InvocationExpression());
    }

    @Test
    public void EventVisitorHasIMethodDeclaration() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new MethodDeclaration() {
        });
    }

    @Test
    public void EventVisitorHasIPropertyDeclaration() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new PropertyDeclaration() {
        });
    }

    @Test
    public void EventVisitorHasIAssignment() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new Assignment() {
        });
    }

    @Test
    public void EventVisitorHasIExpressionStatement() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new ExpressionStatement());
    }

    @Test
    public void EventVisitorHasILabelledStatement() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new LabelledStatement());
    }

    @Test
    public void EventVisitorHasIReturnStatement() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new ReturnStatement());
    }

    @Test
    public void EventVisitorHasIEventSubscriptionStatement() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new EventSubscriptionStatement());
    }

    @Test
    public void EventVisitorHasIDoLoop() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new DoLoop());
    }

    @Test
    public void EventVisitorHasIForEachLoop() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new ForEachLoop());
    }

    @Test
    public void EventVisitorHasIForLoop() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new ForLoop());
    }

    @Test
    public void EventVisitorHasIIfElseBlock() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new IfElseBlock());
    }

    @Test
    public void EventVisitorHasILockBlock() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new LockBlock());
    }

    @Test
    public void EventVisitorHasISwitchBlock() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new SwitchBlock());
    }

    @Test
    public void EventVisitorHasITryBlock() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new TryBlock());
    }

    @Test
    public void EventVisitorHasIUncheckedBlock() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new UncheckedBlock());
    }

    @Test
    public void EventVisitorHasIUsingBlock() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new UsingBlock());
    }

    @Test
    public void EventVisitorHasIWhileLoop() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new WhileLoop());
    }

    @Test
    public void EventVisitorHasIBinaryExpression() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new BinaryExpression());
    }

    @Test
    public void EventVisitorHasIComposedExpression() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new ComposedExpression());
    }

    @Test
    public void EventVisitorHasIIfElseExpression() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new IfElseExpression());
    }

    @Test
    public void EventVisitorHasIUnaryExpression() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new UnaryExpression());
    }

    @Test
    public void EventVisitorHasILoopHeaderBlockExpression() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new LoopHeaderBlockExpression());
    }

    @Test
    public void EventVisitorHasILambdaExpression() {
        EventVisitor sut = new EventVisitor();

        EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new LambdaExpression());
    }
   // @Test
   // public void EventVisitorHasIStatementList() {
    //    EventVisitor sut = new EventVisitor();

   //     EventVisitorTestHelper.accept(sut, setupTokenHelper()).verify(new LinkedList<IStatement>());
   // }

    @Test
    public void visitIIDEEvent() {
        EvaluationVisitor ev = new EvaluationVisitor();

        EventVisitorTestHelper.accept(ev, setupTokenHelper()).verify(new LambdaExpression());

    }



    private APISentenceTree setupTokenHelper () {
        APISentenceTree asp = new APISentenceTree();
        APIToken t1 = new APIToken("[?] [Some.Namespace.Type, Some.Namespace].Operation1()");
        asp.addToken(t1);
        return asp;
    }


}
