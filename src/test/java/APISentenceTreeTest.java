import extractor.APISentenceTree;
import extractor.APIToken;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class APISentenceTreeTest {
    
    @Test
    public void flatten() {
        APISentenceTree asp = new APISentenceTree();

        APIToken t1 = new APIToken("[?] [type, namespace].operation1()");
        APIToken t2 = new APIToken("[?] [type, namespace].operation2()");
        APIToken t3 = new APIToken("[?] [type, namespace].operation3()");
        APIToken t4 = new APIToken("[?] [type, namespace].operation4()");
        APIToken t5 = new APIToken("[?] [type, namespace].operation5()");
        APIToken t6 = new APIToken("[?] [type, namespace].operation6()");
        APIToken t7 = new APIToken("[?] [type, namespace].operation7()");
        APIToken t8 = new APIToken("[?] [type, namespace].operation8()");
        APIToken t9 = new APIToken("[?] [type, namespace].operation9()");
        APIToken t10 = new APIToken("[?] [type, namespace].operation10()");
        APIToken t11 = new APIToken("[?] [type, namespace].operation11()");
        APIToken t12 = new APIToken("[?] [type, namespace].operation12()");
        APIToken t13 = new APIToken("[?] [type, namespace].operation13()");

        asp.addToken(t1);
        asp.addToken(t2);
        APISentenceTree b1 = asp.branch(t2);
        APISentenceTree b12 = asp.branch(t2);
        b1.addToken(t3);
        b1.addToken(t4);
        b12.addToken(t10);
        b12.branch(t10).addToken(t11);
        b12.branch(t10).addToken(t12);
        b12.addToken(t13);
        APISentenceTree b21 = b1.branch(t4);
        APISentenceTree b22 = b1.branch(t4);
        b21.addToken(t5);
        b22.addToken(t6);
        b22.addToken(t7);
        b1.addToken(t8);
        asp.addToken(t9);


        // M1, M2, M9
        // M1, M2, M3, M4, M5, M8, M9
        // M1, M2, M3, M4, M6, M7, M8, M9
        
        List<List<APIToken>> expected = new ArrayList<>();

        expected.add(new ArrayList<>());
        expected.get(0).add(t1);
        expected.get(0).add(t2);
        expected.get(0).add(t3);
        expected.get(0).add(t4);
        expected.get(0).add(t5);
        expected.get(0).add(t8);
        expected.get(0).add(t9);

        expected.add(new ArrayList<>());
        expected.get(1).add(t1);
        expected.get(1).add(t2);
        expected.get(1).add(t3);
        expected.get(1).add(t4);
        expected.get(1).add(t6);
        expected.get(1).add(t7);
        expected.get(1).add(t8);
        expected.get(1).add(t9);
        
        expected.add(new ArrayList<>());
        expected.get(2).add(t1);
        expected.get(2).add(t2);
        expected.get(2).add(t10);
        expected.get(2).add(t11);
        expected.get(2).add(t13);
        expected.get(2).add(t9);

        expected.add(new ArrayList<>());
        expected.get(3).add(t1);
        expected.get(3).add(t2);
        expected.get(3).add(t10);
        expected.get(3).add(t12);
        expected.get(3).add(t13);
        expected.get(3).add(t9);


        System.out.println(asp.flatten().size());
        
        assertThat(asp.flatten(), is(expected));
    }
    
    @Test
    public void branch() {
        APISentenceTree asp = new APISentenceTree();

        APIToken t1 = new APIToken("[?] [type, namespace].operation1()");
        APIToken t2 = new APIToken("[?] [type, namespace].operation2()");
        APIToken t3 = new APIToken("[?] [type, namespace].operation3()");
        APIToken t4 = new APIToken("[?] [type, namespace].operation4()");
        
        asp.addToken(t1);
        APISentenceTree aspB = asp.branch(t2);
        aspB.addToken(t3);
        aspB.addToken(t4);

        assertEquals(asp.getBranches().get(t2).get(0).getTokens().get(0), t3);
        assertEquals(asp.getBranches().get(t2).get(0).getTokens().get(1), t4);
    }
    
    @Test
    public void numberOfSentences() {
        APISentenceTree asp = new APISentenceTree();

        APIToken t1 = new APIToken("[?] [type, namespace].operation1()");
        APIToken t2 = new APIToken("[?] [type, namespace].operation2()");
        APIToken t3 = new APIToken("[?] [type, namespace].operation3()");
        APIToken t4 = new APIToken("[?] [type, namespace].operation4()");
        APIToken t5 = new APIToken("[?] [type, namespace].operation5()");
        APIToken t6 = new APIToken("[?] [type, namespace].operation6()");
        APIToken t7 = new APIToken("[?] [type, namespace].operation7()");
        APIToken t8 = new APIToken("[?] [type, namespace].operation8()");
        APIToken t9 = new APIToken("[?] [type, namespace].operation9()");
        APIToken t10 = new APIToken("[?] [type, namespace].operation10()");
        APIToken t11 = new APIToken("[?] [type, namespace].operation11()");
        APIToken t12 = new APIToken("[?] [type, namespace].operation12()");
        APIToken t13 = new APIToken("[?] [type, namespace].operation13()");
        APIToken t14 = new APIToken("[?] [type, namespace].operation14()");
        APIToken t15 = new APIToken("[?] [type, namespace].operation15()");
        APIToken t16 = new APIToken("[?] [type, namespace].operation16()");
        APIToken t17 = new APIToken("[?] [type, namespace].operation17()");
        APIToken t18 = new APIToken("[?] [type, namespace].operation18()");
        APIToken t19 = new APIToken("[?] [type, namespace].operation19()");
        APIToken t20 = new APIToken("[?] [type, namespace].operation20()");
        APIToken t21 = new APIToken("[?] [type, namespace].operation21()");
        APIToken t22 = new APIToken("[?] [type, namespace].operation22()");
        APIToken t23 = new APIToken("[?] [type, namespace].operation23()");
        APIToken t24 = new APIToken("[?] [type, namespace].operation24()");
        APIToken t25 = new APIToken("[?] [type, namespace].operation25()");
        APIToken t26 = new APIToken("[?] [type, namespace].operation26()");
        APIToken t27 = new APIToken("[?] [type, namespace].operation27()");
        APIToken t28 = new APIToken("[?] [type, namespace].operation28()");
        APIToken t29 = new APIToken("[?] [type, namespace].operation29()");
        APIToken t30 = new APIToken("[?] [type, namespace].operation30()");
        APIToken t31 = new APIToken("[?] [type, namespace].operation31()");
        APIToken t32 = new APIToken("[?] [type, namespace].operation32()");
        APIToken t33 = new APIToken("[?] [type, namespace].operation33()");
        APIToken t34 = new APIToken("[?] [type, namespace].operation34()");
        APIToken t35 = new APIToken("[?] [type, namespace].operation35()");
        APIToken t36 = new APIToken("[?] [type, namespace].operation36()");
        APIToken t37 = new APIToken("[?] [type, namespace].operation37()");
        APIToken t38 = new APIToken("[?] [type, namespace].operation38()");
        APIToken t39 = new APIToken("[?] [type, namespace].operation39()");
        APIToken t40 = new APIToken("[?] [type, namespace].operation40()");
        APIToken t41 = new APIToken("[?] [type, namespace].operation41()");

        asp.addToken(t1);
        asp.addToken(t2);
        asp.addToken(t3);
        asp.addToken(t4);
        asp.addToken(t5);
        APISentenceTree asp5_1 = asp.branch(t5);
        APISentenceTree asp5_2 = asp.branch(t5);
        asp5_1.addToken(t6);
        asp5_2.addToken(t7);
        asp5_2.addToken(t8);
        APISentenceTree asp8_1 = asp5_2.branch(t8);
        APISentenceTree asp8_2 = asp5_2.branch(t8);
        asp8_1.addToken(t9);
        asp8_1.addToken(t11);
        asp8_2.addToken(t10);
        asp8_2.addToken(t12);
        APISentenceTree asp12_1 = asp8_2.branch(t12);
        APISentenceTree asp12_2 = asp8_2.branch(t12);
        asp12_1.addToken(t13);
        asp12_2.addToken(t14);
        asp12_2.addToken(t15);
        APISentenceTree asp15_1 = asp12_2.branch(t15);
        APISentenceTree asp15_2 = asp12_2.branch(t15);
        asp15_1.addToken(t16);
        asp15_2.addToken(t17);
        asp12_2.addToken(t18);
        asp12_2.addToken(t19);
        APISentenceTree asp19_1 = asp12_2.branch(t19);
        APISentenceTree asp19_2 = asp12_2.branch(t19);
        asp19_1.addToken(t20);
        asp19_2.addToken(t21);
        asp12_2.addToken(t22);
        asp12_2.addToken(t23);
        APISentenceTree asp23_1 = asp12_2.branch(t23);
        APISentenceTree asp23_2 = asp12_2.branch(t23);
        asp23_1.addToken(t24);
        asp23_2.addToken(t25);
        asp12_2.addToken(t26);
        asp8_2.addToken(t27);
        asp5_2.addToken(t28);
        asp5_2.addToken(t29);
        asp5_2.addToken(t30);
        asp5_2.addToken(t31);
        APISentenceTree asp31_1 = asp5_2.branch(t31);
        APISentenceTree asp31_2 = asp5_2.branch(t31);
        asp31_1.addToken(t32);
        asp31_2.addToken(t33);
        asp5_2.addToken(t34);
        asp.addToken(t35);
        APISentenceTree asp35_1 = asp.branch(t35);
        asp35_1.addToken(t36);
        asp.addToken(t37);
        APISentenceTree asp37_1 = asp.branch(t37);
        APISentenceTree asp37_2 = asp.branch(t37);
        asp37_1.addToken(t38);
        asp37_2.addToken(t39);
        asp.addToken(t40);
        asp.addToken(t41);


        System.out.println(asp.numberOfSentences());
        System.out.println(asp.flatten().size());

        // should be 84 both
//        assertEquals(asp.numberOfSentences(), new Long(asp.flatten().size()));
//        assertThat(asp.numberOfSentences() > (long) asp.flatten().size());
    }
    
    @Test
    public void flattenWithDepth() {
        APISentenceTree asp = new APISentenceTree();

        APIToken t1 = new APIToken("[?] [type, namespace].operation1()");
        APIToken t2 = new APIToken("[?] [type, namespace].operation2()");
        APIToken t3 = new APIToken("[?] [type, namespace].operation3()");
        APIToken t4 = new APIToken("[?] [type, namespace].operation4()");
        APIToken t5 = new APIToken("[?] [type, namespace].operation5()");
        APIToken t6 = new APIToken("[?] [type, namespace].operation6()");
        APIToken t7 = new APIToken("[?] [type, namespace].operation7()");
        APIToken t8 = new APIToken("[?] [type, namespace].operation8()");
        APIToken t9 = new APIToken("[?] [type, namespace].operation9()");
        APIToken t10 = new APIToken("[?] [type, namespace].operation10()");
        APIToken t11 = new APIToken("[?] [type, namespace].operation11()");
        APIToken t12 = new APIToken("[?] [type, namespace].operation12()");
        APIToken t13 = new APIToken("[?] [type, namespace].operation13()");
        APIToken t14 = new APIToken("[?] [type, namespace].operation14()");
        APIToken t15 = new APIToken("[?] [type, namespace].operation15()");
        APIToken t16 = new APIToken("[?] [type, namespace].operation16()");
        APIToken t17 = new APIToken("[?] [type, namespace].operation17()");
        APIToken t18 = new APIToken("[?] [type, namespace].operation18()");
        APIToken t19 = new APIToken("[?] [type, namespace].operation19()");
        APIToken t20 = new APIToken("[?] [type, namespace].operation20()");
        APIToken t21 = new APIToken("[?] [type, namespace].operation21()");
        APIToken t22 = new APIToken("[?] [type, namespace].operation22()");
        APIToken t23 = new APIToken("[?] [type, namespace].operation23()");
        APIToken t24 = new APIToken("[?] [type, namespace].operation24()");
        APIToken t25 = new APIToken("[?] [type, namespace].operation25()");
        APIToken t26 = new APIToken("[?] [type, namespace].operation26()");
        APIToken t27 = new APIToken("[?] [type, namespace].operation27()");
        APIToken t28 = new APIToken("[?] [type, namespace].operation28()");
        APIToken t29 = new APIToken("[?] [type, namespace].operation29()");
        APIToken t30 = new APIToken("[?] [type, namespace].operation30()");
        APIToken t31 = new APIToken("[?] [type, namespace].operation31()");
        APIToken t32 = new APIToken("[?] [type, namespace].operation32()");
        APIToken t33 = new APIToken("[?] [type, namespace].operation33()");
        APIToken t34 = new APIToken("[?] [type, namespace].operation34()");
        APIToken t35 = new APIToken("[?] [type, namespace].operation35()");
        APIToken t36 = new APIToken("[?] [type, namespace].operation36()");
        APIToken t37 = new APIToken("[?] [type, namespace].operation37()");
        APIToken t38 = new APIToken("[?] [type, namespace].operation38()");
        APIToken t39 = new APIToken("[?] [type, namespace].operation39()");
        APIToken t40 = new APIToken("[?] [type, namespace].operation40()");
        APIToken t41 = new APIToken("[?] [type, namespace].operation41()");

        asp.addToken(t1);
        asp.addToken(t2);
        asp.addToken(t3);
        asp.addToken(t4);
        asp.addToken(t5);
        APISentenceTree asp5_1 = asp.branch(t5);
        APISentenceTree asp5_2 = asp.branch(t5);
        asp5_1.addToken(t6);
        asp5_2.addToken(t7);
        asp5_2.addToken(t8);
        APISentenceTree asp8_1 = asp5_2.branch(t8);
        APISentenceTree asp8_2 = asp5_2.branch(t8);
        asp8_1.addToken(t9);
        asp8_1.addToken(t11);
        asp8_2.addToken(t10);
        asp8_2.addToken(t12);
        APISentenceTree asp12_1 = asp8_2.branch(t12);
        APISentenceTree asp12_2 = asp8_2.branch(t12);
        asp12_1.addToken(t13);
        asp12_2.addToken(t14);
        asp12_2.addToken(t15);
        APISentenceTree asp15_1 = asp12_2.branch(t15);
        APISentenceTree asp15_2 = asp12_2.branch(t15);
        asp15_1.addToken(t16);
        asp15_2.addToken(t17);
        asp12_2.addToken(t18);
        asp12_2.addToken(t19);
        APISentenceTree asp19_1 = asp12_2.branch(t19);
        APISentenceTree asp19_2 = asp12_2.branch(t19);
        asp19_1.addToken(t20);
        asp19_2.addToken(t21);
        asp12_2.addToken(t22);
        asp12_2.addToken(t23);
        APISentenceTree asp23_1 = asp12_2.branch(t23);
        APISentenceTree asp23_2 = asp12_2.branch(t23);
        asp23_1.addToken(t24);
        asp23_2.addToken(t25);
        asp12_2.addToken(t26);
        asp8_2.addToken(t27);
        asp5_2.addToken(t28);
        asp5_2.addToken(t29);
        asp5_2.addToken(t30);
        asp5_2.addToken(t31);
        APISentenceTree asp31_1 = asp5_2.branch(t31);
        APISentenceTree asp31_2 = asp5_2.branch(t31);
        asp31_1.addToken(t32);
        asp31_2.addToken(t33);
        asp5_2.addToken(t34);
        asp.addToken(t35);
        APISentenceTree asp35_1 = asp.branch(t35);
        asp35_1.addToken(t36);
        asp.addToken(t37);
        APISentenceTree asp37_1 = asp.branch(t37);
        APISentenceTree asp37_2 = asp.branch(t37);
        asp37_1.addToken(t38);
        asp37_2.addToken(t39);
        asp.addToken(t40);
        asp.addToken(t41);

        assertEquals(28, asp.flatten(2).size());
    }
    
    @Test
    public void addToken() {
        APISentenceTree asp = new APISentenceTree();
        APIToken token = new APIToken("[?] [type, namespace].operation()");
        asp.addToken(token);
        
        assertEquals(1, asp.getTokens().size());
        assertThat(asp.getTokens().get(0), is(token));
    }
    
    @Test
    public void getTokens() {
        APISentenceTree asp = new APISentenceTree();
        APIToken token = new APIToken("[?] [type, namespace].operation()");
        List<APIToken> tokenList = new ArrayList();
        asp.addToken(token);
        tokenList.add(token);

        assertThat(asp.getTokens(), is(tokenList));
    }
    
    @Test
    public void getBranches() {
        APISentenceTree asp = new APISentenceTree();

        APIToken t1 = new APIToken("[?] [type, namespace].operation1()");
        APIToken t2 = new APIToken("[?] [type, namespace].operation2()");
        APIToken t3 = new APIToken("[?] [type, namespace].operation3()");
        APIToken t4 = new APIToken("[?] [type, namespace].operation4()");

        asp.addToken(t1);
        APISentenceTree aspB = asp.branch(t2);
        aspB.addToken(t3);
        aspB.addToken(t4);

        Map<APIToken, List<APISentenceTree>> branches = new HashMap<>();
        branches.put(t2, new ArrayList<APISentenceTree>());
        branches.get(t2).add(new APISentenceTree());
        branches.get(t2).get(0).addToken(t3);
        branches.get(t2).get(0).addToken(t4);

        assertThat(asp.getBranches(), IsMapContaining.hasKey(t2));
//        assertThat(asp.getBranches(), IsMapContaining.hasEntry(t2, branches.get(t2)));
    }
}
