import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.datastructures.Tuple;
import ngram.NgramRecommenderClient;
import opennlp.tools.util.StringList;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class NgramRecommenderClientTest {

    @Test
    public void NgramRecommender() throws IOException {
        NgramRecommenderClient nrc = new NgramRecommenderClient("MockData/models/NUnit.Framework.xml");
        Set<Tuple<IMethodName, Double>> modeloutput = nrc.query(
                new StringList(
                        "NUnit.Framework.Assert,AreEqual",
                        "NUnit.Framework.Assert,AreEqual"
                ));
        assertEquals(modeloutput.toString(), "[<<NUnit.Framework.Assert,AreSame>, 0.2857142857142857>]");
    }
    
    @Test
    public void query() throws IOException {
        NgramRecommenderClient nrc = new NgramRecommenderClient();
        nrc.train("MockData/sentences/Namespace.txt");
        Set<Tuple<IMethodName, Double>> a1 = nrc.query(new StringList("Namespace.Some.Class5,Operation1"));
        Set<Tuple<IMethodName, Double>> a2 = nrc.query(new StringList(
                "Namespace.Some.Class1,new",
                "Namespace.Some.Class1,Operation1"
        ));
        Set<Tuple<IMethodName, Double>> a3 = nrc.query(new StringList("Namespace.Some.Class2,Operation1"));
        Set<Tuple<IMethodName, Double>> a4 = nrc.query(new StringList(
                "Namespace.Some.Class4,Operation3",
                "Namespace.Some.Class5,Operation2"
        ));
        Set<Tuple<IMethodName, Double>> a5 = nrc.query(new StringList("Namespace.Some.Class5,Operation1"));

        assertTrue(a1.iterator().next().getFirst().toString().equals("<Namespace.Some.Class5,Operation2>"));
        assertTrue(a2.iterator().next().getFirst().toString().equals("<Namespace.Some.Class1,Operation2>"));
        assertTrue(a3.iterator().next().getFirst().toString().equals("<Namespace.Some.Class5,Operation1>"));
        assertTrue(a4.iterator().next().getFirst().toString().equals("<Namespace.Some.Class5,Operation1>"));
        assertTrue(a5.iterator().next().getFirst().toString().equals("<Namespace.Some.Class5,Operation2>"));
    }
}
