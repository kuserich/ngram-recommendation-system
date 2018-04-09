import java.util.ArrayList;
import java.util.List;

public class APISentence {
    
    List<APIToken> apiTokens = new ArrayList<>();
    
    public void add(APIToken apiToken) {
        this.apiTokens.add(apiToken);
    }
    
}
