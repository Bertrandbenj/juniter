package juniter.gui.business.page;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.index.*;
import juniter.gui.technical.AbstractJuniterFX;
import juniter.repository.jpa.index.*;
import juniter.service.BlockService;
import juniter.service.Index;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Function;

import static juniter.gui.JuniterBindings.*;
import static juniter.gui.technical.Formats.DATETIME_FORMAT;

@Component
@ConditionalOnExpression("${juniter.useJavaFX:false}")
public class Database extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(Database.class);

    private static ObservableList<IINDEX> iindex = FXCollections.observableArrayList();
    private static ObservableList<BINDEX> bindex = FXCollections.observableArrayList();
    private static ObservableList<CINDEX> cindex = FXCollections.observableArrayList();
    private static ObservableList<MINDEX> mindex = FXCollections.observableArrayList();
    private static ObservableList<SINDEX> sindex = FXCollections.observableArrayList();
    public Label qCount;
    public Button index1Button;
    public Button pauseButton;

    private ObservableList<String> queryEx = FXCollections.observableArrayList(
            "FROM BINDEX p WHERE currency = 'g1'",
            "SELECT number, hash FROM BINDEX p WHERE currency = 'g1'",
            "SELECT c FROM CINDEX c, IINDEX i WHERE (c.receiver = i.pub OR c.issuer = i.pub) AND i.uid LIKE '%BnimajneB%'",
            "SELECT m FROM MINDEX m, IINDEX i WHERE m.pub = i.pub AND i.uid LIKE '%BnimajneB%'",
            "SELECT i.uid AS theDude , ir.uid AS certifier, c.written FROM CINDEX c, IINDEX i, IINDEX ir WHERE i.uid LIKE '%BnimajneB%' AND c.receiver = i.pub AND ir.pub = c.issuer ",
            "SELECT i.uid AS theDude , iis.uid AS certified, c.written FROM CINDEX c, IINDEX i, IINDEX iis WHERE i.uid LIKE '%BnimajneB%' AND c.issuer = i.pub AND iis.pub = c.receiver ",
            "SELECT s FROM IINDEX i, SINDEX s WHERE i.uid LIKE '%BnimajneB%' AND s.conditions LIKE CONCAT('%',i.pub,'%')",
            "SELECT s FROM SINDEX s WHERE conditions LIKE '%XHX(%' OR LIKE '%CSV(%' OR LIKE '%CLTV(%'");

    @FXML
    private TableView tableQuery;

    @FXML
    private ComboBox<String> jpql;
    @FXML
    private Button indexUntilButton;
    @FXML
    private TableColumn<Object, BStamp> cWritten;

    @FXML
    private CheckBox
            ckMember,
            ckIOP,
            ckMOP,
            ckCOP,
            ckSOP,
            ckLeaving,
            ckKick,
            ckConsumed,
            ckWasMember;
    @FXML
    private TextField tfHash;
    @FXML
    private TableColumn<Object, BStamp> mWritten;

    @FXML
    private TableColumn<Object, BStamp> iWritten;
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
    private TableColumn iWotbidCol;
    @FXML
    private TableColumn<Object, BStamp> iSignedCol;
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
    private TableColumn<Object, BStamp> mSigned;
    @FXML
    private TableColumn<String, Long> mExpiresOn;
    @FXML
    private TableColumn<String, Long> mExpiredOn;
    @FXML
    private TableColumn<String, Long> mRevokesOn;
    @FXML
    private TableColumn mRevokedOn;
    @FXML
    private TableColumn mLeaving;
    @FXML
    private TableColumn mRevocationSig;
    @FXML
    private TableColumn<String, Long> mChainableOn;
    @FXML
    private TextField filterM;
    @FXML
    private TableColumn cOp;
    @FXML
    private TableColumn<String, Long> cExpiresOn;
    @FXML
    private TableColumn<String, Long> cExpiredOn;
    @FXML
    private TableColumn cSig;
    @FXML
    private TableColumn<String, Long> cChainableOn;

    @FXML
    private TextField filterC;
    @FXML
    private TableColumn<String, Long> sWrittenTime;
    @FXML
    private TableColumn<String, Long> sLocktime;
    @FXML
    private TableColumn sPos;
    @FXML
    private TableColumn sTx;

    @FXML
    private TableColumn sConditions;
    @FXML
    private TextField filterS;


    @FXML
    private TableColumn cSigned;

    @FXML
    private TableColumn bDividendCol;
    @FXML
    private TableColumn<String, Long> bmedianTimeCol;
    @FXML
    private TableColumn bMembersCountCol;
    @FXML
    private TableColumn<String, Long> bTimeCol;
    @FXML
    private TableColumn bSizeCol;
    @FXML
    private TableColumn bIssuerCol;
    @FXML
    private TableColumn sConsumedCol;
    @FXML
    private TableColumn<Object, BStamp> sWritten;
    @FXML
    private TableColumn<Object, BStamp> sSigned;
    @FXML
    private TableColumn sIdentifierCol;
    @FXML
    private TableColumn sBaseCol;
    @FXML
    private TableColumn sAmountCol;
    @FXML
    private TableColumn sOpCol;
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
    private TableColumn<String, Integer> bNumberCol;
    @FXML
    private TableColumn bHashCol;


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
    private BINDEXRepository bRepo;
    @Autowired
    private CINDEXRepository cRepo;
    @Autowired
    private IINDEXRepository iRepo;
    @Autowired
    private MINDEXRepository mRepo;
    @Autowired
    private SINDEXRepository sRepo;

    @Autowired
    private Index index;

    @FXML
    private TextField indexTil;
    @FXML
    private ProgressBar indexBar;

    @Autowired
    private BlockService blockService;


    @FXML
    public void indexUntil() {


        isIndexing.setValue(true);


        currentBindex.setValue(index.head_());


        int until;
        try {
            until = Integer.parseInt(indexTil.getText());
        } catch (Exception e) {
            until = blockService.currentBlockNumber();
        }

        index.indexUntil(until, false, CURRENCY.get());
    }

    @FXML
    public void indexReset() {
        index.reset(true, CURRENCY.get());
        currentBindex.setValue(null);
    }


    public void index1() {

        isIndexing.setValue(true);
        index.indexUntil(currentBindexN.get() + 1, false, CURRENCY.get());
        //JuniterBindings.indexLogMessage.setValue("Validated " + JuniterBindings.currentBindex.intValue());

    }

    public void revert1() {
        if (isIndexing.get())
            return;

        index.revert1(CURRENCY.get());

    }

    private TextFieldTableCell<String, Long> medianTimeColumnFormat() {

        return new TextFieldTableCell<>() {


            @Override
            public void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    long unixSeconds = item;
                    Date date = new Date(unixSeconds * 1000L);

                    setText(item + "");
                    setTooltip(new Tooltip("" + DATETIME_FORMAT.format(date)));


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


    private void mapBStampColumn2(TableColumn<Object, BStamp> col, String name) {

        col.setCellValueFactory(new PropertyValueFactory<>(name));

        TableColumn<Object, Integer> numCol = new TableColumn<>("On");
        numCol.setCellValueFactory(param -> {
            var res = (Integer) null;
            try {
                res = col.getCellData(param.getValue()).getNumber();
            } catch (Exception ignored) {
            }
            return new SimpleObjectProperty<>(res);
        });


        TableColumn<Object, String> hashCol = new TableColumn<>("Hash");
        hashCol.setVisible(false);
        hashCol.setCellValueFactory(param -> {
            var res = "";
            try {
                res = col.getCellData(param.getValue()).getHash();
            } catch (Exception ignored) {
            }
            return new SimpleObjectProperty<>(res);
        });

        TableColumn<Object, Long> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(param -> {
            var res = (Long) null;
            try {
                res = col.getCellData(param.getValue()).getMedianTime();
            } catch (Exception ignored) {
            }
            return new SimpleObjectProperty<>(res);
        });
        timeCol.setVisible(!name.toLowerCase().equals("signed"));

        col.getColumns().setAll(numCol, hashCol, timeCol);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        jpql.setItems(queryEx);
        jpql.getSelectionModel().select(0);

        indexUntilButton.disableProperty().bind(isIndexing);
        index1Button.disableProperty().bind(isIndexing);
        pauseButton.disableProperty().bind(isIndexing.not());

//        isIndexing.addListener((v, oldValue, newValue) -> {
//            if (!newValue)
//                indexUntilButton.setText(">>");
//            else
//                indexUntilButton.setText("||");
//        });


        indexBar.progressProperty().bind(currentBindexN.divide(highestDBBlock));

        txCountB.textProperty().bind(new SimpleStringProperty("Count ").concat(Bindings.size(bindex)));


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
        mapBStampColumn2(iWritten, "written");
        //mapColumn(iWrittenOn, "writtenOn");
        mapColumn(iSigCol, "sig");
        mapBStampColumn2(iSignedCol, "signed");
        mapColumn(iHashCol, "hash");
        mapColumn(iKickCol, "kick");
        mapColumn(iMemberCol, "member");
        mapColumn(iWasMemberCol, "wasMember");
        mapColumn(iWotbidCol, "wotbid");

        mapColumn(cIssuerCol, "issuer");
        mapColumn(cReceiverCol, "receiver");
        mapColumn(cSigned, "createdOn");

        mapBStampColumn2(cWritten, "written");
        //mapColumn(cWrittenOn, "writtenOn");
        mapColumn(cChainableOn, "chainable_on");
        mapColumn(cExpiredOn, "expired_on");
        mapColumn(cExpiresOn, "expires_on");
        mapColumn(cSig, "sig");
        mapColumn(cOp, "op");

        mapColumn(mPubCol, "pub");
        mapColumn(mStatus, "type");
        mapBStampColumn2(mWritten, "written");
        mapColumn(mChainableOn, "chainable_on");
        mapBStampColumn2(mSigned, "signed");
        mapColumn(mRevokedOn, "revoked");
        mapColumn(mExpiresOn, "expires_on");
        mapColumn(mRevokesOn, "revokes_on");
        mapColumn(mLeaving, "leaving");
        mapColumn(mExpiredOn, "expired_on");
        mapColumn(mRevocationSig, "revocation");
        mapColumn(mOpCol, "op");

        mapBStampColumn2(sWritten, "written");
        mapBStampColumn2(sSigned, "signed");
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

        EventHandler<ActionEvent> filterEventI = (e) -> filteredI.setPredicate(this::matchesFilterI);

        filterI.setOnAction(filterEventI);
        ckIOP.setOnAction(filterEventI);
        ckMember.setOnAction(filterEventI);
        ckWasMember.setOnAction(filterEventI);
        ckKick.setOnAction(filterEventI);
        tfHash.setOnAction(filterEventI);

        SortedList<IINDEX> sortedData = new SortedList<>(filteredI);
        sortedData.comparatorProperty().bind(tableI.comparatorProperty());

        // ===================   FILTER  COLUMNS MINDEX =================

        var filteredM = new FilteredList<>(mindex);
        EventHandler<ActionEvent> listenerM = (e) -> filteredM.setPredicate(this::matchesFilterM);

        filterM.setOnAction(listenerM);
        ckLeaving.setOnAction(listenerM);
        ckMOP.setOnAction(listenerM);

        SortedList<MINDEX> sortedM = new SortedList<>(filteredM);
        sortedM.comparatorProperty().bind(tableM.comparatorProperty());


        // ===================   FILTER  COLUMNS CINDEX =================

        var filteredC = new FilteredList<>(cindex);
        EventHandler<ActionEvent> listenerC = (e) -> filteredC.setPredicate(this::matchesFilterC);

        filterC.setOnAction(listenerC);
        ckCOP.setOnAction(listenerC);

        SortedList<CINDEX> sortedC = new SortedList<>(filteredC);
        sortedC.comparatorProperty().bind(tableC.comparatorProperty());


        // ===================   FILTER  COLUMNS SINDEX =================

        var filteredS = new FilteredList<>(sindex);
        EventHandler<ActionEvent> listenerS = (e) -> filteredS.setPredicate(this::matchesFilterS);

        filterS.setOnAction(listenerS);
        ckSOP.setOnAction(listenerS);
        ckConsumed.setOnAction(listenerS);

        SortedList<SINDEX> sortedS = new SortedList<>(filteredS);
        sortedS.comparatorProperty().bind(tableS.comparatorProperty());


        // ===================   FANCY COLORS  =================
        final var headTime = bRepo.head().map(BINDEX::getNumber);
        final var bindexsize = bRepo.count();
        bNumberCol.setCellFactory(t -> new TextFieldTableCell<>() {


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

        mChainableOn.setCellFactory(t -> medianTimeColumnFormat());
        cChainableOn.setCellFactory(t -> medianTimeColumnFormat());
        cExpiresOn.setCellFactory(t -> medianTimeColumnFormat());
        mExpiresOn.setCellFactory(t -> medianTimeColumnFormat());
        mExpiredOn.setCellFactory(t -> medianTimeColumnFormat());
        mRevokesOn.setCellFactory(t -> medianTimeColumnFormat());

        // ===================   BINDEX FILTER  =================

        tableB.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                LOG.info("onSelect  " + newSelection.getNumber() + "-" + newSelection.getHash() + "   " + newSelection);

                iindex.clear();
                iindex.addAll(iRepo.writtenOn(newSelection.getNumber(), newSelection.getHash()));

                cindex.clear();
                cindex.addAll(cRepo.writtenOn(newSelection.getNumber(), newSelection.getHash()));

                mindex.clear();
                mindex.addAll(mRepo.writtenOn(newSelection.getNumber(), newSelection.getHash()));

                sindex.clear();
                sindex.addAll(sRepo.writtenOn(newSelection.getNumber(), newSelection.getHash()));

            }
        });


        tableB.setItems(bindex);
        tableI.setItems(sortedData);
        tableM.setItems(sortedM);
        tableC.setItems(sortedC);
        tableS.setItems(sortedS);

        currentBindex.addListener(c -> bindex.setAll(bRepo.findAll()));
        bindex.setAll(bRepo.findAll());

        Platform.runLater(this::reload);
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
        boolean res;

        var expectOP = ckSOP.isSelected() ? "CREATE" : "UPDATE";
        res = ckSOP.isIndeterminate() || s.getOp().equals(expectOP);
        res &= ckConsumed.isIndeterminate() || Boolean.valueOf(ckConsumed.isSelected()).equals(s.isConsumed());


        if (filterS.getText() != null && !filterS.getText().equals("")) {
            res &= s.getIdentifier().toLowerCase().contains(filterS.getText().toLowerCase());
        }


        return res;
    }

    private boolean matchesFilterC(CINDEX c) {
        boolean res;

        var expectOP = ckCOP.isSelected() ? "CREATE" : "UPDATE";
        res = ckCOP.isIndeterminate() || c.getOp().equals(expectOP);

        if (filterC.getText() != null && !filterC.getText().equals("")) {
            res &= c.getIssuer().equals(filterC.getText()) || c.getReceiver().equals(filterC.getText());
        }

        return res;
    }

    private boolean matchesFilterM(MINDEX m) {
        boolean res;

        var expectOP = ckMOP.isSelected() ? "CREATE" : "UPDATE";
        res = ckMOP.isIndeterminate() || m.getOp().equals(expectOP);

        res &= ckLeaving.isIndeterminate() || Boolean.valueOf(ckLeaving.isSelected()).equals(m.getLeaving());

        if (filterM.getText() != null && !filterM.getText().equals("")) {
            res &= m.getPub().toLowerCase().contains(filterM.getText().toLowerCase());
        }


        return res;
    }

    private boolean matchesFilterI(IINDEX i) {

        boolean res;

        var expectOP = ckIOP.isSelected() ? "CREATE" : "UPDATE";
        res = ckIOP.isIndeterminate() || i.getOp().equals(expectOP);

        res &= ckMember.isIndeterminate() || Boolean.valueOf(ckMember.isSelected()).equals(i.getMember());

        res &= ckWasMember.isIndeterminate() || Boolean.valueOf(ckWasMember.isSelected()).equals(i.getWasMember());

        res &= ckKick.isIndeterminate() || Boolean.valueOf(ckKick.isSelected()).equals(i.getKick());

        if (filterI.getText() != null && !filterI.getText().equals("")) {
            res &= (i.getUid() != null && i.getUid().toLowerCase().contains(filterI.getText().toLowerCase())
                    || i.getPub().equals(filterI.getText()));
        }

        if (tfHash.getText() != null && !tfHash.getText().equals("")) {
            res &= i.getHash().contains(tfHash.getText());
        }

        return res; // Does not match
    }

    private void show(Integer blockNumber) {
        LOG.info("showing node at " + blockNumber);
    }

    @Autowired
    private EntityManager em;

    static <S, T> Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> createArrayValueFactory(Function<S, T[]> arrayExtractor, final int index) {
        if (index < 0) {
            return cd -> null;
        }
        return cd -> {
            T[] array = arrayExtractor.apply(cd.getValue());
            return array == null || array.length <= index ? null : new SimpleObjectProperty<>(array[index]);
        };
    }

    @FXML
    public void jpqlQuery() {
        tabPane.getSelectionModel().selectLast();

        var q =jpql.getSelectionModel().getSelectedItem();
        LOG.info("jpql Query " + q);
        LOG.info("params " +  em.createQuery(q).getParameters());

        var list = em.createQuery(q).setMaxResults(1000).getResultList();

        if (list.size() <= 0)
            return;
        tableQuery.getColumns().clear();

        LOG.info(list.get(0).getClass() + "=> " + list.get(0));

        // case where there is no pre-defined object
        if (list.get(0).getClass().isArray()) {
            var fields = jpql.getValue().substring(jpql.getValue().indexOf("SELECT") + 6, jpql.getValue().indexOf("FROM")).split(",");

            for (int i = 0; i < fields.length; i++) {

                //cleanup
                fields[i] = (fields[i].contains("AS")?fields[i].substring(fields[i].indexOf("AS")+2):fields[i]).trim();

                var tc = new TableColumn<>(fields[i]);
                tableQuery.getColumns().add(tc);
                tc.setCellValueFactory(createArrayValueFactory(o -> {
                    Object[] objects = (Object[]) o;
                    return objects;
                }, i));
                int finalI = i;
                tableQuery.getItems().clear();
                list.forEach(c -> {
                    Object[] objects = (Object[]) c;
                    String rec = "record : ";
                    for (int x = 0; x < fields.length; x++) {
                        var it = Array.get(c, x);
                        rec += it + " ";
                    }
                    LOG.info(rec);
                    tableQuery.getItems().add(c);
                    //tc.setCellValueFactory(createArrayValueFactory(o -> objects, finalI));
                });

            }

        } else {        // case where there is a pre-defined object

            for (Method f : list.get(0).getClass().getMethods()) {
                if (!f.getName().startsWith("get") || "getClass".equals(f.getName()) || "getLong".equals(f.getName()))
                    continue;
                var firstLetter = f.getName().substring(3, 4).toLowerCase();
                var fieldName = firstLetter + f.getName().substring(4);

                LOG.info(fieldName + "  " + f.getName());

                var tc = new TableColumn<>(fieldName);

                tableQuery.getColumns().add(tc);
                mapColumn(tc, fieldName);
            }
            tableQuery.setItems(FXCollections.observableArrayList(list));

        }

        tableQuery.setColumnResizePolicy(p -> true);
        qCount.textProperty().bind(new SimpleStringProperty("Count ").concat(tableQuery.getItems().size()));
    }


    @FXML
    public void reload() {
        Platform.runLater(() -> {

            txCountM.textProperty().set("Count " + mRepo.count());
            mindex.clear();
            mindex.addAll(mRepo.findSome(PageRequest.of(0, 500)).getContent());

            txCountI.textProperty().set("Count " + iRepo.count());
            iindex.clear();
            iindex.addAll(iRepo.findSome(PageRequest.of(0, 500)).getContent());//.stream().map(i -> modelMapper.map(i, GlobalValid.IINDEX.class)).collect(Collectors.toList()));

            txCountC.textProperty().set("Count " + cRepo.count());
            cindex.clear();
            cindex.addAll(cRepo.findSome(PageRequest.of(0, 500)).getContent());//.stream().map(c -> modelMapper.map(c, GlobalValid.CINDEX.class)).collect(Collectors.toList()));

            txCountS.textProperty().set("Count " + sRepo.count());
            sindex.clear();
            sindex.addAll(sRepo.findSome(PageRequest.of(0, 500)).getContent());//.stream().map(s -> modelMapper.map(s, GlobalValid.SINDEX.class)).collect(Collectors.toList()));


        });
        tabPane.requestLayout();

    }


    public void pause() {
        isIndexing.setValue(false);
    }
}
