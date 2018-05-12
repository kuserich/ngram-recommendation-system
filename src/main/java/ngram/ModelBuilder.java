package ngram;

import opennlp.tools.languagemodel.NGramLanguageModel;
import opennlp.tools.ngram.NGramModel;
import opennlp.tools.ngram.NGramUtils;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.StringList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelBuilder {

    private final String SentenceString = "";
    private StringList tokens;
    private NGramLanguageModel model;

    private final int NGRAM_MIN_LENGTH = 2;
    private final int NGRAM_MAX_LENGTH = 5;

    public ModelBuilder() {
        model = new NGramLanguageModel(NGRAM_MAX_LENGTH);
    }

    public void train(String trainFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(trainFile));
        String line;
        while((line = br.readLine()) != null) {
            addTokensFromLineToModel(line);
        }
    }
    
    private void addTokensFromLineToModel(String apiSentenceLine) {
        apiSentenceLine = apiSentenceLine.substring(1, apiSentenceLine.length()-1);
        StringList tokens = new StringList(apiSentenceLine.split(", "));
        model.add(tokens, NGRAM_MIN_LENGTH, NGRAM_MAX_LENGTH);
    }
    
    public double getTokenProbability(StringList tokens) {
        return model.calculateProbability(tokens);
    }

    public StringList predictNextTokens(String compare) {
        return model.predictNextTokens(new StringList(compare));
    }

    public void nGramModel() {
        NGramModel nGramModel = new NGramModel();
        nGramModel.add(this.tokens, 2, 5);

        System.out.println("Tokens: " + this.tokens);

        for (StringList ngram : nGramModel) {
            System.out.println(nGramModel.getCount(ngram) + " - " + ngram);
            System.out.println(NGramUtils.calculateLaplaceSmoothingProbability(ngram, nGramModel, 0.2));
        }
        System.out.println("Total ngrams: " + nGramModel.numberOfGrams());

    }
}
