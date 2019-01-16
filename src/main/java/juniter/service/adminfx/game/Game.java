package juniter.service.adminfx.game;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import juniter.service.adminfx.include.Bus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Game implements Initializable {
    private static final Logger LOG = LogManager.getLogger();

    @FXML
    private Canvas canvas;
    @FXML
    private TextField erg;

    private final int speed = 150;

    private Player player;
    private Player marcel  ;
    private IntValue score = new IntValue(0);

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        erg.setOnKeyPressed(e -> {
            String code = e.getCode().toString();
            //System.out.println("Game " + code);
            if (!Bus.input.contains(code))
                Bus.input.add(code);
        });

        erg.setOnKeyReleased(e -> {
            String code = e.getCode().toString();
            Bus.input.remove(code);
        });

        GraphicsContext gc = canvas.getGraphicsContext2D();


        var trm = new Character("/adminfx/game/trm.jpeg", "2,718, but why?", 300, 20);
        var trmPage = new Character("/adminfx/game/blankpage.png", "Hmmm, a TRM page covered in blood !", 600, 20);
        // var duniter = new Character("https://g1.data.duniter.fr/user/profile/2ny7YAdmzReQxAayyJZsyVYwYhVyax2thKcGknmQy5nQ/_image/avatar.png","Duniter ...", 800, 350);

        var duniter = new Character("/adminfx/game/duniter.png", "A protocol, a software, and more ...", 100, 350);
        var puits = new Character("/adminfx/game/mariopipe.png", List.of("- C'est Marcel!","- Kimamila?"), (int) canvas.getWidth() / 2 + 50, (int) canvas.getHeight() / 2);


        player = new Player();
        marcel = new Player();
        player.setPosition(canvas.getWidth() / 2, canvas.getHeight() / 2);

        ArrayList<Coins> moneybagList = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            Coins moneybag = new Coins();
            moneybag.setImage("/adminfx/game/coin.png");

            double px = (canvas.getWidth() - 50) * Math.random() + 25;
            double py = (canvas.getHeight() - 50) * Math.random() + 25;
            moneybag.setPosition(px, py);
            moneybagList.add(moneybag);
        }

        LongValue lastNanoTime = new LongValue(System.nanoTime());


        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;
                Bounds boundsInScene = canvas.localToScene(canvas.getBoundsInLocal(), true);
                // game logic
                // move only within the frame

                player.setVelocity(0, 0);
                if (Bus.input.contains("LEFT"))
                    player.addVelocity(-speed, 0, boundsInScene);
                if (Bus.input.contains("RIGHT"))
                    player.addVelocity(speed, 0, boundsInScene);
                if (Bus.input.contains("UP"))
                    player.addVelocity(0, -speed, boundsInScene);
                if (Bus.input.contains("DOWN"))
                    player.addVelocity(0, speed, boundsInScene);
                if (Bus.input.contains("SPACE") && score.value > 0) {
                    moneybagList.add(player.payment());
                    score.value--;
                }
                moneybagList.removeIf(puits::intersects);

                player.update(elapsedTime);


                // collision detection

                Iterator<Coins> moneybagIter = moneybagList.iterator();
                while (moneybagIter.hasNext()) {
                    Coins moneybag = moneybagIter.next();
                    if (player.intersects(moneybag)) {
                        moneybagIter.remove();
                        score.value++;
                    }
                }

                // render
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                for (Coins moneybag : moneybagList)
                    moneybag.render(gc);
                if (moneybagList.isEmpty() && score.value == 0) {
                    gc.strokeText("Gratulálok, you saved Marcel ! ", 550, canvas.getHeight() / 2 - 100);
                    gc.fillText("What was the relative value of the coin?", 500, canvas.getHeight() / 2 - 70);

                    marcel.setPosition(player.positionX + 100, player.positionY);
                    marcel.render(gc);
                    puits.render(gc, false);
                    player.render(gc);
                    this.stop();
                }
                trm.render(gc, trm.intersects(player));
                trmPage.render(gc, trmPage.intersects(player));
                duniter.render(gc, duniter.intersects(player));
                puits.render(gc, puits.intersects(player));


                Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
                gc.setFont(theFont);
                gc.setFill(Color.GREEN);
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(1);
                String pointsText = "Ğ1 " + score.value;
                gc.fillText(pointsText, 33, 33);
                gc.strokeText(pointsText, 33, 33);

                player.render(gc);
                //var v = player.getViewport();
                //var b = player.getBoundary();
                //gc.strokeRect(v.getMinX(), v.getMinY(), v.getWidth(), v.getHeight());
                //gc.strokeRect(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());


            }
        }.start();
    }
}
