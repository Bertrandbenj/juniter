package juniter.service.adminfx.juniterriens;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import juniter.service.adminfx.juniterriens.characters.Player;
import juniter.service.adminfx.juniterriens.characters.WhiteRabbit;
import juniter.service.adminfx.juniterriens.objects.Coins;
import juniter.service.adminfx.juniterriens.engine.Collectable;
import juniter.service.adminfx.juniterriens.engine.Curiosity;
import juniter.service.adminfx.juniterriens.objects.Items;
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
    private WhiteRabbit bunny;
    private Items boots;

    private Player marcel;
    private Integer score = 0;

    public static MediaPlayer backgroundMusic;

    {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        backgroundMusic = new MediaPlayer(new Media(getClass().getResource("/adminfx/game/save.mp3").toExternalForm()));
        backgroundMusic.setAutoPlay(true);
        backgroundMusic.setVolume(0.1);   // from 0 to 1
        backgroundMusic.setOnEndOfMedia(() ->  //  loop (repeat) the music
                backgroundMusic.seek(Duration.ZERO));

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


        var trm = new Curiosity("/adminfx/game/trm.jpeg", "2,718, but why?", 300, 20);
        var trmPage = new Curiosity("/adminfx/game/blankpage.png", "Hmmm, a TRM page covered in blood !", 600, 20);
        // var duniter = new Curiosity("https://g1.data.duniter.fr/user/profile/2ny7YAdmzReQxAayyJZsyVYwYhVyax2thKcGknmQy5nQ/_image/avatar.png","Duniter ...", 800, 350);

        var duniter = new Curiosity("/adminfx/game/duniter.png", "A protocol, a software, and more ...", 100, 350);
        var puits = new Curiosity("/adminfx/game/mariopipe.png", List.of("- C'est Marcel!", "- Kimamila?"), (int) canvas.getWidth() / 2 + 50, (int) canvas.getHeight() / 2);


        player = new Player();
        player.getAnimation().play();

        marcel = new Player();
        bunny = new WhiteRabbit();
        bunny.getAnimation().play();

        boots = new Items("Boots", "/adminfx/game/gear_boots.png", 1, 15);

        boots.setPosition(900, 400);
        bunny.setPosition(950, 70);
        player.setPosition(canvas.getWidth() / 2, canvas.getHeight() / 2);

        ArrayList<Collectable> collectableList = new ArrayList<>();
        collectableList.add(boots);

        for (int i = 0; i < 15; i++) {
            Coins coin = new Coins("/adminfx/game/coin.png");

            double px = (canvas.getWidth() - 50) * Math.random() + 25;
            double py = (canvas.getHeight() - 50) * Math.random() + 25;
            coin.setPosition(px, py);
            collectableList.add(coin);
        }
        Bounds boundsInScene = canvas.localToScene(canvas.getBoundsInLocal(), true);

        //new AnimationTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> {


            //LOG.info("currentNanoTime " + System.currentTimeMillis());

            // game logic
            // move only within the frame
            player.setVelocity(0, 0);
            bunny.setVelocity(0, 0);

            if (Bus.input.contains("LEFT")) {
                player.addVelocity(-speed, 0, boundsInScene);
                bunny.addVelocity(-speed, 0, boundsInScene);
            }
            if (Bus.input.contains("RIGHT")) {
                player.addVelocity(speed, 0, boundsInScene);
                bunny.addVelocity(speed, 0, boundsInScene);

            }
            if (Bus.input.contains("UP")) {
                player.addVelocity(0, -speed, boundsInScene);
                bunny.addVelocity(0, -speed, boundsInScene);

            }
            if (Bus.input.contains("DOWN")) {
                player.addVelocity(0, speed, boundsInScene);
                bunny.addVelocity(0, speed, boundsInScene);

            }
            if (Bus.input.contains("SPACE") && score > 0) {
                collectableList.add(player.payment());
                score--;
            }
            collectableList.removeIf(puits::intersects);

            player.update(0.01);
            bunny.update(0.01);

            // collision detection

            Iterator<Collectable> collectableIter = collectableList.iterator();
            while (collectableIter.hasNext()) {
                var collectable = collectableIter.next();
                if (player.intersects(collectable)) {
                    if (collectable instanceof Items) {
                        player.takeItem((Items) collectable);
                    } else if (collectable instanceof Coins) {
                        Coins.playSound();
                        collectableIter.remove();
                        score++;
                    }

                }
            }

            // render
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            for (Collectable moneybag : collectableList)
                moneybag.render(gc);
            if (collectableList.isEmpty() && score == 0) {
                gc.strokeText("Gratulálok, you saved Marcel ! ", 550, canvas.getHeight() / 2 - 100);
                gc.fillText("What was the relative value of the coin?", 500, canvas.getHeight() / 2 - 70);

                marcel.setPosition(player.positionX + 70, player.positionY + 70);
                marcel.render(gc);
                puits.render(gc, false);
                player.render(gc);
                marcel.currentClip = 14;
                marcel.getAnimation().play();
            }
            trm.render(gc, trm.intersects(player));
            trmPage.render(gc, trmPage.intersects(player));
            duniter.render(gc, duniter.intersects(player));
            puits.render(gc, puits.intersects(player));
            bunny.render(gc);
            // boots.render(gc);


            Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
            gc.setFont(theFont);
            gc.setFill(Color.GREEN);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            String pointsText = "Ğ1 " + score;
            gc.fillText(pointsText, 33, 33);
            gc.strokeText(pointsText, 33, 33);

            player.render(gc);
            //var v = player.getViewport();
            //var b = player.getBoundary();
            //gc.strokeRect(v.getMinX(), v.getMinY(), v.getWidth(), v.getHeight());
            //gc.strokeRect(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());


        }
        ));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


}
