package juniter.service.adminfx.include;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public interface Bus {


    // updatable value from the outside
    SimpleDoubleProperty currentBindex = new SimpleDoubleProperty(.0);
    SimpleDoubleProperty maxBindex = new SimpleDoubleProperty(42.);
    SimpleDoubleProperty maxDBBlock = new SimpleDoubleProperty(42.);
    SimpleDoubleProperty maxPeerBlock = new SimpleDoubleProperty(42.);

    SimpleBooleanProperty isIndexing = new SimpleBooleanProperty(false);
    SimpleBooleanProperty isDownloading = new SimpleBooleanProperty(false);
    SimpleStringProperty rawDocument = new SimpleStringProperty("here comes the Document in DUP format");

    SimpleStringProperty indexLogMessage = new SimpleStringProperty(" ... ");
    SimpleStringProperty peerLogMessage = new SimpleStringProperty(" ... ");
    SimpleStringProperty sparkLogMessage = new SimpleStringProperty(" ... ");
    SimpleStringProperty docPoolLogMessage = new SimpleStringProperty(" ... ");


}
