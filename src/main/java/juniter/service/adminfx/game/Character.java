package juniter.service.adminfx.game;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;


public class Character extends ImageView {
    private Image image;
    private double positionX;
    private double positionY;
    private double width;
    private double height;
    private List<String> labels;

    public Character(String img, List<String> lbls, int x, int y) {
        setPosition(x, y);
        image = new Image(img);
        labels = lbls;
        width = image.getWidth() / 7;
        height = image.getHeight() / 4;


        setImage(image);

    }

    public Character(String img, String lbl, int x, int y) {
        this(img,List.of(lbl),x,y);
    }

    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public void render(GraphicsContext gc, boolean displayLabel) {
        var v = getViewport();
        gc.drawImage(image, positionX, positionY,50, 50);

        if(displayLabel){
            gc.setFont(Font.font("Helvetica",16));
            gc.setFill(Color.BLACK);
            for (String label : labels) {
                gc.fillText(label, positionX + 50, positionY+30);
            }
        }

    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    public boolean intersects(Player s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    public boolean intersects(Coins s) {
        return s.getBoundary().intersects(this.getBoundary());
    }


    public String toString() {
        return " Position: [" + positionX + "," + positionY + "]";
    }
}