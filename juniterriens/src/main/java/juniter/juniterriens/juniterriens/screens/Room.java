package juniter.juniterriens.juniterriens.screens;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import juniter.juniterriens.include.JuniterBindings;
import juniter.juniterriens.juniterriens.characters.Player;
import juniter.juniterriens.juniterriens.engine.Collectable;
import juniter.juniterriens.juniterriens.engine.Gate;
import juniter.juniterriens.juniterriens.objects.Coins;
import juniter.juniterriens.juniterriens.objects.Items;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

@Getter
@Setter
@Component
public abstract class Room implements Initializable {

    protected static final Logger LOG = LogManager.getLogger(Room.class);

    public static Canvas canvas;

    protected static Room singleton;

    protected ArrayList<Collectable> collectables = new ArrayList<>();

    protected List<Gate> gates = new ArrayList<>();

    protected Timeline timeline = new Timeline();


//    static public Room singleton(Class<? extends Room> x) {
//
//        if (singleton == null) {
//            try {
//                singleton = (Room) x.getDeclaredConstructors()[0].newInstance();
//            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
//                LOG.error("Creating room ", e);
//            }
//        }
//
//        return singleton;
//    }

    protected GraphicsContext gc(){
        return canvas.getGraphicsContext2D();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void run( ) {

        LOG.info("Entering Room " + this.getClass().getSimpleName());
        var boundsInScene = canvas.localToScene(canvas.getBoundsInLocal(), true);
        var gc = canvas.getGraphicsContext2D();

        preset();

        timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(16), event -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            // render gates
            gates.forEach(g->{
                if (g.intersects(Player.get())) {
                    timeline.stop();
                    LOG.info("Leaving room "+ this.getClass().getSimpleName());

                    Player.get().setPosition(g.getInitPlayerX(),g.getInitPlayerY());
                    g.getRoom().run();
                } else {
                    g.render(gc);
                }
            });

            // Handle player actions
//            if(JuniterBindings.input.isEmpty()){
                Player.get().setVelocity(0,0);
//            }
            if (JuniterBindings.input.contains("LEFT")) {
                Player.get().addVelocity(-1, 0, boundsInScene);
//                bunny.addVelocity(-speed, 0, boundsInScene);
            }
            if (JuniterBindings.input.contains("RIGHT")) {
                Player.get().addVelocity(1, 0, boundsInScene);
//                bunny.addVelocity(speed, 0, boundsInScene);
            }
            if (JuniterBindings.input.contains("UP")) {
                Player.get().addVelocity(0, -1, boundsInScene);
//                bunny.addVelocity(0, -speed, boundsInScene);
            }
            if (JuniterBindings.input.contains("DOWN")) {
                Player.get().addVelocity(0, 1, boundsInScene);
//                bunny.addVelocity(0, speed, boundsInScene);
            }
            if (JuniterBindings.input.contains("SPACE") && Player.get().getScore() > 0) {
                collectables.add(Player.get().payment());
                Player.get().score--;
            }

            Player.get().update(0.01);

            // collision detection

            Iterator<Collectable> collectableIter = collectables.iterator();
            while (collectableIter.hasNext()) {
                var collectable = collectableIter.next();
                if ( Player.get().intersects(collectable)) {
                    if (collectable instanceof Items) {
                        Player.get().takeItem((Items) collectable);

                    } else if (collectable instanceof Coins) {
                        Coins.playSound();
                        Player.get().score++;
                    }
                    collectableIter.remove();
                }
            }


            roomSpecific();

            Player.get().render(gc);

            // render score
            var theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
            gc.setFont(theFont);
            gc.setFill(Color.GREEN);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            var pointsText = "Ğ1 " + Player.get().getScore();
            gc.fillText(pointsText, 33, 33);
            gc.strokeText(pointsText, 33, 33);

        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    abstract void roomSpecific();

    abstract void preset();

}
