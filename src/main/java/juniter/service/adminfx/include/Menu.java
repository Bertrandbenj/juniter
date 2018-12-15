package juniter.service.adminfx.include;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Menu extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger();

    @FXML ImageView logoMain;
    @FXML ImageView logoNotary;
    @FXML ImageView logoGraphs;
    @FXML ImageView logoNetwork;
    @FXML ImageView logoDatabase;

    public Menu() { }

    @Override
    public void start(Stage primaryStage) {
        AnchorPane page = (AnchorPane) load("/adminfx/include/Menu.fxml");
        Scene scene = new Scene(page);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    @FXML
    public void viewMain(ActionEvent event) {
        LOG.info("view Main " + event.getEventType());

        BorderPane page =  (BorderPane) load("/adminfx/FrontPage.fxml");

        Scene scene = new Scene(page);
        Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
        current.setTitle("Juniter - Welcome ");

        logoMain.setImage(new Image("/adminfx/images/whiterabbit.jpg"));
        logoGraphs.setImage(new Image("/adminfx/images/logo.png"));
        logoNotary.setImage(new Image("/adminfx/images/logo.png"));
        logoNetwork.setImage(new Image("/adminfx/images/logo.png"));
        logoDatabase.setImage(new Image("/adminfx/images/logo.png"));

        current.setScene(scene);
        current.show();
    }

    @FXML
    public void viewSVGGraph(ActionEvent event) {
        LOG.info("view SVG Graph " + event.getEventType());

        BorderPane page = (BorderPane) load("/adminfx/GraphPanel.fxml");

        Scene scene = new Scene(page);

        Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
        current.setScene(scene);
        current.setTitle("Juniter - Graphs");

        logoGraphs.setImage(new Image("/adminfx/images/dotex.png"));
        logoNotary.setImage(new Image("/adminfx/images/logo.png"));
        logoMain.setImage(new Image("/adminfx/images/logo.png"));
        logoNetwork.setImage(new Image("/adminfx/images/logo.png"));
        logoDatabase.setImage(new Image("/adminfx/images/logo.png"));

        current.show();
    }

    @FXML
    public void viewNotary(ActionEvent event) {
        LOG.info("view Notary " + event.getEventType());

        BorderPane page = (BorderPane) load("/adminfx/DUPNotary.fxml");
        Scene scene = new Scene(page);
        Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();

        current.setScene(scene);
        current.setTitle("Juniter - Notary");

        logoNotary.setImage(new Image("/adminfx/images/keep_calm_im_the_notary_puzzle.jpg"));
        logoGraphs.setImage(new Image("/adminfx/images/logo.png"));
        logoMain.setImage(new Image("/adminfx/images/logo.png"));
        logoNetwork.setImage(new Image("/adminfx/images/logo.png"));
        logoDatabase.setImage(new Image("/adminfx/images/logo.png"));

        current.show();
    }

    public void viewNetwork(ActionEvent event) {
        LOG.info("view Network " + event.getEventType());

        BorderPane page =  (BorderPane) load("/adminfx/Network.fxml");

        Scene scene = new Scene(page);
        Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
        current.setTitle("Juniter - Network ");

        logoMain.setImage(new Image("/adminfx/images/logo.png"));
        logoGraphs.setImage(new Image("/adminfx/images/logo.png"));
        logoNotary.setImage(new Image("/adminfx/images/logo.png"));
        logoNetwork.setImage(new Image("/adminfx/images/network.png"));
        logoDatabase.setImage(new Image("/adminfx/images/logo.png"));


        current.setScene(scene);
        current.show();
    }

    public void viewDatabase(ActionEvent event) {
        LOG.info("view Database " + event.getEventType());

        BorderPane page =  (BorderPane) load("/adminfx/Database.fxml");

        Scene scene = new Scene(page);
        Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
        current.setTitle("Juniter - Database ");

        logoMain.setImage(new Image("/adminfx/images/logo.png"));
        logoGraphs.setImage(new Image("/adminfx/images/logo.png"));
        logoNotary.setImage(new Image("/adminfx/images/logo.png"));
        logoNetwork.setImage(new Image("/adminfx/images/logo.png"));
        logoDatabase.setImage(new Image("/adminfx/images/database.png"));

        current.setScene(scene);
        current.show();
    }
}
