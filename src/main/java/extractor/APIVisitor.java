package extractor;

import cc.kave.commons.model.events.completionevents.Context;
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
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import util.Utilities;

import java.util.List;

public class APIVisitor implements ISSTNodeVisitor<APISentenceTree, APIToken> {

    /**
     * Entry level declaration. 
     * 
     * {@link IMethodDeclaration} is used for entire methods. That is, it includes
     * and encapsulates a single method body with all its statements.
     * There is no information that we need to take from this statement itself but
     * we will only process its body. Hence, we return the result we receive from 
     * visiting the body.
     * 
     * (because we want to add tokens to a list/sentence and semantically, 
     * this function should return this list)
     * 
     * Notice that this function is called in {@link SentenceExtractor#processContext(Context, String)}
     * and will always receive a newly created {@link APISentenceTree} object.
     * Furthermore, this function will always call {@link #visit(List, APISentenceTree)}
     * and propagate further processing.
     *
     * TODO: {@link cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration} contains a field isEntryPoint, see what this does
     * 
     * @see SentenceExtractor#processContext(Context, String)
     *          
     * @see #visit(IInvocationExpression, APISentenceTree)
     * @see #visit(List, APISentenceTree)
     * 
     * @param statement
     *          Method declaration statement that contains the body of a method.
     *          
     * @param context
     *          newly created {@link APISentenceTree} that will be filled with the
     *          {@link IInvocationExpression} objects from the body of the method
     * 
     * @return
     *          extractor.APIToken from {@link #visit(List, APISentenceTree)}
     */
    @Override
    public APIToken visit(IMethodDeclaration statement, APISentenceTree context) {
        return this.visit(statement.getBody(), context);
    }

