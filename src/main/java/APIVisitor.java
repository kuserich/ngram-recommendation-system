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
import java.util.Set;

public class APIVisitor implements ISSTNodeVisitor<Set<APITokenSet>, APITokenSet> {
    
    @Override
    public APITokenSet visit(ISST isst, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(IDelegateDeclaration statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(IEventDeclaration statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(IFieldDeclaration statement, Set<APITokenSet> context) {
        return null;
    }

    /**
     * This will never be called and used as we are processing methods in {@link SentenceExtractor#extract(String)}
     * 
     * @param statement 
     *          ISSTNode of a method
     * @param context
     *          Set 
     * @return
     *          null
     */
    @Override
    public APITokenSet visit(IMethodDeclaration statement, Set<APITokenSet> context) {
        return this.visit(statement.getBody(), context);
    }

    @Override
    public APITokenSet visit(IPropertyDeclaration statement, Set<APITokenSet> context) {
        // TODO: what is this?
        this.visit(statement.getGet(), context);
        this.visit(statement.getSet(), context);
        return null;
    }

    @Override
    public APITokenSet visit(IVariableDeclaration statement, Set<APITokenSet> context) {
        // TODO: what here?
        
        // System.out.println("IVariableDeclaration");
        // TODO: is this for new Class, e.g. someVariable = new SomePackage.AnotherPackage.ClassName()?
        // TODO: TypeName three entries if not local?
        // TODO: identifier if library second?
        // System.out.println(statement.getType()); // TODO: <- this has many methods

        // System.out.println(statement.getType().getNamespace());
        // System.out.println(statement.getType().getAssembly());
        // System.out.println(statement.getType().getName());
        // System.out.println(statement.getType().getFullName());
        return null;
    }

    @Override
    public APITokenSet visit(IAssignment statement, Set<APITokenSet> context) {
        return statement.getExpression().accept(this, context);
    }

    @Override
    public APITokenSet visit(IBreakStatement statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(IContinueStatement statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(IExpressionStatement statement, Set<APITokenSet> context) {
        return statement.getExpression().accept(this, context);
    }

    @Override
    public APITokenSet visit(IGotoStatement statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(ILabelledStatement statement, Set<APITokenSet> context) {
        statement.getStatement().accept(this, context);
        return null;
    }

    @Override
    public APITokenSet visit(IReturnStatement statement, Set<APITokenSet> context) {
        statement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public APITokenSet visit(IThrowStatement statement, Set<APITokenSet> context) {
//        statement.getReference().accept(this, context); 
        return null;
    }

    @Override
    public APITokenSet visit(IEventSubscriptionStatement statement, Set<APITokenSet> context) {
        statement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public APITokenSet visit(IDoLoop statement, Set<APITokenSet> context) {
        statement.getCondition().accept(this, context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APITokenSet visit(IForEachLoop statement, Set<APITokenSet> context) {
        statement.getDeclaration().accept(this, context);
        statement.getLoopedReference().accept(this, context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APITokenSet visit(IForLoop statement, Set<APITokenSet> context) {
        this.visit(statement.getInit(), context);
        statement.getCondition().accept(this, context);
        this.visit(statement.getStep(), context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APITokenSet visit(IIfElseBlock statement, Set<APITokenSet> context) {
        statement.getCondition().accept(this, context);
        this.visit(statement.getThen(), context);
        this.visit(statement.getElse(), context);
        return null;
    }

    @Override
    public APITokenSet visit(ILockBlock statement, Set<APITokenSet> context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APITokenSet visit(ISwitchBlock statement, Set<APITokenSet> context) {
        statement.getReference().accept(this, context);
        this.visit(statement.getDefaultSection(), context);

        for(ICaseBlock caseBlock : statement.getSections()) {
            this.visit(caseBlock.getBody(), context);
        }

        return null;
    }

    @Override
    public APITokenSet visit(ITryBlock statement, Set<APITokenSet> context) {
        this.visit(statement.getBody(), context);
        this.visit(statement.getFinally(), context);
        for(ICatchBlock catchBlock : statement.getCatchBlocks()) {
            this.visit(catchBlock.getBody(), context);
        }
        return null;
    }

    @Override
    public APITokenSet visit(IUncheckedBlock statement, Set<APITokenSet> context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APITokenSet visit(IUnsafeBlock statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(IUsingBlock statement, Set<APITokenSet> context) {
        this.visit(statement.getBody(), context);
        statement.getReference().accept(this, context);
        return null;
    }

    @Override
    public APITokenSet visit(IWhileLoop statement, Set<APITokenSet> context) {
        statement.getCondition().accept(this, context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APITokenSet visit(IBinaryExpression statement, Set<APITokenSet> context) {
        statement.getLeftOperand().accept(this, context);
        statement.getRightOperand().accept(this, context);
        return null;
    }

    @Override
    public APITokenSet visit(ICastExpression statement, Set<APITokenSet> context) {
        // TODO: which one?
        return null;
    }

    @Override
    public APITokenSet visit(ICompletionExpression statement, Set<APITokenSet> context) {
        statement.getVariableReference().accept(this, context);
        return null;
    }

    @Override
    public APITokenSet visit(IComposedExpression statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(IIfElseExpression statement, Set<APITokenSet> context) {
        statement.getCondition().accept(this, context);
        statement.getThenExpression().accept(this, context);
        statement.getElseExpression().accept(this, context);
        return null;
    }

    @Override
    public APITokenSet visit(IIndexAccessExpression statement, Set<APITokenSet> context) {
        // TODO: which one?
        return null;
    }

    @Override
    public APITokenSet visit(IInvocationExpression statement, Set<APITokenSet> context) {
        for(ISimpleExpression parameter : statement.getParameters()) {
            parameter.accept(this, context);
        }
        
        IMethodName methodName = statement.getMethodName();
        if(!methodName.getDeclaringType().getAssembly().isLocalProject()) {
            APIToken apiToken = new APIToken();

            if(methodName.isConstructor()) {
                apiToken.operation = "new";
                apiToken.invocation = "class constructor";
            } else {
                apiToken.operation = methodName.getName(); 
                if(methodName.getIdentifier().startsWith("static")) {
                    apiToken.invocation = "static operation";
                } else {
                    apiToken.invocation = "instance operation";
                }
            }
            
//            apiToken.type = methodName.getDeclaringType().getFullName();
            apiToken.type = methodName.getDeclaringType().getName(); // TODO: use only the name
            apiToken.namespace = methodName.getDeclaringType().getNamespace().getIdentifier();

            String nameSpace = methodName.getDeclaringType().getNamespace().getIdentifier();
//            if(!context.containsKey(nameSpace)) {
//                context.put(nameSpace, new HashSet<>());
//            }
//            context.add(apiToken);
        }
        return null;
    }

    @Override
    public APITokenSet visit(ILambdaExpression statement, Set<APITokenSet> context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APITokenSet visit(ITypeCheckExpression statement, Set<APITokenSet> context) {
        statement.getReference().accept(this, context);
        return null;
    }

    @Override
    public APITokenSet visit(IUnaryExpression statement, Set<APITokenSet> context) {
        statement.getOperand().accept(this, context);
        return null;
    }

    @Override
    public APITokenSet visit(ILoopHeaderBlockExpression statement, Set<APITokenSet> context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APITokenSet visit(IConstantValueExpression statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(INullExpression statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(IReferenceExpression statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(IEventReference statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(IFieldReference statement, Set<APITokenSet> context) {
        // TODO: maybe this?
        // System.out.println(statement.getFieldName());
        return null;
    }

    @Override
    public APITokenSet visit(IIndexAccessReference statement, Set<APITokenSet> context) {
        statement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public APITokenSet visit(IMethodReference statement, Set<APITokenSet> context) {
        // System.out.println(statement.getMethodName());
        return null;
    }

    @Override
    public APITokenSet visit(IPropertyReference statement, Set<APITokenSet> context) {
        // System.out.println(statement.getPropertyName());
        return null;
    }

    @Override
    public APITokenSet visit(IVariableReference statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(IUnknownReference statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(IUnknownExpression statement, Set<APITokenSet> context) {
        return null;
    }

    @Override
    public APITokenSet visit(IUnknownStatement statement, Set<APITokenSet> context) {
        return null;
    }
    
    // Generic

    /**
     * Given a list of statements, this function calls the accept function for each of the statements included
     * in the list. Such lists occur in 
     *      {@link IMethodDeclaration#getBody()},
     *      {@link IForEachLoop#getBody()},
     *      etc
     *  @param body
     *          list of statements
     *          
     * @param context
     */
    public APITokenSet visit(List<IStatement> body, Set<APITokenSet> context) {
        for(IStatement statement : body) {
            statement.accept(this, context);
        }
        return null;
    }
}
