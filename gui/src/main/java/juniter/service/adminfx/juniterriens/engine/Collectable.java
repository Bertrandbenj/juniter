package juniter.service.adminfx.juniterriens.engine;

import juniter.service.adminfx.juniterriens.characters.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public interface Collectable {

    default boolean intersects(Player s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    Rectangle2D getBoundary();

    public void render(GraphicsContext gc);

}
