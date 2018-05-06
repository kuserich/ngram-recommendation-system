package ngram;


import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.ICallsRecommender;
import cc.kave.rsse.calls.datastructures.Tuple;
import opennlp.tools.util.StringList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExampleClient implements ICallsRecommender {


    /**
     * use the recommender-specific query format to query proposals
     *
     * @param Object with index 0 as API File Path, index 1 with Input String the query in a format specfic to the recommender
     * @return a sorted set of the proposed methods plus probability
     **/
    @Override
    public Set<Tuple<IMethodName, Double>> query(Object args) {

        ArrayList input = (ArrayList) args;

        try {
            ModelBuilder model = new ModelBuilder((String) input.get(0));
            String stringToCompare = (String) input.get(1);

            StringList recommendation = model.getNextTokens(stringToCompare);

            System.out.println("The Input String is " + stringToCompare);
            System.out.println("The Proposed Methods");
            System.out.println(recommendation);
            System.out.println("The probability");
            System.out.println(model.getTokenProbability(recommendation));


            //TODO: somehow have the response as a IMethodName
            Set<Tuple<IMethodName, Double>> output = null;

            return null;

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
