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
}
