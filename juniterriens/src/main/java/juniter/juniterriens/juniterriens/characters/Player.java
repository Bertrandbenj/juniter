package juniter.juniterriens.juniterriens.characters;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import juniter.juniterriens.juniterriens.engine.Collectable;
import juniter.juniterriens.juniterriens.engine.Curiosity;
import juniter.juniterriens.juniterriens.objects.Coins;
import juniter.juniterriens.juniterriens.objects.Items;
import lombok.Data;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@Getter
public class Player extends ImageView {

    private static final Logger LOG = LogManager.getLogger();

    private Image _image;
    public double positionX;
    public double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;

    private Rectangle2D[] clips;
    public int currentClip = 14;
    private Timeline timeline;

    private ArrayList<Items> inventory;


    private final IntegerProperty frameCounter = new SimpleIntegerProperty(0);
    Transition animation;

    public Transition getAnimation() {
        return animation;
    }


    public Player() {
        setPosition(0, 0);
        setVelocity(0, 0);
        _image = new Image("/juniterriens/game/img/body.png");

        width = _image.getWidth() / 7;
        height = _image.getHeight() / 4;
        inventory = new ArrayList<>();

        clips = new Rectangle2D[4 * 7];
        int count = 0;
        for (int row = 0; row < 4; row++)
            for (int column = 0; column < 7; column++, count++)
                clips[count] = new Rectangle2D(width * column, height * row, width, height);

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
        positionX = x;
        positionY = y;
    }

    public void setVelocity(double x, double y) {
        velocityX = x;
        velocityY = y;
    }

    public void addVelocity(double x, double y, Bounds frame) {
        animation.play();
        double coef = 1;
        if (inventory.stream().anyMatch(items -> items.is("Boots"))) {
            coef *= 2;
        }
        if (x > 0) {
            currentClip = 21;
            if (positionX >= frame.getWidth() - width) {
                coef = 0;
            }
        }
        if (x < 0) {
            currentClip = 7;
            if (positionX <= 0) {
                coef = 0;
            }
        }
        if (y > 0) {
            currentClip = 14;
            if (positionY >= frame.getHeight() - height) {
                coef = 0;
            }
        }
        if (y < 0) {
            currentClip = 0;
            if (positionY <= 0) {
                coef = 0;
            }
        }

        setViewport(clips[currentClip]);
        velocityX += coef * x;
        velocityY += coef * y;
    }

    public  void update(double time) {
        var up = new Transition() {
            {
                setCycleDuration(Duration.millis(1000));
                setVelocity(velocityX, velocityY);
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
        positionX += velocityX * time;
        positionY += velocityY * time;
    }

    public  Coins payment() {
        ///LOG.info("pos " + positionX + " " + positionY);
        var coin = new Coins();
        coin.setPosition(positionX + 25 - (currentClip == 7 ? 80 : 0) + (currentClip == 21 ? 80 : 0)

                , positionY + 25 - (currentClip == 0 ? 80 : 0) + (currentClip == 14 ? 80 : 0));
        Coins.playSound();
        return coin;
    }

    public  void render(GraphicsContext gc) {
        var v = getViewport();
        gc.drawImage(_image, v.getMinX(), v.getMinY(), v.getWidth(), v.getHeight(), positionX, positionY, v.getWidth(), v.getHeight());
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, 50, 50);
    }

    /**
     * player.getBoundary().getMinY() <= (bounds.getMinY() - player.height);
     *
     * @param frame
     * @return
     */
    public  boolean isInside(Bounds frame) {

        return getBoundary().getMinY() >= frame.getMinY() - 25
                && getBoundary().getMinX() >= frame.getMinX() - 25
                && getBoundary().getMaxY() <= frame.getMaxY() - 25
                && getBoundary().getMaxX() <= frame.getMaxX() - 25
                ;
    }

    public  boolean intersects(Collectable collectable) {
        return collectable.getBoundary().intersects(this.getBoundary());
    }
    public  boolean intersects(Curiosity collectable) {
        return collectable.getBoundary().intersects(this.getBoundary());
    }

    public String toString() {
        return " Position: [" + positionX + "," + positionY + "]"
                + " Velocity: [" + velocityX + "," + velocityY + "]";
    }


}