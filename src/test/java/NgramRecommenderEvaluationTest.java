import evaluation.NgramRecommenderEvaluation;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NgramRecommenderEvaluationTest {

    @Test
    public void findAllUsers() {
        List<String> zips = NgramRecommenderEvaluation.findAllUsers("MockData");

        assertEquals(zips.size(), 1);
    }



}
