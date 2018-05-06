package ngram;

import opennlp.tools.languagemodel.NGramLanguageModel;
import opennlp.tools.ngram.NGramModel;
import opennlp.tools.ngram.NGramUtils;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.StringList;

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

    private final String SentenceString;
    private StringList tokens;
    private NGramLanguageModel model;


    public ModelBuilder(String APIFilePath) throws IOException {

        Stream<String> apiSentences = Files.lines(Paths.get(APIFilePath), StandardCharsets.UTF_8);
        SentenceString = apiSentences.collect(Collectors.joining());
        this.buildTokensAndModel();

    }

    private void buildTokensAndModel() {

        this.tokens = new StringList(WhitespaceTokenizer.INSTANCE.tokenize(this.SentenceString));
        this.model = new NGramLanguageModel();
        this.model.add(this.tokens, 2, 5);

    }

    public double getTokenProbability(StringList tokens) {

        return this.model.calculateProbability(tokens);

    }

    public StringList getNextTokens(String compare) {

        return this.model.predictNextTokens(new StringList(compare));

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
