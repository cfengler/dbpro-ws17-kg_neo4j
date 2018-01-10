package de.tuberlin.dbpro.ws17.kg_neo4j.old.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EquivalenceRelation {

    public long dbProId = 0;
    public String gcdId = "";
    public List<Long> permIds = null;
    public List<String> deDbPediaIds = null;
    public List<String> dbPediaIds = null;
    public List<String> tags = null;

    public EquivalenceRelation() {
        permIds = new ArrayList<>();
        deDbPediaIds = new ArrayList<>();
        dbPediaIds = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public String getDbProId_GcdId_PermIdsString() {
        Set<Long> uniquePermIds = new HashSet<>(permIds);
        if (uniquePermIds.size() < permIds.size()) {
            String test = "haha";
        }

        return dbProId + ";" +
            gcdId + ";" +
            uniquePermIds.stream().map(l -> l.toString()).collect(Collectors.joining("|"));
    }

    public String getDbProId_GcdId_PermIds_DeDbPediaIdsString() {
        Set<String> uniqueDeDbPediaIds = new HashSet<>(deDbPediaIds);
        if (uniqueDeDbPediaIds.size() < deDbPediaIds.size()) {
            String test = "haha";
        }

        return getDbProId_GcdId_PermIdsString() + ";" +
            uniqueDeDbPediaIds.stream().collect(Collectors.joining("|"));
    }

    public String getDbProId_GcdId_PermIds_DeDbPediaIds_DbPediaIdsString() {
        Set<String> uniqueDbPediaIds = new HashSet<>(dbPediaIds);
        if (uniqueDbPediaIds.size() < dbPediaIds.size()) {
            String test = "haha";
        }

        return getDbProId_GcdId_PermIds_DeDbPediaIdsString() + ";" +
            uniqueDbPediaIds.stream().collect(Collectors.joining("|"));
    }

    public String getEr_TagsString() {
        Set<String> uniqueTags = new HashSet<>(tags);
        if (uniqueTags.size() < dbPediaIds.size()) {
            String test = "haha";
        }

        return getDbProId_GcdId_PermIds_DeDbPediaIds_DbPediaIdsString() + ";" +
            uniqueTags.stream().collect(Collectors.joining("|"));
    }


}
