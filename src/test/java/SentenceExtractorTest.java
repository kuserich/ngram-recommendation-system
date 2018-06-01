import extractor.SentenceExtractor;
import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class SentenceExtractorTest {
    
    private static String mockExtractionOutputDir = "MockData/mockOutput/";
    private static String mockContextsDir = "MockData/contexts";
    private static int fileOutputSize = 27;

    @After
    public void removeBuiltTxt() {
        File dir = new File(mockExtractionOutputDir);

        if (dir.isDirectory()) {
            for (File file : dir.listFiles())
                if (!file.isDirectory())
                    file.delete();
        }
    }


    @Test
    public void extractSentencesFilesCreatedTest() {
        SentenceExtractor se = new SentenceExtractor();
        se.extract(mockContextsDir, mockExtractionOutputDir);

        File dir = new File(mockExtractionOutputDir);
        if(dir.isDirectory()) {
            assertEquals(dir.listFiles().length, fileOutputSize);
        }
    }
}
