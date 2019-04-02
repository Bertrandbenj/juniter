package juniter.juniterriens;

import javafx.scene.chart.*;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import juniter.core.model.dto.IssuersFrameDTO;
import juniter.juniterriens.include.Bindings;
import juniter.juniterriens.include.ConfirmBox;
import juniter.juniterriens.include.AbstractJuniterFX;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import juniter.core.validation.BlockLocalValid;
import juniter.repository.jpa.block.BlockRepository;
import juniter.service.bma.PeerService;
import juniter.service.bma.loader.PeerLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Network extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger();

    public static final Integer PERIOD = 3 * 288;
    @FXML
    private CheckBox testLowerThan;
    @FXML
    private CheckBox testHigherThan;
    @FXML
    private CheckBox delHigherThan;
    @FXML
    private CheckBox delLowerThan;
    @FXML
    private LineChart medianTime;
    @FXML
    private NumberAxis medianY;
    @FXML
    private NumberAxis medianX;

    @FXML
    private BorderPane contentPane;

    @FXML
    private LineChart issuersFrame;

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
    private LineChart issuersFrameVar;

    @FXML
    private StackedAreaChart pow;

    @FXML
    private FlowPane peersList;

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


    public ListProperty<PeerService.NetStats> choice = new SimpleListProperty<>(observableList);

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

                    blockRepo.block_(id).forEach(block -> {
                        blockRepo.delete(block);
                    });
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        observableList.addListener((ListChangeListener<PeerService.NetStats>) c -> {
            //LOG.info("initialize ListChangeListener: " + c);

            var buttons = c.getList().stream().map(ns -> {
                var res = new Button(ns.getSuccess() + "/" + ns.getCount() + " " +
                        String.format("%.2f", ns.getLastNormalizedScore() * 100) + "% " + ns.getHost());
                res.setFont(Font.font(11));
                return res;
            }).collect(Collectors.toList());

            Platform.runLater(() -> {
                peersList.getChildren().setAll(buttons);
                peersList.requestLayout();
            });
            //refresh();
        });

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Grapefruit", 10),
                        new PieChart.Data("Oranges", 10),
                        new PieChart.Data("Plums", 10),
                        new PieChart.Data("Pears", 22),
                        new PieChart.Data("Apples", 30));
        netstats.dataProperty().setValue(pieChartData);

        observableList.addListener((ListChangeListener<PeerService.NetStats>) c ->
                pieChartData.setAll(
                        c.getList().stream()
                                .map(ns -> {
                                    var st = ns.getHost().substring(ns.getHost().indexOf("://") + 3);
                                    if (st.contains("/"))
                                        st = st.substring(0, st.indexOf("/"));
                                    return new PieChart.Data(st, ns.getLastNormalizedScore());
                                })
                                .collect(Collectors.toList()))

        );


        Platform.runLater(() -> {
            var current = Bindings.currenBlock.get().getNumber();
            var issuersPoints = blockRepo.issuersFrameFromTo(current - PERIOD, current);


            //issuersFrameVarX.setAutoRanging(false);
            issuersFrameVarX.setTickUnit(PERIOD / 4);
            issuersFrameVarX.setLowerBound(current - PERIOD);
            issuersFrameVarX.setUpperBound(current);

            // issuersFrameVarY.setAutoRanging(false);
            issuersFrameVarY.setLowerBound(issuersPoints.stream().mapToInt(IssuersFrameDTO::getIssuersFrameVar).min().orElse(0) - 10);
            issuersFrameVarY.setUpperBound(issuersPoints.stream().mapToInt(IssuersFrameDTO::getIssuersFrameVar).max().orElse(0) + 10);

            XYChart.Series series = new XYChart.Series();
            series.getData().addAll(issuersPoints.stream()
                    .map(frame -> new XYChart.Data<>(frame.getNumber(), frame.getIssuersFrameVar()))
                    .collect(Collectors.toList()));

            //issuersFrame.setCreateSymbols(false);

            issuersFrameVar.getData().setAll(series);
        });

        Platform.runLater(() -> {
            var current = Bindings.currenBlock.get().getNumber();
            var issuersPoints = blockRepo.issuersFrameFromTo(current - PERIOD, current);

            //issuersFrameX.setAutoRanging(false);
            issuersFrameX.setTickUnit(PERIOD / 4);
            issuersFrameX.setLowerBound(current - PERIOD);
            issuersFrameX.setUpperBound(current);

            //issuersFrameY.setAutoRanging(false);
//            issuersFrameVarY.setLowerBound(50);
//            issuersFrameVarY.setUpperBound(180);

            issuersFrameVarY.setLowerBound(issuersPoints.stream().mapToInt(IssuersFrameDTO::getIssuersFrame).min().orElse(80) - 10);
            issuersFrameVarY.setUpperBound(issuersPoints.stream().mapToInt(IssuersFrameDTO::getIssuersFrame).max().orElse(250) + 10);
            XYChart.Series series = new XYChart.Series();

            series.getData().addAll(issuersPoints.stream()
                    .map(frame -> new XYChart.Data<>(frame.getNumber(), frame.getIssuersFrame()))
                    .collect(Collectors.toList()));

            //issuersFrame.setCreateSymbols(false);

            issuersFrame.getData().setAll(series);
        });


        Platform.runLater(() -> {
            var current = Bindings.currenBlock.get().getNumber();
            var issuersPoints = blockRepo.issuersFrameFromTo(current - PERIOD, current);

            //issuersFrameX.setAutoRanging(false);
            powX.setTickUnit(PERIOD / 4);
            powX.setLowerBound(current - PERIOD);
            powX.setUpperBound(current);

            //issuersFrameY.setAutoRanging(false);
//            issuersFrameVarY.setLowerBound(50);
//            issuersFrameVarY.setUpperBound(180);

            powY.setLowerBound(issuersPoints.stream().mapToInt(IssuersFrameDTO::getPowMin).min().orElse(0) - 10);
            powY.setUpperBound(issuersPoints.stream().mapToInt(IssuersFrameDTO::getPowMin).max().orElse(250) + 10);
            XYChart.Series series = new XYChart.Series();

            series.getData().addAll(issuersPoints.stream()
                    .map(frame -> new XYChart.Data<>(frame.getNumber(), frame.getPowMin()))
                    .collect(Collectors.toList()));


            pow.getData().setAll(series);
        });

        Platform.runLater(() -> {
            var current = Bindings.currenBlock.get().getNumber();
            var issuersPoints = blockRepo.issuersFrameFromTo(current - PERIOD, current);

            //issuersFrameX.setAutoRanging(false);
            medianX.setTickUnit(PERIOD / 4);
            medianX.setLowerBound(current - PERIOD);
            medianX.setUpperBound(current);


//            issuersFrameVarY.setLowerBound(50);
//            issuersFrameVarY.setUpperBound(180);

            var medianMin = issuersPoints.stream().mapToLong(IssuersFrameDTO::getMedianTime).min().orElse(0);
            var medianMax = issuersPoints.stream().mapToLong(IssuersFrameDTO::getMedianTime).max().orElse(250);
            medianY.setAutoRanging(false);
            medianY.setLowerBound(medianMin - 10);
            medianY.setUpperBound(medianMax + 10);

            XYChart.Series series = new XYChart.Series();

            series.getData().addAll(issuersPoints.stream()
                    .map(frame -> new XYChart.Data<>(frame.getNumber(), frame.getMedianTime()))
                    .collect(Collectors.toList()));


            medianTime.getData().setAll(series);
        });

    }


    public void ping(ActionEvent actionEvent) {
        peerService.pings();
    }
}
