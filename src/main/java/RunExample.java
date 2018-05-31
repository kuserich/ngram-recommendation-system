import cc.kave.commons.utils.io.Directory;
import evaluation.NgramRecommenderEvaluation;
import extractor.SentenceExtractor;
import ngram.NgramRecommenderClient;
import opennlp.tools.util.StringList;
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
//        System.out.println("\n+-----------------------------------------------------------+");
//        System.out.println("|                                                           |");
//        System.out.println("| Stepwise API usage assistance using ngram language models |");
//        System.out.println("|                                                           |");
//        System.out.println("+-----------------------------------------------------------+");
//        System.out.println("| SENTENCE EXTRACTION                                       |");
//        System.out.println("+-----------------------------------------------------------+");
//
//        extractSentences();
//
//        System.out.println("+-----------------------------------------------------------+");
//        System.out.println("| MDOEL TRAINING                                            |");
//        System.out.println("+-----------------------------------------------------------+");
//        
//        trainModels();
        predictionExample();
        evaluationExample();
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
     * @throws IOException 
     *          thrown if there is an error with reading or writing the files
     * @see #trainModel(String, String)
     *          method that trains a single model
     */
    public static void trainModels() throws IOException {
        IoHelper.createDirectoryIfNotExists(modelsDir);
        System.out.println("[INFO]\t Training models");
        Set<String> inputFiles = new Directory(extractionOutputDir).findFiles(s -> s.endsWith(".txt"));
        int cnt = 1;
        for (String file : inputFiles) {
            System.out.println("[INFO]\t Training " + String.valueOf(cnt++) + "/" + inputFiles.size() + " (" + file + ")");
            trainModel(extractionOutputDir + file, modelsDir);
        }
    }

    /**
     * Trains the given model and stores it to the given output directory.
     *
     * @param inputFile       
     *          file containing the API sentences to train a model
     * @param outputDirectory 
     *          directory into which the trained model will be stored
     * @throws IOException 
     *          thrown if there is an error with reading or writing the files
     */
    private static void trainModel(String inputFile, String outputDirectory) throws IOException {
        String modelName = inputFile.split("/")[1].replace(".txt", "");
        NgramRecommenderClient nrc = new NgramRecommenderClient();
        nrc.train(inputFile);

        System.out.println("[INFO]\t Serializing model (" + modelName + ")");
        nrc.serialize(new FileOutputStream(outputDirectory + modelName + ".xml"));
    }

    /**
     * 
     * @throws IOException
     */
    public static void predictionExample() throws IOException {
        NgramRecommenderClient nrc = new NgramRecommenderClient("models/NUnit.Framework.xml");
        System.out.println(nrc.query(new StringList("NUnit.Framework.Assert,AreEqual")));

        System.out.println(nrc.query(
                new StringList(
                        "NUnit.Framework.Assert,AreEqual",
                        "NUnit.Framework.Assert,AreEqual"
                )));
        System.out.println(nrc.query(
                new StringList(
                        "eifach öppis",
                        "Ngfjkghfert,gfjgf"
                )));
        System.out.println(nrc.query(
                new StringList(
                        "NUnit.Framework.Assert,AreEqual",
                        "NUnit.Framework.Assert,IsTrue"
                )));
        System.out.println(nrc.query(
                new StringList(
                        "NUnit.Framework.Assert,AreEqual",
                        "NUnit.Framework.Assert,IsTrue",
                        "NUnit.Framework.Assert,AreEqual",
                        "NUnit.Framework.Assert,IsTrue"
                )));

        System.out.println(nrc.query(
                new StringList(
                        "NUnit.Framework.Assert,AreEqual",
                        "NUnit.Framework.CollectionAssert,AreEqual",
                        "NUnit.Framework.Assert,AreEqual",
                        "NUnit.Framework.Assert,AreEqual"
                )));

    }

    /**
     * 
     * @throws IOException
     */
    public static void evaluationExample() throws IOException {
        NgramRecommenderEvaluation.readAllEvents(eventsDir);
    }
}
