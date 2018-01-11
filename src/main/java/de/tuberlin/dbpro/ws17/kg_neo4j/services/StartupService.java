package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.Company;
import de.tuberlin.dbpro.ws17.kg_neo4j.services.LabelsService;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupService implements CommandLineRunner, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void run(String... strings) throws Exception {
        //final CompanyService companyService = applicationContext.getBean(CompanyService.class);
        //List<Company> companies = companyService.findBySearchString("Global Load Control");

        //TODO: 1. importiere abstract (wichtig)

        //TODO: 2. importiere Tochtergesellschaftsbeziehungen parent und susidiary (wichtig)
        //TODO: 2.1 importiere Land und Bundesland
        //TODO:
        //TODO: 3. importier property names (vielleicht auch nicht)
        //TODO: 3.1 21010 DbProIds haben keine HAS_LABEL Relation (MATCH (n:DbProId) WHERE NOT (n)-[:HAS_LABEL]-() RETURN count(n))
        //TODO: 4. Slogan importieren aber nur einen
        //TODO: 5. Set<Label> prüfen ob doppelt wichtig ist, sonst raus damit und nur ein Label
        //TODO: 5.1 erstelle Hauptlabels basierend auf DbPediaLebls erst de, dann en, dann was anderes
        //TODO:


        //for (Company c:companies) {
        //    System.out.println(c.name);
        //}
        //TODO: bloß nicht nochmal ausführen, dann wird alles doppelt
        //final LabelsService labelsService = applicationContext.getBean(LabelsService.class);
        //labelsService.importNodesAndRelations();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
