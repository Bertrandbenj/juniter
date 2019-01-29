package juniter.service.adminfx;

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
import juniter.repository.jpa.BlockRepository;
import juniter.service.adminfx.include.AbstractJuniterFX;
import juniter.service.adminfx.include.ConfirmBox;
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

                    blockRepo.block(id).ifPresent(block -> {
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
                var res =  new Button(ns.getSuccess() + "/" + ns.getCount() + " " +
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
    }


    public void ping(ActionEvent actionEvent) {
        peerService.huhu();
    }
}
