package juniter.gui;


import javafx.application.Platform;
import javafx.application.Preloader;
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
import juniter.repository.jpa.block.BlockRepository;
import juniter.repository.jpa.index.BINDEXRepository;
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
    private BlockRepository blockRepo;
    @Autowired
    private PeerService peers;

    @Autowired
    private Optional<Interplanetary> interplanetary;
//    private Interplanetary interplanetary;

    public FrontPage() {
    }


    @FXML
    public void bulkLoad() {

        if (JuniterBindings.isDownloading.getValue()) {
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

        if (null == blockRepo) {
            throw new IllegalStateException("BlockRepository was not injected properly");
        }

        var scene = JuniterBindings.screenController.getMain();
        if (JuniterBindings.screenController.getMain() == null) {
            BorderPane page = (BorderPane) load("/gui/FrontPage.fxml");
            JuniterBindings.screenController.addScreen("Main", page);
            scene = new Scene(page);
            JuniterBindings.screenController.setMain(scene);
            JuniterBindings.screenController.activate("Main");

        }

        JuniterBindings.screenController.setMain(scene);

        primaryStage.setScene(scene);
        primaryStage.show();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadBar.progressProperty().bind(JuniterBindings.currentDBBlock.divide(JuniterBindings.maxPeerBlock));
        JuniterBindings.maxBindex.setValue(blockRepo.currentBlockNumber());
        JuniterBindings.currentDBBlock.setValue(blockRepo.count());
        JuniterBindings.currentBindex.setValue(bRepo.head().map(BINDEX::getNumber).orElse(0));
        JuniterBindings.maxDBBlock.setValue(blockRepo.currentBlockNumber());
        JuniterBindings.currenBlock.setValue(blockRepo.current().orElseGet(() -> blockLoader.fetchAndSaveBlock("current")));

        JuniterBindings.peers.set(peers);

        m.setText(JuniterBindings.currenBlock.get().getMonetaryMass() + "");


        var mc = JuniterBindings.currenBlock.get().getMembersCount();
        var h24 = blockRepo.block(JuniterBindings.currenBlock.get().getNumber() - 288)
                .map(DBBlock::getMembersCount)
                .map(x -> mc - x)
                .orElse(0);

        n.setText(mc + " " + (h24 == 0 ? "" : (h24 > 0 ? "+" : "-") + h24));

        Date date = new Date(JuniterBindings.currenBlock.get().getMedianTime() * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+1"));
        String formattedDate = sdf.format(date);
        median.setText(formattedDate);
        size.setText(JuniterBindings.currenBlock.get().getSize() + "");
        number.setText(JuniterBindings.currenBlock.get().getNumber() + "");
        // test.setText(txRepo.transactionsOfIssuer_("4weakHxDBMJG9NShULG1g786eeGh7wwntMeLZBDhJFni").singleton(0).getWrittenOn()+"");


    }


    @FXML
    public void ipfs() {

        Platform.runLater(() ->
                interplanetary.ifPresent(Interplanetary::dumpChain)
        );


    }


}
