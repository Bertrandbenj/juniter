package juniter.gui.game;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import juniter.core.crypto.SecretBox;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.CINDEX;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.tx.TxInput;
import juniter.gui.include.I18N;
import juniter.gui.include.ScreenController;
import juniter.service.bma.PeerService;

import java.util.*;

public interface GameBindings {

    IntegerProperty money = new SimpleIntegerProperty(0);



}
