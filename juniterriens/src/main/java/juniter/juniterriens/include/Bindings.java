package juniter.juniterriens.include;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import juniter.core.model.DBBlock;

import java.util.ArrayList;

public interface Bindings {

    // updatable value from the outside
    SimpleDoubleProperty currentBindex = new SimpleDoubleProperty(.0);
    SimpleDoubleProperty maxBindex = new SimpleDoubleProperty(42.);
    SimpleDoubleProperty currentDBBlock = new SimpleDoubleProperty(42.);
    SimpleDoubleProperty maxDBBlock = new SimpleDoubleProperty(42.);

    SimpleDoubleProperty maxPeerBlock = new SimpleDoubleProperty(42.);

    SimpleBooleanProperty isIndexing = new SimpleBooleanProperty(false);
    SimpleBooleanProperty isDownloading = new SimpleBooleanProperty(false);
    SimpleStringProperty rawDocument = new SimpleStringProperty("here comes the Document in DUP format");

    SimpleStringProperty indexLogMessage = new SimpleStringProperty(" ... ");
    SimpleStringProperty peerLogMessage = new SimpleStringProperty(" ... ");
    SimpleStringProperty memoryLogMessage = new SimpleStringProperty(" ... ");
    SimpleStringProperty docPoolLogMessage = new SimpleStringProperty(" ... ");

    ArrayList<String> input = new ArrayList<>();

    SimpleStringProperty selectedTheme = new SimpleStringProperty("/juniterriens/css/no-theme.css");


    SimpleObjectProperty<DBBlock> currenBlock = new SimpleObjectProperty<>();


}
