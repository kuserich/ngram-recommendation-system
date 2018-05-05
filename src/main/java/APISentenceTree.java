import java.util.*;

public class APISentenceTree {
    
    private List<APIToken> tokens = new ArrayList<>();
    private Map<APIToken, List<APISentenceTree>> branches = new HashMap<>();
    
    public List<APIToken> getTokens() {
        return tokens;
    }
    
    public Map<APIToken, List<APISentenceTree>> getBranches() {
        return branches;
    }
    
    public void addToken(APIToken token) {
        this.tokens.add(token);
    }
    
    public APISentenceTree branch() {
        return branch(tokens.get(tokens.size()-1));
    }
    
    public APISentenceTree branch(APIToken token) {
        if(!branches.containsKey(token)) {
            branches.put(token, new ArrayList<>());
        }
        APISentenceTree sentence = new APISentenceTree();
        this.branches.get(token).add(sentence);
        return sentence;
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
