package juniter.juniterriens.juniterriens.screens;

import javafx.scene.image.Image;
import juniter.juniterriens.juniterriens.engine.Gate;

public class SandOfKronos extends Room {

    private Image sc = new Image("/juniterriens/game/img/SandClock.png");

    public SandOfKronos() {

    }


    @Override
    void roomSpecific() {
        gc().drawImage(sc, 0,0,sc.getWidth(), sc.getHeight());
    }

    @Override
    void preset() {
        gates.add(new Gate(new Neighbourhood(), canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight() - 80));
    }
}
