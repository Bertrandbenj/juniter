package juniter.service.adminfx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.index.BINDEXRepository;
import juniter.service.adminfx.include.AbstractJuniterFX;
import juniter.service.adminfx.include.Bus;
import juniter.service.bma.loader.BlockLoader;
import juniter.service.bma.loader.MissingBlocksLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class FrontPage extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger();


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



    public FrontPage() {
    }


    @FXML
    public void bulkLoad() {

        if (Bus.isDownloading.getValue()) {
            return;
        }
        blockLoader.bulkLoad2();
    }


    @FXML
    public void loadMissing() {
        mBlockLoader.checkMissingBlocks();
    }

    Scene scene;

    @Override
    public void start(Stage primaryStage) {

        if (null == blockRepo) {
            throw new IllegalStateException("BlockRepository was not injected properly");
        }

        BorderPane page = (BorderPane) load("/adminfx/FrontPage.fxml");

        scene = new Scene(page);

        primaryStage.setTitle("Juniter - Admin panel ");
        primaryStage.setScene(scene);

        primaryStage.getIcons().add(new Image(FrontPage.class.getResourceAsStream("/adminfx/images/logo.png")));
        primaryStage.show();
        primaryStage.setOnHidden(e -> Platform.exit());



    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        loadBar.progressProperty().bind(Bus.currentDBBlock.divide(Bus.maxPeerBlock));
        Bus.maxBindex.setValue(blockRepo.currentBlockNumber());
        Bus.currentDBBlock.setValue(blockRepo.count());
        Bus.currentBindex.setValue(bRepo.head().map(b -> b.number).orElse(0));
        Bus.maxDBBlock.setValue(blockRepo.currentBlockNumber());


    }


}
