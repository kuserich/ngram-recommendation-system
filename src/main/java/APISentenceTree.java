import java.util.*;

public class APISentenceTree {
    
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

    public List<List<APIToken>> flatten() {
        List<List<APIToken>> sentenceList = new ArrayList<>();
        sentenceList.add(new ArrayList<>());
        return flatten(sentenceList);
    }
    
    public List<List<APIToken>> flatten(List<List<APIToken>> sentenceList) {
            for(APIToken token : tokens) {
                for(List<APIToken> sentence : sentenceList) {
                    sentence.add(token);
                }
                
                if(branches.containsKey(token)) {
                    List<List<List<APIToken>>> copiedSentenceLists = new ArrayList<>();
                    for(APISentenceTree asp : branches.get(token)) {
                        copiedSentenceLists.add(copySentenceList(sentenceList));
                    }
                    for(int i=0; i<branches.get(token).size(); i++) {
                        sentenceList.addAll(branches.get(token).get(i).flatten(copiedSentenceLists.get(i)));
                    }
                }
            }
        
        return sentenceList;
    }
    
    public List<List<APIToken>> copySentenceList(List<List<APIToken>> sentenceList) {
        List<List<APIToken>> copiedList = new ArrayList<>();
        for(List<APIToken> sentence : sentenceList) {
            copiedList.add((List) ((ArrayList) sentence).clone());
        }
        return copiedList;
    }
    
    public APIToken getLastValidToken() {
        if(tokens.size() > 0) {
            return tokens.get(tokens.size()-1);
        }
        return null;
    }

    public boolean isEmpty() {
        return tokens.size() == 0;
    }
    
    @Override
    public String toString() {
        return toString(2);
    }

    /**
     * Return the string representation of an APISentenceTree.
     * 
     *   (<Token, Some>, <Token, SomeOther>
     *      <Branch, First>, <Branch, StillFirst>
     *          <Branch, Second>
     *      <Branch, ThirdButFromRoot>
     *   <Token, Some>)
     *       
     *       
     * @param branchIndent
     *          number of spaces that the current branch should be indented.
     *          Notice that for every {@link APISentenceTree} in {@link #branch}
     *          the function calls {@link #toString(int)} with an increased 
     *          branchIndent.
     * 
     * @return
     *          string representation of an APISentenceTree
     */
    public String toString(int branchIndent) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(int i = 0; i< tokens.size(); i++) {
            APIToken token = tokens.get(i);
            sb.append(token.toString());

            if(branches.containsKey(token)) {
                for(APISentenceTree branch : branches.get(token)) {
                    sb.append("\n");
                    char[] repeat = new char[branchIndent];
                    Arrays.fill(repeat, ' ');
                    sb.append(new String(repeat));
                    sb.append(branch.toString(branchIndent+2));
                }
            }

            if(i< tokens.size()-1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
    
}
