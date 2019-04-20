package juniter.gui.game.screens;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import juniter.gui.game.Game;
import juniter.gui.game.engine.Collectable;
import juniter.gui.game.engine.Curiosity;
import juniter.gui.game.engine.Gate;
import juniter.gui.include.I18N;
import juniter.gui.include.JuniterBindings;
import juniter.gui.include.TxBox;


public class TheBeginning extends Room {

    private Curiosity duniter, trm;


    public TheBeginning() {

    }

    @Override
    void roomSpecific() {

        var theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
        gc().setFill(Color.valueOf("F4F4F4"));
        gc().setFont(theFont);
        gc().fillText(I18N.get("game.pi.rule5"), canvas.getWidth() / 2, canvas.getHeight() / 2);

        if (JuniterBindings.selectedTheme.getValue().equals(JuniterBindings.DARK_THEME)) {
            Game.foundRule5 = true;
        }


        duniter.render();
        trm.render();

        for (Collectable moneybag : collectables)
            moneybag.render(gc());

        collectables.forEach(x -> {

            if (!txOpen) {
                if (duniter.intersects(x)) {
                    txOpen = true;
                    Platform.runLater(() -> txOpen = TxBox.display("JT:TKS:DUNITER", "2ny7YAdmzReQxAayyJZsyVYwYhVyax2thKcGknmQy5nQ"));
                }

                if (trm.intersects(x)) {
                    txOpen = true;
                    Platform.runLater(() -> txOpen = TxBox.display("JT:TKS:TRM", "Ds1z6Wd8hNTexBoo3LVG2oXLZN4dC9ZWxoWwnDbF1NEW"));
                }
            }
        });

        collectables.removeIf(duniter::intersects);
        collectables.removeIf(trm::intersects);

    }


    @Override
    void preset() {
        gates.add(new Gate(new SavingMarcel(), canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight() - 100));
        gates.add(new Gate(new RoundTable(), canvas.getWidth() / 2, canvas.getHeight() - 50, canvas.getWidth() / 2, 100));
        gates.add(new Gate(new Neighbourhood(), 0, canvas.getHeight() / 2, canvas.getWidth() - 100, canvas.getHeight() / 2));
        gates.add(new Gate(new Snaky(), canvas.getWidth() - 50, canvas.getHeight() / 2, 100, canvas.getHeight() / 2));


        duniter = new Curiosity("/gui/game/img/duniter.png", I18N.get("game.trm"), 200, 200);

        trm = new Curiosity("/gui/game/img/trm.jpeg", I18N.get("game.trm"), canvas.getWidth() - 200, 200);
        trm.setLabelPos(new Point2D(canvas.getWidth() - 300, 250));

        setCoins();
    }


}
