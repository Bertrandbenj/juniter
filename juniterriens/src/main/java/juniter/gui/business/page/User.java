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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;
import juniter.core.model.dbo.index.Account;
import juniter.core.model.dbo.index.CINDEX;
import juniter.core.model.dbo.index.IINDEX;
import juniter.core.model.dbo.index.MINDEX;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.tx.TxUnlock;
import juniter.gui.technical.AbstractJuniterFX;
import juniter.repository.jpa.block.BlockRepository;
import juniter.repository.jpa.block.TxRepository;
import juniter.repository.jpa.index.AccountRepository;
import juniter.service.core.Index;
import juniter.user.UserSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
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
    @FXML
    private TextField pk;
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
    private ListView<CINDEX> sentCerts;
    @FXML
    private ListView<CINDEX> receiveCerts;

    private UserSettings userSettings = new UserSettings();

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private Index index;
    @Autowired
    private TxRepository txRepository;
    @Autowired
    private BlockRepository blockRepo;

    @Autowired
    private EntityManager em;


    private static ObservableList<String> periodList = FXCollections.observableArrayList("Day", "Week", "Month", "Equinox", "Year", "All");


    private ObservableList<Transaction> sentTxList = FXCollections.observableArrayList();
    private ObservableList<Transaction> receivedTxList = FXCollections.observableArrayList();
    private ObservableList<CINDEX> sentCertList = FXCollections.observableArrayList();
    private ObservableList<CINDEX> receivedCertList = FXCollections.observableArrayList();
    private ObjectProperty<IINDEX> idty = new SimpleObjectProperty<>();
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wallet.setItems(FXCollections.observableArrayList(userSettings.getWallets()));
        wallet.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            Platform.runLater(() -> {
                sentTxList.setAll(txRepository.transactionsOfIssuer_(newValue));
                receivedTxList.setAll(txRepository.transactionsOfReceiver_(newValue));
                refreshAccountOverTime(newValue);
            });

            Platform.runLater(() -> {
                receivedCertList.setAll(index.getC(null, newValue).collect(Collectors.toList()));
                sentCertList.setAll(index.getC(newValue, null).collect(Collectors.toList()));
            });

            Platform.runLater(() -> {
                account.setText(accountRepository.accountOf(newValue).getBSum() + "");
            });

        });

        pk.textProperty().bind(wallet.valueProperty());
        sentCerts.setItems(sentCertList);
        receiveCerts.setItems(receivedCertList);
        txSent.textProperty().bind(Bindings.createStringBinding(() -> sentTxList.size() + "", sentTxList));
        txReceived.textProperty().bind(Bindings.createStringBinding(() -> receivedTxList.size() + "", receivedTxList));
        status.textProperty().bind(Bindings.createStringBinding(() -> idty.get() != null ? "ok" : "not ok", idty));

        wallet.getSelectionModel().selectFirst();

        txChart.getData().setAll(series);
        txTime.setTickLabelFormatter(new MyStringConv());

    }

    private void refreshAccountOverTime(String newValue) {
        Platform.runLater(() -> {
            series.getData().clear();
            var data = Stream.concat(receivedTxList.stream(), sentTxList.stream()).sorted(Comparator.comparing(a -> a.getWritten().getMedianTime())).collect(Collectors.toList());
            List<Pair<Long, Integer>> tmp = new ArrayList<>();
            for (var t : data) {
                var sum = 0;
                for (var j = 0; j < t.getIssuers().size(); j++) {
                    if (t.getIssuers().get(j).equals(newValue)) {
                        int finalJ = j;
                        var unlocks = t.getUnlocks().stream().filter(un -> un.getFctParam().equals(String.valueOf(finalJ))).map(TxUnlock::getInputRef).collect(Collectors.toList());
                        for (int in = 0; in < t.getInputs().size(); in++) {
                            if (unlocks.contains(in))
                                sum -= t.getInputs().get(in).getAmount();
                        }
                        for (int out = 0; out < t.getOutputs().size(); out++) {
                            var txOut = t.getOutputs().get(out);
                            if (txOut.getCondition().contains("SIG(" + newValue + ")")) {
                                sum += txOut.getAmount();
                            }
                        }
                    }
                }

                tmp.add(new Pair(t.getWritten().getMedianTime(), sum));

            }

            txRepository
                    .dividendsOf(newValue)
                    .forEach(b -> {
                        LOG.info(b);
                        tmp.add(new Pair(b.getMedianTime(), b.getDividend()));
                    });

            var result = tmp.stream().collect(Collectors.groupingBy(Pair::getKey, Collectors.summingInt(Pair::getValue)));

            var begin = data.get(0).getWritten().getMedianTime();
            var end = data.get(data.size() - 1).getWritten().getMedianTime();
            txTime.setLowerBound(begin);
            txTime.setUpperBound(end);
            txTime.setTickUnit((end - begin) / 4.);
            txTime.setAutoRanging(false);
            series.getData().addAll(result.entrySet().stream()
                    .map(frame -> new XYChart.Data<>(frame.getKey(), frame.getValue()))
                    .collect(Collectors.toList()));
        });
    }


}
