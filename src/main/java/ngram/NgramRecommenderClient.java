package ngram;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.ICallsRecommender;
import cc.kave.rsse.calls.datastructures.Tuple;
import extractor.APIToken;
import opennlp.tools.languagemodel.NGramLanguageModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.StringList;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NgramRecommenderClient extends NGramLanguageModel implements ICallsRecommender<StringList> {

    private static final int DEFAULT_N = 5;

    private static final int NGRAM_MIN_LENGTH = 2;
    private static final int NGRAM_MAX_LENGTH = 5;
    
    private static String modelName = "";
    
    public NgramRecommenderClient() {
        super(DEFAULT_N);
    }
    
    public NgramRecommenderClient(String model) throws IOException {
        this(model, DEFAULT_N);
    }
    
    public NgramRecommenderClient(String model, int n) throws IOException {
        super(new FileInputStream(model), n);
        setModelNameFromFileName(model);
    }

    /**
     * Trains a language model with the given file.
     * 
     * @param trainFile
     *          file that includes API sentences to train
     *          
     * @throws IOException
     *          thrown if there is an error with reading the file
     */
    public void train(String trainFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(trainFile));
        String line;
        while((line = br.readLine()) != null) {
            add(new StringList(WhitespaceTokenizer.INSTANCE.tokenize(line)), 
                    NGRAM_MIN_LENGTH, NGRAM_MAX_LENGTH);
        }
        setModelNameFromFileName(trainFile);
    }

    /**
     * Takes a model file path and sets {@link #modelName} to the name of
     * the file without its path and extension.
     * 
     * @param modelFile
     *          path to a model file
     */
    private void setModelNameFromFileName(String modelFile) {
        if(modelFile.contains(".txt")) {
            modelFile = modelFile.replace(".txt", "");
        }
        if(modelFile.contains("/")) {
            String[] filenameSplit = modelFile.split("/");
            modelName = filenameSplit[filenameSplit.length-1];
        } else {
            modelName = modelFile;
        }
    }

    
    @Override
        public Set<Tuple<IMethodName, Double>> query(StringList stringToCompare) {
        Set<Tuple<IMethodName, Double>> recommendation = new HashSet<>();
        
        StringList predictedTokenStrings = predictNextTokens(stringToCompare);
        
        Iterator<String> iter = predictedTokenStrings.iterator();
        // add only the first APIToken of all predicted tokens
        if(iter.hasNext()) {
            try {
                String tokenString = iter.next();
                String type = tokenString.split(",")[0];
                String operation = tokenString.split(",")[1];

                StringBuilder tokenStringBuilder = new StringBuilder(); 
                tokenStringBuilder.append("[?] [");
                tokenStringBuilder.append(type);
                tokenStringBuilder.append(", ");
                tokenStringBuilder.append(modelName, 0, modelName.length()-4);
                tokenStringBuilder.append("].");
                tokenStringBuilder.append(operation);
                tokenStringBuilder.append("()");
                
                APIToken token = new APIToken(tokenStringBuilder.toString());
                
                Tuple<IMethodName, Double> prediction = Tuple.newTuple(token, calculateProbability(predictedTokenStrings));
                recommendation.add(prediction);

            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println("[WARN]\tInvalid tokenString, skipping result");
//                System.out.println(e);
            }
            
        }
        return recommendation;
    }

    @Override
    public Set<Tuple<IMethodName, Double>> query(Context context) {
        return null;
    }

    @Override
    public Set<Tuple<IMethodName, Double>> query(Context context, List list) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }
    
}
