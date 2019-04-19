package juniter.juniterriens;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import juniter.core.model.dto.node.IssuersFrameDTO;
import juniter.core.validation.BlockLocalValid;
import juniter.juniterriens.include.AbstractJuniterFX;
import juniter.juniterriens.include.JuniterBindings;
import juniter.juniterriens.include.ConfirmBox;
import juniter.repository.jpa.block.BlockRepository;
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
import java.util.stream.Stream;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Network extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(Network.class);


    @FXML
    private ComboBox<String> period;

    @FXML
    private CheckBox testLowerThan;

    @FXML
    private CheckBox testHigherThan;

    @FXML
    private CheckBox delHigherThan;

    @FXML
    private CheckBox delLowerThan;

    @FXML
    private LineChart<Integer, Long> medianTime;

    @FXML
    private NumberAxis medianY;

    @FXML
    private NumberAxis medianX;

    @FXML
    private LineChart<Integer, Integer> issuersFrame;

    @FXML
    private PieChart netstats;

    @FXML
    private NumberAxis issuersFrameX;

    @FXML
    private NumberAxis issuersFrameY;

    @FXML
    private NumberAxis powX;

    @FXML
    private NumberAxis powY;

    @FXML
    private NumberAxis issuersFrameVarX;

    @FXML
    private NumberAxis issuersFrameVarY;

    @FXML
    private LineChart<Integer, Integer> issuersFrameVar;

    @FXML
    private StackedAreaChart<Integer, Integer> pow;

    @FXML
    private TextField delSome;

    @FXML
    private TextField tstSome;

    @Autowired
    private PeerLoader peerLoader;

    @Autowired
    private PeerService peerService;

    @Autowired
    private BlockRepository blockRepo;


    public static ObservableList<PeerService.NetStats> observableList = FXCollections.observableArrayList();
    private static ObservableList<String> periodList = FXCollections.observableArrayList("Day", "Week", "Month", "Equinox", "Year", "All");
    private static ObservableList<PieChart.Data> pieChartData =
            FXCollections.observableArrayList(
                    new PieChart.Data("Grapefruit", 10),
                    new PieChart.Data("Oranges", 10),
                    new PieChart.Data("Plums", 10));


    @FXML
    public void pairing() {
        peerLoader.doPairing();
    }

    @FXML
    private void peerCheck() {
        peerLoader.runPeerCheck();
    }

    @FXML
    public void deleteSome() {
        String[] ids = delSome.getText().split(",");

        Stream.of(ids) //
                .map(Integer::parseInt)//
                .forEach(id -> {
                    LOG.info("deleting Blocks # " + id);

                    blockRepo.block_(id).forEach(block -> blockRepo.delete(block));
                });
    }


    @FXML
    public void testSome() {
        String[] ids = tstSome.getText().split(",");

        Stream.of(ids) //
                .map(Integer::parseInt)//
                .forEach(id -> {
                    LOG.info("testing Blocks # " + id);

                    blockRepo.block(id).ifPresent(block -> {
                        boolean result = false;
                        try {
                            BlockLocalValid.Static.assertBlock(block);

                        } catch (AssertionError ea) {
                            result = ConfirmBox.display("AssertionError", ea.getMessage());
                        }
                        LOG.info("testing Block # " + result);
                    });
                });
        ConfirmBox.display("All good", "repository node is local valid  ");

    }


    @Override
    public void start(Stage primaryStage) {
        LOG.info("Starting Network");

        primaryStage.setTitle("Juniter - Network ");
        primaryStage.show();
    }

    private Integer PERIOD = 3 * 288;

    @FXML
    @SuppressWarnings("unchecked")
    private void refreshGraphs() {


        final var range = PERIOD;
        final var tick = (range / 4);


        Platform.runLater(() -> {
            var current = JuniterBindings.currenBlock.get().getNumber();
            var issuersPoints = blockRepo.issuersFrameFromTo(current - range, current);


            //issuersFrameVarX.setAutoRanging(false);
            issuersFrameVarX.setTickUnit(tick);
            issuersFrameVarX.setLowerBound(current - range);
            issuersFrameVarX.setUpperBound(current);

            // issuersFrameVarY.setAutoRanging(false);
            issuersFrameVarY.setLowerBound(issuersPoints.stream().mapToInt(IssuersFrameDTO::getIssuersFrameVar).min().orElse(0) - 10);
            issuersFrameVarY.setUpperBound(issuersPoints.stream().mapToInt(IssuersFrameDTO::getIssuersFrameVar).max().orElse(0) + 10);

            XYChart.Series<Integer, Integer> series = new XYChart.Series<>();
            series.getData().addAll(issuersPoints.stream()
                    .map(frame -> new XYChart.Data<>(frame.getNumber(), frame.getIssuersFrameVar()))
                    .collect(Collectors.toList()));

            //issuersFrame.setCreateSymbols(false);

            issuersFrameVar.getData().setAll(series);
        });

        Platform.runLater(() -> {
            var current = JuniterBindings.currenBlock.get().getNumber();
            var issuersPoints = blockRepo.issuersFrameFromTo(current - range, current);

            //issuersFrameX.setAutoRanging(false);
            issuersFrameX.setTickUnit(tick);
            issuersFrameX.setLowerBound(current - range);
            issuersFrameX.setUpperBound(current);

            //issuersFrameY.setAutoRanging(false);
//            issuersFrameVarY.setLowerBound(50);
//            issuersFrameVarY.setUpperBound(180);

            issuersFrameY.setLowerBound(issuersPoints.stream().mapToInt(IssuersFrameDTO::getIssuersFrame).min().orElse(80) - 10);
            issuersFrameY.setUpperBound(issuersPoints.stream().mapToInt(IssuersFrameDTO::getIssuersFrame).max().orElse(250) + 10);
            XYChart.Series<Integer, Integer> series = new XYChart.Series<>();

            series.getData().addAll(issuersPoints.stream()
                    .map(frame -> new XYChart.Data<>(frame.getNumber(), frame.getIssuersFrame()))
                    .collect(Collectors.toList()));

            //issuersFrame.setCreateSymbols(false);

            issuersFrame.getData().setAll(series);
        });


        Platform.runLater(() -> {
            var current = JuniterBindings.currenBlock.get().getNumber();
            var issuersPoints = blockRepo.issuersFrameFromTo(current - range, current);

            //issuersFrameX.setAutoRanging(false);
            powX.setTickUnit(tick);
            powX.setLowerBound(current - range);
            powX.setUpperBound(current);

            //issuersFrameY.setAutoRanging(false);
//            issuersFrameVarY.setLowerBound(50);
//            issuersFrameVarY.setUpperBound(180);

            powY.setLowerBound(issuersPoints.stream().mapToInt(IssuersFrameDTO::getPowMin).min().orElse(0) - 10);
            powY.setUpperBound(issuersPoints.stream().mapToInt(IssuersFrameDTO::getPowMin).max().orElse(250) + 10);
            XYChart.Series<Integer, Integer> series = new XYChart.Series<>();

            series.getData().addAll(issuersPoints.stream()
                    .map(frame -> new XYChart.Data<>(frame.getNumber(), frame.getPowMin()))
                    .collect(Collectors.toList()));


            pow.getData().setAll(series);
        });

        Platform.runLater(() -> {
            var current = JuniterBindings.currenBlock.get().getNumber();
            var issuersPoints = blockRepo.issuersFrameFromTo(current - range, current);
            Long prev = null;
            for (IssuersFrameDTO ifd : issuersPoints) {
                if (prev != null) {
                    var tmp = ifd.getMedianTime();
                    ifd.setMedianTime(tmp - prev);
                    prev = tmp;
                } else {
                    prev = ifd.getMedianTime();
                    ifd.setMedianTime(100L);
                }

            }

            //issuersFrameX.setAutoRanging(false);
            medianX.setTickUnit(tick);
            medianX.setLowerBound(current - range);
            medianX.setUpperBound(current);

//            issuersFrameVarY.setLowerBound(50);
//            issuersFrameVarY.setUpperBound(180);

//            var medianMin = issuersPoints.stream().mapToLong(IssuersFrameDTO::getMedianTime).min().orElse(0);
//            var medianMax = issuersPoints.stream().mapToLong(IssuersFrameDTO::getMedianTime).max().orElse(250);
            medianY.setAutoRanging(false);
            medianY.setLowerBound(0);
            medianY.setUpperBound(1000);

            XYChart.Series<Integer, Long> series = new XYChart.Series<>();

            series.getData().addAll(issuersPoints.stream()
                    .map(frame -> new XYChart.Data<>(frame.getNumber(), frame.getMedianTime()))
                    .peek(f -> Tooltip.install(f.getNode(), new Tooltip(df.format(f.getYValue()) + "XX")))

                    .collect(Collectors.toList()));


            medianTime.getData().setAll(series);

            /*
             * Browsing through the Data and applying ToolTip
             * as well as the class on hover
             */
            for (XYChart.Series<Integer, Long> s : medianTime.getData()) {
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


        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        period.setItems(periodList);

        netstats.dataProperty().setValue(pieChartData);

        observableList.addListener((ListChangeListener<PeerService.NetStats>) c -> {
            pieChartData.setAll(
                    c.getList().stream()
                            .map(ns -> {
                                var st = ns.getHost().substring(ns.getHost().indexOf("://") + 3);
                                if (st.contains("/"))
                                    st = ns.getSuccess() + "/" + ns.getCount() + " " + st.substring(0, st.indexOf("/"));

                                return new PieChart.Data(st, ns.getLastNormalizedScore());
                            })
                            .collect(Collectors.toList()));

            for (final PieChart.Data data : pieChartData) {
                Tooltip.install(data.getNode(), new Tooltip(df.format(data.getPieValue() * 100) + "%"));
            }

        });

        period.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        PERIOD = Integer.parseInt(newValue);
                    } catch (Exception e) {
                        switch (newValue) {
                            case "Day":
                                PERIOD = 288;
                                break;
                            case "Week":
                                PERIOD = 7 * 288;
                                break;
                            case "Month":
                                PERIOD = 30 * 288;
                                break;
                            case "Equinox":
                                PERIOD = 183 * 288;
                                break;
                            case "Year":
                                PERIOD = 365 * 288;
                                break;
                            case "All":
                                PERIOD = 730 * 288;
                                break;
                            default:
                                PERIOD = 3 * 288;
                        }
                    }
                    refreshGraphs();
                });

        refreshGraphs();


    }

    private DecimalFormat df = new DecimalFormat("#.##");

    @FXML
    public void ping() {
        peerService.pings();
    }
}
