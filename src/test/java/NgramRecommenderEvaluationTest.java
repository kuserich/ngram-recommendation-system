import evaluation.NgramRecommenderEvaluation;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NgramRecommenderEvaluationTest {
    private static String mockDir = "MockData/event";

    @Test
    public void findAllUsers() {
        List<String> zips = NgramRecommenderEvaluation.findAllUsers(mockDir);
        assertEquals(zips.size(), 1);
    }

    @Test
    public void readAllEventsTest() throws IOException {
        NgramRecommenderEvaluation.readAllEvents(mockDir);
    }



}
