package juniter.gui.game.characters;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import juniter.gui.game.objects.Coins;


public class WhiteRabbit extends ImageView {

    private Image image;
    private double positionX;
    private Double positionY;
    private Double velocityX;
    private Double velocityY;
    private Double width;
    private Double height;

    private Rectangle2D[] clips;
    private int currentClip = 0;


    private Transition animation;

    public Transition getAnimation() {
        return animation;
    }


    private final IntegerProperty frameCounter = new SimpleIntegerProperty(0);


    public WhiteRabbit() {

        setPosition(0, 0);
        setVelocity(0, 0);
        image = new Image("/gui/game/img/bunnysheet4.png");
        int rows = 4;
        int col = 8;

        width = image.getWidth() / col;
        height = image.getHeight() / rows;

        clips = new Rectangle2D[rows * col];
        int count = 0;
        for (int row = 0; row < rows; row++)
            for (int column = 0; column < col; column++, count++)
                clips[count] = new Rectangle2D(width * column, height * row, width, height);

        setImage(image);
        setViewport(clips[currentClip]);

        animation = new Transition() {
            {
                setCycleDuration(Duration.millis(1000));
            }

            @Override
            protected void interpolate(double frac) {
                final int index = Math.min((int) Math.floor(frac * 8), 8 - 1);

                setViewport(clips[index + currentClip]);
            }


        };
        animation.setInterpolator(Interpolator.LINEAR);
        animation.setCycleCount(Animation.INDEFINITE);
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

        x *= 2;
        y *= 2;
        double coef = 1;
        if (x > 0) {
            currentClip = 16;
            if (positionX >= frame.getWidth() - width) {
                coef = 0;
            }
        }
        if (x < 0) {
            currentClip = 24;
            if (positionX <= 0) {
                coef = 0;
            }
        }
        if (y > 0) {
            currentClip = 0;
            if (positionY >= frame.getHeight() - height) {
                coef = 0;
            }
        }
        if (y < 0) {
            currentClip = 8;
            if (positionY <= 0) {
                coef = 0;
            }
        }

        //setViewport(clips[currentClip]);
        velocityX += coef * x;
        velocityY += coef * y;
    }

    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }

    public  void render(GraphicsContext gc) {
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

    public boolean intersects(WhiteRabbit s) {
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