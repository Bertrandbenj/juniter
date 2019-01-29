package juniter.service.adminfx;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import juniter.core.model.index.*;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.index.*;
import juniter.service.Index;
import juniter.service.adminfx.include.AbstractJuniterFX;
import juniter.service.adminfx.include.Bus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Database extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger();

    private static ObservableList<IINDEX> iindex = FXCollections.observableArrayList();
    private static ObservableList<BINDEX> bindex = FXCollections.observableArrayList();
    private static ObservableList<CINDEX> cindex = FXCollections.observableArrayList();
    private static ObservableList<MINDEX> mindex = FXCollections.observableArrayList();
    private static ObservableList<SINDEX> sindex = FXCollections.observableArrayList();
    @FXML
    private Button indexUntilButton;
    @FXML
    private TableColumn cWritten_on;

    @FXML
    private CheckBox ckMember, ckIOP, ckMOP, ckCOP, ckSOP, ckLeaving, ckKick, ckConsumed, ckWasMember;
    @FXML
    private TextField tfHash;
    @FXML
    private TableColumn mWritten_on;

    @FXML
    private TableColumn iWritten_on;
    @FXML
    private TabPane tabPane;
    @FXML
    private TableColumn bAvgSizeCol;
    @FXML
    private TableColumn bMonetaryMassCol;
    @FXML
    private TableColumn bPowMinCol;
    @FXML
    private TableColumn bIssuersCountCol;
    @FXML
    private TableColumn bIssuersFrameCol;
    @FXML
    private TableColumn bIssuerFrameVarCol;
    @FXML
    private TextField filterB;
    @FXML
    private TableColumn iWotbidCol;
    @FXML
    private TableColumn iCreatedOnCol;
    @FXML
    private TableColumn iSigCol;
    @FXML
    private TableColumn iHashCol;
    @FXML
    private TableColumn iMemberCol;
    @FXML
    private TableColumn iWasMemberCol;
    @FXML
    private TableColumn iKickCol;
    @FXML
    private TextField filterI;
    @FXML
    private TableColumn mOpCol;
    @FXML
    private TableColumn mCreatedOn;
    @FXML
    private TableColumn mExpiresOn;
    @FXML
    private TableColumn mExpiredOn;
    @FXML
    private TableColumn mRevokesOn;
    @FXML
    private TableColumn mRevokedOn;
    @FXML
    private TableColumn mLeaving;
    @FXML
    private TableColumn mRevocationSig;
    @FXML
    private TableColumn mChainableOn;
    @FXML
    private TextField filterM;
    @FXML
    private TableColumn cOp;
    @FXML
    private TableColumn cExpiresOn;
    @FXML
    private TableColumn cExpiredOn;
    @FXML
    private TableColumn cSig;
    @FXML
    private TableColumn cSignedOn;
    @FXML
    private TableColumn cChainableOn;
    @FXML
    private TableColumn cFromWid;
    @FXML
    private TableColumn ctoWid;
    @FXML
    private TextField filterC;
    @FXML
    private TableColumn sWrittenTime;
    @FXML
    private TableColumn sLocktime;
    @FXML
    private TableColumn sPos;
    @FXML
    private TableColumn sTx;

    @FXML
    private TableColumn sConditions;
    @FXML
    private TextField filterS;


    @FXML
    private TableColumn cCreatedOn;

    @FXML
    private TableColumn bDividendCol;
    @FXML
    private TableColumn bmedianTimeCol;
    @FXML
    private TableColumn bMembersCountCol;
    @FXML
    private TableColumn bTimeCol;
    @FXML
    private TableColumn bSizeCol;
    @FXML
    private TableColumn bIssuerCol;
    @FXML
    private TableColumn iWrittenOn;
    @FXML
    private TableColumn sConsumedCol;
    @FXML
    private TableColumn sWrittenOn;
    @FXML
    private TableColumn sCreatedOn;
    @FXML
    private TableColumn sIdentifierCol;
    @FXML
    private TableColumn sBaseCol;
    @FXML
    private TableColumn sAmountCol;
    @FXML
    private TableColumn sOpCol;
    @FXML
    private TableColumn cWrittenOn;
    @FXML
    private TableColumn mWrittenOn;
    @FXML
    private TableColumn iOpCol;
    @FXML
    private TableColumn iPubCol;
    @FXML
    private TableColumn iUidCol;
    @FXML
    private Label txCountI;
    @FXML
    private Label txCountM;
    @FXML
    private Label txCountC;
    @FXML
    private Label txCountS;
    @FXML
    private Label txCountB;

    @FXML
    private TableColumn bNumberCol;
    @FXML
    private TableColumn bHashCol;
    @FXML
    private FlowPane flowPanel;


    @FXML
    private TableView<IINDEX> tableI;
    @FXML
    private TableView<BINDEX> tableB;
    @FXML
    private TableView<MINDEX> tableM;
    @FXML
    private TableView<CINDEX> tableC;
    @FXML
    private TableView<SINDEX> tableS;


    @FXML
    private TableColumn mPubCol;
    @FXML
    private TableColumn mStatus;
    @FXML
    private TableColumn cIssuerCol;
    @FXML
    private TableColumn cReceiverCol;

    @Autowired
    BINDEXRepository bRepo;
    @Autowired
    CINDEXRepository cRepo;
    @Autowired
    IINDEXRepository iRepo;
    @Autowired
    MINDEXRepository mRepo;
    @Autowired
    SINDEXRepository sRepo;

    @Autowired
    private Index indexer;

    @FXML
    private TextField indexTil;
    @FXML
    private ProgressBar indexBar;

    @Autowired
    private BlockRepository blockRepo;


    @FXML
    public void indexUntil() {

        Bus.isIndexing.setValue(!Bus.isIndexing.get());
        Bus.currentBindex.setValue(-1);

        if (Bus.isIndexing.get()) {
            indexUntilButton.setText("||");
        } else {
            indexUntilButton.setText(">>");
        }


        int until;
        try {
            until = Integer.parseInt(indexTil.getText());
        } catch (Exception e) {
            until = blockRepo.currentBlockNumber();
        }

        Bus.maxBindex.setValue(until);

        indexer.indexUntil(until, false);
    }

    @FXML
    public void indexReset() {
        indexer.reset(true);
        Bus.currentBindex.setValue(0);
    }


    public void index1() {

        Bus.isIndexing.setValue(!Bus.isIndexing.get());


        indexer.indexUntil(Bus.currentBindex.intValue() + 1, false);
        //Bus.indexLogMessage.setValue("Validated " + Bus.currentBindex.intValue());

    }

    public void revert1(ActionEvent actionEvent) {
        if (Bus.isIndexing.get())
            return;

        bRepo.head().ifPresent(h -> {
            bRepo.delete(h);

            iRepo.deleteAll(
                    iRepo.writtenOn(h.number + "-" + h.hash)
            );
            mRepo.deleteAll(
                    mRepo.writtenOn(h.number + "-" + h.hash)
            );
            cRepo.deleteAll(
                    cRepo.writtenOn(h.number + "-" + h.hash)
            );
            sRepo.deleteAll(
                    sRepo.writtenOn(h.number + "-" + h.hash)
            );


            Bus.currentBindex.setValue(h.number - 1);
            Bus.indexLogMessage.setValue("Reverted to " + Bus.currentBindex.intValue() + " from " + h);

            indexer.reset(false);
        });

    }

    private TextFieldTableCell medianTimeColumnFormat() {

        return new TextFieldTableCell<String, Long>() {


            @Override
            public void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    long unixSeconds = item;
                    Date date = new java.util.Date(unixSeconds * 1000L);
                    SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+1"));
                    String formattedDate = sdf.format(date);
                    setText(item + "");
                    setTooltip(new Tooltip("" + formattedDate));


                    //setStyle("-fx-background-color: #" + hsvGradient(ratio) + ";");
                }
            }

        };
    }

    @Override
    public void start(Stage primaryStage) {
        LOG.info("Starting Database");

        primaryStage.setTitle("Juniter - Database  ");
        primaryStage.show();
    }

    private void mapColumn(TableColumn col, String name) {
        col.setCellValueFactory(new PropertyValueFactory<>(name));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indexBar.progressProperty().bind(Bus.currentBindex.divide(Bus.maxBindex));


        // ===================   MAP  COLUMNS =================
        mapColumn(bNumberCol, "number");
        mapColumn(bHashCol, "hash");
        mapColumn(bDividendCol, "dividend");
        mapColumn(bIssuerCol, "issuer");
        mapColumn(bMembersCountCol, "membersCount");
        mapColumn(bSizeCol, "size");
        mapColumn(bTimeCol, "time");
        mapColumn(bmedianTimeCol, "medianTime");
        mapColumn(bMonetaryMassCol, "mass");
        mapColumn(bAvgSizeCol, "avgBlockSize");
        mapColumn(bIssuerFrameVarCol, "issuersFrameVar");
        mapColumn(bIssuersCountCol, "issuersCount");
        mapColumn(bIssuersFrameCol, "issuersFrame");
        mapColumn(bPowMinCol, "powMin");

        mapColumn(iOpCol, "op");
        mapColumn(iPubCol, "pub");
        mapColumn(iUidCol, "uid");
        mapColumn(iWritten_on, "written_on");
        mapColumn(iWrittenOn, "writtenOn");
        mapColumn(iSigCol, "sig");
        mapColumn(iCreatedOnCol, "created_on");
        mapColumn(iHashCol, "hash");
        mapColumn(iKickCol, "kick");
        mapColumn(iMemberCol, "member");
        mapColumn(iWasMemberCol, "wasMember");
        mapColumn(iWotbidCol, "wotbid");

        mapColumn(cIssuerCol, "issuer");
        mapColumn(cReceiverCol, "receiver");
        mapColumn(cCreatedOn, "createdOn");
        mapColumn(cWritten_on, "written_on");
        mapColumn(cWrittenOn, "writtenOn");
        mapColumn(cChainableOn, "chainable_on");
        mapColumn(cExpiredOn, "expired_on");
        mapColumn(cExpiresOn, "expires_on");
        mapColumn(cFromWid, "from_wid");
        mapColumn(ctoWid, "to_wid");
        mapColumn(cSig, "sig");
        mapColumn(cOp, "op");

        mapColumn(mPubCol, "pub");
        mapColumn(mStatus, "type");
        mapColumn(mWritten_on, "written_on");
        mapColumn(mWrittenOn, "writtenOn");

        mapColumn(mChainableOn, "chainable_on");
        mapColumn(mCreatedOn, "created_on");
        mapColumn(mRevokedOn, "revoked_on");
        mapColumn(mExpiresOn, "expires_on");
        mapColumn(mRevokesOn, "revokes_on");
        mapColumn(mLeaving, "leaving");
        mapColumn(mExpiredOn, "expired_on");
        mapColumn(mRevocationSig, "revocation");
        mapColumn(mOpCol, "op");

        mapColumn(sWrittenOn, "written_on");
        mapColumn(sWrittenTime, "written_time");
        mapColumn(sCreatedOn, "created_on");
        mapColumn(sAmountCol, "amount");
        mapColumn(sBaseCol, "base");
        mapColumn(sConsumedCol, "consumed");
        mapColumn(sIdentifierCol, "identifier");
        mapColumn(sOpCol, "op");
        mapColumn(sConditions, "conditions");
        mapColumn(sLocktime, "locktime");
        mapColumn(sPos, "pos");
        mapColumn(sTx, "tx");


        // ===================   FILTER  COLUMNS INDEX =================

        var filteredI = new FilteredList<>(iindex);
        ChangeListener listenerI = (observable, oldValue, newValue) ->
                filteredI.setPredicate(this::matchesFilterI);

        filterI.onActionProperty().addListener(listenerI);
        ckIOP.selectedProperty().addListener(listenerI);
        ckMember.selectedProperty().addListener(listenerI);
        ckWasMember.selectedProperty().addListener(listenerI);
        ckKick.selectedProperty().addListener(listenerI);
        tfHash.onActionProperty().addListener(listenerI);

        SortedList<IINDEX> sortedData = new SortedList<>(filteredI);
        sortedData.comparatorProperty().bind(tableI.comparatorProperty());

        // ===================   FILTER  COLUMNS MINDEX =================

        var filteredM = new FilteredList<>(mindex);
        ChangeListener listenerM = (observable, oldValue, newValue) ->
                filteredM.setPredicate(this::matchesFilterM);

        filterM.onActionProperty().addListener(listenerM);
        ckLeaving.onActionProperty().addListener(listenerM);
        ckMOP.onActionProperty().addListener(listenerM);

        SortedList<MINDEX> sortedM = new SortedList<>(filteredM);
        sortedM.comparatorProperty().bind(tableM.comparatorProperty());


        // ===================   FILTER  COLUMNS CINDEX =================

        var filteredC = new FilteredList<>(cindex);
        ChangeListener listenerC = (observable, oldValue, newValue) ->
                filteredC.setPredicate(this::matchesFilterC);

        filterC.onActionProperty().addListener(listenerC);
        ckCOP.selectedProperty().addListener(listenerC);

        SortedList<CINDEX> sortedC = new SortedList<>(filteredC);
        sortedC.comparatorProperty().bind(tableC.comparatorProperty());


        // ===================   FILTER  COLUMNS SINDEX =================

        var filteredS = new FilteredList<>(sindex);
        ChangeListener listenerS = (observable, oldValue, newValue) ->
                filteredS.setPredicate(this::matchesFilterS);

        filterS.setOnAction(e -> filteredS.setPredicate(this::matchesFilterS));
        ckSOP.selectedProperty().addListener(listenerS);
        ckConsumed.selectedProperty().addListener(listenerS);

        SortedList<SINDEX> sortedS = new SortedList<>(filteredS);
        sortedS.comparatorProperty().bind(tableS.comparatorProperty());


        // ===================   FANCY COLORS  =================
        final var headTime = bRepo.head().map(BINDEX::getNumber);
        final var bindexsize = bRepo.count();
        bNumberCol.setCellFactory(t -> new TextFieldTableCell<String, Integer>() {


            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null && headTime.isPresent()) {
                    var ratio = (headTime.get() - item) * 1. / bindexsize;
                    setStyle("-fx-background-color: #" + hsvGradient(ratio) + ";");
                }
            }


        });

        //  ===================   SET MEDIAN-TIME HUMAN FORMAT   =================
        bmedianTimeCol.setCellFactory(t -> medianTimeColumnFormat());
        bTimeCol.setCellFactory(t -> medianTimeColumnFormat());
        sWrittenTime.setCellFactory(t -> medianTimeColumnFormat());
        mChainableOn.setCellFactory(t -> medianTimeColumnFormat());
        cChainableOn.setCellFactory(t -> medianTimeColumnFormat());
        cExpiresOn.setCellFactory(t -> medianTimeColumnFormat());
        mExpiresOn.setCellFactory(t -> medianTimeColumnFormat());
        mExpiredOn.setCellFactory(t -> medianTimeColumnFormat());
        mRevokesOn.setCellFactory(t -> medianTimeColumnFormat());

        // ===================   BINDEX FILTER  =================

        tableB.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                LOG.info("onSelect  " + newSelection.number + "-" + newSelection.hash + "   " + newSelection);

                iindex.clear();
                iindex.addAll(iRepo.writtenOn(newSelection.number + "-" + newSelection.hash));

                cindex.clear();
                cindex.addAll(cRepo.writtenOn(newSelection.number + "-" + newSelection.hash));

                mindex.clear();
                mindex.addAll(mRepo.writtenOn(newSelection.number + "-" + newSelection.hash));

                sindex.clear();
                sindex.addAll(sRepo.writtenOn(newSelection.number + "-" + newSelection.hash));

            }
        });


        tableB.setItems(bindex);
        tableI.setItems(sortedData);
        tableM.setItems(mindex);
        tableC.setItems(cindex);
        tableS.setItems(sindex);
    }

    private String hsvGradient(double ratio) {
        var hsvRed = ratio >= .5 ? 255 : Math.round(ratio * 2 * 255);
        var hsvGreen = ratio >= .5 ? Math.round((1 - ratio) * 2 * 255) : 255;
        // TODO: do this clean
        hsvGreen = Math.min(Math.max(hsvGreen, 0), 255);
        hsvRed = Math.min(Math.max(hsvRed, 0), 255);
        return String.format("%02x%02x00", hsvRed, hsvGreen);
    }

    private String rgbGradient(double ratio) {
        var redLevel = Math.round(ratio * 256);
        return String.format("%02x%02x00", redLevel, 256 - redLevel);
    }


    private boolean matchesFilterS(SINDEX s) {
        boolean res = true;

        var expectOP = ckSOP.isSelected() ? "CREATE" : "UPDATE";
        res &= ckSOP.isIndeterminate() || s.getOp().equals(expectOP);

        return res;
    }

    private boolean matchesFilterC(CINDEX c) {
        boolean res = true;

        var expectOP = ckCOP.isSelected() ? "CREATE" : "UPDATE";
        res &= ckCOP.isIndeterminate() || c.getOp().equals(expectOP);

        return res;
    }

    private boolean matchesFilterM(MINDEX m) {
        boolean res = true;

        var expectOP = ckMOP.isSelected() ? "CREATE" : "UPDATE";
        res &= ckMOP.isIndeterminate() || m.getOp().equals(expectOP);


        if (filterM.getText() != null && !filterM.getText().equals("")) {
            res &= m.getPub().toLowerCase().contains(filterM.getText().toLowerCase());
        }


        return res;
    }

    private boolean matchesFilterI(IINDEX i) {

        boolean res = true;

        var expectOP = ckIOP.isSelected() ? "CREATE" : "UPDATE";
        res &= ckIOP.isIndeterminate() || i.getOp().equals(expectOP);


        res &= ckMember.isIndeterminate() || i.getMember() == ckMember.isSelected();


        res &= ckWasMember.isIndeterminate() || i.getWasMember() == null || i.getWasMember() == ckWasMember.isSelected();


        res &= ckKick.isIndeterminate() || i.getKick() == ckLeaving.isSelected();


        if (filterI.getText() != null && !filterI.getText().equals("")) {
            res &= (i.getUid() != null && i.getUid().equals(filterI.getText())
                    || i.getPub().equals(filterI.getText()));
        }

        if (tfHash.getText() != null && !tfHash.getText().equals("")) {
            res &= tfHash.getText().equals(i.getHash());
        }

        return res; // Does not match
    }

    public void show(Integer blockNumber) {
        LOG.info("showing block at " + blockNumber);
    }

    @FXML
    public void reload(ActionEvent actionEvent) {
        Platform.runLater(() -> {

            var blocks = bRepo.findAll();//.stream().map(b -> modelMapper.map(b, GlobalValid.BINDEX.class)).collect(Collectors.toList());

            // draw the button list // TODO decide what to do of the flow layout, if anything
            flowPanel.getChildren().clear();
            blocks.forEach(block -> {
                var button = new Button(block.number + "");
                button.setFont(new Font(9));
                button.setOnAction(event -> show(block.number));
                //flowPanel.getChildren().add(button);
            });


            //  map table and refresh
            bindex.clear();
            txCountB.textProperty().set("Count " + bRepo.count());
            bindex.addAll(blocks);


            txCountM.textProperty().set("Count " + mRepo.count());
            mindex.clear();
            mindex.addAll(mRepo.findAll());

            txCountI.textProperty().set("Count " + iRepo.count());
            iindex.clear();
            iindex.addAll(iRepo.findAll());//.stream().map(i -> modelMapper.map(i, GlobalValid.IINDEX.class)).collect(Collectors.toList()));

            txCountC.textProperty().set("Count " + cRepo.count());
            cindex.clear();
            cindex.addAll(cRepo.findAll());//.stream().map(c -> modelMapper.map(c, GlobalValid.CINDEX.class)).collect(Collectors.toList()));

            txCountS.textProperty().set("Count " + sRepo.count());
            sindex.clear();
            sindex.addAll(sRepo.findAll());//.stream().map(s -> modelMapper.map(s, GlobalValid.SINDEX.class)).collect(Collectors.toList()));


        });
        tabPane.requestLayout();

    }

    public void button1(ActionEvent actionEvent) {

    }

}
