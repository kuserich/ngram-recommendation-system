import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.*;
import cc.kave.commons.model.ssts.declarations.*;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.*;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.references.*;
import cc.kave.commons.model.ssts.statements.*;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

import java.util.List;

public class APIVisitor implements ISSTNodeVisitor<APISentenceTree, APIToken> {
    
    @Override
    public APIToken visit(ISST isst, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IDelegateDeclaration statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IEventDeclaration statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IFieldDeclaration statement, APISentenceTree context) {
        return null;
    }

    /**
     * Entry level declaration. // TODO: DANGEROUS AS SEMANTICS ARE DIFFERENT
     * // TODO: {@link cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration} contains a field isEntryPoint, see what this does
     * 
     * {@link IMethodDeclaration} is used for entire methods. That is, it includes
     * and encapsulates a single body with all its statements.
     * There is no information that we need to take from this statement itself but
     * process its body. Hence, we return the result we receive from visiting the body.
     * 
     * TODO: perhaps in the future we will move this outside the visitor
     * (because we want to add tokens to a list/sentence and semantically, 
     * this function should return this list)
     * 
     * Problem: at the moment we return {@link APIToken} objects whereas here
     * we would return an {@link APISentenceTree}.
     * 
     * @param statement
     * @param context
     * 
     * @return
     */
    @Override
    public APIToken visit(IMethodDeclaration statement, APISentenceTree context) {
        // DO NOT USE THIS
        return this.visit(statement.getBody(), context);
    }

    @Override
    public APIToken visit(IPropertyDeclaration statement, APISentenceTree context) {
        // TODO: what is this?
        this.visit(statement.getGet(), context);
        this.visit(statement.getSet(), context);
        return null;
    }

    /**
     * Scratch stateStoreSpy = new Scratch();
     * ---------------------   <--- this part is a VariableDeclaration,
     *                              hence, no information gain (i.e. the
     *                              actual information will be processed
     *                              in the right side declaration)
     * @param statement
     * @param context
     * 
     * @see #visit(IAssignment, APISentenceTree)
     *          contains the right side information in raw form.
     *          That is, {@link IAssignment} itself does not store any 
     *          useful information but will propagate processing (such
     *          that we can capture the information on the right side
     *          of the declaration, if needed - which is the case for
     *          methods and object instatiations only)
     * 
     * @return null
     *          this information is not needed
     */
    @Override
    public APIToken visit(IVariableDeclaration statement, APISentenceTree context) {
        return null;
    }

    /**
     * Propagates processing to the statement or expression used in 
     * the assignment. This expression can be of any type.
     * 
     * Scratch stateStoreSpy = new Scratch();
     *                         --------------  <--- contains this part
     * 
     * @param statement
     * @param context
     * 
     * @see #visit(IInvocationExpression, APISentenceTree)
     *          if the statement passed to this function contains useful
     *          information, it will be a {@link IInvocationExpression}.
     * @return
     *          return the result of propagating the expression contained
     */
    @Override
    public APIToken visit(IAssignment statement, APISentenceTree context) {
        return statement.getExpression().accept(this, context);
    }

    @Override
    public APIToken visit(IBreakStatement statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IContinueStatement statement, APISentenceTree context) {
        return null;
    }


    /**
     * Have not yet fully figured out what this is but may contain {@link IInvocationExpression}
     * and therefore must be propagated.
     * 
     * @param statement
     * @param context
     * 
     * @return
     *          return the result of propagating the expression contained
     */
    @Override
    public APIToken visit(IExpressionStatement statement, APISentenceTree context) {
        return statement.getExpression().accept(this, context);
    }

