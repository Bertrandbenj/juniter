package juniter.gui.game.characters;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import juniter.gui.game.engine.Collectable;
import juniter.gui.game.engine.Curiosity;
import juniter.gui.game.objects.Coins;
import juniter.gui.game.objects.Items;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@Getter

public class Player extends ImageView {

    private static final Logger LOG = LogManager.getLogger(Player.class);

    static Player singleton;

    public static Player get() {
        if (singleton == null) {
            singleton = new Player();
        }
        return singleton;
    }

    private Image _image;
    protected Point2D pos;
    protected Point2D velocity;
    protected Point2D displaySize;

    public int score;

    private final int speed = 150;

    private Rectangle2D[] clips;
    public int currentClip = 14;

    private ArrayList<Items> inventory;


    private final IntegerProperty frameCounter = new SimpleIntegerProperty(0);

    private Transition animation;


    public Player() {
        setPosition(0, 0);
        setVelocity(0, 0);
        _image = new Image("/gui/game/img/body.png");

        displaySize = new Point2D(_image.getWidth() / 7, _image.getHeight() / 4);

        inventory = new ArrayList<>();

        clips = new Rectangle2D[4 * 7];
        int count = 0;
        for (int row = 0; row < 4; row++)
            for (int column = 0; column < 7; column++, count++)
                clips[count] = new Rectangle2D(displaySize.getX() * column, displaySize.getY() * row, displaySize.getX(), displaySize.getY());

        setImage(_image);
        setViewport(clips[currentClip]);

        animation = new Transition() {
            {
                setCycleDuration(Duration.millis(1000));
            }

            @Override
            protected void interpolate(double frac) {
                final int index = Math.min((int) Math.floor(frac * 7), 7 - 1);

                setViewport(clips[index + currentClip]);
            }


        };
        animation.setInterpolator(Interpolator.LINEAR);
        animation.setCycleCount(5);
    }

    public void takeItem(Items item) {
        if (item != null)
            inventory.add(item);
    }

    public void setPosition(double x, double y) {
        pos = new Point2D(x, y);
    }

    public void setVelocity(double x, double y) {
        velocity = new Point2D(x, y);
    }

    public void addVelocity(double x, double y, Bounds frame) {

        x *= speed;
        y *= speed;
        animation.play();
        double coef = 1;
        if (inventory.stream().anyMatch(items -> items.is("Boots"))) {
            coef *= 2;
        }
        if (x > 0) {
            currentClip = 21;
            if (pos.getX() >= frame.getWidth() - displaySize.getX()) {
                coef = 0;
            }
        }
        if (x < 0) {
            currentClip = 7;
            if (pos.getX() <= 0) {
                coef = 0;
            }
        }
        if (y > 0) {
            currentClip = 14;
            if (pos.getY() >= frame.getHeight() - displaySize.getY()) {
                coef = 0;
            }
        }
        if (y < 0) {
            currentClip = 0;
            if (pos.getY() <= 0) {
                coef = 0;
            }
        }

        setViewport(clips[currentClip]);

        velocity = velocity.add(x, y).multiply(coef);

    }

    public void update(double time) {
        var up = new Transition() {
            {
                setCycleDuration(Duration.millis(1000));
                //setVelocity(velocity.getX(),velocity.getY());
            }

            @Override
            protected void interpolate(double frac) {
                final int index = Math.min((int) Math.floor(frac * 7), 7 - 1);
                //LOG.info("interpolate " + positionX + " " + positionY + " " + time + " " + frac);
                setViewport(clips[index + currentClip]);
            }
        };
        up.setInterpolator(Interpolator.LINEAR);

        up.setCycleCount(7);
        up.play();

        pos = pos.add(velocity.multiply(time));

    }

    public Coins payment() {
        ///LOG.info("pos " + positionX + " " + positionY);
        var coin = new Coins();
        coin.setPosition(pos.getX() + 25 - (currentClip == 7 ? 80 : 0) + (currentClip == 21 ? 80 : 0)
                , pos.getY() + 25 - (currentClip == 0 ? 80 : 0) + (currentClip == 14 ? 80 : 0));
        Coins.playSound();
        return coin;
    }

    public void render(GraphicsContext gc) {
        var v = getViewport();
        gc.drawImage(_image, v.getMinX(), v.getMinY(), v.getWidth(), v.getHeight(), pos.getX(), pos.getY(), v.getWidth(), v.getHeight());
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(pos.getX(), pos.getY(), 50, 50);
    }


    public boolean intersects(Collectable collectable) {
        return collectable.getBoundary().intersects(this.getBoundary());
    }

    public boolean intersects(Curiosity collectable) {
        return collectable.getBoundary().intersects(this.getBoundary());
    }

    public String toString() {
        return " Position: [" + pos.getX() + "," + pos.getY() + "]"
                + " Velocity: [" + velocity.getX() + "," + velocity.getY() + "]";
    }


}