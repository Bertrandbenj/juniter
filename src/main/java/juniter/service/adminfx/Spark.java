package juniter.service.adminfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import juniter.service.SparkService;
import juniter.service.UtilsService;
import juniter.service.adminfx.include.AbstractJuniterFX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Spark extends AbstractJuniterFX implements Initializable {
    private static final Logger LOG = LogManager.getLogger();
    public TitledPane transformPane;

    @FXML
    private TextArea show;

    @FXML
    private VBox wotList;

    @FXML
    private VBox indexList;

    @FXML
    private VBox txList;

    @FXML
    private VBox workingList;

    @Autowired
    SparkService sparkService;

    @Autowired
    UtilsService utilsService;

    @Value("${juniter.dataPath:/tmp/juniter/data/}")
    private String dataPath;

    @Autowired
    SparkSession spark;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transformPane.setAnimated(false);
        transformPane.setExpanded(true);
        transformPane.expandedProperty().setValue(true);
        transformPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                LOG.info("isNowExpanded");
            }
        });
        transformPane.requestLayout();

        // INDEX
        sparkService.iindex = spark.read().parquet(dataPath + "blockchain/iindex");
        sparkService.cindex = spark.read().parquet(dataPath + "blockchain/cindex");
        sparkService.mindex = spark.read().parquet(dataPath + "blockchain/mindex");
        //sparkService.bindex = spark.read().parquet(dataPath+"blockchain/biindex");
        //sparkService.sindex = spark.read().parquet(dataPath+"blockchain/sindex");

        // TX
        sparkService.transactions = spark.read().parquet(dataPath + "blockchain/transactions");
        sparkService.inputs = spark.read().parquet(dataPath + "blockchain/inputs");
        sparkService.outputs = spark.read().parquet(dataPath + "blockchain/outputs");

        //  WOT
        sparkService.joiners = spark.read().parquet(dataPath + "blockchain/joiners");
        sparkService.idty = spark.read().parquet(dataPath + "blockchain/idty");
        sparkService.actives = spark.read().parquet(dataPath + "blockchain/actives");
        sparkService.leavers = spark.read().parquet(dataPath + "blockchain/leavers");
        sparkService.excluded = spark.read().parquet(dataPath + "blockchain/excluded");
        sparkService.revoked = spark.read().parquet(dataPath + "blockchain/revocs");

        // Working
        sparkService.tree = spark.read().parquet(dataPath + "blockchain/tree");
        sparkService.blockchain = spark.read().parquet(dataPath + "blockchain/chain");


        refreshTableList();

    }


    private VBox makeButton(Dataset<Row> table, String name) {
        if (table == null)
            return new VBox();

        try {
            var label = new Button(name + " - " + table.count() + " rows " + " - " + table.columns().length + " cols ");
            label.setFont(Font.font(12));
            label.setOnAction(e -> {
                show.setText(table.showString(100, 20, false));
            });
            var col = new Label(Arrays.toString(table.columns()));

            var box = new HBox(label);
            var vbox = new VBox(box);
            return vbox;
        } catch (Exception e) {
            LOG.error("error making button " + name + " - ", e);
        }
        return new VBox();
    }


    private void refreshTableList() {
        wotList.getChildren().clear();
        wotList.getChildren().addAll(
                makeButton(sparkService.actives, "actives"),
                makeButton(sparkService.excluded, "excluded"),
                makeButton(sparkService.leavers, "leavers"),
                makeButton(sparkService.revoked, "revoked"),
                makeButton(sparkService.joiners, "joiners"),
                makeButton(sparkService.idty, "idty"));

        indexList.getChildren().clear();
        indexList.getChildren().addAll(
                makeButton(sparkService.iindex, "iindex"),
                makeButton(sparkService.cindex, "cindex"),
                makeButton(sparkService.mindex, "mindex")
                //,makeButton(sparkService.sindex, "sindex")
        );

        txList.getChildren().clear();
        txList.getChildren().addAll(
                makeButton(sparkService.transactions, "transactions"),
                makeButton(sparkService.inputs, "inputs"),
                makeButton(sparkService.unlocks, "unlocks"),
                makeButton(sparkService.outputs, "outputs")
        );

        workingList.getChildren().clear();
        workingList.getChildren().addAll(
                makeButton(sparkService.blockchain, "chain"),
                makeButton(sparkService.tree, "tree"));

    }

    @FXML
    public void parseBlockchain(ActionEvent actionEvent) {
        sparkService.parseBlockchain();
        refreshTableList();
    }

    @FXML
    public void jsonToParquet(ActionEvent actionEvent) {
        sparkService.jsonToParquet();
    }

    @FXML
    public void computeAsOf(ActionEvent actionEvent) {
        sparkService.computeAsOf();
    }


    @FXML
    public void dumpJsonRows(ActionEvent actionEvent) {
        utilsService.dumpJsonRows();
    }

    @FXML
    public void duniterToParquet(ActionEvent actionEvent) {
        sparkService.duniterToParquet();
    }

    @FXML
    public void dumpIndexes(ActionEvent actionEvent) {
        sparkService.dumpIndexes();
    }

    @FXML
    public void compare(ActionEvent actionEvent) {
        sparkService.compare();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOG.info("Starting Spark");

        //primaryStage.setTitle("Juniter - Spark  ");
        //primaryStage.show();
    }
}
