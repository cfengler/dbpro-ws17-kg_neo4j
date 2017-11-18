package de.tuberlin.dbpro.ws17.kg_neo4j.common;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Relation extends Entity {

    public Node firstNode;
    public Node secondNode;

    public List<String> labels;
    public Map<String, String> properties;

    public Relation(Node firstNode, Node secondNode) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
    }

    public String getCypher(String key) {
        String result = "[" + key;

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
        result += "]";

        return result;
    }
}
