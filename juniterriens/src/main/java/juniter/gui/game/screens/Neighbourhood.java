package juniter.gui.game.screens;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import juniter.core.model.dbo.index.CINDEX;
import juniter.core.model.dbo.tx.Transaction;
import juniter.gui.game.engine.Collectable;
import juniter.gui.game.engine.Curiosity;
import juniter.gui.game.engine.Gate;
import juniter.gui.technical.I18N;
import juniter.gui.business.panel.TaxPanel;
import juniter.gui.business.popup.TxBox;

import static juniter.gui.JuniterBindings.*;

public class Neighbourhood extends Room {

    //private List<Curiosity> curiosities = new ArrayList<>();

    private Curiosity vote, frama;


    public Neighbourhood() {

    }

    @Override
    void roomSpecific() {
        curiosities.forEach(Curiosity::render);


        for (Collectable moneybag : collectables)
            moneybag.render(gc());

        collectables.forEach(x -> {

            if (!popupOpen) {

                if (frama.intersects(x)) {
                    popupOpen = true;
                    popupOpen = TxBox.display("JT:TKS:FRAMA", "XXX");
                }

                if (vote.intersects(x)) {
                    popupOpen = true;


                    Stage stage = new Stage(StageStyle.UNDECORATED);
                     stage.setTitle("Tax panel");
                    try {
                        var tax = new TaxPanel();
                        tax.start(stage);
                        //tax.init();
                        //stage.showAndWait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    stage.show();


                }
            }
        });

        collectables.removeIf(vote::intersects);
        collectables.removeIf(frama::intersects);
    }

    @Override
    void preset() {
        gates.add(new Gate(new Market(), canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight() - 100));
        gates.add(new Gate(new SandOfKronos(), canvas.getWidth() / 2, canvas.getHeight() - 50, canvas.getWidth() / 2, 100));
        gates.add(new Gate(new TheBeginning(), canvas.getWidth() - 50, canvas.getHeight() / 2, 100, canvas.getHeight() / 2));


        vote = new Curiosity("/gui/game/img/vote.png",
                I18N.get("game.vote.urne"),
                canvas.getWidth() / 2 + 100,
                canvas.getHeight() / 2 + 50);

        frama = new Curiosity("/gui/game/img/frama.png",
                I18N.get("game.framasoft.intro"),
                canvas.getWidth() / 2,
                canvas.getHeight() / 2 + 50);

        curiosities.add(vote);
        curiosities.add(frama);


        int i = 0;
        for (Transaction t : txRelated) {

            var x = 50 + (i * 60);

            var y = 150 + ((i % 5 - 1) * 60);

            var tx = new Curiosity("/gui/game/img/human-city2.png", "tx " + t.getIssuers(), x, y);
            curiosities.add(tx);
            i++;
        }

        i = 0;
        for (CINDEX t : certsRelated) {
            var x = 50;
            ;
            var y = 50 + (i * 60);
            curiosities.add(new Curiosity("/gui/game/img/human-city.png", "cert " + t.getIssuer() + " -> " + t.getReceiver(), x, y));
            i++;
        }
        setCoins();

    }

}
