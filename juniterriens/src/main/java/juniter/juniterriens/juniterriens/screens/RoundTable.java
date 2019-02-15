package juniter.juniterriens.juniterriens.screens;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;


public class RoundTable extends Application {
    @Override
    public void start(Stage stage) {


        //Drawing Rectangle1
        Rectangle r1 = new Rectangle(192, 40, 16, 10);
        r1.setFill(Color.BLUE);
        r1.setStroke(Color.BLACK);

        Circle c = new Circle(200, 200, 150);
        c.setFill(Color.WHITE);
        c.setStroke(Color.BLACK);

        Circle cin = new Circle(200, 200, 125);
        cin.setFill(Color.WHITE);
        cin.setStroke(Color.BLACK);

        Label l = new Label ("The constant\n was sealed\n by nature");
        l.setLayoutX(200-l.getWidth()/2);
        l.setLayoutY(200-l.getHeight()/2);

        Group root = new Group(r1, c, cin, l);

        // creats seats
        for (int i = 0; i < 59; i++) {

            Rectangle rX = new Rectangle(r1.getX(), r1.getY(), r1.getWidth(), r1.getHeight());
            rX.setFill(Color.BURLYWOOD);
            rX.setStroke(Color.BLACK);
            Rotate rotate = new Rotate(360. * i / 59, 200, 200);
            rX.getTransforms().addAll(rotate);

            root.getChildren().add(rX);
        }


        //Creating a scene object
        Scene scene = new Scene(root, 600, 400);


        //Setting title to the Stage
        stage.setTitle("Rotation transformation example");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}