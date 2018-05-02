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

public class APIVisitor implements ISSTNodeVisitor<APISentence, APIToken> {
    
    @Override
    public APIToken visit(ISST isst, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(IDelegateDeclaration statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(IEventDeclaration statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(IFieldDeclaration statement, APISentence context) {
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
    public APIToken visit(IMethodDeclaration statement, APISentence context) {
        return this.visit(statement.getBody(), context);
    }

    @Override
    public APIToken visit(IPropertyDeclaration statement, APISentence context) {
        // TODO: what is this?
        this.visit(statement.getGet(), context);
        this.visit(statement.getSet(), context);
        return null;
    }

    @Override
    public APIToken visit(IVariableDeclaration statement, APISentence context) {
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
    public APIToken visit(IAssignment statement, APISentence context) {
        return statement.getExpression().accept(this, context);
    }

    @Override
    public APIToken visit(IBreakStatement statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(IContinueStatement statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(IExpressionStatement statement, APISentence context) {
        return statement.getExpression().accept(this, context);
    }

    @Override
    public APIToken visit(IGotoStatement statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(ILabelledStatement statement, APISentence context) {
        statement.getStatement().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IReturnStatement statement, APISentence context) {
        statement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IThrowStatement statement, APISentence context) {
//        statement.getReference().accept(this, context); 
        return null;
    }

    @Override
    public APIToken visit(IEventSubscriptionStatement statement, APISentence context) {
        statement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IDoLoop statement, APISentence context) {
        // TODO: branch
        statement.getCondition().accept(this, context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IForEachLoop statement, APISentence context) {
        // TODO: branch
        statement.getDeclaration().accept(this, context);
        statement.getLoopedReference().accept(this, context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IForLoop statement, APISentence context) {
        // TODO: branch
        this.visit(statement.getInit(), context);
        statement.getCondition().accept(this, context);
        this.visit(statement.getStep(), context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IIfElseBlock statement, APISentence context) {
        // TODO: branch
        statement.getCondition().accept(this, context);
        this.visit(statement.getThen(), context);
        this.visit(statement.getElse(), context);
        return null;
    }

    @Override
    public APIToken visit(ILockBlock statement, APISentence context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(ISwitchBlock statement, APISentence context) {
        // TODO: branch
        statement.getReference().accept(this, context);
        this.visit(statement.getDefaultSection(), context);

        for(ICaseBlock caseBlock : statement.getSections()) {
            this.visit(caseBlock.getBody(), context);
        }

        return null;
    }

    @Override
    public APIToken visit(ITryBlock statement, APISentence context) {
        // TODO: branch
        this.visit(statement.getBody(), context);
        this.visit(statement.getFinally(), context);
        for(ICatchBlock catchBlock : statement.getCatchBlocks()) {
            this.visit(catchBlock.getBody(), context);
        }
        return null;
    }

    @Override
    public APIToken visit(IUncheckedBlock statement, APISentence context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IUnsafeBlock statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(IUsingBlock statement, APISentence context) {
        // TODO: branch???
        this.visit(statement.getBody(), context);
        statement.getReference().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IWhileLoop statement, APISentence context) {
        // TODO: branch
        statement.getCondition().accept(this, context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IBinaryExpression statement, APISentence context) {
        statement.getLeftOperand().accept(this, context);
        statement.getRightOperand().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(ICastExpression statement, APISentence context) {
        // TODO: which one?
        return null;
    }

    @Override
    public APIToken visit(ICompletionExpression statement, APISentence context) {
        statement.getVariableReference().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IComposedExpression statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(IIfElseExpression statement, APISentence context) {
        // TODO: branch
        statement.getCondition().accept(this, context);
        statement.getThenExpression().accept(this, context);
        statement.getElseExpression().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IIndexAccessExpression statement, APISentence context) {
        // TODO: which one?
        return null;
    }

    /**
     * 
     * @param statement
     * @param context
     * @return
     */
    @Override
    public APIToken visit(IInvocationExpression statement, APISentence context) {
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
            context.addToken(apiToken);
        }
        return null;
    }

    @Override
    public APIToken visit(ILambdaExpression statement, APISentence context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(ITypeCheckExpression statement, APISentence context) {
        statement.getReference().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IUnaryExpression statement, APISentence context) {
        statement.getOperand().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(ILoopHeaderBlockExpression statement, APISentence context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IConstantValueExpression statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(INullExpression statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(IReferenceExpression statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(IEventReference statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(IFieldReference statement, APISentence context) {
        // TODO: maybe this?
        // System.out.println(statement.getFieldName());
        return null;
    }

    @Override
    public APIToken visit(IIndexAccessReference statement, APISentence context) {
        statement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IMethodReference statement, APISentence context) {
        // System.out.println(statement.getMethodName());
        return null;
    }

    @Override
    public APIToken visit(IPropertyReference statement, APISentence context) {
        // System.out.println(statement.getPropertyName());
        return null;
    }

    @Override
    public APIToken visit(IVariableReference statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(IUnknownReference statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(IUnknownExpression statement, APISentence context) {
        return null;
    }

    @Override
    public APIToken visit(IUnknownStatement statement, APISentence context) {
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
    public APIToken visit(List<IStatement> body, APISentence context) {
        for(IStatement statement : body) {
            statement.accept(this, context);
        }
        return null;
    }
}
