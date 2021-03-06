package de.tuberlin.dbpro.ws17.kg_neo4j.common;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class ParseHelper {

    public static String getGcdIdFromText(String text) {
        String[] textSplit = text.split("_");
        if (textSplit.length == 2) {
            String gcdIdAsString = textSplit[1].substring(0, textSplit[1].length() - 1);
            return gcdIdAsString;
        }
        return null;
    }

    public static Long getPermIdFromText(String text) {
        String[] textSplit = text.split("_");
        if (textSplit.length == 2) {
            try {
                String permIdAsString = textSplit[1].substring(0, textSplit[1].length() - 1);
                return Long.parseLong(permIdAsString);
            }
            catch (NumberFormatException nfe) {

            }
        }
        return 0L;
    }

    private static String deDbPediaPrefix = "<http://de.dbpedia.org/resource/";

    public static String getDeDbPediaIdFromText(String text) {
        String tempText = text.replace(deDbPediaPrefix, "");
        return StringEscapeUtils.unescapeJava(tempText.substring(0, tempText.length() - 1));
    }

    private static String dbPediaPrefix = "<http://dbpedia.org/resource/";

    public static String getDbPediaIdFromText(String text) {
        String tempText = text.replace(dbPediaPrefix, "");
        String unescapedText = tempText.substring(0, tempText.length() - 1);
        try {
            unescapedText = URLDecoder.decode(unescapedText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return unescapedText;
    }

    public static String getDataProviderName(String text) {
        return text.substring(0, text.length() - 2);
    }

    public static String getDbPediaLabelValue(String text) {
        try {
            String labelText = StringEscapeUtils.unescapeJava(text.substring(0, text.lastIndexOf("@")));

            return labelText.substring(1, labelText.length() - 1);
        }
        catch (Exception e) {
            return "";
        }
    }

    public static String getDbPediaLabelLanguageKey(String text) {
        try {
            String languageKeyText = StringEscapeUtils.unescapeJava(text.substring(text.lastIndexOf("@") + 1));

            return languageKeyText;
        } catch (Exception e) {
            String a = text.replace("\\\\\"","\"");
            String b = text.replace("\\\"","\"");
            String c = text.replace("\"","\"");
            String b2 = text.replaceAll("\\\"","\"");

            return "";
        }

    }

    public static String getDbPediaAbstractValue(String text) {
        int lastIndexOfAt = text.lastIndexOf("@");
        String value = StringEscapeUtils.unescapeJava(text.substring(1, lastIndexOfAt - 1));
        return value;
    }

    public static String getDbPediaAbstractLanguageKey(String text) {
        int lastIndexOfAt = text.lastIndexOf("@");
        String languageKey = StringEscapeUtils.unescapeJava(text.substring(lastIndexOfAt + 1));
        return languageKey;
    }

    public static String getDbPediaCountryNameFromText(String text) {
        String result = getDbPediaIdFromText(text);
        result = result.replaceAll("_", " ");
        return result;
    }

    public static int getDbPediaFormationYearFromText(String text) {
        int result = Integer.parseInt(text.substring(1,5));
        return result;
    }

    public static int getDbPediaNumberOfEmployeesValue(String text) {
        String textSubstring = text.substring(1, text.indexOf("^^") - 1);
        //String[] textSplit = text.split("\\^\\^");
        //String textSubstring = textSplit[0].substring(1, text.length() - 1);
        int result = Integer.parseInt(textSubstring);
        return result;
    }

}
