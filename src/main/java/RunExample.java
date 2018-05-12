import extractor.SentenceExtractor;
import ngram.ExampleClient;

import java.util.ArrayList;

public class RunExample {
    
    public static String outputDir = "output/";
    public static String contextsDir = "Contexts-170503/linq2db";
//    public static String contextsDir = "Contexts-170503/duplicati/duplicati/Duplicati CommandLine Only.sln-contexts.zip";

    public static String eventsDir = "Events-170301-2";

    public static void main(String[] args) {
        SentenceExtractor se = new SentenceExtractor();
        se.extract(contextsDir, outputDir);
    }
    
}
