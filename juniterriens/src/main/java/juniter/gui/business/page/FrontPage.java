package juniter.gui.business.page;


import javafx.application.Preloader;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import juniter.core.model.dbo.index.BINDEX;
import juniter.gui.technical.AbstractJuniterFX;
import juniter.gui.technical.PageName;
import juniter.repository.jpa.index.AccountsRepository;
import juniter.service.bma.loader.BMABlockFetcher;
import juniter.service.jpa.JPABlockService;
import juniter.service.jpa.Index;
import juniter.service.jpa.PeerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static juniter.gui.JuniterBindings.*;
import static juniter.gui.technical.Formats.DATETIME_FORMAT;

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
    private BMABlockFetcher blockFetchers;

    @Autowired
    private Index index;

    @Autowired
    private JPABlockService blockService;

    @Autowired
    private PeerService peers;

    @Autowired
    private AccountsRepository rep;

    public FrontPage() {
    }


    @FXML
    public void bulkLoad() {
        blockFetchers.startBulkLoad();
    }


    @FXML
    public void loadMissing() {
        blockService.checkMissingBlocksAsync();
    }


    @Override
    public void start(Stage primaryStage) {
        LOG.info("Starting FrontPage");

        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));

        if (null == blockService) {
            throw new IllegalStateException("JPABlockService was not injected properly");
        }

        var scene = screenController.getMain();
        if (scene == null) {
            BorderPane page = (BorderPane) load("/gui/page/FrontPage.fxml");

            screenController.addScreen(PageName.MAIN, page);
            scene = new Scene(page);
            screenController.setMain(scene);
            screenController.activate(PageName.MAIN);

        }

        screenController.setMain(scene);
        primaryStage.getIcons().add(new Image("/gui/images/logo.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOG.info("Accounts " + rep.accountOf("4weakHxDBMJG9NShULG1g786eeGh7wwntMeLZBDhJFni"));
        currentBindexN.bind(Bindings.createIntegerBinding(() -> currentBindex.get().getNumber(), currentBindex));
        loadBar.progressProperty().bind(dlRatio);

        peerProp.set(peers);

        m.textProperty().bind(Bindings.createStringBinding(() -> String.format("%,.2f", currentBindex.get().getMass() / 100.), currentBindex));


        n.textProperty().bind(Bindings.createStringBinding(() -> {
                    var mc = currentBindex.get().getMembersCount();
                    var h24 = index.getBRepo().byNum(currentBindexN.get() - 288, "g1")
                            .map(BINDEX::getMembersCount)
                            .map(x -> mc - x)
                            .orElse(0);
                    return mc + " " + (h24 == 0 ? "" : (h24 > 0 ? "+" : "-") + h24);
                }
                , currentBindex));


        median.textProperty().bind(Bindings.createObjectBinding(() -> {
                    var date = new Date(currentBindex.get().getMedianTime() * 1000L);

                    return DATETIME_FORMAT.format(date);
                }
                , currentBindex));


        size.textProperty().bind(Bindings.createObjectBinding(() ->
                currentBindex.get().getSize().toString(), currentBindex));


        number.textProperty().bind(currentBindexN.asString());

        // test.setText(txRepo.transactionsOfIssuer("4weakHxDBMJG9NShULG1g786eeGh7wwntMeLZBDhJFni").singleton(0).getWrittenOn()+"");

        // INIT Binded Properties
        highestDBBlock.setValue(blockService.currentBlockNumber());
        currenBlock.setValue(blockService.currentOrFetch());
        currentDBBlockNum.setValue(blockService.currentBlockNumber());
        currentBindex.setValue(index.head().orElse(BINDEX.before0()));

    }


}
