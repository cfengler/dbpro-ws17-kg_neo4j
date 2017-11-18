package de.tuberlin.dbpro.ws17.kg_neo4j.common;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CypherService {
    public static String getCypher(List<String> labels) {
        String result = "";
        for (String label: labels) {
            result += ":" + label;
        }
        return result;
    }

    public static  String getCypther(Map<String, String> properties) {
        String result = "";

        if (!properties.isEmpty()) {
            result += " { ";

            for (Iterator<Map.Entry<String, String>> mapEntryIterator = properties.entrySet().iterator(); mapEntryIterator.hasNext();) {
                Map.Entry<String, String> mapEntry = mapEntryIterator.next();

                result += mapEntry.getKey() + ": '" + mapEntry.getValue() + "'";

                if (mapEntryIterator.hasNext()) {
                    result += ", ";
                }
            }

            result += " }";
        }

        return result;
    }
}
