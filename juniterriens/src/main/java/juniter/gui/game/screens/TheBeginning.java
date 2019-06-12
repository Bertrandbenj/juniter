package juniter.gui.game.screens;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import juniter.gui.game.Game;
import juniter.gui.game.engine.Collectable;
import juniter.gui.game.engine.Curiosity;
import juniter.gui.game.engine.Gate;
import juniter.gui.include.I18N;
import juniter.gui.include.JuniterBindings;
import juniter.gui.include.Technology;
import juniter.gui.include.TxBox;


public class TheBeginning extends Room {

    private Curiosity duniter, trm, techtree;


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


        for (Collectable x : collectables) {
            if (!popupOpen) {
                if (duniter.intersects(x)) {
                    popupOpen = true;
                    popupOpen = TxBox.display("JT:TKS:DUNITER", "2ny7YAdmzReQxAayyJZsyVYwYhVyax2thKcGknmQy5nQ");
                }

                if (trm.intersects(x)) {
                    popupOpen = true;
                    popupOpen = TxBox.display("JT:TKS:TRM", "Ds1z6Wd8hNTexBoo3LVG2oXLZN4dC9ZWxoWwnDbF1NEW");
                }

                if (techtree.intersects(x)) {
                    popupOpen = true;

                    Stage stage = new Stage(StageStyle.UNDECORATED);
                    stage.setTitle(I18N.get("vocab.technologies"));
                    try {
                        var tech = new Technology();
                        tech.start(stage);
                        //tax.init();
                        //stage.showAndWait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    stage.show();
                }
            }
        }

    }


    @Override
    void preset() {
        gates.add(new Gate(new SavingMarcel(), canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight() - 100));
        gates.add(new Gate(new RoundTable(), canvas.getWidth() / 2, canvas.getHeight() - 50, canvas.getWidth() / 2, 100));
        gates.add(new Gate(new Neighbourhood(), 0, canvas.getHeight() / 2, canvas.getWidth() - 100, canvas.getHeight() / 2));
        gates.add(new Gate(new Snaky(), canvas.getWidth() - 50, canvas.getHeight() / 2, 100, canvas.getHeight() / 2));


        duniter = new Curiosity("/gui/game/img/duniter.png", I18N.get("game.trm"), 300, 150);

        trm = new Curiosity("/gui/game/img/trm.jpeg", I18N.get("game.trm"), canvas.getWidth() - 350, 150);
        trm.setLabelPos(new Point2D(canvas.getWidth() - 450, 250));

        techtree = new Curiosity("/gui/game/img/techtree.png", I18N.get("game.techtree"), canvas.getWidth() / 3, canvas.getHeight() - 50);

        curiosities.add(duniter);
        curiosities.add(trm);
        curiosities.add(techtree);

        setCoins();
    }


}
