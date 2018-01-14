package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.*;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaAffiliatedCompanyRelationRepository;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaLabelRepository;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbProIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Component
@Scope(value = "singleton")
public class CompanyService {

    private DbProIdRepository dbProIdRepository = null;
    //private DbPediaLabelRepository dbPediaLabelRepository = null;
    private DbPediaAffiliatedCompanyRelationRepository dbPediaAffiliatedCompanyRelationRepository = null;

    @Autowired
    public CompanyService(DbProIdRepository dbProIdRepository,
                          DbPediaAffiliatedCompanyRelationRepository dbPediaAffiliatedCompanyRelationRepository) {
                          //DbPediaLabelRepository dbPediaLabelRepository) {
        this.dbProIdRepository = dbProIdRepository;
        this.dbPediaAffiliatedCompanyRelationRepository = dbPediaAffiliatedCompanyRelationRepository;
        //this.dbPediaLabelRepository = dbPediaLabelRepository;
    }

    public List<Company> findBySearchString(String searchString) {

        //Page<DbProId> dbProIdsTemp = dbProIdRepository.findAll(PageRequest.of(0, 1000, Sort.Direction.ASC, "value"));
        //checkDbProIds(dbProIdsTemp);
        //importNodesForDbPediaIds(dbPediaLabelsByDbPediaId, dbPediaIds);
        //while (dbProIdsTemp.hasNext()) {
        //    System.out.println("ich arbeite");
        //    dbProIdsTemp = dbProIdRepository.findAll(dbProIdsTemp.nextPageable());
            //checkDbProIds(dbProIdsTemp);
        //}

        List<DbProId> dbProIds = dbProIdRepository.getDbProIdsThatHasLabelContainingValue(searchString);
        if (dbProIds.size() > 0) {
            return findByDbProIds(dbProIds);
        }
        //TODO: search somewhere else...
        return new ArrayList<>();
    }

//    private void checkDbProIds(Page<DbProId> dbProIdsTemp) {
//        for (DbProId dbProId:dbProIdsTemp) {
//            if (dbProId.getDbPediaLabels() == null || dbProId.getDbPediaLabels().size() == 0) {
//                System.out.println("ahaha");
//            }
//        }
//    }



    private List<Company> findByDbProIds(Iterable<DbProId> dbProIds) {
        List<Company> result = new ArrayList<>();
        for (DbProId dbProId:dbProIds) {
            Company company = findByDbProId(dbProId);
            if (company != null) {
                result.add(company);
            }
        }
        return result;
    }

    public Company findByDbProId(DbProId dbProId) {
        Optional<DbProId> searchedDbProId = this.dbProIdRepository.findById(dbProId.getId());
        if (searchedDbProId.isPresent()) {
            Company company = new Company();
            company.id = searchedDbProId.get().getId();
            company.name = selectName(searchedDbProId.get().getDbPediaLabels());// .iterator().next().getValue();
            company.dbPediaAbstract = selectDbPediaAbstract(searchedDbProId.get().getDbPediaAbstract());
            if (searchedDbProId.get().getDbPediaParentCompany() != null) {
                company.parentCompany = getCompanyInfoByDbPediaAffiliatedCompanyRelationParentCompany(searchedDbProId.get().getDbPediaParentCompany());
            }

            if (searchedDbProId.get().getDbPediaSubsidiaries() != null) {

                company.subsidiaries = getCompanyInfoByDbPediaAffiliatedCompanyRelationsSubsidiary(searchedDbProId.get().getDbPediaSubsidiaries());// DbProIds(searchedDbProId.get().getDbPediaSubsidiaries().stream().map(t -> t.getSubsidiary()));
            }

            return company;
        }
        return null;
    }

    public CompanyInfo getCompanyInfoByDbProId(DbProId dbProId) {
        Optional<DbProId> searchedDbProId = this.dbProIdRepository.findById(dbProId.getId());
        if (searchedDbProId.isPresent()) {
            CompanyInfo companyInfo = new CompanyInfo();
            companyInfo.id = searchedDbProId.get().getId();
            companyInfo.name = selectName(searchedDbProId.get().getDbPediaLabels());
            return companyInfo;
        }
        return null;
    }

    public CompanyInfo getCompanyInfoByDbPediaAffiliatedCompanyRelationParentCompany(DbPediaAffiliatedCompanyRelation dbPediaAffiliatedCompanyRelation) {
        Optional<DbPediaAffiliatedCompanyRelation> relation = dbPediaAffiliatedCompanyRelationRepository.findById(dbPediaAffiliatedCompanyRelation.getId());
        if (relation.isPresent()) {
            return getCompanyInfoByDbProId(relation.get().getParentCompany());
        }
        return null;
    }

    public List<CompanyInfo> getCompanyInfoByDbPediaAffiliatedCompanyRelationsSubsidiary(Set<DbPediaAffiliatedCompanyRelation> dbPediaAffiliatedCompanyRelations) {
        List<CompanyInfo> result = new ArrayList<>();

        dbPediaAffiliatedCompanyRelations.forEach(dbPediaAffiliatedCompanyRelation -> {
            Optional<DbPediaAffiliatedCompanyRelation> relation = dbPediaAffiliatedCompanyRelationRepository.findById(dbPediaAffiliatedCompanyRelation.getId());
            if (relation.isPresent()) {
                result.add(getCompanyInfoByDbProId(relation.get().getSubsidiary()));
            }
        });

        return result;
    }

    private String preferredLanguageKey = "de";
    private String fallbackLanguageKey = "en";

    private String selectName(Set<DbPediaLabel> dbPediaLabels) {
        if (dbPediaLabels == null || dbPediaLabels.size() == 0) {
            return null;
        }

        for (DbPediaLabel dbPediaLabel:dbPediaLabels) {
            if (dbPediaLabel.getLanguageKey().equals(preferredLanguageKey)) {
                return dbPediaLabel.getValue();
            }
        }

        for (DbPediaLabel dbPediaLabel:dbPediaLabels) {
            if (dbPediaLabel.getLanguageKey().equals(fallbackLanguageKey)) {
                return dbPediaLabel.getValue();
            }
        }

        return dbPediaLabels.iterator().next().getValue();
    }

    private String selectDbPediaAbstract(Set<DbPediaAbstract> dbPediaAbstracts) {
        if (dbPediaAbstracts == null || dbPediaAbstracts.size() == 0) {
            return null;
        }

        for (DbPediaAbstract dbPediaAbstract:dbPediaAbstracts) {
            if (dbPediaAbstract.getLanguageKey().equals(preferredLanguageKey)) {
                return dbPediaAbstract.getValue();
            }
        }

        for (DbPediaAbstract dbPediaAbstract:dbPediaAbstracts) {
            if (dbPediaAbstract.getLanguageKey().equals(fallbackLanguageKey)) {
                return dbPediaAbstract.getValue();
            }
        }

        return dbPediaAbstracts.iterator().next().getValue();
    }

}
