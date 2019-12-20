package juniter.gui.business.popup;

import com.google.common.util.concurrent.AtomicDouble;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import juniter.core.model.dbo.net.NetStats;
import juniter.core.model.dbo.net.EndPointType;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.tx.TxInput;
import juniter.core.model.dbo.tx.TxOutput;
import juniter.core.model.dbo.tx.TxUnlock;
import juniter.core.model.dto.raw.WrapperResponse;
import juniter.core.model.dto.raw.WrapperTransaction;
import juniter.gui.game.screens.Room;
import juniter.gui.technical.AbstractJuniterFX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static juniter.gui.JuniterBindings.*;

@Component

public class TxBox extends AbstractJuniterFX implements Initializable {
    private static final Logger LOG = LogManager.getLogger(TxBox.class);
    @FXML
    public TextField amount;
    @FXML
    public VBox outputs;
    @FXML
    public TextArea doc;
    @FXML
    public Label targetPubkeyLabel;
    @FXML
    public TextField comment;
    @FXML
    public Button ok;
    @FXML
    public Button cancel;


    public static boolean displayLater(String prefix, String targetPubkey) {
        Platform.runLater(() -> Room.popupOpen = TxBox.display(prefix, targetPubkey));
        return false;

    }

    private static boolean display(String prefix, String targetPubkeyL) {

        LOG.info("Opening txBox for pub " + targetPubkeyL + " on prefix " + prefix);

        targetComment.setValue(prefix);

        targetPubkey.setValue(targetPubkeyL);

        var userPub = secretBox.get().getPublicKey();
        var tx = new TxBox();

        var stage = new Stage(StageStyle.UNDECORATED);
        stage.setTitle("Tax panel");

        try {

            tx.start(stage);
            tx.init();

            //stage.showAndWait();
        } catch (Exception e) {
            LOG.error("starting txBox",e);
        }
        stage.show();

        return false;
    }


    @Override
    public void start(Stage window) throws Exception {

        window.setOnCloseRequest(e -> Room.popupOpen = false);

        window.getIcons().add(new Image("/gui/images/tx.png"));
        window.initModality(Modality.APPLICATION_MODAL);


        window.setMinWidth(700);
        window.setMinHeight(400);

        var page = (BorderPane) load("/gui/include/TxBox.fxml");

        Scene scene = new Scene(page);
        window.setScene(scene);
        window.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cancel.setCancelButton(true);
        cancel.setOnAction(e -> ((Stage) cancel.getScene().getWindow()).close());

        var tx = new Transaction();

        tx.setCurrency("g1");
        tx.setLocktime(0);
        tx.setVersion(10);

        tx.setBlockstamp(currenBlock.get().bStamp());
        tx.setIssuers(List.of(targetPubkeyLabel.getText()));
        tx.setComment("");

        targetPubkeyLabel.textProperty().bind(targetPubkey);
        comment.textProperty().bind(targetComment);

        amount.setOnAction(al -> {
            LOG.info("onAction");

            var amo = Integer.parseInt(amount.getText());

            //set inputs
            AtomicInteger ai = new AtomicInteger(0);
            tx.setInputs(sources
                    .stream()
                    .takeWhile(s -> ai.getAndAdd(s.getAmount()) < amo)
                    .collect(Collectors.toList()));

            // set unlocks
            for (int i = 0; i < tx.getInputs().size(); i++)
                tx.getUnlocks().add(new TxUnlock(i + ":SIG(" + 0 + ")"));


            // set tax outputs
            AtomicDouble perc = new AtomicDouble(0);
            tax.forEach((k, v) -> {
                if (k.equals(targetPubkeyLabel.getText()))
                    return; // break if taxed address is an output

                var rate = v * overallTaxRate.get() / 100;


                perc.addAndGet(rate);
                var to = new TxOutput();
                to.setBase(0);
                to.setAmount((int) Math.ceil(amo * rate));
                to.setCondition("SIG(" + k + ")");
                tx.getOutputs().add(to);
                outputs.getChildren().add(new Label((100 * rate) + "% - " + to.getAmount() + " - " + k));
            });


            // set actual output
            var amountLeft = amo - tx.getOutputs().stream().mapToInt(TxOutput::getAmount).sum();
            var to = new TxOutput();
            to.setBase(0);
            to.setAmount(amountLeft);
            to.setCondition("SIG(" + targetPubkeyLabel.getText() + ")");
            tx.getOutputs().add(to);
            outputs.getChildren().add(new Label((100 * (1 - perc.get())) + "% - " + to.getAmount() + " - " + targetPubkeyLabel));


            // set rest output
            var rest = new TxOutput();
            rest.setBase(0);
            rest.setAmount(tx.getInputs().stream().mapToInt(TxInput::getAmount).sum() - amo);
            rest.setCondition("SIG(" + targetPubkeyLabel.getText() + ")");
            tx.getOutputs().add(rest);


            // set Doc to emit
            tx.setSignatures(List.of(secretBox.get().sign(tx.toDUPdoc(false))));

            doc.setText(tx.toDUPdoc(true));

        });

        //Clicking will set answer and close window
        ok.setOnAction(e -> {

            var dest = "tx/process";
            var reqBodyData = new WrapperTransaction(tx.toDUPdoc(true));

            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            LOG.info("send tx {}", reqBodyData);


            peerProp.get().nextHosts(EndPointType.BASIC_MERKLED_API, 5)
                    .stream().parallel()
                    .map(NetStats::getHost)
                    .forEach(reqURL -> {

                        try {
                            //objectMapper.writeValueAsString(reqBodyData);
                            //var reqURL = JuniterBindings.peerProp.get().nextHost().get().getHost();
                            reqURL += (reqURL.endsWith("/") ? "" : "/") + dest;

                            LOG.info("sent Tx to {}", reqURL);

                            var request = new HttpEntity<>(reqBodyData, headers);

                            var response = new RestTemplate().postForEntity(reqURL, request, WrapperResponse.class);

                            LOG.info("sendDoc response {}", response);

                            if (response.getStatusCodeValue() != 200)
                                throw new AssertionError("post Doc status code {} " + response);
                            else
                                LOG.info("sendDoc response : {}", response);

//                            window.close();

                        } catch (Exception | AssertionError ex) {
                            StringWriter sw = new StringWriter();
                            ex.printStackTrace(new PrintWriter(sw));
                            LOG.error("sendDoc Error ", ex);
                        }

                    });

        });

    }
}