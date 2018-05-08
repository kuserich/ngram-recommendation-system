import extractor.APISentenceTree;
import extractor.APIToken;
import extractor.SentenceExtractor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class RunExample {

    /*
     * download the interaction data and unzip it into the root of this project (at
     * the level of the pom.xml). Unpack it, you should now have a folder that
     * includes a bunch of folders that have dates as names and that contain .zip
     * files.
     */
    public static String eventsDir = "Events-170301-2";

    /*
     * download the context data and follow the same instructions as before.
     */
    public static String contextsDir = "Contexts-170503/";

    public static void main(String[] args) throws IOException {
        SentenceExtractor se = new SentenceExtractor();
        List<List<APIToken>> apiSentences = se.extract(contextsDir);

        PrintWriter writer = new PrintWriter("rawApiLines.txt", "UTF-8");
        String output = "";
        for (List<APIToken> sentence : apiSentences) {
            writer.println(" ");

            for (APIToken token : sentence) {
                output = output + " " + token.toString().replaceAll("\\s+","");
            }
            writer.println(output);
            output = "";
            writer.println(" ");
        }
    }

    public static void testFlatten() {
        APISentenceTree asp = new APISentenceTree();

        APIToken t1 = new APIToken();
        t1.setNamespace("System");
        t1.setOperation("M1");
        APIToken t2 = new APIToken();
        t2.setNamespace("System");
        t2.setOperation("M2");
        APIToken t3 = new APIToken();
        t3.setNamespace("System");
        t3.setOperation("M3");
        APIToken t4 = new APIToken();
        t4.setNamespace("System");
        t4.setOperation("M4");
        APIToken t5 = new APIToken();
        t5.setNamespace("System");
        t5.setOperation("M5");
        APIToken t6 = new APIToken();
        t6.setNamespace("System");
        t6.setOperation("M6");
        APIToken t7 = new APIToken();
        t7.setNamespace("System");
        t7.setOperation("M7");
        APIToken t8 = new APIToken();
        t8.setNamespace("System");
        t8.setOperation("M8");
        APIToken t9 = new APIToken();
        t9.setNamespace("System");
        t9.setOperation("M9");
        APIToken t10 = new APIToken();
        t10.setNamespace("System");
        t10.setOperation("M10");
        APIToken t11 = new APIToken();
        t11.setNamespace("System");
        t11.setOperation("M11");
        APIToken t12 = new APIToken();
        t12.setNamespace("System");
        t12.setOperation("M12");
        APIToken t13 = new APIToken();
        t13.setNamespace("System");
        t13.setOperation("M13");

        asp.addToken(t1);
        asp.addToken(t2);
        APISentenceTree b1 = asp.branch(t2);
        b1.addToken(t3);
        b1.addToken(t4);
        APISentenceTree b21 = b1.branch(t4);
        APISentenceTree b22 = b1.branch(t4);
        b21.addToken(t5);
        b22.addToken(t6);
        b22.addToken(t7);
        b1.addToken(t8);
        asp.addToken(t9);


        // M1, M2, M9
        // M1, M2, M3, M4, M8, M9
        // M1, M2, M3, M4, M5, M8, M9
        // M1, M2, M3, M4, M6, M7, M8, M9


        for (List<APIToken> sentence : asp.flatten()) {
            System.out.println("(");
            for (APIToken token : sentence) {
                System.out.println("\t" + token.toString());
            }
            System.out.println(")");
            System.out.println();
        }
    }

}
