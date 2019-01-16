package juniter.service.adminfx.game;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

public class Coins {
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;

    public Coins() {
        positionX = 0;
        positionY = 0;
        velocityX = 0;
        velocityY = 0;
    }

    public void setImage(Image i) {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setImage(String filename) {
        Image i = new Image(filename);
        setImage(i);
    }

    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public void setVelocity(double x, double y) {
        velocityX = x;
        velocityY = y;
    }

    public void addVelocity(double x, double y) {
        velocityX += x;
        velocityY += y;
    }

    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY, 25,25);
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, 25, 25);
    }

    public boolean intersects(Coins s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    public String toString() {
        return " Position: [" + positionX + "," + positionY + "]"
                + " Velocity: [" + velocityX + "," + velocityY + "]";
    }
}