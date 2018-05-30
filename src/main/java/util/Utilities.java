package util;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import extractor.APISentenceTree;

public class Utilities {

    /**
     * Returns whether the given method name contains illegal name or namespace.
     *
     * We have noticed during development that there are a lot of method names 
     * that only contain question marks. As we cannot use this in any meaningful
     * way we have decided to remove them.
     *
     * @see extractor.APIVisitor#visit(IInvocationExpression, APISentenceTree)
     *          the function that uses {@link #hasIllegalMethodName(IMethodName)}
     *
     * @param methodName
     *          Method name of a statement (usually an {@link IInvocationExpression}
     * @return
     *          boolean statement whether the given method name has an 'illegal'
     *          name. We consider a method name 'illegal' if it contains only
     *          question marks.
     */
    public static boolean hasIllegalMethodName(IMethodName methodName) {
        // we check both the name of the declaring type as well as the namespace
        return methodName.getDeclaringType().getName().equals("???")
                || methodName.getDeclaringType().getNamespace().getIdentifier().equals("???");
    }
    
}
