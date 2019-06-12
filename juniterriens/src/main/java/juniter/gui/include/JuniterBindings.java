package juniter.gui.include;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import juniter.core.crypto.SecretBox;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.CINDEX;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.tx.TxInput;
import juniter.service.bma.PeerService;

import java.util.*;

public interface JuniterBindings {

    String BLANK_THEME = "/gui/css/no-theme.css";
    String DARK_THEME = "/gui/css/clair-de-june.css";

    DoubleProperty overallTaxRate = new SimpleDoubleProperty(20.);


    Map<String, Double> tax = Map.of(
            "TENGx7WtzFsTXwnbrPEvb6odX2WnqYcnnrjiiLvp1mS", 0.2, // remuniter
            "78ZwwgpgdH5uLZLbThUQH7LKwPgjMunYfLiCfUCySkM8", 0.2, // developpeurs
            "77UVGVmbBLyh5gM51X8tbMtQSvnMwps2toB67qHn32aC", 0.3, // junidev
            "2ny7YAdmzReQxAayyJZsyVYwYhVyax2thKcGknmQy5nQ", 0.3 // cgeek 5000 × 2(DU/Euro) × 12 × 4 = 480.000 DU cap
    );

    ScreenController screenController = new ScreenController();


    BooleanProperty playing = new SimpleBooleanProperty(false);


    // updatable value from the outside
    DoubleProperty currentBindex = new SimpleDoubleProperty(.0);
    DoubleProperty maxBindex = new SimpleDoubleProperty(42.);
    DoubleProperty currentDBBlock = new SimpleDoubleProperty(42.);
    DoubleProperty maxDBBlock = new SimpleDoubleProperty(42.);

    DoubleProperty maxPeerBlock = new SimpleDoubleProperty(42.);

    BooleanProperty isIndexing = new SimpleBooleanProperty(false);
    BooleanProperty isDownloading = new SimpleBooleanProperty(false);
    StringProperty rawDocument = new SimpleStringProperty("here comes the Document in DUP format");

    StringProperty indexLogMessage = new SimpleStringProperty(" ... ");
    StringProperty peerLogMessage = new SimpleStringProperty(" ... ");
    StringProperty memoryLogMessage = new SimpleStringProperty(" ... ");
    StringProperty docPoolLogMessage = new SimpleStringProperty(" ... ");

    ArrayList<String> input = new ArrayList<>();

    StringProperty selectedTheme = new SimpleStringProperty(BLANK_THEME);
    ObservableList<String> themes = FXCollections.observableArrayList(DARK_THEME, BLANK_THEME);
    ObservableList<Locale> langs = FXCollections.observableArrayList(I18N.getSupportedLocales());
    ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();
    StringProperty targetPubkey = new SimpleStringProperty();
    StringProperty targetComment = new SimpleStringProperty();


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
    ObjectProperty<PeerService> peers = new SimpleObjectProperty<>();

    List<CINDEX> certsRelated = new ArrayList<>();

    List<Transaction> txRelated = new ArrayList<>();

}
