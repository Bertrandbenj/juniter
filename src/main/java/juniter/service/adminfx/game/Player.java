package juniter.service.adminfx.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Player extends ImageView {

    private static final Logger LOG = LogManager.getLogger();

    private Image image;
     double positionX;
     double positionY;
    private double velocityX;
    private double velocityY;
    double width;
    double height;

    private Rectangle2D[] clips;
    private int currentClip = 14;
    private Timeline timeline;

    private final IntegerProperty frameCounter = new SimpleIntegerProperty(0);


    public Player() {
        setPosition(0, 0);
        setVelocity(0, 0);
        image = new Image("/adminfx/game/body.png");

        width = image.getWidth() / 7;
        height = image.getHeight() / 4;

        clips = new Rectangle2D[4 * 7];
        int count = 0;
        for (int row = 0; row < 4; row++)
            for (int column = 0; column < 7; column++, count++)
                clips[count] = new Rectangle2D(width * column, height * row, width, height);

        setImage(image);
        setViewport(clips[currentClip]);

        timeline = new Timeline(
                new KeyFrame(new Duration(1000), event -> {
                    frameCounter.set((frameCounter.get() + 1) % 7);
                    setViewport(clips[frameCounter.get()]);
                })
        );
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

        double coef = 1;
        if (x > 0) {
            currentClip = 21;
            if (positionX >= frame.getWidth()-width) {
                coef = 0;
            }
        }
        if (x < 0) {
            currentClip = 7;
            if (positionX <= 0 ) {
                coef = 0;
            }
        }
        if (y > 0) {
            currentClip = 14;
            if (positionY >= frame.getHeight()-height ) {
                coef = 0;
            }
        }
        if (y < 0) {
            currentClip = 0;
            if (positionY <= 0 ) {
                coef = 0;
            }
        }

        setViewport(clips[currentClip]);
        velocityX += coef * x;
        velocityY += coef * y;
    }

    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }

    public Coins payment() {
        LOG.info("pos " + positionX + " " + positionY);
        var coin = new Coins();
        coin.setPosition( positionX +25- (currentClip == 7 ? 80:0) + (currentClip == 21 ? 80:0)

                , positionY + 25 - (currentClip == 0 ? 80:0) + (currentClip == 14 ? 80:0));
        coin.setImage("/adminfx/game/coin.png");
        return coin;
    }

    public void render(GraphicsContext gc) {
        var v = getViewport();
        gc.drawImage(image, v.getMinX(), v.getMinY(), v.getWidth(), v.getHeight(), positionX, positionY, v.getWidth(), v.getHeight());
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
    public boolean isInside(Bounds frame) {


        return getBoundary().getMinY() >= frame.getMinY() - 25
                && getBoundary().getMinX() >= frame.getMinX() - 25
                && getBoundary().getMaxY() <= frame.getMaxY() - 25
                && getBoundary().getMaxX() <= frame.getMaxX() - 25
                ;
    }

    public boolean intersects(Player s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    public boolean intersects(Coins s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    public String toString() {
        return " Position: [" + positionX + "," + positionY + "]"
                + " Velocity: [" + velocityX + "," + velocityY + "]";
    }


}