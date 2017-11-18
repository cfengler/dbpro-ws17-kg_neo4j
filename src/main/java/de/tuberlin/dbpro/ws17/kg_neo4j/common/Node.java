package de.tuberlin.dbpro.ws17.kg_neo4j.common;

import java.util.*;

public class Node {

    public List<String> labels;
    public Map<String, String> properties;

    public Node() {
        this.labels = new ArrayList<String>();
        this.properties = new HashMap<String, String>();
    }

    public String getCypher(String key) {
        String result = "(" + key;

        result += CypherService.getCypher(labels);

        result += CypherService.getCypther(properties);

        result += ")";

        return result;
    }
}
