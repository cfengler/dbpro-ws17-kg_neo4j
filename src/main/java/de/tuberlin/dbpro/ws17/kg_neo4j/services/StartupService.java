package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.Company;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.CompanyInfo;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaFormationYear;
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
        //testSearchByLabel();

//        List<Company> companies = companyService.getCompaniesByFormationYear(1985);






        //1. importiere abstract (wichtig)
        //final DbPediaAbstractService service = applicationContext.getBean(DbPediaAbstractService.class);
        //service.importDbPediaAbstract();

        //2. importiere Tochtergesellschaftsbeziehungen parent und susidiary (wichtig)
        //final DbPediaAffiliatedCompanyRelationService service = applicationContext.getBean(DbPediaAffiliatedCompanyRelationService.class);
        //service.importDbPediaAffiliatedCompanyRelations();
        //2.1 importiere Land und Bundesland

        //final DbPediaLocationService service = applicationContext.getBean(DbPediaLocationService.class);
        //service.importDbPediaLocations();
        //2.2 import Formation Year
        //final DbPediaFormationYearService service = applicationContext.getBean(DbPediaFormationYearService.class);
        //service.importDbPediaFormationYears();
        //2.3 import Number of Employees
        //final DbPediaNumberOfEmployeesService service = applicationContext.getBean(DbPediaNumberOfEmployeesService.class);
        //service.importDbPediaNumberOfEmployees();
        //TODO:
        //TODO: 3. importier property names (vielleicht auch nicht)
        //TODO: 3.1 21010 DbProIds haben keine HAS_LABEL Relation (MATCH (n:DbProId) WHERE NOT (n)-[:HAS_LABEL]-() RETURN count(n))
        //TODO: 4. Slogan importieren aber nur einen
        //TODO: 5. Set<Label> prüfen ob doppelt wichtig ist, sonst raus damit und nur ein Label
        //TODO: 5.1 erstelle Hauptlabels basierend auf DbPediaLebls erst de, dann en, dann was anderes
        //TODO:



        //TODO: bloß nicht nochmal ausführen, dann wird alles doppelt
        //final LabelsService labelsService = applicationContext.getBean(LabelsService.class);
        //labelsService.importNodesAndRelations();
    }

    private void testSearchByLabel() {
        final CompanyService companyService = applicationContext.getBean(CompanyService.class);
        //List<Company> companies = companyService.getCompaniesByLabelContainingName("Porsche Automobil Holding");

        //List<Company> companies = companyService.getCompaniesByLabelContainingName("Volkswagen Group");
        List<Company> companies = companyService.getCompaniesByAbstractContainingValue("Baumeister");
        //List<Company> companies = companyService.getCompaniesByLabelContainingName("Volkswagen Commercial Vehicles");
        //List<Company> companies = companyService.getCompaniesByLabelContainingName("Lufthansa");
        //List<Company> companies = companyService.getCompaniesByLabelContainingName("Activision");
        if (companies == null || companies.size() == 0) {
            System.out.println("Keine Daten geunden.");
        }
        else {
            for (Company company:companies) {
                printCompany(company);
            }
        }
    }

    private void printCompany(Company company) {
        System.out.println("###COMPANY BEGIN###");
        System.out.println("id: " + company.dbProId.getValue());
        System.out.println("name: " + company.name);
        System.out.println("dbPediaAbstract: " + company.dbPediaAbstract);

        System.out.print("DbPediaCountries: ");
        company.dbPediaLocationCountries.forEach(s -> System.out.print(s + ";"));
        System.out.println();

        System.out.print("DbPediaCities: ");
        company.dbPediaLocationCities.forEach(s -> System.out.print(s + ";"));
        System.out.println();

        if (company.dbPediaParentCompany != null) {
            System.out.println("Parent-Company: " + company.dbPediaParentCompany.name);
        }
        if (company.dbPediaSubsidiaries != null) {
            for (Company subsidiary:company.dbPediaSubsidiaries) {
                System.out.println("Subsidiary: " + subsidiary.name);
            }
        }
        if (company.dbPediaFormationYears != null) {
            System.out.print("dbPediaFormationYears: ");
            company.dbPediaFormationYears.forEach(s -> System.out.print(s + ";"));
            System.out.println();
        }
        if (company.dbPediaNumberOfEmployees != null) {
            System.out.print("dbPediaNumberOfEmployees: ");
            company.dbPediaNumberOfEmployees.forEach(s -> System.out.print(s + ";"));
            System.out.println();
        }
        System.out.println("###COMPANY END###");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
