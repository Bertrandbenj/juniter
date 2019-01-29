package juniter.service.adminfx.juniterriens.engine;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import juniter.service.adminfx.juniterriens.characters.Player;

public interface Collectable {

    default boolean intersects(Player s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    Rectangle2D getBoundary();

    public void render(GraphicsContext gc);

}
