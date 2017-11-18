package de.tuberlin.dbpro.ws17.kg_neo4j.common;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.Entity;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Node extends Entity {

    public List<String> labels;
    public Map<String, String> properties;

    public String getCypher(String key) {
        String result = "(" + key;

        for (String label: labels) {
            result += ":" + label;
        }

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
        result += ")";

        return result;
    }
}
