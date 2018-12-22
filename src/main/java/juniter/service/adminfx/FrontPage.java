package juniter.service.adminfx;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import juniter.core.validation.StaticValid;
import juniter.repository.jpa.BlockRepository;
import juniter.service.Indexer;
import juniter.service.adminfx.include.AbstractJuniterFX;
import juniter.service.adminfx.include.ConfirmBox;
import juniter.service.bma.loader.BlockLoader;
import juniter.service.bma.loader.MissingBlocksLoader;
import juniter.service.bma.loader.PeerLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class FrontPage extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger();


    // updatable value from the outside
    public static SimpleDoubleProperty currentBindex = new SimpleDoubleProperty(.0);
    public static SimpleDoubleProperty maxBindex = new SimpleDoubleProperty(42.);
    public static SimpleDoubleProperty maxDBBlock = new SimpleDoubleProperty(42.);
    public static SimpleDoubleProperty maxPeerBlock = new SimpleDoubleProperty(42.);

    public static SimpleBooleanProperty isIndexing = new SimpleBooleanProperty(false);
    public static SimpleBooleanProperty isDownloading = new SimpleBooleanProperty(false);

    public static SimpleStringProperty indexLogMessage = new SimpleStringProperty("here comes the log");


    @FXML private Label indexLog;

    @FXML private TextField delSome;

    @FXML private TextField tstSome;



    //                                  LOADING  SECTION
    @Autowired  private BlockRepository blockRepo;

    @Autowired  private Indexer indexer;

    @FXML private TextField indexTil;
    @FXML private ProgressBar indexBar;
    @FXML private ProgressIndicator indexIndic;

    @FXML private ProgressBar loadBar ;
    @FXML private ProgressIndicator loadIndic;



    @Autowired
    BlockLoader blockLoader ;
    @Autowired
    MissingBlocksLoader mBlockLoader ;




    public FrontPage() { }

    @FXML
    public void indexUntil(){

        if(isIndexing.get())
            return;

        int until ;
        try{
            until = Integer.parseInt(indexTil.getText());
        }catch(Exception e){
            until = blockRepo.currentBlockNumber();
        }

        maxBindex.setValue(until);
        indexer.indexUntil(until);
    }

    @FXML
    public void indexReset(){
        indexer.init();
        currentBindex.setValue(0);
    }



    public void index1(ActionEvent actionEvent) {

        if(isIndexing.get())
            return;

        indexer.indexUntil(currentBindex.intValue()+1);
    }

    public void revert1(ActionEvent actionEvent) {
        if(isIndexing.get())
            return;


    }



    @FXML
    public void bulkLoad(){

        if(isDownloading.getValue()){
            return;
        }
        blockLoader.bulkLoad();
    }



    @FXML
    public void loadMissing(){
        mBlockLoader.checkMissingBlocks();
    }

    @Autowired
    PeerLoader peerLoader;

    @FXML
    public void pairing(){
        peerLoader.doPairing();
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
                            StaticValid.assertBlock(block);

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

        if (null == blockRepo) {
            throw new IllegalStateException("BlockRepository was not injected properly");
        }

        BorderPane page = (BorderPane) load("/adminfx/FrontPage.fxml");

        Scene scene = new Scene(page);

        primaryStage.setTitle("Juniter - Admin panel ");
        primaryStage.setScene(scene);

        primaryStage.getIcons().add(new Image(FrontPage.class.getResourceAsStream("/adminfx/images/logo.png")));
        primaryStage.show();
        primaryStage.setOnHidden(e -> Platform.exit());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        indexBar.progressProperty().bind(currentBindex.divide(maxBindex));
        indexIndic.progressProperty().bind(currentBindex.divide(maxBindex));

        loadBar.progressProperty().bind(maxDBBlock.divide(maxPeerBlock));
        loadIndic.progressProperty().bind(maxDBBlock.divide(maxPeerBlock));

        indexLog.textProperty().bind(indexLogMessage.concat(" - Bidx: ").concat(currentBindex).concat(" - DB: ").concat(maxDBBlock).concat(" - Peer: ").concat(maxPeerBlock.intValue()).concat(" - "));

    }


    @FXML
    private void peerCheck(){
        peerLoader.runPeerCheck();
    }
}
