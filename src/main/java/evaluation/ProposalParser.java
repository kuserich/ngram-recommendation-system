package evaluation;

import opennlp.tools.util.StringList;

import java.util.ArrayList;

public class ProposalParser {

    private static String selected;
    private StringList tokens;

    ProposalParser(String proposal) {
        selected = proposal;
    }

    public StringList getTokens() {
        parseSelectedProposal();
        return tokens;
    }


    private void parseSelectedProposal() {

        String cleanedInput = replaceStartingStrings(selected);
        tokens = splitAndCleane(cleanedInput);
    }

    /**
     * Cleanes starting strings
     *
     * @param selected Give Selected Input
     * @return String
     */
    private String replaceStartingStrings(String selected) {
        return selected.replace("get ", "")
                .replace("static ", "")
                .replace("directly ", "")
                .replace("set get ", "");
    }

    /**
     * Removes type declarations,
     * Splites with lookback regex
     * Ignores unwanted entry parts
     * <p>
     * returns null if the parsing was not possible
     *
     * @param replaced Given to Replace Input
     * @return Returns stringlist for input into model
     */
    private StringList splitAndCleane(String replaced) {
        ArrayList<String> cleaned = removeTypeDeclarations(replaced);


        ArrayList<String> tokenList = new ArrayList<>();

        for (String e : cleaned) {
            String[] content = lookBehindSplit(e);

            for (String entry : content) {

                //Again ignore unneeded method stuff
                if (!entry.startsWith(".") && !entry.endsWith("()") && entry.length() > 1) {
                    String[] object = entry.replace("[", "").replace("]", "").split(", ");

                    if (object.length >= 2 && !object[0].isEmpty() && !object[1].isEmpty()) {
                        tokenList.add(buildToken(object[0], object[1]));
                    }
                }
            }
        }
        if (tokenList.size() >= 2) {
            return getOutput(tokenList);
        } else {
            return null;
        }
    }

    /**
     * StringList output builder
     *
     * @param tokenList List that has the built tokens
     * @return StringList with concat tokens
     */
    private StringList getOutput(ArrayList<String> tokenList) {
        StringBuilder tokenString = new StringBuilder();
        for (String token : tokenList) {
            tokenString.append(token).append(",");
        }

        return new StringList(tokenString.replace(tokenString.length() - 1, tokenString.length(), "").toString());
    }


    /**
     * Explode the string with lookback
     *
     * @param e Part of Selected String
     * @return Returns splitted Returns splitted String[]
     */
    private String[] lookBehindSplit(String e) {
        return e.split("(?<=\\])|(?=\\[)");
    }

    /**
     * Build the Token with type and operation
     *
     * @param type      Type of the Proposal
     * @param operation Operation String of Proposal
     * @return Concated Token in form for Model
     */
    private String buildToken(String type, String operation) {
        return type + "," + operation;
    }

    /**
     * Split and Remove Type declaraions from given string
     *
     * @param replaced Preprocesed input
     * @return Removed Type declaration from String
     */
    private ArrayList<String> removeTypeDeclarations(String replaced) {
        ArrayList<String> output = new ArrayList<>();
        String[] splitted = replaced.split("(?<=\\] )");

        for (String item : splitted) {
            if (!item.contains("p:")) {
                output.add(item);
            }
        }

        return output;
    }
}
