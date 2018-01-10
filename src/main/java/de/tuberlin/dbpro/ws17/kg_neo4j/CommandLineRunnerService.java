package de.tuberlin.dbpro.ws17.kg_neo4j;

import de.tuberlin.dbpro.ws17.kg_neo4j.services.LabelsService;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerService implements CommandLineRunner, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void run(String... strings) throws Exception {

        final LabelsService labelsService = applicationContext.getBean(LabelsService.class);
        labelsService.importNodesAndRelations();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
