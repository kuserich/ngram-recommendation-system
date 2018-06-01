package extractor;

import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import cc.kave.commons.model.ssts.ISST;
import util.Utilities;

/**
 * This class implements and represents a single API token.
 * We refer to 'token' or 'API token' as a possible code instruction that involves some API type.
 * A code instruction is an invocation expression 
 * {@link cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression} on a method in
 * an API. We consider normal operations, static operations and constructors as operations.
 * 
 * 
 */
public class APIToken extends MethodName {

    /**
     * Calls the constructor of the parent class.
     * The parent constructor calls the same constructor with "[?] [?].???()" set as identifier.
     * This creates an APIToken with an unknown type, operation, namespace, ... but with valid
     * (non-null) attributes.
     * 
     * @see MethodName
     *          parent class
     *          
     * @see #APIToken(String)
     *          second constructor which includes an identifier
     */
    public APIToken() {
        super();
    }

    /**
     * Calls the constructor of the parent class with the given identifier.
     * The identifier is used to fill the attributes of the APIToken.
     * 
     * An identifier will have the following structure
     *      [p:int] [a.b.c.MyType, MyProject].M([some.framework.IBla, fw, 1.2.3.4] foo)
     * such that it holds the following information
     *      [<return type>] [<declaring type>].<simple name>(<parameter list>)
     *      
     * Further details on the structure and semantics of the contents of the identifier
     * can be found at
     *      <a href="http://www.kave.cc/caret/simplified-syntax-trees">KaVE Project - Symplified Syntax Trees</a>
     *      (http://www.kave.cc/caret/simplified-syntax-trees)
     * 
     * Notice that APIToken inherits most of its attributes and functionality from {@link MethodName}.
     * 
     * @see #APIToken()
     *          second constructor
     *          
     * @param identifier
     *          structured string as described above
     */
    public APIToken(String identifier) {
        super(identifier);
    }

    /**
     * Returns the invocation type of this token.
     * The invocation type can be instance (normal), static or constructor.
     * 
     * @see Invocation
     *          enum that holds invocation types
     * @see Invocation#INSTANCE_OPERATION
     *          value used if the invocation is a normal instance operation
     * @see Invocation#CLASS_CONSTRUCTOR
     *          value used if the invocation is the instantiation of a new object,
     *          i.e. if a constructor is called
     * @see Invocation#STATIC_OPERATION
     *          value used if the invocation is on a static operation
     * 
     * @return
     *          invocation type of this token
     */
    public Invocation getInvocation() {
        if(getIdentifier().startsWith("static")) {
            return Invocation.STATIC_OPERATION;
        }
        
        if(getOperation().equals("new")) {
            return Invocation.CLASS_CONSTRUCTOR;
        }
        
        return Invocation.INSTANCE_OPERATION;
    }

    /**
     * Returns the type of the APIToken.
     * 
     * The type of an APIToken (or Membername for that matter) is the class of the object 
     * it instantiates. In this function we return the entire type identifier including
     * its package/namespace. For instance, this class (APIToken) will have the following
     * type:
     *      evaluation.APIToken
     *      
     * TODO: why do we add namepsace?
     * 
     * @see #getDeclaringType()#getFullName()
     *          method that we initially used in this method but may return strings
     *          that include generic types and their mappings, which cause issues in
     *          the code elements that use this method
     * @see #getDeclaringType()#getName()
     *          returns the actual type of the token/name without any namespaces or 
     *          package identifiers
     * 
     * @return
     *          Type of the APIToken with the following structure: Some.Namespace.Type
     */
    public String getType() {
        // or only getDeclaringType().getName()
        // return getDeclaringType().getFullName();
        if(getNamespace() != null && getNamespace().length() > 0) {
            // when using getDeclareType().getName() some objects return lowercase
            // names, hence we capitalize (using getDeclaringType().getFullName() does
            // not exhibit this behavior but has the issue that it might return names
            // that include generic types and their mappings, which cause issues in
            // the code elements that use this method
            return getNamespace()+"."+Utilities.capitalize(getDeclaringType().getName());
        }
        return Utilities.capitalize(getDeclaringType().getName());
    }

    /**
     * Returns the operation that this token encompasses.
     * 
     * Notice that if this is the instantiation of a new object, the operation is equal
     * to the constructor of the type. In this case, this function returns "new" (as out-
     * lined in the paper).
     * 
     * @see #getName()
     *          operation of this token (if not constructor)
     * 
     * @return
     *          operation that this token encompasses.
     */
    public String getOperation() {
        if(isConstructor()) {
            return "new";
        }
        return getName();
    }

    /**
     * Returns the namespace that this token encompasses.
     * 
     * This method is used as a shorthand for
     * {@link #getDeclaringType()#getNamespace()#getIdentifier()}
     * 
     * @return
     *          namespace of this token
     */
    public String getNamespace() {
        return getDeclaringType().getNamespace().getIdentifier();
    }

    /**
     * Returns whether this token is empty.
     * A token is considered empty if it was instantiated with the default identifier
     * ("[?] [?].???()") and thus does not have any values for type, namespace, ...
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

    /**
     * Return string representation of this token.
     * That is, returns the type and operation of this token.
     * 
     * Example:
     *      <Some.Namespace.Type,Operation>
     *          
     * @return
     *          String representation of this token
     */
    @Override
    public String toString() {
        return "<"+getType()+","+getOperation()+">";
    }
}