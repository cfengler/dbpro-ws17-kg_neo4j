package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.*;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope(value = "singleton")
public class CompanyService {

    private DbProIdService dbProIdService = null;
    private DbPediaLabelRepository dbPediaLabelRepository = null;
    private DbPediaAbstractRepository dbPediaAbstractRepository = null;
    private DbPediaAffiliatedCompanyRelationRepository dbPediaAffiliatedCompanyRelationRepository = null;
    private DbPediaLocationCountryRepository dbPediaLocationCountryRepository = null;
    private DbPediaLocationCityRepository dbPediaLocationCityRepository = null;
    private DbPediaFormationYearRepository dbPediaFormationYearRepository = null;

    @Autowired
    public CompanyService(DbProIdService dbProIdService,
                          DbPediaLabelRepository dbPediaLabelRepository,
                          DbPediaAbstractRepository dbPediaAbstractRepository,
                          DbPediaAffiliatedCompanyRelationRepository dbPediaAffiliatedCompanyRelationRepository,
                          DbPediaLocationCountryRepository dbPediaLocationCountryRepository,
                          DbPediaLocationCityRepository dbPediaLocationCityRepository,
                          DbPediaFormationYearRepository dbPediaFormationYearRepository) {
        this.dbProIdService = dbProIdService;
        this.dbPediaAffiliatedCompanyRelationRepository = dbPediaAffiliatedCompanyRelationRepository;
        this.dbPediaLabelRepository = dbPediaLabelRepository;
        this.dbPediaAbstractRepository = dbPediaAbstractRepository;
        this.dbPediaLocationCountryRepository = dbPediaLocationCountryRepository;
        this.dbPediaLocationCityRepository = dbPediaLocationCityRepository;
        this.dbPediaFormationYearRepository = dbPediaFormationYearRepository;
    }

    public List<Company> getCompaniesByLabelContainingName(String name) {
        List<DbPediaLabel> dbPediaLabels = dbPediaLabelRepository.findByValueContainsIgnoreCase(name);
        Set<Long> dbProIdValues = new HashSet<>();
        dbPediaLabels.stream().forEach(dbPediaLabel -> dbProIdValues.add(dbPediaLabel.getDbProId().getValue()));

        if (dbProIdValues.size() > 0) {
            List<DbProId> dbProIds = dbProIdService.findDbProIdsByValueIn(dbProIdValues);
            List<Company> result = new ArrayList<>(convertDbProIdsToCompanies(dbProIds));
            return result;
        }
        //TODO: search somewhere else...
        return new ArrayList<>();
    }

    public List<Company> getCompaniesByAbstractContainingValue(String value) {
        List<DbPediaAbstract> dbPediaAbstracts = this.dbPediaAbstractRepository.findByValueContainsIgnoreCase(value);
        Set<Long> dbProIdValues = new HashSet<>();
        dbPediaAbstracts.stream().forEach(dbPediaAbstract -> dbProIdValues.add(dbPediaAbstract.getDbProId().getValue()));

        if (dbProIdValues.size() > 0) {
            List<DbProId> dbProIds = dbProIdService.findDbProIdsByValueIn(dbProIdValues);
            List<Company> result = new ArrayList<>(convertDbProIdsToCompanies(dbProIds));
            return result;
        }

        return new ArrayList<>();
    }

    public List<Company> getCompaniesByLocationContainingName(String name) {
        List<DbPediaLocationCountry> dbPediaLocationCountries = this.dbPediaLocationCountryRepository.findByNameContainsIgnoreCase(name);
        List<DbPediaLocationCity> dbPediaLocationCities = this.dbPediaLocationCityRepository.findByNameContainsIgnoreCase(name);
        Set<Long> dbProIdValues = new HashSet<>();
        dbPediaLocationCountries.forEach(dbPediaLocationCountry -> dbPediaLocationCountry.getDbProIds().forEach(dbProId -> dbProIdValues.add(dbProId.getValue())));
        dbPediaLocationCities.forEach(dbPediaLocationCity -> dbPediaLocationCity.getDbProIds().forEach(dbProId -> dbProIdValues.add(dbProId.getValue())));

        List<DbProId> dbProIds = dbProIdService.findDbProIdsByValueIn(dbProIdValues);
        List<Company> result = convertDbProIdsToCompanies(dbProIds);
        //resolveDbPediaAffiliatedCompanyRelations(result);
        return result;
    }

    public List<Company> getCompaniesByFormationYear(Integer year) {
        List<DbPediaFormationYear> dbPediaFormationYears = this.dbPediaFormationYearRepository.findByValue(year);
        Set<Long> dbProIdValues = new HashSet<>();
        dbPediaFormationYears.forEach(dbPediaFormationYear -> dbPediaFormationYear.getDbProIds().forEach(dbProId -> dbProIdValues.add(dbProId.getValue())));
        List<DbProId> dbProIds = dbProIdService.findDbProIdsByValueIn(dbProIdValues);
        List<Company> result = convertDbProIdsToCompanies(dbProIds);

        return result;
    }

