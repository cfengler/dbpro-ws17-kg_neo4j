package de.tuberlin.dbpro.ws17.kg_neo4j.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Relation {

    public Node firstNode;
    public Node secondNode;

    public List<String> labels;
    public Map<String, String> properties;

    public Relation(Node firstNode, Node secondNode) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;

        this.labels = new ArrayList<String>();
        this.properties = new HashMap<String, String>();
    }

    public String getCypher(String key) {
        String result = "[" + key;

        result += CypherService.getCypher(labels);

        result += CypherService.getCypther(properties);

        result += "]";

        return result;
    }
}
