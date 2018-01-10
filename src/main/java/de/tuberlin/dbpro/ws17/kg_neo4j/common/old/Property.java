package de.tuberlin.dbpro.ws17.kg_neo4j.common.old;

public class Property {
    public PropertyType type = PropertyType.STRING;
    public String name;
    public Object value;

    public Property(PropertyType type, String name, Object value) {
        this(name, value);

        this.type = type;
    }

    public Property(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
