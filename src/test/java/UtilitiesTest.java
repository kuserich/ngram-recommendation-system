import extractor.APIToken;
import opennlp.tools.util.StringList;
import org.junit.Test;
import util.Utilities;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UtilitiesTest {

    @Test
    public void hasIllegalMethodName() {
        
    }
    
    @Test
    public void apiSentenceToStringList() {
        APIToken t1 = new APIToken("[?] [Some.Namespace.Type, Some.Namespace].Operation1()");
        APIToken t2 = new APIToken("[?] [Some.Namespace.Type, Some.Namespace].Operation2()");
        APIToken t3 = new APIToken("[?] [Some.Namespace.Type, Some.Namespace].Operation3()");
        APIToken t4 = new APIToken("[?] [Some.Namespace.Type, Some.Namespace].Operation4()");
        List<APIToken> sentence = new ArrayList<>();
        sentence.add(t1);
        sentence.add(t2);
        sentence.add(t3);
        sentence.add(t4);

        StringList stringList = new StringList(
                "Some.Namespace.Type,Operation1",
                "Some.Namespace.Type,Operation2",
                "Some.Namespace.Type,Operation3",
                "Some.Namespace.Type,Operation4"
        );
        
        assertEquals(Utilities.apiSentenceToStringList(sentence), stringList);
    }
    
    @Test
    public void capitalize() {
        String t1 = "test";
        String t2 = "Test";
        assertEquals(t2, Utilities.capitalize(t1));
    }
}