    private List<Company> convertDbProIdsToCompanies(Iterable<DbProId> dbProIds) {
        List<Company> result = new ArrayList<>();
        for (DbProId dbProId:dbProIds) {
            Company company = convertDbProIdToCompany(dbProId);

            result.add(company);
        }
        return result;
    }

    private Company convertDbProIdToCompany(DbProId dbProId) {
        Company company = new Company();
        company.dbProId = dbProId;
        company.name = selectName(dbProId.getDbPediaLabels());
        company.dbPediaAbstract = selectDbPediaAbstract(dbProId.getDbPediaAbstract());
        company.dbPediaLocationCountries = getDbPediaLocationCountries(dbProId.getDbPediaLocationCountries());
        company.dbPediaLocationCities = getDbPediaLocationCities(dbProId.getDbPediaLocationCities());
        company.dbPediaFormationYears = getDbPediaFormationYears(dbProId.getDbPediaFormationYears());
        company.dbPediaNumberOfEmployees = getDbPediaNumberOfEmployees(dbProId.getDbPediaNumberOfEmployees());
        return company;
    }

    public void resolveDbPediaAffiliatedCompanyRelations(List<Company> companies) {
        for (Company company:companies) {
            resolveDbPediaParentCompanyRelation(company);
        }
    }

    public void resolveDbPediaAffiliatedCompanyRelation(Company company) {
        resolveDbPediaParentCompanyRelation(company);
        resolveDbPediaSubsidiariesRelations(company);
    }

    private void resolveDbPediaParentCompanyRelation(Company company) {
        if (company.dbProId.getDbPediaParentCompany() != null && company.dbPediaParentCompany == null) {
            Optional<DbPediaAffiliatedCompanyRelation> relation = dbPediaAffiliatedCompanyRelationRepository.findById(company.dbProId.getDbPediaParentCompany().getId());
            if (relation.isPresent()) {
                DbProId dbProIdParentCompany = dbProIdService.findDbProIdByValue(relation.get().getParentCompany().getValue());
                if (dbProIdParentCompany != null) {
                    company.dbPediaParentCompany = convertDbProIdToCompany(dbProIdParentCompany);
                    resolveDbPediaParentCompanyRelation(company.dbPediaParentCompany);
                }

            }
            //notLoadedRelations.add(company.dbProId.getDbPediaParentCompany());
        }
    }

    private void resolveDbPediaSubsidiariesRelations(Company company) {
        if (company.dbProId.getDbPediaSubsidiaries() != null && company.dbPediaSubsidiaries == null) {
            List<Long> customIds = company.dbProId.getDbPediaSubsidiaries()
                .stream()
                .map(dbPediaAffiliatedCompanyRelation -> dbPediaAffiliatedCompanyRelation.getCustomId())
                .collect(Collectors.toList());
            List<DbPediaAffiliatedCompanyRelation> relations = dbPediaAffiliatedCompanyRelationRepository.findByCustomIdIn(customIds);

            List<Long> dbProIdValues = relations.stream().map(relation -> relation.getSubsidiary().getValue()).collect(Collectors.toList());

            List<DbProId> dbProIds = dbProIdService.findDbProIdsByValueIn(dbProIdValues);
            company.dbPediaSubsidiaries = convertDbProIdsToCompanies(dbProIds);

            for (Company subsidiary:company.dbPediaSubsidiaries) {
                resolveDbPediaSubsidiariesRelations(subsidiary);
            }
        }
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

    private List<String> getDbPediaLocationCountries(Set<DbPediaLocationCountry> dbPediaLocationCountries) {
        if (dbPediaLocationCountries == null || dbPediaLocationCountries.size() == 0) {
            return new ArrayList<>();
        }

        return dbPediaLocationCountries.stream().map(dbPediaLocationCountry -> dbPediaLocationCountry.getName()).collect(Collectors.toList());
    }

    private List<String> getDbPediaLocationCities(Set<DbPediaLocationCity> dbPediaLocationCities) {
        if (dbPediaLocationCities == null || dbPediaLocationCities.size() == 0) {
            return new ArrayList<>();
        }

        return dbPediaLocationCities.stream().map(dbPediaLocationCity -> dbPediaLocationCity.getName()).collect(Collectors.toList());
    }

    private List<Integer> getDbPediaFormationYears(Set<DbPediaFormationYear> dbPediaFormationYearSet) {
        if (dbPediaFormationYearSet == null || dbPediaFormationYearSet.size() == 0) {
            return new ArrayList<>();
        }

        return dbPediaFormationYearSet.stream().map(dbPediaFormationYear -> dbPediaFormationYear.getValue()).collect(Collectors.toList());
    }

    private List<Integer> getDbPediaNumberOfEmployees(Set<DbPediaNumberOfEmployees> dbPediaNumberOfEmployees) {
        if (dbPediaNumberOfEmployees == null || dbPediaNumberOfEmployees.size() == 0) {
            return new ArrayList<>();
        }

        return dbPediaNumberOfEmployees.stream().map(dbPediaFormationYear -> dbPediaFormationYear.getValue()).collect(Collectors.toList());
    }

}
