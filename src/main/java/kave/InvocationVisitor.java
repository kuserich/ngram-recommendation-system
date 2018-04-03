package kave;

import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;

import javax.sound.midi.Soundbank;
import java.util.List;

public class InvocationVisitor extends AbstractTraversingNodeVisitor<Void, Void> {

	@Override
	public Void visit(IInvocationExpression expr, Void context) {
		System.out.printf("found invoaction of %s\n", expr.getMethodName().getName());
		if(expr.getMethodName().isConstructor()) {
			System.out.println("\tand it's class yeye");
		}
		return null;
	}
	
	public Void visit(IIfElseExpression expr, Void context) {
		System.out.println("Found IfElse hehe");
		System.out.println("\tcondition is:");
		System.out.print(expr.getCondition());
		System.out.println("\tthen is:");
		System.out.print(expr.getThenExpression());
		System.out.println("\telse is:");
		System.out.print(expr.getElseExpression());
		return null;
	}
}
