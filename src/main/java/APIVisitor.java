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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class APIVisitor implements ISSTNodeVisitor<Map<String, Set<APIToken>>, APIToken> {
    
    @Override
    public APIToken visit(ISST isst, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(IDelegateDeclaration statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(IEventDeclaration statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(IFieldDeclaration statement, Map<String, Set<APIToken>> context) {
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
    public APIToken visit(IMethodDeclaration statement, Map<String, Set<APIToken>> context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IPropertyDeclaration statement, Map<String, Set<APIToken>> context) {
        this.visit(statement.getGet(), context);
        this.visit(statement.getSet(), context);
        return null;
    }

    @Override
    public APIToken visit(IVariableDeclaration statement, Map<String, Set<APIToken>> context) {
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
    public APIToken visit(IAssignment statement, Map<String, Set<APIToken>> context) {
        statement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IBreakStatement statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(IContinueStatement statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(IExpressionStatement statement, Map<String, Set<APIToken>> context) {
        statement.getExpression().accept(this, context);
        return null; // TODO: identifier if library second?
    }

    @Override
    public APIToken visit(IGotoStatement statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(ILabelledStatement statement, Map<String, Set<APIToken>> context) {
        statement.getStatement().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IReturnStatement statement, Map<String, Set<APIToken>> context) {
        statement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IThrowStatement statement, Map<String, Set<APIToken>> context) {
//        statement.getReference().accept(this, context); 
        return null;
    }

    @Override
    public APIToken visit(IEventSubscriptionStatement statement, Map<String, Set<APIToken>> context) {
        statement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IDoLoop statement, Map<String, Set<APIToken>> context) {
        // TODO: branch
        statement.getCondition().accept(this, context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IForEachLoop statement, Map<String, Set<APIToken>> context) {
        // TODO: branch
        statement.getDeclaration().accept(this, context);
        statement.getLoopedReference().accept(this, context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IForLoop statement, Map<String, Set<APIToken>> context) {
        // TODO: branch
        this.visit(statement.getInit(), context);
        statement.getCondition().accept(this, context);
        this.visit(statement.getStep(), context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IIfElseBlock statement, Map<String, Set<APIToken>> context) {
        // TODO: branch
        statement.getCondition().accept(this, context);
        this.visit(statement.getThen(), context);
        this.visit(statement.getElse(), context);
        return null;
    }

    @Override
    public APIToken visit(ILockBlock statement, Map<String, Set<APIToken>> context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(ISwitchBlock statement, Map<String, Set<APIToken>> context) {
        // TODO: branch
        statement.getReference().accept(this, context);
        this.visit(statement.getDefaultSection(), context);

        for(ICaseBlock caseBlock : statement.getSections()) {
            this.visit(caseBlock.getBody(), context);
        }

        return null;
    }

    @Override
    public APIToken visit(ITryBlock statement, Map<String, Set<APIToken>> context) {
        // TODO: branch
        this.visit(statement.getBody(), context);
        this.visit(statement.getFinally(), context);
        for(ICatchBlock catchBlock : statement.getCatchBlocks()) {
            this.visit(catchBlock.getBody(), context);
        }
        return null;
    }

    @Override
    public APIToken visit(IUncheckedBlock statement, Map<String, Set<APIToken>> context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IUnsafeBlock statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(IUsingBlock statement, Map<String, Set<APIToken>> context) {
        // TODO: branch???
        this.visit(statement.getBody(), context);
        statement.getReference().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IWhileLoop statement, Map<String, Set<APIToken>> context) {
        // TODO: branch
        statement.getCondition().accept(this, context);
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IBinaryExpression statement, Map<String, Set<APIToken>> context) {
        statement.getLeftOperand().accept(this, context);
        statement.getRightOperand().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(ICastExpression statement, Map<String, Set<APIToken>> context) {
        // TODO: which one?
        return null;
    }

    @Override
    public APIToken visit(ICompletionExpression statement, Map<String, Set<APIToken>> context) {
        statement.getVariableReference().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IComposedExpression statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(IIfElseExpression statement, Map<String, Set<APIToken>> context) {
        // TODO: branch
        statement.getCondition().accept(this, context);
        statement.getThenExpression().accept(this, context);
        statement.getElseExpression().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IIndexAccessExpression statement, Map<String, Set<APIToken>> context) {
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
    public APIToken visit(IInvocationExpression statement, Map<String, Set<APIToken>> context) {
        // TODO: add here

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
            if(!context.containsKey(nameSpace)) {
                context.put(nameSpace, new HashSet<>());
            }
            context.get(nameSpace).add(apiToken);
        }
        return null;
    }

    @Override
    public APIToken visit(ILambdaExpression statement, Map<String, Set<APIToken>> context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(ITypeCheckExpression statement, Map<String, Set<APIToken>> context) {
        statement.getReference().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IUnaryExpression statement, Map<String, Set<APIToken>> context) {
        statement.getOperand().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(ILoopHeaderBlockExpression statement, Map<String, Set<APIToken>> context) {
        this.visit(statement.getBody(), context);
        return null;
    }

    @Override
    public APIToken visit(IConstantValueExpression statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(INullExpression statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(IReferenceExpression statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(IEventReference statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(IFieldReference statement, Map<String, Set<APIToken>> context) {
        // TODO: maybe this?
        // System.out.println(statement.getFieldName());
        return null;
    }

    @Override
    public APIToken visit(IIndexAccessReference statement, Map<String, Set<APIToken>> context) {
        statement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public APIToken visit(IMethodReference statement, Map<String, Set<APIToken>> context) {
        // System.out.println(statement.getMethodName());
        return null;
    }

    @Override
    public APIToken visit(IPropertyReference statement, Map<String, Set<APIToken>> context) {
        // System.out.println(statement.getPropertyName());
        return null;
    }

    @Override
    public APIToken visit(IVariableReference statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(IUnknownReference statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(IUnknownExpression statement, Map<String, Set<APIToken>> context) {
        return null;
    }

    @Override
    public APIToken visit(IUnknownStatement statement, Map<String, Set<APIToken>> context) {
        return null;
    }
    
    // Generic

    /**
     * Given a list of statements, this function calls the accept function for each of the statements included
     * in the list. Such lists occur in 
     *      {@link IMethodDeclaration#getBody()},
     *      {@link IForEachLoop#getBody()},
     *      etc
     *      
     * @param body
     *          list of statements
     *          
     * @param context
     */
    public void visit(List<IStatement> body, Map<String, Set<APIToken>> context) {
        for(IStatement statement : body) {
            statement.accept(this, context);
        }
    }
}
