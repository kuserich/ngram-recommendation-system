package evaluation;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.*;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.*;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.statements.*;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import extractor.APISentenceTree;
import extractor.APIToken;
import extractor.APIVisitor;
import extractor.SentenceExtractor;
import util.Utilities;

import java.util.List;

public class EventVisitor extends APIVisitor {
    
    private APIToken selection;
    private boolean hasEventFired = false;
    
    public EventVisitor() {
        super();
    }
    
    public EventVisitor(APIToken selection) {
        setSelection(selection);
    }


    public APIToken getSelection() {
        return selection;
    }

    public void setSelection(APIToken selection) {
        this.selection = selection;
    }

    public boolean hasEventFired() {
        return hasEventFired;
    }

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
        if(!hasEventFired) {
            return super.visit(statement, context);
        }
        
        return null;
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
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

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
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
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
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
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
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
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
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
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
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }

    @Override
    public APIToken visit(IDoLoop statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }

    @Override
    public APIToken visit(IForEachLoop statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }

    @Override
    public APIToken visit(IForLoop statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }

    @Override
    public APIToken visit(IIfElseBlock statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }

    @Override
    public APIToken visit(ILockBlock statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }

    @Override
    public APIToken visit(ISwitchBlock statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }

    @Override
    public APIToken visit(ITryBlock statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }

    @Override
    public APIToken visit(IUncheckedBlock statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }

    @Override
    public APIToken visit(IUsingBlock statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }

    @Override
    public APIToken visit(IWhileLoop statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

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
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }
    @Override
    public APIToken visit(ICompletionExpression statement, APISentenceTree context) {
        /*System.out.println("COMPLETION EXPRESSION FROM EVENTVISITOR");
        System.out.println("\t"+statement.getToken());
        Iterable<ISSTNode> children = statement.getChildren();
        if(statement.getVariableReference() != null) {
            System.out.println("\t"+statement.getVariableReference().getIdentifier());
        }
        if(statement.getTypeReference() != null) {
            System.out.println("\t"+statement.getTypeReference().getFullName());
        }*/
//        statement.getVariableReference().accept(this, context);
        context.addToken(selection);
        this.hasEventFired = true;
        return null;
    }

    @Override
    public APIToken visit(IComposedExpression statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }

    @Override
    public APIToken visit(IIfElseExpression statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }

    /**
     *
     * @param statement
     * @param context
     * @return
     */
    @Override
    public APIToken visit(IInvocationExpression statement, APISentenceTree context) {
        String tokenNamespace = statement.getMethodName().getDeclaringType().getNamespace().getIdentifier();
        if(!hasEventFired && tokenNamespace.equals(selection.getNamespace())) {
            return super.visit(statement, context);
        }
        return null;
    }

    @Override
    public APIToken visit(IUnaryExpression statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;
    }

    @Override
    public APIToken visit(ILoopHeaderBlockExpression statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

        return null;    }

    @Override
    public APIToken visit(ILambdaExpression statement, APISentenceTree context) {
        if(!hasEventFired) {
            return super.visit(statement, context);
        }

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
            if(!hasEventFired) {
                statement.accept(this, context);
            } else {
                return null;
            }
        }

        return null;
    }
}
