import java.util.ArrayList;
import java.util.List;

public class APISentence {
    
    private List<APIToken> apiTokens = new ArrayList<>();
    private List<APISentence> branches = new ArrayList<>();
    
    public void addToken(APIToken token) {
        this.apiTokens.add(token);
    }
    
    public APISentence branch() {
        APISentence sentence = new APISentence();
        this.branches.add(sentence);
        return sentence;
    }
    
}
