package juniter.gui.include;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import juniter.core.crypto.SecretBox;

public class IssuerBox {

    //Create variable
    static private SecretBox answer;

    public static SecretBox display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Issuer");
        window.setMinWidth(250);
        Label label = new Label();
        label.setText("Welcome dude ");



        PasswordField salt = new PasswordField();
        PasswordField password = new PasswordField();
        Label pubkey = new Label();


        //Create two buttons
        Button yesButton = new Button("Yes");

        //Clicking will set answer and close window
        yesButton.setOnAction(e -> {
            answer = new SecretBox(salt.getText(), password.getText());
            window.close();
        });

        VBox layout = new VBox(10);

        //Add buttons
        layout.getChildren().addAll(label, salt, password, pubkey, yesButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        //Make sure to return answer
        return answer;
    }

}