package juniter.service.adminfx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import juniter.repository.jpa.index.*;
import juniter.service.adminfx.include.AbstractJuniterFX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Database extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger();

    public static ObservableList<IINDEX> iindex = FXCollections.observableArrayList();
    public static ObservableList<BINDEX> bindex = FXCollections.observableArrayList();
    public static ObservableList<CINDEX> cindex = FXCollections.observableArrayList();
    public static ObservableList<MINDEX> mindex = FXCollections.observableArrayList();
    public static ObservableList<SINDEX> sindex = FXCollections.observableArrayList();
    public TabPane tabPane;
    @FXML private TableColumn bAvgSizeCol;
    @FXML private TableColumn bMonetaryMassCol;
    @FXML private TableColumn bPowMinCol;
    @FXML private TableColumn bIssuersCountCol;
    @FXML private TableColumn bIssuersFrameCol;
    @FXML private TableColumn bIssuerFrameVarCol;
    @FXML private TextField filterB;
    @FXML private TableColumn iWotbidCol;
    @FXML private TableColumn iCreatedOnCol;
    @FXML private TableColumn iHashCol;
    @FXML private TableColumn iMemberCol;
    @FXML private TableColumn iWasMemberCol;
    @FXML private TableColumn iKickCol;
    @FXML private TextField filterI;
    @FXML private TableColumn mOpCol;
    @FXML private TableColumn mCreatedOn;
    @FXML private TableColumn mexpiresOn;
    @FXML private TableColumn mExpiredOn;
    @FXML private TableColumn mRevokesOn;
    @FXML private TableColumn mRevokedOn;
    @FXML private TableColumn mLeaving;
    @FXML private TableColumn mRevocationSig;
    @FXML private TableColumn mChainableOn;
    @FXML private TextField filterM;
    @FXML private TableColumn cOp;
    @FXML private TableColumn cExpiresOn;
    @FXML private TableColumn cExpiredOn;
    @FXML private TableColumn cSig;
    @FXML private TableColumn cSignedOn;
    @FXML private TableColumn cChainableOn;
    @FXML private TableColumn cFromWid;
    @FXML private TableColumn ctoWid;
    @FXML private TextField filterC;
    @FXML private TableColumn sWrittenTime;
    @FXML private TableColumn sLocktime;
    @FXML private TableColumn sPos;
    @FXML private TableColumn sConditions;
    @FXML private TextField filterS;


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




    @Override
    public void start(Stage primaryStage) {
        LOG.info("Starting Database");

        primaryStage.setTitle("Juniter - Database  ");
        primaryStage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {



        tableB.setItems(bindex);
        tableI.setItems(iindex);
        tableM.setItems(mindex);
        tableC.setItems(cindex);
        tableS.setItems(sindex);

        bNumberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        bHashCol.setCellValueFactory(new PropertyValueFactory<>("hash"));
        bDividendCol.setCellValueFactory(new PropertyValueFactory<>("dividend"));
        bIssuerCol.setCellValueFactory(new PropertyValueFactory<>("issuer"));
        bMembersCountCol.setCellValueFactory(new PropertyValueFactory<>("membersCount"));
        bSizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        bTimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        bmedianTimeCol.setCellValueFactory(new PropertyValueFactory<>("medianTime"));
        bMonetaryMassCol.setCellValueFactory(new PropertyValueFactory<>("mass"));
        bAvgSizeCol.setCellValueFactory(new PropertyValueFactory<>("avgBlockSize"));
        bIssuerFrameVarCol.setCellValueFactory(new PropertyValueFactory<>("issuersFrameVar"));
        bIssuersCountCol.setCellValueFactory(new PropertyValueFactory<>("issuersCount"));
        bIssuersFrameCol.setCellValueFactory(new PropertyValueFactory<>("issuersFrame"));
        bPowMinCol.setCellValueFactory(new PropertyValueFactory<>("powMin"));


        iOpCol.setCellValueFactory(new PropertyValueFactory<>("op"));
        iPubCol.setCellValueFactory(new PropertyValueFactory<>("pub"));
        iUidCol.setCellValueFactory(new PropertyValueFactory<>("uid"));
        iWrittenOn.setCellValueFactory(new PropertyValueFactory<>("written_on"));
        iCreatedOnCol.setCellValueFactory(new PropertyValueFactory<>("created_on"));
        iHashCol.setCellValueFactory(new PropertyValueFactory<>("hash"));
        iKickCol.setCellValueFactory(new PropertyValueFactory<>("kick"));
        iMemberCol.setCellValueFactory(new PropertyValueFactory<>("member"));
        iWasMemberCol.setCellValueFactory(new PropertyValueFactory<>("wasMember"));
        iWotbidCol.setCellValueFactory(new PropertyValueFactory<>("wotbid"));


        cIssuerCol.setCellValueFactory(new PropertyValueFactory<>("issuer"));
        cReceiverCol.setCellValueFactory(new PropertyValueFactory<>("receiver"));
        cCreatedOn.setCellValueFactory(new PropertyValueFactory<>("createdOn"));
        cWrittenOn.setCellValueFactory(new PropertyValueFactory<>("written_on"));
        cChainableOn.setCellValueFactory(new PropertyValueFactory<>("chainable_on"));
        cExpiredOn.setCellValueFactory(new PropertyValueFactory<>("expired_on"));
        cFromWid.setCellValueFactory(new PropertyValueFactory<>("from_wid"));
        ctoWid.setCellValueFactory(new PropertyValueFactory<>("to_wid"));
        cSig.setCellValueFactory(new PropertyValueFactory<>("sig"));
        cOp.setCellValueFactory(new PropertyValueFactory<>("op"));


        mPubCol.setCellValueFactory(new PropertyValueFactory<>("pub"));
        mStatus.setCellValueFactory(new PropertyValueFactory<>("type"));
        mWrittenOn.setCellValueFactory(new PropertyValueFactory<>("written_on"));
        mChainableOn.setCellValueFactory(new PropertyValueFactory<>("chainable_on"));
        mCreatedOn.setCellValueFactory(new PropertyValueFactory<>("created_on"));
        mRevokedOn.setCellValueFactory(new PropertyValueFactory<>("revoked_on"));
        mexpiresOn.setCellValueFactory(new PropertyValueFactory<>("expires_on"));
        mRevokesOn.setCellValueFactory(new PropertyValueFactory<>("revokes_on"));
        mLeaving.setCellValueFactory(new PropertyValueFactory<>("leaving"));
        mExpiredOn.setCellValueFactory(new PropertyValueFactory<>("expired_on"));
        mRevocationSig.setCellValueFactory(new PropertyValueFactory<>("revocation"));
        mOpCol.setCellValueFactory(new PropertyValueFactory<>("op"));


        sWrittenOn.setCellValueFactory(new PropertyValueFactory<>("written_on"));
        sAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        sBaseCol.setCellValueFactory(new PropertyValueFactory<>("base"));
        sConsumedCol.setCellValueFactory(new PropertyValueFactory<>("consumed"));
        sIdentifierCol.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        sOpCol.setCellValueFactory(new PropertyValueFactory<>("op"));
        sConditions.setCellValueFactory(new PropertyValueFactory<>("conditions"));
        sLocktime.setCellValueFactory(new PropertyValueFactory<>("locktime"));
        sPos.setCellValueFactory(new PropertyValueFactory<>("pos"));


        filterI.setOnAction(event -> {
            iindex.clear();
            iindex.addAll(iRepo.search(filterI.getText()));
        });

        filterM.setOnAction(event -> {
            mindex.clear();
            mindex.addAll(mRepo.search(filterM.getText()));
        });

        filterC.setOnAction(event -> {
            cindex.clear();
            cindex.addAll(cRepo.search(filterC.getText()));
        });

        filterS.setOnAction(event -> {
            sindex.clear();
            sindex.addAll(sRepo.search(filterS.getText()));
        });

        tableB.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                LOG.info("onSelect  " + newSelection.number + "-" + newSelection.hash + "   "+newSelection  );


                iindex.clear();
                iindex.addAll(iRepo.idtyWrittenOn(newSelection.number + "-" + newSelection.hash));

                cindex.clear();
                cindex.addAll(cRepo.writtenOn(newSelection.number + "-" + newSelection.hash));

                mindex.clear();
                mindex.addAll(mRepo.writtenOn(newSelection.number + "-" + newSelection.hash));

                sindex.clear();
                sindex.addAll(sRepo.writtenOn(newSelection.number + "-" + newSelection.hash));

            }
        });
    }


    @FXML
    public void onSelectBINDEX() {
        Platform.runLater(() -> {
            var selected = tableB.getSelectionModel().getSelectedItem();
            if (selected == null)
                return;
            LOG.info("onSelect  " + selected);
            var fromDB = iRepo.idtyWrittenOn(selected.number + "-" + selected.hash);
            // .collect(Collectors.toList());

            if (fromDB.size() <= 0)
                return;

            LOG.info("clearing and adding   " + fromDB.size());


            iindex.clear();
            //tableI.getSelectionModel().setSelectionMode();
            iindex.addAll(fromDB);

        });

    }


    public void show(Integer blockNumber) {
        LOG.info("showing block at " + blockNumber);

    }

    @FXML
    public void reload(ActionEvent actionEvent) {
        Platform.runLater(() -> {

            var blocks = bRepo.findAll();//.stream().map(b -> modelMapper.map(b, GlobalValid.BINDEX.class)).collect(Collectors.toList());

            // draw the button list
            flowPanel.getChildren().clear();
            blocks.forEach(block -> {
                var button = new Button(block.number + "");
                button.setFont(new Font(9));
                button.setOnAction(event -> show(block.number));
                flowPanel.getChildren().add(button);
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
