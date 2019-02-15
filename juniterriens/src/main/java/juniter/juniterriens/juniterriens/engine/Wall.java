package juniter.juniterriens.juniterriens.engine;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;

public class Wall implements Obstacle {

    public static Wall TOP, BOT, LEFT, RIGHT;

    private Rectangle2D rect;

    Wall(double x, double y, double width, double heigth) {
        rect = new Rectangle2D(x, y, width, heigth);
    }


    @Override
    public Rectangle2D getBoundary() {
        return rect;
    }


    public static void setWall(Canvas canvas ){

        TOP = new Wall(0,0,canvas.getWidth(),20);

        BOT = new Wall(0,canvas.getHeight(),canvas.getWidth(),20);

        LEFT = new Wall(0,0,20,canvas.getHeight());

        RIGHT = new Wall(canvas.getWidth(),0,20,canvas.getHeight());

    }

}
