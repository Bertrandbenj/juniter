package juniter.service.adminfx.juniterriens.engine;

import javafx.geometry.Rectangle2D;
import juniter.service.adminfx.juniterriens.characters.Player;

public interface Obstacle {
    Rectangle2D getBoundary();

    default boolean intersect(Player p){
        return p.getBoundary().intersects(getBoundary());
    }

}