    /**
     * Propagates processing of the get and set statements in the property declaration
     * 
     * @param statement
     *          Property declaration
     * @param context
     *          APISentenceTree to which the get and set statements might be added
     *          
     * @return
     *          null
     */
    @Override
    public APIToken visit(IPropertyDeclaration statement, APISentenceTree context) {
        this.visit(statement.getGet(), context);
        this.visit(statement.getSet(), context);
        
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


    /**
     * Have not yet fully figured out what this is but may contain
     * {@link IInvocationExpression} and therefore must be propagated.
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

    /**
     * Propagates processing of the statement.
     * 
     * @param statement
     *          labelled statement
     * @param context
     *          APISentenceTree to which the included statement might be added
     *          
     * @return
     *          returns an APIToken if the statement is an {@link IInvocationExpression}
     *          otherwise null
     */
    @Override
    public APIToken visit(ILabelledStatement statement, APISentenceTree context) {
        return statement.getStatement().accept(this, context);
    }

    /**
     * Propagates the visitor to the expression that is returned.
     * {@link IReturnStatement} is a simple return statement and the expression
     * it returns may be of any other form and therefore potentially interesting
     * further processing (e.g. return new SomeInterestingClass())
     * 
     * @param statement
     *          simple return statement
     * @param context
     *          APISentenceTree to which the expression in the return statement
     *          might be added
     *          
     * @return
     *          APIToken if the expression includes an {@link IInvocationExpression}
     *          otherwise null
     */
    @Override
    public APIToken visit(IReturnStatement statement, APISentenceTree context) {
        return statement.getExpression().accept(this, context);
    }

    /**
     * Propagates processing of the expression in the event subscription statement.
     * 
     * @param statement
     * @param context
     * 
     * @return
     *          APIToken if the expression includes an {@link IInvocationExpression}
     *          otherwise null
     */
    @Override
    public APIToken visit(IEventSubscriptionStatement statement, APISentenceTree context) {
        return statement.getExpression().accept(this, context);
    }

    @Override
    public APIToken visit(IDoLoop statement, APISentenceTree context) {
        APIToken conditionToken = statement.getCondition().accept(this, context);
        APIToken lastValidToken = 
                conditionToken == null ?
                        context.getLastValidToken() == null ?
                                new APIToken() : context.getLastValidToken() : conditionToken;
        
        return this.visit(statement.getBody(), context.branch(lastValidToken));
    }

    @Override
    public APIToken visit(IForEachLoop statement, APISentenceTree context) {
        APIToken declarationToken = statement.getDeclaration().accept(this, context);
        APIToken lastValidToken =
                declarationToken == null ?
                        context.getLastValidToken() == null ?
                                new APIToken() : context.getLastValidToken() : declarationToken;
        
        return this.visit(statement.getBody(), context.branch(lastValidToken));
    }

    @Override
    public APIToken visit(IForLoop statement, APISentenceTree context) {
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

        return this.visit(statement.getBody(), context.branch(lastValidToken));
    }

    @Override
    public APIToken visit(IIfElseBlock statement, APISentenceTree context) {
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
        return this.visit(statement.getBody(), context);
    }
    
    @Override
    public APIToken visit(ISwitchBlock statement, APISentenceTree context) {
        // TODO: branch
        // TODO: technically, we should only branch if there is a break statement within the switch block
        APIToken lastValidToken = context.getLastValidToken() == null ?
                new APIToken() : context.getLastValidToken();
        
        for(ICaseBlock caseBlock : statement.getSections()) {
            this.visit(caseBlock.getBody(), context.branch(lastValidToken));
        }
        
        return this.visit(statement.getDefaultSection(), context.branch(lastValidToken));
    }

    @Override
    public APIToken visit(ITryBlock statement, APISentenceTree context) {
        // TODO: branch
        this.visit(statement.getBody(), context);
        this.visit(statement.getFinally(), context);
        
        for(ICatchBlock catchBlock : statement.getCatchBlocks()) {
            APISentenceTree newContext = context.branch(new APIToken());
            this.visit(catchBlock.getBody(), newContext);
            this.visit(statement.getFinally(), newContext);
        }
        return null;
    }

    @Override
    public APIToken visit(IUncheckedBlock statement, APISentenceTree context) {
        return this.visit(statement.getBody(), context);
    }

    @Override
    public APIToken visit(IUsingBlock statement, APISentenceTree context) {
        return this.visit(statement.getBody(), context);
    }

    @Override
    public APIToken visit(IWhileLoop statement, APISentenceTree context) {
        APIToken conditionToken = statement.getCondition().accept(this, context);
        APIToken lastValidToken = 
                conditionToken == null ?
                        context.getLastValidToken() == null?
                                new APIToken() : context.getLastValidToken() : conditionToken;
        
        return this.visit(statement.getBody(), context.branch(lastValidToken));
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
        return null;
    }

    @Override
    public APIToken visit(IComposedExpression statement, APISentenceTree context) {
        // TODO: what is this?
        return null;
    }

    @Override
    public APIToken visit(IIfElseExpression statement, APISentenceTree context) {
        APIToken conditionToken = statement.getCondition().accept(this, context);

        APIToken lastValidToken =
                conditionToken == null ?
                        context.getLastValidToken() == null?
                                new APIToken() : context.getLastValidToken() : conditionToken;
        
        statement.getThenExpression().accept(this, context.branch(lastValidToken));
        return statement.getElseExpression().accept(this, context.branch(lastValidToken));
    }
    
    /**
     * 
     * @param statement
     * @param context
     * @return
     */
    @Override
    public APIToken visit(IInvocationExpression statement, APISentenceTree context) {
        // TODO: check in what order we should process an invocation statement
        for(ISimpleExpression parameter : statement.getParameters()) {
            parameter.accept(this, context);
        }
        
        IMethodName methodName = statement.getMethodName();
        if(methodName.getDeclaringType().getAssembly().isLocalProject() 
                || Utilities.hasIllegalMethodName(methodName)) {
            return null;
        }
        
        
        APIToken apiToken = new APIToken(methodName.getIdentifier());

        // it is very important that we add our tokens within the visit methods.
        // particularly, we will only add any tokens in this visit method as we
        // are only interested in invocation expressions.
        // We are unable to use the return statement of a single visit method
        // to return the tokens that we want to add because i) some visit methods
        // would need to return multiple results ii) it would be more appropriate
        // for some visit functions to return entire extractor.APISentenceTree objects.
        // We cannot use different return types and hence omit the strategy of
        // using return types.
        context.addToken(apiToken);
        return apiToken;
    }

    @Override
    public APIToken visit(IUnaryExpression statement, APISentenceTree context) {
        return statement.getOperand().accept(this, context);
    }

    @Override
    public APIToken visit(ILoopHeaderBlockExpression statement, APISentenceTree context) {
        return this.visit(statement.getBody(), context);
    }

    @Override
    public APIToken visit(ILambdaExpression statement, APISentenceTree context) {
        // TODO: perhaps as new sentencetree
        return this.visit(statement.getBody(), context);
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
     * @see #visit(IMethodDeclaration, APISentenceTree)     
     * @see #visit(IInvocationExpression, APISentenceTree) 
     * 
     * @param body
     *          list of statements
     *          
     * @param context
     *          {@link APISentenceTree} that will be filled with the
     *          {@link IInvocationExpression} statements included in the body
     */
    public APIToken visit(List<IStatement> body, APISentenceTree context) {
        for(IStatement statement : body) {
            statement.accept(this, context);
        }
        return null;
    }
    
    // Unused visits

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

    @Override
    public APIToken visit(ITypeCheckExpression statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IUnsafeBlock statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IIndexAccessExpression statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IThrowStatement statement, APISentenceTree context) {
        return null;
    }

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

    @Override
    public APIToken visit(IBreakStatement statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IContinueStatement statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IGotoStatement statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IMethodReference statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IPropertyReference statement, APISentenceTree context) {
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
        return null;
    }

    @Override
    public APIToken visit(IConstantValueExpression statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(INullExpression statement, APISentenceTree context) {
        return null;
    }

    @Override
    public APIToken visit(IIndexAccessReference statement, APISentenceTree context) {
        return null;
    }
    
}
