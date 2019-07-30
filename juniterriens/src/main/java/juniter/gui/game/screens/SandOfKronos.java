package juniter.gui.game.screens;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import juniter.gui.game.engine.Curiosity;
import juniter.gui.game.engine.Gate;
import juniter.gui.include.I18N;

import java.util.List;

public class SandOfKronos extends Room {

    private Image sc = new Image("/gui/game/img/SandClock.png");

    private Curiosity einstein;

    public SandOfKronos() {

    }


    @Override
    void roomSpecific() {

        var displayW = canvas.getHeight() * sc.getWidth() / sc.getHeight();
        gc().drawImage(sc, 150, 0, displayW, canvas.getHeight());

        einstein.render();

    }

    @Override
    void preset() {
        gates.add(new Gate(new Neighbourhood(), canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight() - 100));

        einstein = new Curiosity("/gui/game/img/einstein.png",
                List.of( I18N.get("game.einstein.mercury"), I18N.get("game.einstein.mercury2")),
                canvas.getWidth() - 200, canvas.getHeight() - 100);
        einstein.setLabelPos(new Point2D(400,canvas.getHeight() - 180));
    }
}
