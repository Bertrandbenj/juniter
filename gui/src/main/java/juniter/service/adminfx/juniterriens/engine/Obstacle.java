package juniter.service.adminfx.juniterriens.engine;

import juniter.service.adminfx.juniterriens.characters.Player;
import javafx.geometry.Rectangle2D;

public interface Obstacle {
    Rectangle2D getBoundary();

    default boolean intersect(Player p){
        return p.getBoundary().intersects(getBoundary());
    }

}
