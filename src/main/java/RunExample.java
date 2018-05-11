import extractor.SentenceExtractor;

public class RunExample {
    
    public static String outputDir = "output/";
    public static String contextsDir = "Contexts-170503/";
    public static String eventsDir = "Events-170301-2";

    public static void main(String[] args) {
        SentenceExtractor se = new SentenceExtractor();
        se.extract(contextsDir, outputDir);
    }
    
}
