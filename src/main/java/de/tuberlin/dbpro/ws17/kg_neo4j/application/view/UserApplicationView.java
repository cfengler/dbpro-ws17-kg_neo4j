package de.tuberlin.dbpro.ws17.kg_neo4j.application.view;

import de.tuberlin.dbpro.ws17.kg_neo4j.ApplicationStartup;
import de.tuberlin.dbpro.ws17.kg_neo4j.application.Layout;
import de.tuberlin.dbpro.ws17.kg_neo4j.application.MainNeighbourLayout;
import de.tuberlin.dbpro.ws17.kg_neo4j.application.Model;
import de.tuberlin.dbpro.ws17.kg_neo4j.application.ParentSubsidiaryLayout;
import de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel.CellTypeViewModel;
import de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel.GraphViewModel;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.Company;
import de.tuberlin.dbpro.ws17.kg_neo4j.services.CompanyService;
import javafx.application.Application;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

public class UserApplicationView extends Application {

    private static final BorderPane pnFoundation = new BorderPane();
    private static final GridPane pnSearch = new GridPane();
    private static final BorderPane pnSearchField = new BorderPane();
    private static final HBox pnSearchType = new HBox();
    private static final VBox pnSelection = new VBox();
    private static final ScrollPane spSelection = new ScrollPane();
    private static final BorderPane pnSelectionHeadline = new BorderPane();
    private static final VBox pnSelectionReturn = new VBox();

    private static final Label lblHeadline = new Label();
    private static final Label lblSelection = new Label();
    private static final Label lblSelectionReturn = new Label();

    private static final ScrollPane spInformation = new ScrollPane();
    private static final Label lblInformation = new Label();

    private static final Button btnSearch = new Button();
    private static final Button btnSelectionReturn = new Button();

    private static final TextField tfSearch = new TextField();

    private static final RadioButton rbNodes = new RadioButton();
    private static final RadioButton rbLabels = new RadioButton();
    private static final RadioButton rbEdges = new RadioButton();
    private static final ToggleGroup tgSearch = new ToggleGroup();

    private static final String fontname = "Helvetica Neue";
    private static CompanyService companyService;
    private static List<Company> listCompanies;
    private static GraphViewModel graph;
    private static Model model;
    private static Layout layout;

    private AnnotationConfigApplicationContext context;
    public static long lastTimestamp = 0;

    public void start(Stage primaryStage) {
        context = new AnnotationConfigApplicationContext(ApplicationStartup.class);
        companyService = context.getBean(CompanyService.class);
        //List<Company> test = companyService.getCompaniesByLocationContainingName("Lufthansa");

        primaryStage.setTitle("DBPRO");

        initSearch();
        initSelection();

        lblInformation.setStyle("-fx-wrap-text: true;");
        lblInformation.setPrefWidth(300);
        spInformation.setContent(lblInformation);
        pnFoundation.setRight(spInformation);

        primaryStage.setScene(new Scene(pnFoundation, 1000, 500));
        primaryStage.show();
    }

