package extractor;

import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.ssts.ISST;

public class APIToken implements IName {
    
    private Invocation invocation;
    private String type;
    private String operation;
    
    private String namespace;

    public Invocation getInvocation() {
        return invocation;
    }

    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getNamespace() {
        return namespace;
    }
    
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Returns whether this token is empty.
     * A token is considered empty if all of its values are null or if
     * all of its values are empty strings (i.e. "").
     * 
     * This method is used in {@link APISentenceTree#flatten()} to filter empty APITokens.
     * During extraction in {@link SentenceExtractor#process(ISST)} using {@link APIVisitor#visit},
     * an {@link APISentenceTree} might add 'empty' APITokens to its {@link APISentenceTree#branches}
     * within {@link APISentenceTree#branch(APIToken)}.
     *
     * @see APISentenceTree#flatten()
     *          method that uses this method
     * @see APISentenceTree#branch(APIToken)
     *          method where empty APITokens might be created
     * @see APIVisitor#visit
     *          method that calls {@link APISentenceTree#branch(APIToken)}
     * @see SentenceExtractor#process(ISST)
     *          method that uses {@link APIVisitor} (i.e. the root of empty tokens)
     * 
     * @return
     *          true if this token is empty, false otherwise
     */
    public boolean isEmpty() {
        return (invocation == null)
                && (type == null || type.length() == 0)
                && (operation == null || operation.length() == 0)
                && (namespace == null || namespace.length() == 0);
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public boolean isUnknown() {
        return false;
    }

    @Override
    public boolean isHashed() {
        return false;
    }

    @Override
    public String toString() {
        return "<"+namespace+", "+operation+">";
    }
    
}

enum Invocation {
    STATIC_OPERATION, INSTANCE_OPERATION, CLASS_CONSTRUCTOR
}
