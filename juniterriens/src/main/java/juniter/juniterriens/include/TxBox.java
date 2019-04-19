package juniter.juniterriens.include;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.tx.TxOutput;
import juniter.core.model.dto.raw.WrapperTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TxBox {
    private static final Logger LOG = LogManager.getLogger(TxBox.class);


    public static boolean display(String prefix, Boolean txOpen) {
        var pub = JuniterBindings.secretBox.get().getPublicKey();

        Transaction tx = new Transaction();

        tx.setCurrency("g1");
        tx.setComment(prefix);
        tx.setBlockstamp(JuniterBindings.currenBlock.get().bStamp());

        tx.setSignatures(List.of(JuniterBindings.secretBox.get().sign(tx.toDUPdoc(false))));

        tx.setIssuers(List.of(pub));
        tx.setLocktime(0);
        tx.setVersion(10);


        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(700);

        TextField amount = new TextField();
        Label pubkey = new Label(pub);
        Label doc = new Label();
        List<Label> outputs = new ArrayList<>();


        amount.setOnAction(al -> {
            LOG.info("onAction");

            Double amo = Double.parseDouble(amount.getText());

            JuniterBindings.remunerate.forEach((k, v) -> {

                var to = new TxOutput();
                to.setBase(0);
                to.setAmount((int) Math.ceil(amo * v));
                to.setCondition("SIG(" + k + ")");
                tx.getOutputs().add(to);
            });

            var amountLeft = 1 - JuniterBindings.remunerate.values().stream().mapToDouble(Double::doubleValue).sum();
            var to = new TxOutput();
            to.setBase(0);
            to.setAmount((int) Math.ceil(amountLeft));
            to.setCondition("SIG(" + pub + ")");
            tx.getOutputs().add(to);

            AtomicInteger ai = new AtomicInteger(0);
            var ss = JuniterBindings.sources
                    .stream()
                    .takeWhile(s -> ai.getAndAdd(s.getAmount()) < amo)
                    .collect(Collectors.toList());

            tx.setInputs(ss);


            doc.setText(tx.toDUPdoc(true));

        });


        //Create two buttons
        Button send = new Button("Send");

        //Clicking will set answer and close window
        send.setOnAction(e -> {

            var dest = "tx/process";
            var reqBodyData = new WrapperTransaction(tx.toDUPdoc(true));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


            try {
                //objectMapper.writeValueAsString(reqBodyData);
                var reqURL = JuniterBindings.peers.get().nextHost().get().getHost();
                reqURL += (reqURL.endsWith("/") ? "" : "/") + dest;

                LOG.info("sendDoc posting {} {}", reqURL, reqBodyData);

                var request = new HttpEntity<>(reqBodyData, headers);

                var response = new RestTemplate().postForEntity(reqURL, request, Object.class);

                LOG.info("sendDoc response {}", response);

                if (response.getStatusCodeValue() != 200)
                    throw new AssertionError("post doc status code {} " + response);
                else
                    LOG.info("sendDoc response : {}", response);

                window.close();

            } catch (Exception | AssertionError ex) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                LOG.error("sendDoc Error ", ex);
            }

        });

        VBox layout = new VBox(10);

        //Add buttons
        layout.getChildren().addAll(amount, pubkey);
        layout.getChildren().addAll(outputs);
        layout.getChildren().addAll(send);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return false;
    }

}