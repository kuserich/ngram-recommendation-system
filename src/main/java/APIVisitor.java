import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.blocks.*;
import cc.kave.commons.model.ssts.declarations.*;
import cc.kave.commons.model.ssts.expressions.assignable.*;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.references.*;
import cc.kave.commons.model.ssts.statements.*;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

import java.util.Set;

public class APIVisitor implements ISSTNodeVisitor<Set<ITypeName>, ITypeName> {
    
    @Override
    public ITypeName visit(ISST isst, Set<ITypeName> iTypeNames) {
        System.out.println("ISST");
        return null;
    }

    @Override
    public ITypeName visit(IDelegateDeclaration iDelegateDeclaration, Set<ITypeName> iTypeNames) {
        System.out.println("IDelegateDeclaration");
        System.out.println(iDelegateDeclaration.getName());
        return null;
    }

    @Override
    public ITypeName visit(IEventDeclaration iEventDeclaration, Set<ITypeName> iTypeNames) {
        System.out.println("IEventDeclaration");
        System.out.println(iEventDeclaration.getName());
        return null;
    }

    @Override
    public ITypeName visit(IFieldDeclaration iFieldDeclaration, Set<ITypeName> iTypeNames) {
        System.out.println("IFieldDeclaration");
        System.out.println(iFieldDeclaration.getName());
        return null;
    }

    @Override
    public ITypeName visit(IMethodDeclaration iMethodDeclaration, Set<ITypeName> iTypeNames) {
        System.out.println("IMethodDeclaration");
        System.out.println(iMethodDeclaration.getName());
        iMethodDeclaration.getBody().forEach(s -> System.out.println(s.toString()));
        System.out.println(iMethodDeclaration.isEntryPoint());
        return null;
    }

    @Override
    public ITypeName visit(IPropertyDeclaration iPropertyDeclaration, Set<ITypeName> iTypeNames) {
        System.out.println("IPropertyDeclaration");
        System.out.println(iPropertyDeclaration.getName());
        iPropertyDeclaration.getGet().forEach(s -> System.out.println(s.toString()));
        iPropertyDeclaration.getSet().forEach(s -> System.out.println(s.toString()));
        return null;
    }

    @Override
    public ITypeName visit(IVariableDeclaration iVariableDeclaration, Set<ITypeName> iTypeNames) {
        System.out.println("IVariableDeclaration");
        // TODO: is this for new Class?
        // TODO: TypeName three entries if not local?
        // TODO: identifier if library second?
        System.out.println(iVariableDeclaration.getType()); // TODO: <- this has many methods
        System.out.println(iVariableDeclaration.getReference());
        System.out.println(iVariableDeclaration.isMissing());
        return null;
    }

    @Override
    public ITypeName visit(IAssignment iAssignment, Set<ITypeName> iTypeNames) {
        System.out.println("IAssignment");
        System.out.println(iAssignment.getExpression());
        return null;
    }

    @Override
    public ITypeName visit(IBreakStatement iBreakStatement, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IContinueStatement iContinueStatement, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IExpressionStatement iExpressionStatement, Set<ITypeName> iTypeNames) {
        return null; // TODO: identifier if library second?
    }

    @Override
    public ITypeName visit(IGotoStatement iGotoStatement, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(ILabelledStatement iLabelledStatement, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IReturnStatement iReturnStatement, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IThrowStatement iThrowStatement, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IEventSubscriptionStatement iEventSubscriptionStatement, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IDoLoop iDoLoop, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IForEachLoop iForEachLoop, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IForLoop iForLoop, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IIfElseBlock iIfElseBlock, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(ILockBlock iLockBlock, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(ISwitchBlock iSwitchBlock, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(ITryBlock iTryBlock, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IUncheckedBlock iUncheckedBlock, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IUnsafeBlock iUnsafeBlock, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IUsingBlock iUsingBlock, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IWhileLoop iWhileLoop, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IBinaryExpression iBinaryExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(ICastExpression iCastExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(ICompletionExpression iCompletionExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IComposedExpression iComposedExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IIfElseExpression iIfElseExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IIndexAccessExpression iIndexAccessExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IInvocationExpression iInvocationExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(ILambdaExpression iLambdaExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(ITypeCheckExpression iTypeCheckExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IUnaryExpression iUnaryExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(ILoopHeaderBlockExpression iLoopHeaderBlockExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IConstantValueExpression iConstantValueExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(INullExpression iNullExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IReferenceExpression iReferenceExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IEventReference iEventReference, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IFieldReference iFieldReference, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IIndexAccessReference iIndexAccessReference, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IMethodReference iMethodReference, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IPropertyReference iPropertyReference, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IVariableReference iVariableReference, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IUnknownReference iUnknownReference, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IUnknownExpression iUnknownExpression, Set<ITypeName> iTypeNames) {
        return null;
    }

    @Override
    public ITypeName visit(IUnknownStatement iUnknownStatement, Set<ITypeName> iTypeNames) {
        return null;
    }
}
