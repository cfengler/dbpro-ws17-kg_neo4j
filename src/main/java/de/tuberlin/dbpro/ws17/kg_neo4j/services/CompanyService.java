package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.Company;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaId;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbProId;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaLabelRepository;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbProIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope(value = "singleton")
public class CompanyService {

    private DbProIdRepository dbProIdRepository = null;
    private DbPediaLabelRepository dbPediaLabelRepository = null;

    @Autowired
    public CompanyService(DbProIdRepository dbProIdRepository,
                          DbPediaLabelRepository dbPediaLabelRepository) {
        this.dbProIdRepository = dbProIdRepository;
        this.dbPediaLabelRepository = dbPediaLabelRepository;
    }

    public List<Company> findBySearchString(String searchString) {

        Page<DbProId> dbProIdsTemp = dbProIdRepository.findAll(PageRequest.of(0, 1000, Sort.Direction.ASC, "value"));
        checkDbProIds(dbProIdsTemp);
        //importNodesForDbPediaIds(dbPediaLabelsByDbPediaId, dbPediaIds);
        while (dbProIdsTemp.hasNext()) {
            System.out.println("ich arbeite");
            dbProIdsTemp = dbProIdRepository.findAll(dbProIdsTemp.nextPageable());
            checkDbProIds(dbProIdsTemp);
        }


        List<DbProId> dbProIds = dbProIdRepository.getDbProIdsThatHasLabelContainingValue(searchString);
        if (dbProIds.size() > 0) {
            return findByDbProIds(dbProIds);
        }
        //TODO: search somewhere else...
        return null;
    }

    private void checkDbProIds(Page<DbProId> dbProIdsTemp) {
        for (DbProId dbProId:dbProIdsTemp) {
            if (dbProId.getDbPediaLabels() == null || dbProId.getDbPediaLabels().size() == 0) {
                System.out.println("ahaha");
            }
        }
    }

    private List<Company> findByDbProIds(Iterable<DbProId> dbProIds) {
        List<Company> result = new ArrayList<>();
        for (DbProId dbProId:dbProIds) {
            Optional<DbProId> searchedDbProId = this.dbProIdRepository.findById(dbProId.getId());
            if (searchedDbProId.isPresent()) {
                Company company = new Company();
                company.id = searchedDbProId.get().getId();
                company.name = searchedDbProId.get().getDbPediaLabels().iterator().next().getValue();

                result.add(company);
            }
        }
        return result;
    }
}
