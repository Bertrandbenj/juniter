package juniter.gui.game.engine;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import juniter.gui.game.Game;
import juniter.gui.game.characters.Player;
import juniter.gui.game.screens.Room;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Boat extends Curiosity {

    private Rectangle2D[] clips = new Rectangle2D[8];

    private static Random rand = new Random();

    public boolean left;

    public int speed;

    public Boat(String lbl, double x, double y, boolean left) {
        super("/gui/game/img/rowboat.png", lbl, x, y);

        this.left = left;

        clips[0] = new Rectangle2D(0, 0, 96, 62);
        clips[1] = new Rectangle2D(96, 0, 96, 62);

        setPosition(x, y);
        setDisplaySize(new Point2D(96, 62));
        setViewport(clips[left ? 0 : 1]);
        speed = 30 + rand.nextInt(70);
     }
    private static final Logger LOG = LogManager.getLogger(Game.class);

    public boolean contains(Player p){
        return new Rectangle2D(pos.getX(),pos.getY(),displaySize.getX(),displaySize.getY()).contains(p.getPos());
    }

    public void update(double time) {
        var up = new Transition() {
            {
                setCycleDuration(Duration.millis(1000));

            }

            @Override
            protected void interpolate(double frac) {
                setViewport(clips[left ? 0 : 1]);
            }
        };
        up.setInterpolator(Interpolator.LINEAR);

        up.setCycleCount(7);
        up.play();
        pos = pos.add((left ? -speed : speed) * time, 0);

        if (!Room.canvas.contains(pos.getX(), pos.getY())) {
            if (pos.getX() < 0)
                pos = new Point2D(Room.canvas.getWidth(), pos.getY());
            if (pos.getX() > Room.canvas.getWidth())
                pos = new Point2D(0, pos.getY());
        }

    }


}
