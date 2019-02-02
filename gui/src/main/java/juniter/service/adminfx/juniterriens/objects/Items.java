package juniter.service.adminfx.juniterriens.objects;

import juniter.service.adminfx.juniterriens.engine.Collectable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Items extends ImageView implements Collectable {
    private Image image;
    private double positionX;
    private double positionY;
    private double width;
    private double height;


    private String name;
    private Rectangle2D[] clips;
    private int currentClip = 3;
    private Timeline timeline;
    private final IntegerProperty frameCounter = new SimpleIntegerProperty(0);


    public boolean is(String test){
        if(test!=null && name != null ){
            return name .equals(test);
        }

        return false;
    }

    public  Items(String name) {
        this.name = name;
        setPosition(0, 0);
    }


    public  Items(String name, String img, int rows, int col) {
        this(name);

        image = new Image(img);
        width = image.getWidth() / col;
        height = image.getHeight() / rows;


        clips = new Rectangle2D[rows * col];
        int count = 0;
        for (int row = 0; row < rows; row++)
            for (int column = 0; column < col; column++, count++)
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


    public  void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }


    public void render(GraphicsContext gc) {
        var v = getViewport();
        gc.drawImage(image, v.getMinX(), v.getMinY(), v.getWidth(), v.getHeight(), positionX, positionY, v.getWidth(), v.getHeight());
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, 25, 25);
    }

    public boolean intersects(Items s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    public String toString() {
        return " Position: [" + positionX + "," + positionY + "]";
    }
}