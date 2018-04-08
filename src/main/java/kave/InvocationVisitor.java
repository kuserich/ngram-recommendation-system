package kave;

import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;

public class InvocationVisitor extends AbstractTraversingNodeVisitor<Void, Void> {

	@Override
	public Void visit(IInvocationExpression expr, Void context) {
		System.out.printf("found invoaction of %s\n", expr.getMethodName().getName());
		return null;
	}
}
