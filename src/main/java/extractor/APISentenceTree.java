package extractor;

import java.util.*;

public class APISentenceTree {
    
    private final int DEFAULT_TOSTRING_INDENT = 2;
    private final int MAX_SENTENCES = Integer.MAX_VALUE / 131072; // 2^31/2^17 = 16'384
    
    private List<APIToken> tokens = new ArrayList<>();
    private Map<APIToken, List<APISentenceTree>> branches = new HashMap<>();
    
    public void addToken(APIToken token) {
        this.tokens.add(token);
    }
    
    public APISentenceTree branch(APIToken token) {
        if(!branches.containsKey(token)) {
            branches.put(token, new ArrayList<>());
        }
        APISentenceTree sentence = new APISentenceTree();
        this.branches.get(token).add(sentence);
        return sentence;
    }
    
    public List<APIToken> getTokens() {
        return tokens;
    }
    
    public Map<APIToken, List<APISentenceTree>> getBranches() {
        return branches;
    }

    public List<List<APIToken>> flatten() {
        List<List<APIToken>> sentenceList = new ArrayList<>();
        sentenceList.add(new ArrayList<>());
        return flatten(sentenceList);
    }
    
    public List<List<APIToken>> flatten(int maxDepth) {
        List<List<APIToken>> sentenceList = new ArrayList<>();
        sentenceList.add(new ArrayList<>());
        return flatten(sentenceList, 0, maxDepth);
    }

    public List<List<APIToken>> flatten(List<List<APIToken>> sentenceList) {
        for(APIToken token : tokens) {
            for(List<APIToken> sentence : sentenceList) {
                if(!token.isEmpty()) {
                    sentence.add(token);
                }
            }

            if(branches.containsKey(token) && sentenceList.size() < MAX_SENTENCES) {
                List<List<APIToken>> copiedSentenceLists = copySentenceList(sentenceList);
                List<List<APIToken>> newSentenceList = new ArrayList<>();

                int numberOfBranches = branches.get(token).size();
                for(int i=0; i<numberOfBranches; i++) {
                    if(numberOfBranches > 1) {
                        if(i == numberOfBranches-1) {
                            newSentenceList.addAll(branches.get(token).get(i).flatten(sentenceList));
                            sentenceList = newSentenceList;
                        } else {
                            newSentenceList.addAll(branches.get(token).get(i).flatten(copySentenceList(copiedSentenceLists)));
                        }
                    } else {
                        sentenceList.addAll(branches.get(token).get(i).flatten(copySentenceList(copiedSentenceLists)));
                    }
                }
            }
        }
        return sentenceList;
    }

    public List<List<APIToken>> flatten(List<List<APIToken>> sentenceList, int depth, int maxDepth) {
        for(APIToken token : tokens) {
            for(List<APIToken> sentence : sentenceList) {
                if(!token.isEmpty()) {
                    sentence.add(token);
                }
            }

            if(branches.containsKey(token) && sentenceList.size() < MAX_SENTENCES && depth <= maxDepth) {
                List<List<APIToken>> copiedSentenceLists = copySentenceList(sentenceList);
                List<List<APIToken>> newSentenceList = new ArrayList<>();

                int numberOfBranches = branches.get(token).size();
                for(int i=0; i<numberOfBranches; i++) {
                    APISentenceTree innerTree = branches.get(token).get(i);
                    if(numberOfBranches > 1) {
                        if(i == numberOfBranches-1) {
                            newSentenceList.addAll(innerTree.flatten(sentenceList, depth+1, maxDepth));
                            sentenceList = newSentenceList;
                        } else {
                            newSentenceList.addAll(
                                    innerTree.flatten(
                                            copySentenceList(copiedSentenceLists), depth+1, maxDepth));
                        }
                    } else {
                        sentenceList.addAll(
                                innerTree.flatten(
                                        copySentenceList(copiedSentenceLists), depth+1, maxDepth));
                    }
                }
            }
        }
        return sentenceList;
    }
    
    public int totalNumberOfBranches() {
        int sum = 0;
        sum += branches.size();
        for(APIToken key : branches.keySet()) {
            for(APISentenceTree asp : branches.get(key)) {
                sum += asp.totalNumberOfBranches();
            }
        }
        return sum;
    }

    public Long numberOfSentences() {
        Long total = (long) Math.max(1, Math.pow(2, branches.size()-1));
        if(branches.size() > 0) {
            for(APIToken key : branches.keySet()) {
                for(APISentenceTree iasp : branches.get(key)) {
                    total += iasp.numberOfSentences();
                }
            }
        }
        return total;
    }
    
