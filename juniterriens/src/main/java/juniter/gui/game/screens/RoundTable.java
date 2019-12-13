package juniter.gui.game.screens;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import juniter.gui.game.Game;
import juniter.gui.game.engine.Collectable;
import juniter.gui.game.engine.Curiosity;
import juniter.gui.game.engine.Gate;
import juniter.gui.game.engine.Utils;
import juniter.gui.technical.I18N;
import juniter.gui.business.popup.TxBox;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoundTable extends Room {


    private Curiosity moul, gerard;


    public RoundTable() {

    }

    @Override
    void roomSpecific() {

        moul.render();
        gerard.render();

        // create round table
        for (int i = 0; i < 59; i++) {
            var img = new Image("/gui/game/img/moneybag2.png");
            Utils.drawRotatedImage(gc(), img, 360. * i / 59, 220, 20);
        }

        // Unus pro omnibus, omnes pro uno
        var theFont = Font.font("Helvetica", FontWeight.BOLD, 14);
        gc().setFont(theFont);
        gc().setFill(Color.GREEN);
        gc().setStroke(Color.BLACK);
        gc().setLineWidth(1);
        gc().fillText("Unus pro omnibus", 170, canvas.getHeight() / 2 - 20);
        gc().fillText("omnes pro uno", 185, canvas.getHeight() / 2 + 20);


        for (Collectable moneybag : collectables)
            moneybag.render(gc());

        collectables.forEach(x -> {

            if (!popupOpen) {
                if (moul.intersects(x)) {
                    popupOpen = true;
                    popupOpen = TxBox.displayLater("JT:TKS:DEATHREAPER", "FuorSmMNh27Duufcx8anHdHaU3wAw2YmjCRh1b9UWEdP");
                }

                if (gerard.intersects(x)) {
                    popupOpen = true;
                    popupOpen = TxBox.displayLater("JT:TKS:WOTWIZARD", "CRBxCJrTA6tmHsgt9cQh9SHcCc8w8q95YTp38CPHx2Uk");
                }
            }
        });

        collectables.removeIf(moul::intersects);
        collectables.removeIf(gerard::intersects);

    }


    @Override
    void preset() {

        gates.add(new Gate(new TheBeginning(), canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight() - 100));

        moul = new Curiosity("/gui/game/img/blueWizard.png", Game.expiryList, canvas.getWidth() / 2 + 100, canvas.getHeight() - 100);
        moul.setLabelPos(new Point2D(canvas.getWidth() / 2 + 150, canvas.getHeight() - (100 + 20 * Game.expiryList.size())));

        gerard = new Curiosity("/gui/game/img/wizardgrow.png", List.of(I18N.get("game.gerard.ww"), I18N.get("game.gerard.hi")), canvas.getWidth() - 150, canvas.getHeight() / 2, new Point2D(5, 1));

        gerard.setLabelPos(new Point2D(canvas.getWidth() - 400, canvas.getHeight() / 2 + 50));

        setCoins();
    }


}
