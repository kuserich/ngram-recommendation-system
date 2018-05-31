import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.*;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.*;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.statements.*;
import evaluation.EvaluationVisitor;
import evaluation.EventVisitor;
import extractor.APISentenceTree;
import extractor.APIToken;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EventVisitorTestVerifier {
    private EventVisitor _node;
    private APISentenceTree _context;

    public EventVisitorTestVerifier(EventVisitor node, APISentenceTree context) {
        _node = node;
        _context = context;
    }

    public EventVisitorTestVerifier(EvaluationVisitor node, APISentenceTree context) {
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
        } else if (obj instanceof IInvocationExpression) {

            APIToken t1 = new APIToken();
            ev.setSelection(t1);
            assertEquals(ev.visit((IInvocationExpression) obj, _context), null);

        }
        else if (obj instanceof IMethodDeclaration)
            assertEquals(ev.visit((IMethodDeclaration) obj, _context), null);

        else if (obj instanceof IPropertyDeclaration)
            assertEquals(ev.visit((IPropertyDeclaration) obj, _context), null);

        else if (obj instanceof IAssignment)
            assertEquals(ev.visit((IAssignment) obj, _context), null);

        else if (obj instanceof IExpressionStatement)
            assertEquals(ev.visit((IExpressionStatement) obj, _context), null);

         else if (obj instanceof ILabelledStatement)
            assertEquals(ev.visit((ILabelledStatement) obj, _context), null);

         else if (obj instanceof IReturnStatement)
            assertEquals(ev.visit((IReturnStatement) obj, _context), null);

         else if (obj instanceof IEventSubscriptionStatement)
            assertEquals(ev.visit((IEventSubscriptionStatement) obj, _context), null);

         else if (obj instanceof IDoLoop)
            assertEquals(ev.visit((IDoLoop) obj, _context), null);

         else if (obj instanceof IForEachLoop)
            assertEquals(ev.visit((IForEachLoop) obj, _context), null);

         else if (obj instanceof IForLoop)
            assertEquals(ev.visit((IForLoop) obj, _context), null);

         else if (obj instanceof IIfElseBlock)
            assertNotEquals(ev.visit((IIfElseBlock) obj, _context), null);

         else if (obj instanceof ILockBlock)
            assertEquals(ev.visit((ILockBlock) obj, _context), null);

         else if (obj instanceof ISwitchBlock)
            assertEquals(ev.visit((ISwitchBlock) obj, _context), null);

         else if (obj instanceof ITryBlock)
            assertEquals(ev.visit((ITryBlock) obj, _context), null);

         else if (obj instanceof IUncheckedBlock)
            assertEquals(ev.visit((IUncheckedBlock) obj, _context), null);

         else if (obj instanceof IUsingBlock)
            assertEquals(ev.visit((IUsingBlock) obj, _context), null);

         else if (obj instanceof IWhileLoop)
            assertEquals(ev.visit((IWhileLoop) obj, _context), null);

         else if (obj instanceof IBinaryExpression)
            assertEquals(ev.visit((IBinaryExpression) obj, _context), null);

         else if (obj instanceof IComposedExpression)
            assertEquals(ev.visit((IComposedExpression) obj, _context), null);

         else if (obj instanceof IIfElseExpression)
            assertEquals(ev.visit((IIfElseExpression) obj, _context), null);

         else if (obj instanceof IUnaryExpression)
            assertEquals(ev.visit((IUnaryExpression) obj, _context), null);

         else if (obj instanceof ILoopHeaderBlockExpression)
            assertEquals(ev.visit((ILoopHeaderBlockExpression) obj, _context), null);

         else if (obj instanceof ILambdaExpression)
            assertEquals(ev.visit((ILambdaExpression) obj, _context), null);

         else if (obj instanceof ICompletionEvent) {
            EvaluationVisitor eval = new EvaluationVisitor();
            assertEquals(eval.visit((ICompletionEvent) obj, null), null);
        }

         else if (obj instanceof IIDEEvent) {
            EvaluationVisitor eval = new EvaluationVisitor();
            assertEquals(eval.visit((IIDEEvent) obj, null), null);
        }


         else if (obj instanceof IStatement) {
             System.out.println("istatement");
        }

        else
            throw new RuntimeException();

    }
}
