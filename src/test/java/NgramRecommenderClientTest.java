import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.datastructures.Tuple;
import ngram.NgramRecommenderClient;
import opennlp.tools.util.StringList;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class NgramRecommenderClientTest {
    private static String outputDir = "MockData/output/Microsoft.Win32.txt";

    @Test
    public void NgramRecommender () throws IOException {

        NgramRecommenderClient nrc = new NgramRecommenderClient("MockData/models/NUnit.Framework.xml");
        Set<Tuple<IMethodName, Double>> modeloutput = nrc.query(
                new StringList(
                        "NUnit.Framework.Assert,AreEqual",
                        "NUnit.Framework.Assert,AreEqual"
                ));
        assertEquals(modeloutput.toString(), "[<<NUnit.Framework.Assert,AreSame>, 0.2857142857142857>]");
    }
}
