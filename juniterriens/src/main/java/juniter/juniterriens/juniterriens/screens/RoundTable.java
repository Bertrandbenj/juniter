package juniter.juniterriens.juniterriens.screens;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import juniter.juniterriens.juniterriens.Game;
import juniter.juniterriens.juniterriens.characters.Player;
import juniter.juniterriens.juniterriens.engine.Curiosity;
import juniter.juniterriens.juniterriens.engine.Gate;
import juniter.juniterriens.juniterriens.engine.Utils;
import org.springframework.stereotype.Component;

@Component
public class RoundTable extends Room {


    private Curiosity moul;

    public RoundTable() {


    }

    @Override
    void roomSpecific() {


        moul.render(gc(), moul.intersects(Player.get()));
        // creats seats
        for (int i = 0; i < 59; i++) {
            var img = new Image("/juniterriens/game/img/moneybag2.png");
            Utils.drawRotatedImage(gc(), img, 360. * i / 59, 220, 20);
        }

    }


    @Override
    void preset() {
        gates.add(new Gate(new BBRoom(), canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight() - 100));

        moul = new Curiosity("/juniterriens/game/img/blueWizard.png", Game.list, canvas.getWidth() / 2, canvas.getHeight() - 50);
        moul.setLabelPos(new Point2D(canvas.getWidth() / 2 + 50, canvas.getHeight() - (50 + 20 * Game.list.size())));

    }


}
