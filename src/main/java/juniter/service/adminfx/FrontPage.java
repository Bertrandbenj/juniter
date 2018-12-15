package juniter.service.adminfx;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
import juniter.service.bma.loader.BlockLoader;
import juniter.service.bma.loader.MissingBlocksLoader;
import juniter.service.bma.loader.PeerLoader;
import juniter.service.adminfx.include.ConfirmBox;
import juniter.service.adminfx.include.Menu;
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



    @FXML private TextField delSome;

    @FXML private TextField tstSome;

    @FXML private Menu menuController;




    //                                  LOADING  SECTION
    @Autowired  private BlockRepository blockRepo;

    @Autowired  private Indexer indexer;

    @FXML private TextField indexTil;
    @FXML private ProgressBar indexBar;
    @FXML private ProgressIndicator indexIndic;
    public static DoubleProperty indexUpdater = new SimpleDoubleProperty(.0);

    @FXML private ProgressBar loadBar ;
    @FXML private ProgressIndicator loadIndic;
    public static DoubleProperty loadUpdater = new SimpleDoubleProperty(.0);




    public FrontPage() { }

    @FXML
    public void indexUntil(){

        int res ;
        try{
            res = Integer.parseInt(indexTil.getText());
        }catch(Exception e){
            res = blockRepo.currentBlockNumber();
        }

        indexer.indexUntil(res);
    }

    @FXML
    public void indexReset(){
        indexer.index.init(true);
    }

    @Autowired
    BlockLoader blockLoader ;

    @FXML
    public void bulkLoad(){
        blockLoader.bulkLoad();
    }

    @Autowired
    MissingBlocksLoader mBlockLoader ;

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

        indexBar.progressProperty().bind(indexUpdater);
        indexIndic.progressProperty().bind(indexUpdater);

        loadBar.progressProperty().bind(loadUpdater);
        loadIndic.progressProperty().bind(loadUpdater);

    }

    public void index1(ActionEvent actionEvent) {
    }

    public void revert1(ActionEvent actionEvent) {
    }
}
