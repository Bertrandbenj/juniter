package juniter.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import juniter.core.crypto.SecretBox;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.BINDEX;
import juniter.core.model.dbo.index.CINDEX;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.tx.TxInput;
import juniter.gui.technical.I18N;
import juniter.gui.technical.PageName;
import juniter.gui.technical.ScreenController;
import juniter.gui.technical.Theme;
import juniter.service.core.PeerService;

import java.util.*;

import static juniter.gui.technical.Theme.*;

public interface JuniterBindings {


    StringProperty CURRENCY = new SimpleStringProperty("g1");

//    String JMetroBase = "/gui/css/JMetroBase.css";
//    String BLANK_THEME = "/gui/css/clair-de-june.css";
//    String DARK_THEME = "/gui/css/JMetroDarkTheme.css";
//    String LIGHT_THEME = "/gui/css/JMetroLightTheme.css";

    DoubleProperty overallTaxRate = new SimpleDoubleProperty(20.);
    DBBlock block_0 = new DBBlock();


    Map<String, Double> tax = Map.of(
            "TENGx7WtzFsTXwnbrPEvb6odX2WnqYcnnrjiiLvp1mS", 0.2, // remuniter
            "78ZwwgpgdH5uLZLbThUQH7LKwPgjMunYfLiCfUCySkM8", 0.2, // developpeurs
            "77UVGVmbBLyh5gM51X8tbMtQSvnMwps2toB67qHn32aC", 0.3, // junidev
            "2ny7YAdmzReQxAayyJZsyVYwYhVyax2thKcGknmQy5nQ", 0.3 // cgeek 5000 × 2(DU/Euro) × 12 × 4 = 480.000 DU cap
    );

    ScreenController screenController = ScreenController.singleton;
    ObjectProperty<PageName> currentPageName = new SimpleObjectProperty<>(PageName.MAIN);


    BooleanProperty playing = new SimpleBooleanProperty(false);


    // updatable value from the outside
    ObjectProperty<BINDEX> currentBindex = new SimpleObjectProperty(BINDEX.before0());
    IntegerProperty currentBindexN = new SimpleIntegerProperty(-1);
    IntegerProperty currentDBBlockNum = new SimpleIntegerProperty(42);
    IntegerProperty highestDBBlock = new SimpleIntegerProperty(42);
    IntegerProperty maxPeerBlock = new SimpleIntegerProperty(42);


    DoubleBinding indexRatio = Bindings.add(0.0,currentBindexN.divide(highestDBBlock)) ;
    DoubleBinding dlRatio = Bindings.add(0.0,new SimpleDoubleProperty(0).divide(maxPeerBlock));

    BooleanProperty isIndexing = new SimpleBooleanProperty(false);
    StringProperty rawDocument = new SimpleStringProperty("here comes the Document in DUPComponent format");

    StringProperty indexLogMessage = new SimpleStringProperty(" ... ");
    StringProperty peerLogMessage = new SimpleStringProperty(" ... ");
    StringProperty memoryLogMessage = new SimpleStringProperty(" ... ");
    StringProperty docPoolLogMessage = new SimpleStringProperty(" ... ");

    ArrayList<String> keyboardInput = new ArrayList<>();

    ObjectProperty<Theme> selectedTheme = new SimpleObjectProperty(DARK_THEME);
    ObservableList<Theme> themes = FXCollections.observableArrayList(DARK_THEME, LIGHT_THEME, BLANK_THEME);
    ObservableList<Locale> langs = FXCollections.observableArrayList(I18N.getSupportedLocales());
    ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();
    StringProperty targetPubkey = new SimpleStringProperty();
    StringProperty targetComment = new SimpleStringProperty();

    BooleanProperty advancedUser = new SimpleBooleanProperty(true);


    default StringBinding getStringBinding(String key) {
        return new StringBinding() {
            {
                bind(resources);
            }

            @Override
            public String computeValue() {
                return resources.get().getString(key);
            }
        };
    }

    ObjectProperty<DBBlock> currenBlock = new SimpleObjectProperty<>();


    // USER DATA
    ObjectProperty<SecretBox> secretBox = new SimpleObjectProperty<>(new SecretBox("salt", "password"));

    List<TxInput> sources = new ArrayList<>();
    ObjectProperty<PeerService> peerProp = new SimpleObjectProperty<>();

    List<CINDEX> certsRelated = new ArrayList<>();

    List<Transaction> txRelated = new ArrayList<>();

}
