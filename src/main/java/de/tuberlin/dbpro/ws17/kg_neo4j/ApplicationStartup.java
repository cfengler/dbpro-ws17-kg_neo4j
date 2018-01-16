package de.tuberlin.dbpro.ws17.kg_neo4j;

import javafx.embed.swing.JFXPanel;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan
@EnableNeo4jRepositories("de.tuberlin.dbpro.ws17.kg_neo4j.repositories")
public class ApplicationStartup {

    public static void main(String[] args) {
        //JFXPanel panel = new JFXPanel();
        ConfigurableApplicationContext springContext = SpringApplication.run(ApplicationStartup.class, args);
    }

    @Bean
    public SessionFactory getSessionFactory() {
        return new SessionFactory(configuration(), "de.tuberlin.dbpro.ws17.kg_neo4j.domain");
    }

    @Bean
    public Neo4jTransactionManager transactionManager() throws Exception {
        return new Neo4jTransactionManager(getSessionFactory());
    }

    @Bean
    public org.neo4j.ogm.config.Configuration configuration() {
        return new org.neo4j.ogm.config.Configuration.Builder()
            .uri("bolt://win.roschy.net:7687")
            .build();
    }
}