    @Override
    public APIToken visit(IGotoStatement statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(ILabelledStatement statement, APISentenceTree context) {
        statement.getStatement().accept(this, context);
        return null;
    }

    /**
     * Propagates the visitor to the expression that is returned.
     * {@link IReturnStatement} is a simple return statement and the expression
     * it returns may be of any other form and therefore potentially interesting
     * further processing (e.g. return new SomeInterestingClass())
     * 
     * @param statement
     * @param context
     * @return
     */
    @Override
    public APIToken visit(IReturnStatement statement, APISentenceTree context) {
        statement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IThrowStatement statement, APISentenceTree context) {
        statement.getReference().accept(this, context);
//        statement.getReference().accept(this, context); 
        return null;
    }

    @Override
    public APIToken visit(IEventSubscriptionStatement statement, APISentenceTree context) {
        statement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IDoLoop statement, APISentenceTree context) {
        // TODO: branch
        statement.getCondition().accept(this, context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IForEachLoop statement, APISentenceTree context) {
        // TODO: branch
        statement.getDeclaration().accept(this, context);
        statement.getLoopedReference().accept(this, context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IForLoop statement, APISentenceTree context) {
        // TODO: branch
        APIToken initToken = this.visit(statement.getInit(), context);
        APIToken conditionToken = statement.getCondition().accept(this, context);
        APIToken stepToken = this.visit(statement.getStep(), context);
        
        APIToken lastValidToken = 
                initToken == null ? 
                        conditionToken == null ?
                                stepToken == null ? 
                                        context.getLastValidToken() == null ?
                                        new APIToken() : context.getLastValidToken() 
                                            : stepToken : conditionToken : initToken;

        this.visit(statement.getBody(), context.branch(lastValidToken));
        return lastValidToken;
    }

    @Override
    public APIToken visit(IIfElseBlock statement, APISentenceTree context) {
        // TODO: branch
        APIToken conditionToken = statement.getCondition().accept(this, context);
        APIToken lastValidToken = 
                conditionToken == null ? 
                        context.getLastValidToken() == null ? 
                                new APIToken() : context.getLastValidToken() : conditionToken;
    
        this.visit(statement.getThen(), context.branch(lastValidToken));
        this.visit(statement.getElse(), context.branch(lastValidToken));
        
        return lastValidToken;
    }

    @Override
    public APIToken visit(ILockBlock statement, APISentenceTree context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(ISwitchBlock statement, APISentenceTree context) {
        // TODO: branch
        statement.getReference().accept(this, context);
        this.visit(statement.getDefaultSection(), context);

        for(ICaseBlock caseBlock : statement.getSections()) {
            this.visit(caseBlock.getBody(), context);
        }

        return null;
    }

    @Override
    public APIToken visit(ITryBlock statement, APISentenceTree context) {
        // TODO: branch
        this.visit(statement.getBody(), context);
        this.visit(statement.getFinally(), context);
        for(ICatchBlock catchBlock : statement.getCatchBlocks()) {
            this.visit(catchBlock.getBody(), context);
        }
        return null;
    }

    @Override
    public APIToken visit(IUncheckedBlock statement, APISentenceTree context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IUnsafeBlock statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IUsingBlock statement, APISentenceTree context) {
        // TODO: branch???
        this.visit(statement.getBody(), context);
        statement.getReference().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IWhileLoop statement, APISentenceTree context) {
        // TODO: branch
        statement.getCondition().accept(this, context);
        this.visit(statement.getBody(), context);
        return null;
    }

    /**
     * Propagates the visitor to the expressions contained in an
     * {@link IBinaryExpression} for further processing.
     * 
     * {@link IBinaryExpression} is an expression of the form
     *      XY == AB
     * such that XY or AB can be any other expression 
     * (e.g. SomeClass.staticFunction(new Whatever()) == myObj.getSomething()).
     * 
     * @param statement
     * @param context
     * @return
     */
    @Override
    public APIToken visit(IBinaryExpression statement, APISentenceTree context) {
        statement.getLeftOperand().accept(this, context);
        statement.getRightOperand().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(ICastExpression statement, APISentenceTree context) {
        // TODO: which one?
        return null;
    }

    @Override
    public APIToken visit(ICompletionExpression statement, APISentenceTree context) {
        statement.getVariableReference().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IComposedExpression statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IIfElseExpression statement, APISentenceTree context) {
        // TODO: branch
        statement.getCondition().accept(this, context);
        statement.getThenExpression().accept(this, context);
        statement.getElseExpression().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IIndexAccessExpression statement, APISentenceTree context) {
        // TODO: which one?
        return null;
    }

    /**
     * Returns whether the given method name contains illegal name or namespace.
     * 
     * We have noticed during development that there are a lot of method names 
     * that only contain question marks. As we cannot use this in any meaningful
     * way we have decided to remove them.
     * 
     * @see #visit(IInvocationExpression, APISentenceTree)
     *          the function that uses {@link #hasIllegalMethodName(IMethodName)}
     * 
     * @param methodName
     *          Method name of a statement (usually an {@link IInvocationExpression}
     * @return
     *          boolean statement whether the given method name has an 'illegal'
     *          name. We consider a method name 'illegal' if it contains only
     *          question marks.
     */
    private boolean hasIllegalMethodName(IMethodName methodName) {
        // we check both the name of the declaring type as well as the namespace
        return methodName.getDeclaringType().getName().equals("???")
                || methodName.getDeclaringType().getNamespace().getIdentifier().equals("???");
    }
    
    /**
     * 
     * @param statement
     * @param context
     * @return
     */
    @Override
    public APIToken visit(IInvocationExpression statement, APISentenceTree context) {
        // TODO: is this needed?
        for(ISimpleExpression parameter : statement.getParameters()) {
            parameter.accept(this, context);
        }
        
        IMethodName methodName = statement.getMethodName();
        if(!methodName.getDeclaringType().getAssembly().isLocalProject() && !hasIllegalMethodName(methodName)) {
            APIToken apiToken = new APIToken();

            if(methodName.isConstructor()) {
                apiToken.setOperation("new");
                apiToken.setInvocation("class constructor");
            } else {
                apiToken.setOperation(methodName.getName()); 
                if(methodName.getIdentifier().startsWith("static")) {
                    apiToken.setInvocation("static operation");
                } else {
                    apiToken.setInvocation("instance operation");
                }
            }
            
            apiToken.setType(methodName.getDeclaringType().getName()); // TODO: use only the name
            apiToken.setNamespace( methodName.getDeclaringType().getNamespace().getIdentifier());
            
            context.addToken(apiToken);
            return apiToken;
        }
        return null;
    }

    @Override
    public APIToken visit(ILambdaExpression statement, APISentenceTree context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(ITypeCheckExpression statement, APISentenceTree context) {
        statement.getReference().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IUnaryExpression statement, APISentenceTree context) {
        statement.getOperand().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(ILoopHeaderBlockExpression statement, APISentenceTree context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    /**
     * Not needed
     * 
     * @param statement
     * @param context
     * @return
     */
    @Override
    public APIToken visit(IConstantValueExpression statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(INullExpression statement, APISentenceTree context) {
        return null;
    }

    /**
     * Object abcd = new Object()
     *        ----  <-- contains only the reference
     *        
     * Does not yield any useful information.
     * 
     * @param statement
     * @param context
     * @return
     */
    @Override
    public APIToken visit(IReferenceExpression statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IEventReference statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IFieldReference statement, APISentenceTree context) {
        // TODO: maybe this?
        // System.out.println(statement.getFieldName());
        return null;
    }

    @Override
    public APIToken visit(IIndexAccessReference statement, APISentenceTree context) {
        statement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IMethodReference statement, APISentenceTree context) {
        // System.out.println(statement.getMethodName());
        return null;
    }

    @Override
    public APIToken visit(IPropertyReference statement, APISentenceTree context) {
        // System.out.println(statement.getPropertyName());
        return null;
    }

    @Override
    public APIToken visit(IVariableReference statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IUnknownReference statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IUnknownExpression statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IUnknownStatement statement, APISentenceTree context) {
        return null;
    }
    
    // Generic

    /**
     * Given a list of statements, this function calls the accept function for 
     * each of the statements included in the list. Such lists occur in 
     * 
     *      {@link IMethodDeclaration#getBody()},
     *      {@link IForEachLoop#getBody()},
     *      
     *      and others
     *      
     * // TODO: maybe move
     * PROBLEM: at the moment we return {@link APIToken} objects. in this method, we would rather return
     * an {@link APISentenceTree} (i.e. the context).
     * 
     * @param body
     *          list of statements
     *          
     * @param context
     */
    public APIToken visit(List<IStatement> body, APISentenceTree context) {
        for(IStatement statement : body) {
            /*APIToken token = statement.accept(this, context);
            if(token != null) {
                context.addToken(token);
            }*/
            statement.accept(this, context);
        }
        return null;
    }
}
