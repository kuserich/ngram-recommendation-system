package ngram;


import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.ICallsRecommender;
import cc.kave.rsse.calls.datastructures.Tuple;
import extractor.APIToken;
import opennlp.tools.util.StringList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExampleClient implements ICallsRecommender {


    /**
     * use the recommender-specific query format to query proposals
     *
     * @return a sorted set of the proposed methods plus probability
     **/
    @Override
    public Set<Tuple<IMethodName, Double>> query(Object args) {

        ArrayList input = (ArrayList) args;

        try {
            ModelBuilder model = new ModelBuilder();
            model.train((String) input.get(0));
            String stringToCompare = (String) input.get(1);

            StringList recommendation = model.predictNextTokens(stringToCompare);
            Double proba = model.getTokenProbability(recommendation);

            System.out.println("The Input String is " + stringToCompare);
            System.out.println("The Proposed Methods");
            System.out.println(recommendation);
            System.out.println("The probability");
            System.out.println(proba);


            APIToken methodName = new APIToken();
            methodName.setOperation(recommendation);
            Set<Tuple<IMethodName, Double>> output = new HashSet<Tuple<IMethodName, Double>>();


            output.add(Tuple.newTuple(methodName, proba));

            return output;

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public Set<Tuple<IMethodName, Double>> query(Context context) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Set<Tuple<IMethodName, Double>> query(Context context, List list) {
        return null;
    }
}
