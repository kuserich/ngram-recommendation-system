import cc.kave.commons.utils.io.Directory;
import extractor.SentenceExtractor;
import ngram.NgramRecommenderClient;
import util.IoHelper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

public class RunExample {
    
    public static String modelsDir = "models/";
    public static String extractionOutputDir = "output/";
    public static String contextsDir = "Contexts-170503";

    public static String eventsDir = "Events-170301-2";

    public static void main(String[] args) throws IOException {
//        extractSentences();
        trainModels();
//        predictionExample();
    }

    /**
     * Extracts all API sentences from all contexts in {@link #contextsDir} and stores them 
     * in {@link #extractionOutputDir}
     */
    public static void extractSentences() {
        SentenceExtractor se = new SentenceExtractor();
        se.extract(contextsDir, extractionOutputDir);
    }

    /**
     * Trains all models in {@link #extractionOutputDir} and stores them in {@link #modelsDir}.
     * 
     * @see #trainModel(String, String)
     *          method that trains a single model
     *          
     * @throws IOException
     *          thrown if there is an error with reading or writing the files
     */
    public static void trainModels() throws IOException {
        IoHelper.createDirectoryIfNotExists(modelsDir);
        System.out.println("[INFO]\t Training models");
        Set<String> inputFiles = new Directory(extractionOutputDir).findFiles(s -> s.endsWith(".txt"));
        int cnt = 1;
        for(String file : inputFiles) {
            System.out.println("[INFO]\t Training "+String.valueOf(cnt++)+"/"+inputFiles.size()+" ("+file+")");
            trainModel(extractionOutputDir+file, modelsDir);
        }
    }

    /**
     * Trains the given model and stores it to the given output directory.
     * 
     * @param inputFile
     *          file containing the API sentences to train a model
     * @param outputDirectory
     *          directory into which the trained model will be stored
     *          
     * @throws IOException
     *          thrown if there is an error with reading or writing the files
     */
    private static void trainModel(String inputFile, String outputDirectory) throws IOException {
        String modelName = inputFile.split("/")[1].replace(".txt", "");
        NgramRecommenderClient nrc = new NgramRecommenderClient();
        nrc.train(inputFile);
        
        System.out.println("[INFO]\t Serializing model ("+modelName+")");
        nrc.serialize(new FileOutputStream(outputDirectory+modelName));
    }
    
    public static void predictionExample() throws IOException {
        NgramRecommenderClient nrc = new NgramRecommenderClient("models/System.Linq");
        System.out.println(nrc.query("Select"));
    }
}
