package juniter.gui;


import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.BINDEX;
import juniter.gui.include.AbstractJuniterFX;
import juniter.gui.include.JuniterBindings;
import juniter.repository.jpa.index.BINDEXRepository;
import juniter.service.BlockService;
import juniter.service.bma.PeerService;
import juniter.service.bma.loader.BlockLoader;
import juniter.service.bma.loader.MissingBlocksLoader;
import juniter.service.ipfs.Interplanetary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import static juniter.gui.include.JuniterBindings.*;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class FrontPage extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(FrontPage.class);


    @FXML
    private HBox canvasRoot;
    @FXML
    private Label size;

    @FXML
    private Label number;

    @FXML
    private Label median;

    @FXML
    private Label m;

    @FXML
    private Label n;


    //                                  LOADING  SECTION
    @FXML
    private ProgressBar loadBar;

    @Autowired
    private BlockLoader blockLoader;

    @Autowired
    private MissingBlocksLoader mBlockLoader;

    @Autowired
    private BINDEXRepository bRepo;

    @Autowired
    private BlockService blockService;

    @Autowired
    private PeerService peers;

    @Autowired
    private Optional<Interplanetary> interplanetary;
//    private Interplanetary interplanetary;

    public FrontPage() {
    }


    @FXML
    public void bulkLoad() {

        if (isDownloading.getValue()) {
            return;
        }
        blockLoader.bulkLoad2();
    }


    @FXML
    public void loadMissing() {
        mBlockLoader.checkMissingBlocks();
    }


    @Override
    public void start(Stage primaryStage) {

        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));

        if (null == blockService) {
            throw new IllegalStateException("BlockService was not injected properly");
        }

        var scene = screenController.getMain();
        if (screenController.getMain() == null) {
            BorderPane page = (BorderPane) load("/gui/FrontPage.fxml");
            screenController.addScreen("Main", page);
            scene = new Scene(page);
            screenController.setMain(scene);
            screenController.activate("Main");

        }

        screenController.setMain(scene);

        primaryStage.setScene(scene);
        primaryStage.show();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadBar.progressProperty().bind(currentDBBlock.divide(maxPeerBlock));
        maxBindex.setValue(blockService.currentBlockNumber());
        currentDBBlock.setValue(blockService.currentBlockNumber());
        currentBindex.setValue(bRepo.head().map(BINDEX::getNumber).orElse(0));
        maxDBBlock.setValue(blockService.currentBlockNumber());
        currenBlock.setValue(blockService.current().orElseGet(() -> blockLoader.fetchAndSaveBlock("current")));

        JuniterBindings.peers.set(peers);

        m.textProperty().bind(Bindings.createObjectBinding(() ->
                String.format("%,.2f", currenBlock.get().getMonetaryMass()/100.)));


        var mc = currenBlock.get().getMembersCount();
        var h24 = blockService.block(currenBlock.get().getNumber() - 288)
                .map(DBBlock::getMembersCount)
                .map(x -> mc - x)
                .orElse(0);

        n.textProperty().bind(Bindings.createObjectBinding(() ->
                mc + " " + (h24 == 0 ? "" : (h24 > 0 ? "+" : "-") + h24)));


        median.textProperty().bind(Bindings.createObjectBinding(() -> {
                    var date = new Date(currenBlock.get().getMedianTime() * 1000L);
                    var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+1"));
                    var formattedDate = sdf.format(date);
                    return formattedDate;
                }
                , currenBlock));


        size.textProperty().bind(Bindings.createObjectBinding(() ->
                currenBlock.get().getSize().toString(), currenBlock));


        number.textProperty().bind(Bindings.createObjectBinding(() ->
                currenBlock.get().getNumber().toString(), currenBlock));

        // test.setText(txRepo.transactionsOfIssuer_("4weakHxDBMJG9NShULG1g786eeGh7wwntMeLZBDhJFni").singleton(0).getWrittenOn()+"");


    }


    @FXML
    public void ipfs() {

        Platform.runLater(() ->
                interplanetary.ifPresent(Interplanetary::dumpChain)
        );


    }


}
