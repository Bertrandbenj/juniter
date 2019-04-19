package juniter.juniterriens.juniterriens.engine;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import juniter.juniterriens.juniterriens.characters.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Curiosity extends ImageView implements Obstacle {

    protected Point2D pos;

    protected Point2D displaySize ;

    protected Point2D labelPos;

    protected List<String> labels;



    public Curiosity(String img, List<String> lbls, double x, double y) {
        setPosition(x, y);
        var image = new Image(img);
        labels = lbls;
        displaySize = new Point2D(50, 50);

        setImage(image);
        setViewport(new Rectangle2D(0, 0, image.getWidth(), image.getHeight()));
    }

    public Curiosity(String img, String lbl, double x, double y) {
        this(img, List.of(lbl), x, y);
    }

    public void setPosition(double x, double y) {
        pos = new Point2D(x, y);
        labelPos = new Point2D(x + 50, y + 50); // default value
    }

    public void render(GraphicsContext gc, boolean displayLabel) {
        var v = getViewport();
        gc.drawImage(getImage(), v.getMinX(), v.getMinY(), v.getWidth(), v.getHeight(), pos.getX(), pos.getY(), displaySize.getX(), displaySize.getY());

        if (displayLabel) {
            gc.setFont(Font.font("Helvetica", 16));
            gc.setFill(Color.BLACK);
            var yoffset = 30;
            for (String label : labels) {
                gc.fillText(label, labelPos.getX(), labelPos.getY() + (yoffset += 20));
            }
        }

    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D( pos.getX(), pos.getY(), displaySize.getX(), displaySize.getY());
    }

    public boolean intersects(Player s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    public boolean intersects(Collectable s) {
        return s.getBoundary().intersects(this.getBoundary());
    }


    public String toString() {
        return " Position: [" +  pos.getX()+","+ pos.getY() + "]";
    }
}