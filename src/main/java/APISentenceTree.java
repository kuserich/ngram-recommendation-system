import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(int i = 0; i< tokens.size(); i++) {
            APIToken token = tokens.get(i);
            sb.append(token.toString());
            
            if(branches.containsKey(token)) {
                for(APISentenceTree branch : branches.get(token)) {
                    sb.append(branch.toString());
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
