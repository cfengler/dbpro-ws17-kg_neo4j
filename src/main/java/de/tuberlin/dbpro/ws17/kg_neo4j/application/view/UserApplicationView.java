package de.tuberlin.dbpro.ws17.kg_neo4j.application.view;

import de.tuberlin.dbpro.ws17.kg_neo4j.ApplicationStartup;
import de.tuberlin.dbpro.ws17.kg_neo4j.application.Layout;
import de.tuberlin.dbpro.ws17.kg_neo4j.application.MainNeighbourLayout;
import de.tuberlin.dbpro.ws17.kg_neo4j.application.Model;
import de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel.CellTypeViewModel;
import de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel.GraphViewModel;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.Company;
import de.tuberlin.dbpro.ws17.kg_neo4j.services.CompanyService;
import javafx.application.Application;
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

    private static final Button btnSearch = new Button();
    private static final Button btnSelectionReturn = new Button();

    private static final TextField tfSearch = new TextField();

    private static final RadioButton rbNodes = new RadioButton();
    private static final RadioButton rbLabels = new RadioButton();
    private static final RadioButton rbEdges = new RadioButton();
    private static final ToggleGroup tgSearch = new ToggleGroup();

    private static final String fontname = "Helvetica Neue";


    //protected static void launchApp(Class<? extends UserApplicationView> appClass, String[] args) {
    //    Application.launch(appClass, args);
   // }
    //@Autowired
    //private CompanyService companyService = null;


    public UserApplicationView(){ //CompanyService companyService) {
//        this.companyService = companyService;
    }

    private AnnotationConfigApplicationContext context;

    public void start(Stage primaryStage) {
        context = new AnnotationConfigApplicationContext(ApplicationStartup.class);

        CompanyService companyService = context.getBean(CompanyService.class);
        List<Company> test = companyService.getCompaniesByLocationContainingName("Lufthansa");
        primaryStage.setTitle("DBPRO");

        initSearch();
        initSelection();

        //Beispiel Graph anlegen Anfang
        GraphViewModel graph = new GraphViewModel();

        pnFoundation.setCenter(graph.getScrollPane());

        //Testdaten Anfang
        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("Cell A", CellTypeViewModel.MAIN);
        model.addCell("Cell B", CellTypeViewModel.NEIGHBOUR);
        model.addCell("Cell C", CellTypeViewModel.NEIGHBOUR);
        model.addCell("Cell D", CellTypeViewModel.NEIGHBOUR);
        model.addCell("Cell E", CellTypeViewModel.NEIGHBOUR);
        model.addCell("Cell F", CellTypeViewModel.NEIGHBOUR);
        model.addCell("Cell G", CellTypeViewModel.NEIGHBOUR);

        model.addEdge("Cell A", "Cell B");
        model.addEdge("Cell A", "Cell C");
        model.addEdge("Cell A", "Cell D");
        model.addEdge("Cell A", "Cell E");
        model.addEdge("Cell A", "Cell F");
        model.addEdge("Cell A", "Cell G");

        graph.endUpdate();
        //Testdaten Ende

        //Layout layout = new RandomLayout(graph);
        Layout layout = new MainNeighbourLayout(graph);
        layout.execute();
        //Beispiel Ende

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
        tfSearch.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    search();
                }
            }
        });
        tfSearch.setPromptText("Suchbegriff");
        tfSearch.requestFocus();
        pnSearchField.setCenter(tfSearch);

        //Suchbutton
        btnSearch.setText("suchen");
        btnSearch.setFont(new Font(fontname, 13));
        btnSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                search();
            }
        });
        pnSearchField.setRight(btnSearch);
        pnSearch.add(pnSearchField, 0, 1);

        //Radiobuttons fuer die Suchart
        rbNodes.setText("Knoten");
        rbNodes.setSelected(true);
        rbNodes.setToggleGroup(tgSearch);
        pnSearchType.getChildren().add(rbNodes);

        rbLabels.setText("Bezeichnung");
        rbLabels.setToggleGroup(tgSearch);
        pnSearchType.getChildren().add(rbLabels);

        rbEdges.setText("Kante");
        rbEdges.setToggleGroup(tgSearch);
        pnSearchType.getChildren().add(rbEdges);
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
        btnSelectionReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                unhideSelection();
            }
        });
    }

    public static void search() {
        System.out.println("suchen...");
        String term = tfSearch.getText();
        int selection;
        if(term.equals("")) {
            return;
        }

        pnSelection.getChildren().remove(0, pnSelection.getChildren().size());

        if(rbNodes.isSelected()) {
            selection = 0;
            lblSelection.setText("Knoten");
            //TODO: HIER MUSS DIE SUCHE NACH KNOTEN AN DIE LOGIK WEITERGEGEBEN WERDEN

        } else if (rbLabels.isSelected()) {
            selection = 1;
            lblSelection.setText("Bezeichnungen");
            //TODO: HIER MUSS DIE SUCHE NACH LABELS AN DIE LOGIK WEITERGEGEBEN WERDEN

        } else {
            selection = 2;
            lblSelection.setText("Kanten");
            //TODO: HIER MUSS DIE SUCHE NACH KANTEN AN DIE LOGIK WEITERGEGEBEN WERDEN

        }
        for(int i = 0;i < 10;i++) {
            Button btn = new Button(lblSelection.getText() + " " + term + " " + (i + 1));
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    hideSelection(((Button)event.getSource()).getText());
                }
            });
            pnSelection.getChildren().add(btn);
        }
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
}
