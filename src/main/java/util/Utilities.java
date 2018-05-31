package util;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import extractor.APISentenceTree;
import extractor.APIToken;
import opennlp.tools.util.StringList;

import java.util.List;

/**
 * This class collects a number of useful functions that we use across the entire project.
 */
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
                || methodName.getDeclaringType().getNamespace().getIdentifier().equals("???")
                || methodName.getDeclaringType().getNamespace().getIdentifier().contains("[[");
    }

    /**
     * Transforms the given list of {@link APIToken} to a {@link StringList}
     * containing the string representation {@link APIToken#toString()} of
     * all tokens.
     * 
     * We use this for the {@link ngram.NgramRecommenderClient}.
     * 
     * @see evaluation.NgramRecommenderEvaluation#testWithModel(String, APIToken, List)
     *          uses this method
     * 
     * @param sentence
     *          list of APITokens
     *          
     * @return
     *          StringList containing all APITokens as strings
     */
    public static StringList apiSentenceToStringList(List<APIToken> sentence) {
        String[] strings = new String[sentence.size()];
        for(int i=0;i<sentence.size();i++) {
            APIToken token = sentence.get(i);
            strings[i] = token.getType()+","+token.getOperation();
        }
        return new StringList(strings);
    }

    /**
     * Returns a capitalized form of the given string.
     * That is, changes the case of the first character in the given string to
     * uppercase.
     * 
     * Example:
     *      "input" -> "Input"
     *     
     * @see APIToken#getType()
     *          uses this method. We noticed that the method name in a
     *          type of an APIToken sometimes is lower case and if we 
     *          use it with the namespace, it should be capitalized
     * 
     * @param str
     *          string that is capitalized
     *          
     * @return
     *          capitalized string
     */
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
