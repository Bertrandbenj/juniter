package juniter.gui.business.page;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import juniter.core.model.dbo.NetStats;
import juniter.core.model.dto.node.BlockNetworkMeta;
import juniter.gui.technical.AbstractJuniterFX;
import juniter.gui.technical.I18N;
import juniter.service.BlockService;
import juniter.service.bma.PeerService;
import juniter.service.bma.loader.PeerLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static juniter.gui.JuniterBindings.currentBindex;
import static juniter.gui.JuniterBindings.currentBindexN;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Network extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(Network.class);


    @FXML
    private ComboBox<String> period;
    @FXML
    private PieChart netStatsChart;

    @FXML
    private LineChart<Integer, Long> medianTimeChart;
    @FXML
    private NumberAxis medianY;
    @FXML
    private NumberAxis medianX;

    @FXML
    private LineChart<Integer, Integer> issuersFrameChart;
    @FXML
    private NumberAxis issuersFrameX;
    @FXML
    private NumberAxis issuersFrameY;

    @FXML
    private StackedAreaChart<Integer, Integer> powChart;
    @FXML
    private NumberAxis powX;
    @FXML
    private NumberAxis powY;

    @FXML
    private LineChart<Integer, Integer> issuersFrameVarChart;
    @FXML
    private NumberAxis issuersFrameVarX;
    @FXML
    private NumberAxis issuersFrameVarY;


    @Autowired
    private PeerLoader peerLoader;

    @Autowired
    private PeerService peerService;

    @Autowired
    private BlockService blockService;


    public static ObservableList<NetStats> observableNetStats = FXCollections.observableArrayList();
    private static ObservableList<String> observablePeriodList = FXCollections.observableArrayList("Day", "Week", "Month", "Equinox", "Year", "All");
    private static ObservableList<PieChart.Data> netPieChartData =
            FXCollections.observableArrayList(
                    new PieChart.Data("Node A", 10),
                    new PieChart.Data("Node B", 10),
                    new PieChart.Data("Node C", 10));

    public static ObservableList<BlockNetworkMeta> observableBlockNetInfos = FXCollections.observableArrayList();

    XYChart.Series<Integer, Integer> seriesPOW = new XYChart.Series<>();
    XYChart.Series<Integer, Integer> issuerFrameVarChartData = new XYChart.Series<>();
    XYChart.Series<Integer, Integer> seriesIF = new XYChart.Series<>();
    XYChart.Series<Integer, Long> mtSeries1 = new XYChart.Series<>();
    XYChart.Series<Integer, Long> mtSeries2 = new XYChart.Series<>();

    @FXML
    public void pairing() {
        peerLoader.doPairing();
    }

    @FXML
    private void peerCheck() {
        peerLoader.runPeerCheck();
    }


    @Override
    public void start(Stage primaryStage) {
        LOG.info("Starting Network");

        primaryStage.setTitle("Juniter - Network ");
        primaryStage.show();
    }

    private IntegerProperty PERIOD = new SimpleIntegerProperty(3 * 288);

    @FXML
    @SuppressWarnings("unchecked")
    private void refreshGraphs() {


            /*
             * Browsing through the Data and applying ToolTip
             * as well as the class on hover

            for (XYChart.Series<Integer, Long> s : medianTimeChart.getData()) {
                for (XYChart.Data<Integer, Long> d : s.getData()) {
                    Tooltip.install(d.getNode(), new Tooltip(
                            d.getXValue().toString() + "\n" +
                                    "Number Of Events : " + d.getYValue()));


                    if (d.getNode() != null) {
                        //Adding class on hover
                        d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));

                        //Removing class on exit
                        d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));

                    }
                }
            }
*/

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        period.setItems(observablePeriodList);

        netStatsChart.dataProperty().setValue(netPieChartData);

        // BIND AXIS BOUNDS
        powX.tickUnitProperty().bind(PERIOD.divide(4));
        powX.lowerBoundProperty().bind(currentBindexN.subtract(PERIOD));
        powX.upperBoundProperty().bind(currentBindexN);

        medianX.tickUnitProperty().bind(PERIOD.divide(4));
        medianX.lowerBoundProperty().bind(currentBindexN.subtract(PERIOD));
        medianX.upperBoundProperty().bind(currentBindexN);

        issuersFrameX.tickUnitProperty().bind(PERIOD.divide(4));
        issuersFrameX.lowerBoundProperty().bind(currentBindexN.subtract(PERIOD));
        issuersFrameX.upperBoundProperty().bind(currentBindexN);

        issuersFrameVarX.tickUnitProperty().bind(PERIOD.divide(4));
        issuersFrameVarX.lowerBoundProperty().bind(currentBindexN.subtract(PERIOD));
        issuersFrameVarX.upperBoundProperty().bind(currentBindexN);

//        medianY.setAutoRanging(false);
//        medianY.setLowerBound(0);
//        medianY.setUpperBound(1000);


        currentBindex.addListener(cl -> observableBlockNetInfos.setAll(blockService.issuersFrameFromTo(currentBindexN.subtract(PERIOD).intValue(), currentBindexN.get())));


        powChart.getData().setAll(seriesPOW);
        issuersFrameVarChart.getData().setAll(issuerFrameVarChartData);
        issuersFrameChart.getData().setAll(seriesIF);
        medianTimeChart.getData().setAll(mtSeries1, mtSeries2);

        observableBlockNetInfos.addListener((ListChangeListener<BlockNetworkMeta>) netInfos -> {
            LOG.debug(" ==== REBUILD POW GRAPH");
            powY.setLowerBound(netInfos.getList().stream().mapToInt(BlockNetworkMeta::getPowMin).min().orElse(0) - 10);
            powY.setUpperBound(netInfos.getList().stream().mapToInt(BlockNetworkMeta::getPowMin).max().orElse(250) + 10);
            seriesPOW.getData().setAll(netInfos.getList().stream()
                    .map(frame -> new XYChart.Data<>(frame.getNumber(), frame.getPowMin()))
                    .collect(Collectors.toList()));

            LOG.debug(" ==== REBUILD ISSUER FRAME VAR GRAPH");
            issuersFrameVarY.setLowerBound(netInfos.getList().stream().mapToInt(BlockNetworkMeta::getIssuersFrameVar).min().orElse(0) - 10);
            issuersFrameVarY.setUpperBound(netInfos.getList().stream().mapToInt(BlockNetworkMeta::getIssuersFrameVar).max().orElse(0) + 10);
            issuerFrameVarChartData.getData().addAll(netInfos.getList().stream()
                    .map(frame -> new XYChart.Data<>(frame.getNumber(), frame.getIssuersFrameVar()))
                    .collect(Collectors.toList()));

            LOG.debug(" ==== REBUILD ISSUER FRAME GRAPH");
            issuersFrameY.setLowerBound(netInfos.getList().stream().mapToInt(BlockNetworkMeta::getIssuersFrame).min().orElse(80) - 10);
            issuersFrameY.setUpperBound(netInfos.getList().stream().mapToInt(BlockNetworkMeta::getIssuersFrame).max().orElse(250) + 10);
            seriesIF.getData().addAll(netInfos.getList().stream()
                    .map(frame -> new XYChart.Data<>(frame.getNumber(), frame.getIssuersFrame()))
                    .collect(Collectors.toList()));

            LOG.debug(" ==== REBUILD MEDIAN TIME GRAPH");
            Long prev = null;
            for (BlockNetworkMeta ifd : netInfos.getList()) {
                if (prev != null) {
                    var tmp = ifd.getMedianTime();
                    ifd.setMedianTime(tmp - prev);
                    prev = tmp;
                } else {
                    prev = ifd.getMedianTime();
                    ifd.setMedianTime(300L);
                }

            }
            var avg = (long) netInfos.getList().stream().mapToLong(BlockNetworkMeta::getMedianTime).average().orElseThrow();
            mtSeries1.getData().addAll(netInfos.getList().stream()
                    .map(frame -> new XYChart.Data<>(frame.getNumber(), frame.getMedianTime()))
                    .collect(Collectors.toList()));
            mtSeries2.getData().addAll(netInfos.getList().stream()
                    .map(frame -> new XYChart.Data<>(frame.getNumber(), avg))
                    .collect(Collectors.toList()));
            medianTimeChart.setTitle(I18N.get("net.medianTime") + " (avg: " + avg + "s)");


        });


        observableNetStats.addListener((ListChangeListener<NetStats>) c -> {
            LOG.info("observableNetStats.addListener");
            netPieChartData.setAll(
                    c.getList().stream()
                            .map(ns -> {
                                var st = ns.getHost().substring(ns.getHost().indexOf("://") + 3);
                                if (st.contains("/"))
                                    st = ns.getSuccess() + "/" + ns.getCount() + " " + st.substring(0, st.indexOf("/"));

                                return new PieChart.Data(st, ns.getLastNormalizedScore());
                            })
                            .collect(Collectors.toList()));

            for (final PieChart.Data data : netPieChartData) {
                Tooltip.install(data.getNode(), new Tooltip(df.format(data.getPieValue() * 100) + "%"));
            }

        });

        period.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        PERIOD.set(Integer.parseInt(newValue));
                    } catch (Exception e) {
                        switch (newValue) {
                            case "Day":
                                PERIOD.set(288);
                                break;
                            case "Week":
                                PERIOD.set(7 * 288);
                                break;
                            case "Month":
                                PERIOD.set(30 * 288);
                                break;
                            case "Equinox":
                                PERIOD.set(183 * 288);
                                break;
                            case "Year":
                                PERIOD.set(365 * 288);
                                break;
                            case "All":
                                PERIOD.set(730 * 288);
                                break;
                            default:
                                PERIOD.set(3 * 288);
                        }
                    }
                    observableBlockNetInfos.setAll(blockService.issuersFrameFromTo(currentBindexN.subtract(PERIOD).intValue(), currentBindexN.get()));

                });

        observableBlockNetInfos.setAll(blockService.issuersFrameFromTo(currentBindexN.subtract(PERIOD).intValue(), currentBindexN.get()));

    }

    private DecimalFormat df = new DecimalFormat("#.##");

    @FXML
    public void ping() {
        peerService.pings();
    }

}