    private static void initSearch() {
        //Fundamentales Pane
        pnSearch.setAlignment(Pos.TOP_CENTER);
        pnSearch.setVgap(5);
        pnSearchType.setAlignment(Pos.TOP_CENTER);
        pnSearchType.setSpacing(20);

        //Ueberschrift
        lblHeadline.setText("DBPRO Neo4J Knowledge Graph");
        lblHeadline.setFont(new Font("Helvetica Neue", 30));
        pnSearch.add(lblHeadline, 0, 0);

        //Suchfeld
        tfSearch.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                search();
            }
        });
        tfSearch.setPromptText("Suchbegriff");
        tfSearch.requestFocus();
        pnSearchField.setCenter(tfSearch);

        //Suchbutton
        btnSearch.setText("suchen");
        btnSearch.setFont(new Font(fontname, 13));
        btnSearch.setOnAction(event -> search());
        pnSearchField.setRight(btnSearch);
        pnSearch.add(pnSearchField, 0, 1);

        //Radiobuttons fuer die Suchart
        rbNodes.setText("Knoten");
        rbNodes.setSelected(true);
        rbNodes.setToggleGroup(tgSearch);
        //pnSearchType.getChildren().add(rbNodes);

        rbLabels.setText("Bezeichnung");
        rbLabels.setToggleGroup(tgSearch);
        //pnSearchType.getChildren().add(rbLabels);

        rbEdges.setText("Kante");
        rbEdges.setToggleGroup(tgSearch);
        //pnSearchType.getChildren().add(rbEdges);
        pnSearch.add(pnSearchType, 0, 2);

        pnFoundation.setTop(pnSearch);
    }

    private static void initSelection() {
        //Fundamentale Panes
        pnSelectionHeadline.setPadding(new Insets(5, 5, 5, 5));
        pnSelection.setSpacing(5);
        pnSelectionHeadline.setCenter(spSelection);
        pnFoundation.setLeft(pnSelectionHeadline);

        //Ueberschrift
        lblSelection.setText("Auswahl");
        lblSelection.setFont(new Font(fontname, 20));
        pnSelectionHeadline.setTop(lblSelection);

        //Pane mit Auswahlmoeglichkeiten
        spSelection.setContent(pnSelection);
        spSelection.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spSelection.setStyle("-fx-background-color:transparent;");

        //Pane nach Auswahl
        btnSelectionReturn.setText("<- zurück");
        btnSelectionReturn.setFont(new Font(fontname, 13));
        pnSelectionReturn.setSpacing(5);
        btnSelectionReturn.setOnAction(event -> unhideSelection());
    }

    public static void search() {
        System.out.println("suchen...");
        String term = tfSearch.getText();
        int selection;
        if(term.equals("")) {
            return;
        }

        pnSelection.getChildren().remove(0, pnSelection.getChildren().size());

        if(term.matches("[0-9]+")) {
            listCompanies = companyService.getCompaniesByFormationYear(Integer.parseInt(term));
            lblSelection.setText("Gründungsjahr");
        } else {
            listCompanies = companyService.getCompaniesByLocationContainingName(term);
            lblSelection.setText("Firmensitz");
            if(listCompanies.size() == 0) {
                listCompanies = companyService.getCompaniesByLabelContainingName(term);
                lblSelection.setText("Name");
                if(listCompanies.size() == 0) {
                    listCompanies = companyService.getCompaniesByAbstractContainingValue(term);
                    lblSelection.setText("Abstract");
                }
            }
        }

        for(Company c: listCompanies) {
            Button btn = new Button(c.name);
            btn.setOnAction(event -> displayCompany(c));
            pnSelection.getChildren().add(btn);
        }
    }

    public static void displayCompany(Company company) {
        companyService.resolveDbPediaAffiliatedCompanyRelation(company);
        hideSelection(company.name);
        updateInformation(company);

        graph = new GraphViewModel();
        pnFoundation.setCenter(graph.getScrollPane());
        model = graph.getModel();
        graph.beginUpdate();

        model.addCell(company, CellTypeViewModel.MAIN);
        if(company.dbPediaParentCompany != null) {
            model.addCell(company.dbPediaParentCompany, CellTypeViewModel.PARENT);
            model.addEdge(company.name, company.dbPediaParentCompany.name);
        }
        if(company.dbPediaSubsidiaries != null) {
            for(Company c: company.dbPediaSubsidiaries) {
                model.addCell(c, CellTypeViewModel.SUBSIDIARY);
                model.addEdge(company.name, c.name);
            }
        }

        graph.endUpdate();
        layout = new ParentSubsidiaryLayout(graph);
        layout.execute();
    }

    private static void hideSelection(String term) {
        System.out.println("called hideSelection from " + term);
        pnSelectionReturn.getChildren().remove(0, pnSelectionReturn.getChildren().size());
        lblSelectionReturn.setText(term);
        pnSelectionReturn.getChildren().add(lblSelectionReturn);
        pnSelectionReturn.getChildren().add(btnSelectionReturn);
        spSelection.setContent(pnSelectionReturn);
    }

    private static void unhideSelection() {
        System.out.println("called unhideSelection");
        spSelection.setContent(pnSelection);
    }

    private static void updateInformation(Company company) {
        lblInformation.setText("Name: " + company.name);
        lblInformation.setText(lblInformation.getText() + "\nBPROID: " + company.dbProId);
        if(company.dbPediaLocationCities != null) {
            lblInformation.setText(lblInformation.getText() + "\nDBpedia Städte: " + company.dbPediaLocationCities);
        }
        if(company.dbPediaLocationCountries != null) {
            lblInformation.setText(lblInformation.getText() + "\nDBpedia Länder: " + company.dbPediaLocationCountries);
        }
        if(company.dbPediaFormationYears != null) {
            lblInformation.setText(lblInformation.getText() + "\nDBpedia Gründungen: " + company.dbPediaFormationYears);
        }
        if(company.dbPediaNumberOfEmployees != null) {
            lblInformation.setText(lblInformation.getText() + "\nDBpedia Mitarbeiter: " + company.dbPediaNumberOfEmployees);
        }
        if(company.dbPediaAbstract != null) {
            lblInformation.setText(lblInformation.getText() + "\nDBpedia Abstract: " + company.dbPediaAbstract);
        }
    }

    public static void setTimestamp() {
        lastTimestamp = System.currentTimeMillis();
    }

    public static boolean isClick() {
        long temp = System.currentTimeMillis();
        if(temp - lastTimestamp < 100) {
            return true;
        }
        return false;
    }
}
