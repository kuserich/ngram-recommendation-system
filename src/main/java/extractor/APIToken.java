package extractor;

import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import cc.kave.commons.model.ssts.ISST;
import opennlp.tools.util.StringList;
import util.Utilities;

public class APIToken extends MethodName {
    
    private String namespace;

    public APIToken() {
        super();
    }
    
    public APIToken(String identifier) {
        super(identifier);
    }

    public Invocation getInvocation() {
        if(getIdentifier().startsWith("static")) {
            return Invocation.STATIC_OPERATION;
        }
        return Invocation.INSTANCE_OPERATION;
    }
    
    public String getType() {
        // or only getDeclaringType().getName()
        if(getNamespace() != null && getNamespace().length() > 0) {
            return getNamespace()+"."+Utilities.capitalize(getDeclaringType().getName());
        }
        return Utilities.capitalize(getDeclaringType().getName());
//        return getDeclaringType().getFullName();
    }
    
    public String getOperation() {
        if(isConstructor()) {
            return "new";
        }
        return getName();
    }
    
    public String getNamespace() {
        return getDeclaringType().getNamespace().getIdentifier();
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
        return isUnknown();
    }

    @Override
    public String toString() {
        return "<"+getType()+","+getOperation()+">";
    }
}