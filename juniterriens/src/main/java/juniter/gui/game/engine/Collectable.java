package juniter.gui.game.engine;

import juniter.gui.game.characters.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public interface Collectable {

    default boolean intersects(Player s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    Rectangle2D getBoundary();

    void render(GraphicsContext gc);

    void render();

}
