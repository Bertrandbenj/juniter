package juniter.juniterriens.juniterriens.engine;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import juniter.juniterriens.juniterriens.screens.Room;

public class Boat extends Curiosity {

    private Rectangle2D[] clips = new Rectangle2D[8];

    public boolean left;

    public int speed = 40;

    public Boat(String lbl, double x, double y, boolean left) {
        super("/juniterriens/game/img/rowboat.png", lbl, x, y);

        this.left = left;

        clips[0] = new Rectangle2D(0, 0, 96, 62);
        clips[1] = new Rectangle2D(96, 0, 96, 62);

        setPosition(x, y);
        setDisplaySize(new Point2D(96, 62));
        setViewport(clips[left ? 0 : 1]);
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
        pos = pos.add((left ? -speed : speed) * time, 0)  ;

        if (!Room.canvas.contains(pos.getX(), pos.getY())) {
            if (pos.getX() < 0)
                pos = new Point2D(Room.canvas.getWidth(), pos.getY());
            if (pos.getX() > Room.canvas.getWidth())
                pos = new Point2D(0, pos.getY());
        }

    }


}