    public List<List<APIToken>> copySentenceList(List<List<APIToken>> sentenceList) {
        List<List<APIToken>> copiedList = new ArrayList<>();
        for(List<APIToken> sentence : sentenceList) {
            copiedList.add((List) ((ArrayList) sentence).clone());
        }
        return copiedList;
    }

    /**
     * Return the last {@link APIToken} in {@link #tokens}.
     * This is either the last object in the array or null, if the array is empty.
     * 
     * This method is used in the {@link APIVisitor} for branching tokens. That is,
     * conditions in loops (for, while, ...) as well as simple conditional blocks
     * (if, if-else) yield multiple API sentences (one per possible path).
     * We store such branches in {@link #branches} which uses an {@link APIToken}
     * as the key. It is possible that the conditional expression is not of interest
     * for our objective (i.e. it is not an 
     * {@link cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression}).
     * In this case, we want to use the last valid token (in the case that it's null, 
     * we create a new, empty token).
     *
     * @see APIVisitor
     * @see APIVisitor#visit(cc.kave.commons.model.ssts.blocks.IWhileLoop, APISentenceTree)
     * @see APIVisitor#visit(cc.kave.commons.model.ssts.blocks.IForLoop, APISentenceTree)
     * @see APIVisitor#visit(cc.kave.commons.model.ssts.blocks.IIfElseBlock, APISentenceTree)
     *      and similar
     *      
     * @return
     *          last entry in {@link #tokens} or null
     */
    public APIToken getLastValidToken() {
        if(tokens.size() > 0) {
            return tokens.get(tokens.size()-1);
        }
        return null;
    }

    /**
     * Return whether {@link #tokens} is empty.
     * 
     * @return
     *          true if it's empty, false otherwise
     */
    public boolean isEmpty() {
        return tokens.size() == 0;
    }


    /**
     * 
     * @return
     */
    public int size() {
        return tokens.size();
    }
    
    /**
     * Return the string representation of an extractor.APISentenceTree.
     * This includes all tokens in this tree as well as all tokens in 
     * the trees in the branches.
     * 
     * This function simply calls {@link #toString(int)} with a default indent.
     * 
     * Notice that the string representation differs from the flattened sentences
     * that are retrieved in {@link #flatten()}.
     * 
     * @see #toString(int)
     *          function that is called with an indent
     * @see #DEFAULT_TOSTRING_INDENT
     *          value of the default indent
     * @see #tokens
     *          tokens that will be turned into strings
     * @see #branches
     *          subtrees
     * 
     * @return
     *          String representation of this object
     */
    @Override
    public String toString() {
        return toString(DEFAULT_TOSTRING_INDENT);
    }

    /**
     * Return the string representation of an extractor.APISentenceTree.
     * This includes all tokens in this tree as well as all tokens in 
     * the trees in the branches.
     * 
     *   (<Token, Some>, <Token, SomeOther>
     *      <Branch, First>, <Branch, StillFirst>
     *          <Branch, Second>
     *      <Branch, ThirdButFromRoot>
     *   <Token, Some>)
     *       

     * Notice that the string representation differs from the flattened sentences
     * that are retrieved in {@link #flatten()}.
     *       
     *
     * @see #toString()
     *          method that calls this method with a default indent
     * @see #DEFAULT_TOSTRING_INDENT
     *          value of the default indent used in {@link #toString()}
     * @see #tokens
     *          tokens that are turned into strings
     * @see APIToken#toString()
     *          string representation of {@link APIToken} objects in {@link #tokens}
     * @see #branches
     *          subtrees
     * 
     * @param branchIndent
     *          number of spaces that the current branch should be indented.
     *          Notice that for every {@link APISentenceTree} in {@link #branch}
     *          the function calls {@link #toString(int)} with an increased 
     *          branchIndent.
     * 
     * @return
     *          string representation of an extractor.APISentenceTree
     */
    public String toString(int branchIndent) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(int i = 0; i< tokens.size(); i++) {
            // add every token to the string
            APIToken token = tokens.get(i);
            sb.append(token.toString());

            if(branches.containsKey(token)) {
                for(APISentenceTree branch : branches.get(token)) {
                    sb.append("\n");
                    
                    // add the number of spaces to the string as defined with the indent
                    char[] repeat = new char[branchIndent];
                    Arrays.fill(repeat, ' ');
                    sb.append(new String(repeat));
                    
                    // add the string of the subtree
                    sb.append(branch.toString(branchIndent+2));
                }
            }

            // all tokens but the last one should include a comma
            if(i< tokens.size()-1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
    
}
