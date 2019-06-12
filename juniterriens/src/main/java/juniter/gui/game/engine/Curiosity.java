package juniter.gui.game.engine;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import juniter.gui.game.characters.Player;
import juniter.gui.game.screens.Room;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Getter
@Setter
public class Curiosity extends ImageView implements Obstacle {
    private static final Logger LOG = LogManager.getLogger(Curiosity.class);
    private Transition animation;

    protected Point2D pos;

    protected Point2D displaySize;

    protected Point2D labelPos;

    protected List<String> labels;

    protected Point2D sheetSize;
    private Rectangle2D[] clips;
    private int currentClip = 0;
    private boolean played ;

    public Curiosity(String img, List<String> lbls, double x, double y, Point2D sSize) {
        setPosition(x, y);
        var image = new Image(img);
        labels = lbls;
        displaySize = new Point2D(50, 50);

        setImage(image);

        this.sheetSize = sSize;


        if (!isSheet()) {
            // simple case
            setViewport(new Rectangle2D(0, 0, image.getWidth(), image.getHeight()));
        } else {
            // do clip the image sheet
            int size = (int) (sheetSize.getX() * sheetSize.getY());


            clips = new Rectangle2D[(size)];
            var clipH = image.getHeight() / sheetSize.getY();
            var clipW = image.getWidth() / sheetSize.getX();
            int count = 0;
            for (int row = 0; row < sheetSize.getY(); row++) {

                for (int column = 0; column < sheetSize.getX(); column++, count++) {
                    clips[count] = new Rectangle2D(clipW * column, clipH * row, clipW, clipH);
                }
            }
            setViewport(clips[currentClip]);

            animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(1000));
                }

                @Override
                protected void interpolate(double frac) {
                    final int index = (int) Math.min(Math.floor(frac * size), size - 1);
                    setViewport(clips[index + currentClip]);
                }

            };
            animation.setInterpolator(Interpolator.LINEAR);
            animation.setCycleCount(1);
            animation.setOnFinished(e-> played=true);
        }


    }

    public Curiosity(String img, List<String> lbls, double x, double y) {
        this(img, lbls, x, y, null);
    }

    public Curiosity(String img, String lbl, double x, double y) {
        this(img, List.of(lbl), x, y);
    }

    private boolean isSheet() {
        return sheetSize != null && sheetSize.getY() * sheetSize.getX() > 1;
    }

    public void setPosition(double x, double y) {
        pos = new Point2D(x, y);
        labelPos = new Point2D(x + 50, y + 20); // default value
    }

    public void render() {
        render(Room.canvas.getGraphicsContext2D(), Player.get().intersects(this));
    }

    public void render(GraphicsContext gc, boolean displayLabel) {
        var v = getViewport();
        // LOG.info("Curiosity viewport " + v);
        gc.drawImage(getImage(), v.getMinX(), v.getMinY(), v.getWidth(), v.getHeight(), pos.getX(), pos.getY(), displaySize.getX(), displaySize.getY());

        if (displayLabel) {
            gc.setFont(Font.font("Helvetica", 16));
            gc.setFill(Color.BLACK);
            var yoffset = 30;
            for (String label : labels) {
                gc.fillText(label, labelPos.getX(), labelPos.getY() + (yoffset += 20));
            }

            if (isSheet() && !played) {
                animation.play();
            }

        }

    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(pos.getX(), pos.getY(), displaySize.getX(), displaySize.getY());
    }

    public boolean intersects(Player s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    public boolean intersects(Collectable s) {
        return s.getBoundary().intersects(this.getBoundary());
    }


    public String toString() {
        return " Position: [" + pos.getX() + "," + pos.getY() + "]";
    }
}