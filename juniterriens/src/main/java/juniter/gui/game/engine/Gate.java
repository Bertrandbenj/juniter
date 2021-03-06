package juniter.gui.game.engine;

import juniter.gui.game.characters.Player;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import juniter.gui.game.screens.Room;
import lombok.Getter;

@Getter
public class Gate extends ImageView implements Obstacle {

    private double positionX;
    private double positionY;

    private double initPlayerX;
    private double initPlayerY;

    private double width;
    private double height;
    private Timeline timeline;
    private Room room;


    public Gate(Room room, double x, double y, double playerx, double playery) {
        this(room, x, y);
        initPlayerX=playerx;
        initPlayerY=playery;

    }

    public Gate(Room room, double x, double y ) {
        this.room = room;
        setImage(new Image("/gui/game/img/gate.png"));
        setPosition(x, y);
    }


    public Gate(String img, Timeline timeline, double x, double y) {
        setPosition(x, y);
        setImage(new Image(img));
        //image = new Image(img);
        this.timeline = timeline;
        width = getWidth() / 7;
        height = getHeight() / 4;

    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(getImage(), positionX, positionY, 50, 50);

    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    public boolean intersects(Player s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    public boolean intersects(Collectable s) {
        return s.getBoundary().intersects(this.getBoundary());
    }


    public String toString() {
        return " Position: [" + positionX + "," + positionY + "]";
    }
}