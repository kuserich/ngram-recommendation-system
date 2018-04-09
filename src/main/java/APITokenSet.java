import java.util.Set;

public class APITokenSet {
    
    Set<APIToken> tokens;
    
    public void add(APIToken apiToken) {
        this.tokens.add(apiToken);
    }
}
