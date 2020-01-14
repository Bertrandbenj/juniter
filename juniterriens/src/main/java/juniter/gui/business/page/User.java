package juniter.gui.business.page;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;
import juniter.core.model.dbo.index.Account;
import juniter.core.model.dbo.index.CertRecord;
import juniter.core.model.dbo.index.IINDEX;
import juniter.core.model.dbo.index.MINDEX;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.tx.TxUnlock;
import juniter.gui.technical.AbstractJuniterFX;
import juniter.gui.technical.Formats;
import juniter.service.core.Index;
import juniter.service.core.TransactionService;
import juniter.service.core.WebOfTrust;
import juniter.user.UserSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static juniter.gui.technical.Formats.DATE_FORMAT;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class User extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(User.class);
    public NumberAxis txTime;
    public NumberAxis txAccount;

    @FXML
    private LineChart txChart;
    @FXML
    private ComboBox<String> wallet;
    //    @FXML
//    private TextField pk;
    @FXML
    private TextField uid;
    @FXML
    private Label status;
    @FXML
    private Label account;
    @FXML
    private Label txReceived;
    @FXML
    private Label txSent;
    @FXML
    private HBox sentCerts, receivedCerts;

    private UserSettings userSettings = new UserSettings();

    @Autowired
    private Index index;

    @Autowired
    private WebOfTrust wot;

    @Autowired
    private TransactionService txService;


    @Autowired
    private EntityManager em;


    private static ObservableList<String> periodList = FXCollections.observableArrayList("Day", "Week", "Month", "Equinox", "Year", "All");


    private ObservableList<Transaction> sentTxList = FXCollections.observableArrayList();
    private ObservableList<Transaction> receivedTxList = FXCollections.observableArrayList();
    private ObservableList<CertRecord> sentCertList = FXCollections.observableArrayList();
    private ObservableList<CertRecord> receivedCertList = FXCollections.observableArrayList();
    private ObjectProperty<IINDEX> profileIdentity = new SimpleObjectProperty<>();
    private ObjectProperty<MINDEX> mem = new SimpleObjectProperty<>();
    private ObjectProperty<Account> acc = new SimpleObjectProperty<>();
    private XYChart.Series<Long, Integer> series = new XYChart.Series<>();


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

    public VBox drawCert(CertRecord c) {
        var cert = c.getCert();
        var iss = c.getIssuer();
        var rec = c.getReceiver();
        var date = new Date(cert.getWritten().getMedianTime() * 1000L);
        var b = new Button("more...");
        var iIss = new Image("https://g1.data.duniter.fr/user/profile/" + iss.getPub() + "/_image/avatar.png", 50, 50, true, true);
        var iRec = new Image("https://g1.data.duniter.fr/user/profile/" + rec.getPub() + "/_image/avatar.png", 50, 50, true, true);

        b.setTooltip(new Tooltip(iss.getPub() + " -> " + rec.getPub()));
        var res = new VBox(5);
        // add the issuer
        if (!profileIdentity.getValue().getPub().equals(iss.getPub())) {
            res.getChildren().add(new Label(iss.getUid()));
            res.getChildren().add(new ImageView(iIss));
        }

        res.getChildren().add(new Label(Formats.DATE_FORMAT.format(date)));
        // add the issuer
        if (!profileIdentity.getValue().getPub().equals(rec.getPub())) {
            res.getChildren().add(new ImageView(iRec));
            res.getChildren().add(new Label(rec.getUid()));//,
        }
        res.setPrefHeight(80);
        res.setPrefWidth(150);
        return res;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        profileIdentity.addListener((observable, oldValue, newValue) -> {
            uid.setText(newValue.getUid());

        });

        wallet.setItems(FXCollections.observableArrayList(userSettings.getWallets()));
        wallet.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newWallet) -> {
            index.reduceI(newWallet).ifPresentOrElse(i -> profileIdentity.setValue(i), () -> {
            });

            Platform.runLater(() -> {
                sentTxList.setAll(txService.transactionsOfIssuer(newWallet));
                receivedTxList.setAll(txService.transactionsOfReceiver(newWallet));
                refreshAccountOverTime(newWallet);
            });

            Platform.runLater(() -> {
                var recs = wot.certRecord(newWallet);

                sentCertList.setAll(recs.stream()
                        .filter(r -> r.getIssuer().getPub().equals(newWallet))
                        .collect(Collectors.toList()));
                receivedCertList.setAll(recs.stream().filter(r -> r.getReceiver().getPub().equals(newWallet)).collect(Collectors.toList()));

                sentCerts.getChildren().setAll(
                        sentCertList.stream()
                                .map(this::drawCert)
                                .collect(Collectors.toList()));
                receivedCerts.getChildren().setAll(
                        receivedCertList.stream()
                                .map(this::drawCert)
                                .collect(Collectors.toList()));

            });

            Platform.runLater(() -> {
                account.setText(index.getAccountRepo().accountOf(newWallet).getBSum() + "");
            });

        });

        // pk.textProperty().bind(wallet.valueProperty());


        txSent.textProperty().bind(Bindings.createStringBinding(() -> sentTxList.size() + "", sentTxList));
        txReceived.textProperty().bind(Bindings.createStringBinding(() -> receivedTxList.size() + "", receivedTxList));
        status.textProperty().bind(Bindings.createStringBinding(() -> profileIdentity.get() != null ? "ok" : "not ok", profileIdentity));

        wallet.getSelectionModel().selectFirst();

        txChart.getData().setAll(series);
        txTime.setTickLabelFormatter(new MyStringConv());

    }

    private void refreshAccountOverTime(String currentProfile) {
        Platform.runLater(() -> {
            series.getData().clear();
            var transactions = Stream.concat(receivedTxList.stream(), sentTxList.stream()).sorted(Comparator.comparing(a -> a.getWritten().getMedianTime())).collect(Collectors.toList());
            List<Pair<Long, Integer>> tmp = new ArrayList<>();

            for (var tx : sentTxList) {
                var sum = 0;
                for (var j = 0; j < tx.getIssuers().size(); j++) {
                    if (tx.getIssuers().get(j).equals(currentProfile)) {
                        int finalJ = j;
                        var unlocks = tx.getUnlocks().stream().filter(un -> un.getFctParam().equals(String.valueOf(finalJ))).map(TxUnlock::getInputRef).collect(Collectors.toList());
                        for (int in = 0; in < tx.getInputs().size(); in++) {
                            if (unlocks.contains(in))
                                sum -= tx.getInputs().get(in).getAmount();
                        }
                    }
                }
                tmp.add(new Pair<>(tx.getWritten().getMedianTime(), sum));

            }

            for (var tx : receivedTxList) {
                var sum = 0;
                for (int out = 0; out < tx.getOutputs().size(); out++) {
                    var txOut = tx.getOutputs().get(out);
                    if (txOut.getCondition().contains("SIG(" + currentProfile + ")")) {
                        sum += txOut.getAmount();
                    }
                }

                tmp.add(new Pair<>(tx.getWritten().getMedianTime(), sum));

            }

            txService.dividendsOf(currentProfile)
                    .forEach(b -> {
                        //LOG.info(b);
                        tmp.add(new Pair<>(b.getMedianTime(), b.getDividend()));
                    });

            var dailyVariation = tmp
                    .stream()
                    .collect(Collectors.groupingBy(
                            pair -> toDay(pair.getKey()),
                            Collectors.summingInt(Pair::getValue)))
                    .entrySet().stream().sorted(Comparator.comparingLong(Map.Entry::getKey))

                   // .map(frame -> new XYChart.Data<>(frame.getKey(), frame.getValue()))
                    .collect(Collectors.toList());

            var cumulativeSum = 0  ;
            for (Map.Entry<Long, Integer> d : dailyVariation) {
                cumulativeSum += d.getValue();
                series.getData().add(new XYChart.Data<>(d.getKey(), cumulativeSum));
            }

            var begin = dailyVariation.get(0).getKey() ;
            var end = dailyVariation.get(transactions.size() - 1).getKey();
            txTime.setLowerBound(begin);
            txTime.setUpperBound(end);
            txTime.setTickUnit((end - begin) / 4.);
            txTime.setAutoRanging(false);
            //series.getData().addAll(dailyVariation);
        });
    }


    public Long toDay(long medianTime) {
        return Instant.ofEpochSecond(medianTime).truncatedTo(ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toEpochSecond();
    }

}
