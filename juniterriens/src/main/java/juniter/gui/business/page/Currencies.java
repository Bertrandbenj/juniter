package juniter.gui.business.page;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import juniter.core.model.technical.CcyStats;
import juniter.gui.technical.AbstractJuniterFX;
import juniter.gui.technical.I18N;
import juniter.service.core.BlockService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static juniter.gui.technical.Formats.DATE_FORMAT;
import static juniter.gui.technical.Formats.DECIMAL_4;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Currencies extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(Currencies.class);

    @FXML
    private ComboBox<String> period;


    @FXML
    private LineChart<Long, Long> mMassChart;
    @FXML
    private NumberAxis monetaryMass;
    @FXML
    private NumberAxis mmtime;

    @FXML
    private LineChart<Long, Double> mShareChart;
    @FXML
    private NumberAxis mstime;
    @FXML
    private NumberAxis moneyShare;

    @FXML
    private StackedAreaChart<Long, Integer> popChart;
    @FXML
    private NumberAxis poptime;
    @FXML
    private NumberAxis population;

    @FXML
    private LineChart<Long, Double> popGrowthChart;
    @FXML
    private NumberAxis popgtime;
    @FXML
    private NumberAxis popgrowth;


    @Autowired
    private BlockService blockService;


    private static ObservableList<String> periodList = FXCollections.observableArrayList("Day", "Week", "Month", "Equinox", "Year", "All");


    @Override
    public void start(Stage primaryStage) throws Exception {
        LOG.info("Starting " + this.getClass().getSimpleName());

        primaryStage.setTitle("Juniter - " + this.getClass().getSimpleName());
        primaryStage.show();
    }

    private Integer PERIOD = -1;

    public class MyStringConv extends StringConverter<Number> {

        @Override
        public String toString(Number object) {
            return DATE_FORMAT.format((Double) object * 1000);
        }

        @Override
        public Number fromString(String string) {
            return null;
        }
    }

    ;

    @FXML
    private void refreshGraphs() {


        Platform.runLater(() -> {

            var list = blockService.statsWithUD();
            list.sort(Comparator.naturalOrder());
            if (PERIOD != -1) {
                list = list.subList(list.size() - PERIOD - 1, list.size() - 1);
            }

            for (int i = 1; i < list.size(); i++) {
                var it = list.get(i);
                var prev = list.get(i - 1);
                list.get(i).setMembersGrowth(it.getMembersCount() - prev.getMembersCount());
                list.get(i).setMembersGrowthRate((it.getMembersGrowth() / (double) it.getMembersCount()));
                list.get(i).setMoneyShare(it.getMonetaryMass() / (double) it.getMembersCount());
            }

            var begin = list.get(0);
            var end = list.get(list.size() - 1);
            var tick = (end.getMedianTime() - begin.getMedianTime()) / 4;
            begin.setMembersGrowth(0);
            begin.setMembersGrowthRate(0.0);
            begin.setMoneyShare(begin.getMonetaryMass() / (double) begin.getMembersCount());

            LOG.debug(list);


            LOG.info(" ======== POPULATION CHART");
            poptime.setTickUnit(tick);
            poptime.setTickLabelFormatter(new MyStringConv());
            poptime.setLowerBound(begin.getMedianTime());
            poptime.setUpperBound(end.getMedianTime());

            population.setLowerBound(0);
            population.setUpperBound(list.stream().mapToInt(CcyStats::getMembersCount).max().orElseThrow());
            XYChart.Series<Long, Integer> series = new XYChart.Series<>();

            series.getData().addAll(list.stream()
                    .map(frame -> new XYChart.Data<>(frame.getMedianTime(), frame.getMembersCount()))
                    .collect(Collectors.toList()));

            popChart.getData().setAll(series);


            LOG.info(" ======== POPULATION GROWTH  CHART");
            popgtime.setTickUnit(tick);
            popgtime.setTickLabelFormatter(new MyStringConv());
            popgtime.setLowerBound(begin.getMedianTime());
            popgtime.setUpperBound(end.getMedianTime());


            popgrowth.setLowerBound(-1.0);
            popgrowth.setUpperBound(1.0);

            XYChart.Series<Long, Double> seriespgC = new XYChart.Series<>();
            seriespgC.getData().addAll(
                    list.stream()
                            .map(frame -> new XYChart.Data<>(frame.getMedianTime(), frame.getMembersGrowthRate() * 100))
                            .collect(Collectors.toList()));

            var avg = list.stream().mapToDouble(CcyStats::getMembersGrowthRate).average().getAsDouble() * 100;
            LOG.info("avg pop growth rate " + avg);
            XYChart.Series<Long, Double> seriespgC2 = new XYChart.Series<>();
            seriespgC2.getData().addAll(list.stream()
                    .map(frame -> new XYChart.Data<>(frame.getMedianTime(), avg))
                    .collect(Collectors.toList()));

            popGrowthChart.setTitle(I18N.get("ccy.stats.popgrowth") + " (" + DECIMAL_4.format(avg) + "%)");
            popGrowthChart.getData().setAll(seriespgC, seriespgC2);


            LOG.info(" ======== MONETARY MASS");
            mmtime.setTickUnit(tick);
            mmtime.setTickLabelFormatter(new MyStringConv());
            mmtime.setLowerBound(begin.getMedianTime());
            mmtime.setUpperBound(end.getMedianTime());

            //monetaryMass.setAutoRanging(false);
            monetaryMass.setLowerBound(0);
            monetaryMass.setUpperBound(end.getMonetaryMass());

            XYChart.Series<Long, Long> seriesmm = new XYChart.Series<>();

            seriesmm.getData().addAll(list.stream()
                    .map(frame -> new XYChart.Data<>(frame.getMedianTime(), frame.getMonetaryMass()))
                    //.peek(f -> Tooltip.install(f.getNode(), new Tooltip(DECIMAL_2.format(f.getYValue()) + "XX")))

                    .collect(Collectors.toList()));

            mMassChart.getData().setAll(seriesmm);


            LOG.info(" ======== MEMBERS SHARE");
            mstime.setTickUnit(tick);
            mstime.setTickLabelFormatter(new MyStringConv());
            mstime.setLowerBound(begin.getMedianTime());
            mstime.setUpperBound(end.getMedianTime());

            moneyShare.setLowerBound(0.0);
            moneyShare.setUpperBound(end.getMoneyShare());
            XYChart.Series<Long, Double> seriesms = new XYChart.Series<>();

            seriesms.getData().addAll(list.stream()
                    .map(frame -> new XYChart.Data<>(frame.getMedianTime(), frame.getMoneyShare()))
                    .collect(Collectors.toList()));

            mShareChart.getData().setAll(seriesms);



            /*
             * Browsing through the Data and applying ToolTip
             * as well as the class on hover

            for (XYChart.Series<Long, Long> s : mMassChart.getData()) {
                for (XYChart.Data<Long, Long> d : s.getData()) {
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

        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        period.setItems(periodList);

        period.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        PERIOD = Integer.parseInt(newValue);
                    } catch (Exception e) {
                        switch (newValue) {
                            case "Day":
                                PERIOD = 1;
                                break;
                            case "Week":
                                PERIOD = 7;
                                break;
                            case "Month":
                                PERIOD = 30;
                                break;
                            case "Equinox":
                                PERIOD = 183;
                                break;
                            case "Year":
                                PERIOD = 366;
                                break;
                            case "All":
                            default:
                                PERIOD = -1;
                                break;
                        }
                    }
                    refreshGraphs();
                });

        refreshGraphs();


    }


}
