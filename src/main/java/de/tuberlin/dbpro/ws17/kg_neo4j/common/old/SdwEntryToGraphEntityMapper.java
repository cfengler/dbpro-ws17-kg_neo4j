package de.tuberlin.dbpro.ws17.kg_neo4j.common.old;

public class SdwEntryToGraphEntityMapper {

    private static String gcdIdPrefix = "<http://corp.dbpedia.org/resource/gcd_";

    public static Node getNodeGcdId(String text) {
        String[] textSplit = text.split("_");
        if (textSplit.length == 2 && textSplit[0].equals(gcdIdPrefix)) {
            String gcdIdAsString = textSplit[1].substring(0, textSplit[1].length() - 1);
            Long gcdId = Long.parseLong(gcdIdAsString);

            return getNodeByGcdId(gcdId);
        }
        else {
            return null;
        }
    }

    public static Node getNodeByGcdId(long gcdId) {
        Node result = new Node();
        result.labels.add("GcdId");
        result.properties.put("Value", new Property(PropertyType.LONG, "Value", gcdId));
        return result;
    }

    public static Node getNodeByDbProId(long dbProId) {
        Node result = new Node();
        result.labels.add("DbProId");
        result.properties.put("Value", new Property(PropertyType.LONG, "Value", dbProId));
        return result;
    }

}
