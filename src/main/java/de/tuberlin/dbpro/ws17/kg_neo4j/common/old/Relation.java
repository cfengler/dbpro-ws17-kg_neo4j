package de.tuberlin.dbpro.ws17.kg_neo4j.common.old;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Relation {

    public Node firstNode;
    public Node secondNode;

    public List<String> labels;
    public Map<String, Property> properties;

    public Relation(Node firstNode, Node secondNode) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;

        this.labels = new ArrayList<String>();
        this.properties = new HashMap<String, Property>();
    }

    public String getCypher(String firstNodeKey, String relationKey, String secondNodeKey) {
        String result = "";

        result += firstNode.getCypher(firstNodeKey);
        result += "-[" + relationKey;
        result += CypherService.getCypher(labels);
        result += CypherService.getCypther(properties);
        result += "]->";
        result += secondNode.getCypher(secondNodeKey);

        return result;
    }
}
