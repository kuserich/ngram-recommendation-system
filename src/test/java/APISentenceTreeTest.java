import extractor.APISentenceTree;
import extractor.APIToken;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class APISentenceTreeTest {
    
    @Test
    public void flatten() {
        APISentenceTree asp = new APISentenceTree();

        APIToken t1 = new APIToken();
        t1.setNamespace("System");
        t1.setOperation("M1");
        APIToken t2 = new APIToken();
        t2.setNamespace("System");
        t2.setOperation("M2");
        APIToken t3 = new APIToken();
        t3.setNamespace("System");
        t3.setOperation("M3");
        APIToken t4 = new APIToken();
        t4.setNamespace("System");
        t4.setOperation("M4");
        APIToken t5 = new APIToken();
        t5.setNamespace("System");
        t5.setOperation("M5");
        APIToken t6 = new APIToken();
        t6.setNamespace("System");
        t6.setOperation("M6");
        APIToken t7 = new APIToken();
        t7.setNamespace("System");
        t7.setOperation("M7");
        APIToken t8 = new APIToken();
        t8.setNamespace("System");
        t8.setOperation("M8");
        APIToken t9 = new APIToken();
        t9.setNamespace("System");
        t9.setOperation("M9");
        APIToken t10 = new APIToken();
        t10.setNamespace("System");
        t10.setOperation("M10");
        APIToken t11 = new APIToken();
        t11.setNamespace("System");
        t11.setOperation("M11");
        APIToken t12 = new APIToken();
        t12.setNamespace("System");
        t12.setOperation("M12");
        APIToken t13 = new APIToken();
        t13.setNamespace("System");
        t13.setOperation("M13");

        asp.addToken(t1);
        asp.addToken(t2);
        APISentenceTree b1 = asp.branch(t2);
        b1.addToken(t3);
        b1.addToken(t4);
        APISentenceTree b21 = b1.branch(t4);
        APISentenceTree b22 = b1.branch(t4);
        b21.addToken(t5);
        b22.addToken(t6);
        b22.addToken(t7);
        b1.addToken(t8);
        asp.addToken(t9);


        // M1, M2, M9
        // M1, M2, M3, M4, M8, M9
        // M1, M2, M3, M4, M5, M8, M9
        // M1, M2, M3, M4, M6, M7, M8, M9
        
        List<List<APIToken>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.get(0).add(t1);
        expected.get(0).add(t2);
        expected.get(0).add(t9);

        expected.add(new ArrayList<>());
        expected.get(1).add(t1);
        expected.get(1).add(t2);
        expected.get(1).add(t3);
        expected.get(1).add(t4);
        expected.get(1).add(t8);
        expected.get(1).add(t9);

        expected.add(new ArrayList<>());
        expected.get(2).add(t1);
        expected.get(2).add(t2);
        expected.get(2).add(t3);
        expected.get(2).add(t4);
        expected.get(2).add(t5);
        expected.get(2).add(t8);
        expected.get(2).add(t9);

        expected.add(new ArrayList<>());
        expected.get(3).add(t1);
        expected.get(3).add(t2);
        expected.get(3).add(t3);
        expected.get(3).add(t4);
        expected.get(3).add(t6);
        expected.get(3).add(t7);
        expected.get(3).add(t8);
        expected.get(3).add(t9);
        
        List<List<APIToken>> actual = asp.flatten();
        
        assertThat(actual, is(expected));
    }
    
    @Test
    public void branch() {
        APISentenceTree asp = new APISentenceTree();

        APIToken t1 = new APIToken();
        t1.setNamespace("System");
        t1.setOperation("M1");
        APIToken t2 = new APIToken();
        t2.setNamespace("System");
        t2.setOperation("M2");
        APIToken t3 = new APIToken();
        t3.setNamespace("System");
        t3.setOperation("M3");
        APIToken t4 = new APIToken();
        t4.setNamespace("System");
        t4.setOperation("M4");
        
        asp.addToken(t1);
        APISentenceTree aspB = asp.branch(t2);
        aspB.addToken(t3);
        aspB.addToken(t4);

        assertEquals(asp.getBranches().get(t2).get(0).getTokens().get(0), t3);
        assertEquals(asp.getBranches().get(t2).get(0).getTokens().get(1), t4);
    }
    
}
