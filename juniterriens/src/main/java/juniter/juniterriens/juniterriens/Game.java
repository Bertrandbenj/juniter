package juniter.juniterriens.juniterriens;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import juniter.juniterriens.include.Bindings;
import juniter.juniterriens.juniterriens.characters.Player;
import juniter.juniterriens.juniterriens.characters.WhiteRabbit;
import juniter.juniterriens.juniterriens.engine.Collectable;
import juniter.juniterriens.juniterriens.engine.Curiosity;
import juniter.juniterriens.juniterriens.engine.Gate;
import juniter.juniterriens.juniterriens.engine.Obstacle;
import juniter.juniterriens.juniterriens.objects.Coins;
import juniter.juniterriens.juniterriens.objects.Items;
import juniter.juniterriens.juniterriens.screens.BARoom;
import juniter.repository.jpa.index.MINDEXRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
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

    private static MediaPlayer backgroundMusic;


    private Timeline exercise1 = new Timeline();


    private final int speed = 150;


    private WhiteRabbit bunny;


    private Player player;
    private Player marcel;
    private Integer score = 0;

    private GraphicsContext gc;

    private ArrayList<Collectable> collectables = new ArrayList<>();


    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private Curiosity duniter, puits, trm, trmPage;

    private Gate rt, technology;


    @Autowired
    MINDEXRepository mRepo;


    @EventListener
    public void handleContextStart(ContextStartedEvent cse) {
        System.out.println("Handling context started event.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        backgroundMusic = new MediaPlayer(new Media(getClass().getResource("/juniterriens/game/img/save.mp3").toExternalForm()));
        backgroundMusic.setStartTime(Duration.ZERO);
        backgroundMusic.setVolume(0.1);   // from 0 to 1
       // backgroundMusic.setOnEndOfMedia(() ->  //  loop (repeat) the music
         //       backgroundMusic.seek(Duration.ZERO));

        erg.setOnKeyPressed(e -> {
            String code = e.getCode().toString();
            //System.out.println("Game " + code);
            if (!Bindings.input.contains(code))
                Bindings.input.add(code);
        });

        erg.setOnKeyReleased(e -> {
            String code = e.getCode().toString();
            Bindings.input.remove(code);
        });

        gc = canvas.getGraphicsContext2D();

        entry().play();
    }

    private Timeline entry() {

        // Init map objects
        trm = new Curiosity("/juniterriens/game/img/trm.jpeg", "2,718, but why?", 300, 20);
        trmPage = new Curiosity("/juniterriens/game/img/blankpage.png", "Hmmm, a TRM page covered in blood !", 600, 20);
        duniter = new Curiosity("/juniterriens/game/img/duniter.png", "A protocol, a software, and more ...", 100, 400);
        puits = new Curiosity("/juniterriens/game/img/mariopipe.png", List.of("- C'est Marcel!", "- Kimamila?"), (int) canvas.getWidth() / 2 + 50, (int) canvas.getHeight() / 2);
        obstacles.addAll(List.of(trm, trmPage, duniter, puits));

        // Init Gates objects
        rt = new Gate("/juniterriens/game/img/gate.png", roundTable(gc), 50, 180);
        technology = new Gate( BARoom.get(), 1000, 180);
        obstacles.addAll(List.of(rt, technology));


        player = new Player();
        player.setPosition(canvas.getWidth() / 2, canvas.getHeight() / 2);
        //player.getAnimation().play();

        marcel = new Player();
        marcel.getAnimation().play();


        bunny = new WhiteRabbit();
        bunny.getAnimation().play();
        bunny.setPosition(950, 70);


        // Init collectable items
        Items boots = new Items("Boots", "/juniterriens/game/img/gear_boots.png", 1, 15);
        boots.setPosition(900, 400);
        collectables.clear();
        collectables.add(boots);
        for (int i = 0; i < 15; i++) {
            Coins coin = new Coins();

            double px = (canvas.getWidth() - 50) * Math.random() + 25;
            double py = (canvas.getHeight() - 50) * Math.random() + 25;
            coin.setPosition(px, py);
            collectables.add(coin);
        }


        Bounds boundsInScene = canvas.localToScene(canvas.getBoundsInLocal(), true);

        exercise1.getKeyFrames().setAll(new KeyFrame(Duration.millis(16), event -> {
            // game logic
            // move only within the frame
            player.setVelocity(0, 0);
            bunny.setVelocity(0, 0);

            if (Bindings.input.contains("LEFT")) {
                player.addVelocity(-speed, 0, boundsInScene);
                bunny.addVelocity(-speed, 0, boundsInScene);
            }
            if (Bindings.input.contains("RIGHT")) {
                player.addVelocity(speed, 0, boundsInScene);
                bunny.addVelocity(speed, 0, boundsInScene);

            }
            if (Bindings.input.contains("UP")) {
                player.addVelocity(0, -speed, boundsInScene);
                bunny.addVelocity(0, -speed, boundsInScene);

            }
            if (Bindings.input.contains("DOWN")) {
                player.addVelocity(0, speed, boundsInScene);
                bunny.addVelocity(0, speed, boundsInScene);

            }
            if (Bindings.input.contains("SPACE") && score > 0) {
                collectables.add(player.payment());
                score--;
            }
            collectables.removeIf(puits::intersects);

            player.update(0.01);
            bunny.update(0.01);

            // collision detection

            Iterator<Collectable> collectableIter = collectables.iterator();
            while (collectableIter.hasNext()) {
                var collectable = collectableIter.next();
                if (player.intersects(collectable)) {
                    if (collectable instanceof Items) {
                        player.takeItem((Items) collectable);

                    } else if (collectable instanceof Coins) {
                        Coins.playSound();
                        score++;
                    }
                    collectableIter.remove();
                }
            }

            // render
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            for (Collectable moneybag : collectables)
                moneybag.render(gc);
            if (collectables.isEmpty() && score == 0) {
                exercise1.stop();
                savedMarcel(gc);
            }


            trm.render(gc, trm.intersects(player));
            trmPage.render(gc, trmPage.intersects(player));
            duniter.render(gc, duniter.intersects(player));
            puits.render(gc, puits.intersects(player));
            bunny.render(gc);


            // handle gates
            if (rt.intersects(player)) {
                exercise1.stop();
                rt.getTimeline().play();
            } else {
                rt.render(gc);
            }

            if (technology.intersects(player)) {
                exercise1.stop();
                technology.getTimeline().play();
            } else {
                technology.render(gc);
            }


            Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
            gc.setFont(theFont);
            gc.setFill(Color.GREEN);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            String pointsText = "Ğ1 " + score;
            gc.fillText(pointsText, 33, 33);
            gc.strokeText(pointsText, 33, 33);

            player.render(gc);

        }));

        exercise1.setCycleCount(Animation.INDEFINITE);
        return exercise1;
    }


    private void savedMarcel(GraphicsContext gc) {
        gc.strokeText("Gratulálok, you saved Marcel ! ", 550, canvas.getHeight() / 2 - 100);
        gc.fillText("What was the relative value of the coin?", 500, canvas.getHeight() / 2 - 70);

        puits.render(gc, false);
        player.render(gc);

        marcel.currentClip = 14;
        marcel.setPosition(player.positionX + 70, player.positionY + 70);
        marcel.render(gc);


        var anim = new Timeline();
        anim.getKeyFrames().add(new KeyFrame(Duration.millis(16), event -> {
            marcel.currentClip = 14;
            marcel.setPosition(player.positionX + 70, player.positionY + 70);
            marcel.render(gc);
            puits.render(gc, false);
            player.render(gc);
        }));
        anim.setCycleCount(10);
        anim.play();

        LOG.info("Finished savingMarcel Mission");

    }


    private Timeline room_2_1(GraphicsContext gc) {
        var anim = new Timeline();

        var main = new Gate("/juniterriens/game/img/gate.png", entry(), 50, canvas.getHeight() / 2);
        var room_2_0 = new Gate("/juniterriens/game/img/gate.png", room_2_0(gc), canvas.getWidth() - 50, 50);


        anim.getKeyFrames().add(new KeyFrame(Duration.millis(16), event -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

             player.setPosition(80, 80);
             player.render(gc);

        }));
        anim.setCycleCount(Animation.INDEFINITE);
        return anim;
    }


    private Timeline room_2_0(GraphicsContext gc) {
        var anim = new Timeline();

        var main = new Gate("/juniterriens/game/img/gate.png", null, 50, canvas.getHeight() / 2);
        var room2 = new Gate("/juniterriens/game/img/gate.png", null, canvas.getWidth() - 50, 50);


        anim.getKeyFrames().add(new KeyFrame(Duration.millis(16), event -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            player.setPosition(80, 80);
            player.render(gc);

        }));
        anim.setCycleCount(Animation.INDEFINITE);
        return anim;
    }




    private Timeline roundTable(GraphicsContext gc) {
        var anim = new Timeline();

        var main = new Gate("/juniterriens/game/img/gate.png", null, canvas.getWidth() - 50, canvas.getHeight() / 2);


        long today = System.currentTimeMillis() / 1000L;
        long yesterday = today - 86400;

        var list = mRepo.expiresBetween(yesterday, today);
        list.add(0, "Guten Tag - Moul");
        var moul = new Curiosity("/juniterriens/game/img/blueWizard.png", list, 600, 50);


        anim.getKeyFrames().add(new KeyFrame(Duration.millis(16), event -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            player.setPosition(canvas.getWidth() - 150, canvas.getHeight() / 2);
            player.getAnimation().stop();
            //player.render(gc);

            moul.setPosition(650, 10);

            main.render(gc);

            Bounds boundsInScene = canvas.localToScene(canvas.getBoundsInLocal(), true);

            if (Bindings.input.contains("LEFT")) {
                player.addVelocity(-speed, 0, boundsInScene);
            }
            if (Bindings.input.contains("RIGHT")) {
                player.addVelocity(speed, 0, boundsInScene);
            }
            if (Bindings.input.contains("UP")) {
                player.addVelocity(0, -speed, boundsInScene);
            }
            if (Bindings.input.contains("DOWN")) {
                player.addVelocity(0, speed, boundsInScene);
            }
            player.update(0.01);
            player.render(gc);
            moul.render(gc, moul.intersects(player));

            // creats seats
            for (int i = 0; i < 59; i++) {
                var img = new Image("/juniterriens/game/img/moneybag2.png");
                drawRotatedImage(gc, img, 360. * i / 59, 220, 20);
            }

            if (main.intersects(player)) {
                anim.stop();
                entry().play();
            }


        }));
        anim.setCycleCount(Animation.INDEFINITE);
        return anim;
    }

    /**
     * Sets the transform for the GraphicsContext to rotate around a pivot point.
     *
     * @param gc    the graphics context the transform to applied to.
     * @param angle the angle of rotation.
     * @param px    the x pivot co-ordinate for the rotation (in canvas co-ordinates).
     * @param py    the y pivot co-ordinate for the rotation (in canvas co-ordinates).
     */
    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    /**
     * Draws an image on a graphics context.
     * <p>
     * The image is drawn at (tlpx, tlpy) rotated by angle pivoted around the point:
     * (tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2)
     *
     * @param gc    the graphics context the image is to be drawn on.
     * @param angle the angle of rotation.
     * @param tlpx  the top left x co-ordinate where the image will be plotted (in canvas co-ordinates).
     * @param tlpy  the top left y co-ordinate where the image will be plotted (in canvas co-ordinates).
     */
    private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy) {
        gc.save(); // saves the current state on stack, including the current transform
        rotate(gc, angle, tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2);
        gc.drawImage(image, tlpx, tlpy);
        gc.restore(); // back to original state (before rotation)
    }


}
