import ngram.NgramRecommenderClient;
import org.junit.Test;

import java.io.IOException;

public class NgramRecommenderClientTest {
    private static String outputDir = "MockData/output/Microsoft.Win32.txt";

    @Test
    public void NgramRecommender () throws IOException {

        NgramRecommenderClient nrc = new NgramRecommenderClient();
        nrc.train(outputDir);
    }
}
