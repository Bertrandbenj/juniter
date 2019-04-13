package juniter.juniterriens;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.BINDEX;
import juniter.juniterriens.include.AbstractJuniterFX;
import juniter.juniterriens.include.Bindings;
import juniter.repository.jpa.block.BlockRepository;
import juniter.repository.jpa.index.BINDEXRepository;
import juniter.service.bma.loader.BlockLoader;
import juniter.service.bma.loader.MissingBlocksLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class FrontPage extends AbstractJuniterFX implements Initializable {

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


    public FrontPage() {
    }


    @FXML
    public void bulkLoad() {

        if (Bindings.isDownloading.getValue()) {
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

        BorderPane page = (BorderPane) load("/juniterriens/FrontPage.fxml");

        Scene scene = new Scene(page);

        primaryStage.setTitle("Juniter - Admin panel ");
        primaryStage.setScene(scene);

        primaryStage.getIcons().add(new Image("/juniterriens/images/logo.png"));
        primaryStage.show();
        primaryStage.setOnHidden(e -> Platform.exit());

        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadBar.progressProperty().bind(Bindings.currentDBBlock.divide(Bindings.maxPeerBlock));
        Bindings.maxBindex.setValue(blockRepo.currentBlockNumber());
        Bindings.currentDBBlock.setValue(blockRepo.count());
        Bindings.currentBindex.setValue(bRepo.head().map(BINDEX::getNumber).orElse(0));
        Bindings.maxDBBlock.setValue(blockRepo.currentBlockNumber());
        Bindings.currenBlock.setValue(blockRepo.current().orElseGet(() -> blockLoader.fetchAndSaveBlock("current")));

        m.setText(Bindings.currenBlock.get().getMonetaryMass() + "");


        var mc = Bindings.currenBlock.get().getMembersCount();
        var h24 = blockRepo.block(Bindings.currenBlock.get().getNumber() - 288)
                .map(DBBlock::getMembersCount)
                .map(x -> mc - x)
                .orElse(0);

        n.setText(mc + " " + (h24 == 0 ? "" : (h24 > 0 ? "+" : "-") + h24));

        Date date = new Date(Bindings.currenBlock.get().getMedianTime() * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+1"));
        String formattedDate = sdf.format(date);
        median.setText(formattedDate);
        size.setText(Bindings.currenBlock.get().getSize() + "");
        number.setText(Bindings.currenBlock.get().getNumber() + "");
        // test.setText(txRepo.transactionsOfIssuer_("4weakHxDBMJG9NShULG1g786eeGh7wwntMeLZBDhJFni").singleton(0).getWrittenOn()+"");

    }

}
