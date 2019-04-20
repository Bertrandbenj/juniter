package juniter.gui.game.screens;

import javafx.application.Platform;
import javafx.scene.image.Image;
import juniter.gui.game.engine.Collectable;
import juniter.gui.game.engine.Curiosity;
import juniter.gui.game.engine.Gate;
import juniter.gui.include.TxBox;

public class Snaky extends Room {

    private Curiosity sakia, silkaj;

    private Image sc = new Image("/gui/game/img/python.png");

    public Snaky() {

    }

    @Override
    void roomSpecific() {

        var displayW = canvas.getHeight() * sc.getWidth() / sc.getHeight();
        gc().drawImage(sc, 300, 0, displayW, canvas.getHeight());


        sakia.render();
        silkaj.render();



        for (Collectable moneybag : collectables)
            moneybag.render(gc());

        collectables.forEach(x -> {

            if (!txOpen) {
                if (sakia.intersects(x)) {
                    txOpen = true;
                    Platform.runLater(() -> txOpen = TxBox.display("JT:TKS:SAKIA", "5cnvo5bmR8QbtyNVnkDXWq6n5My6oNLd1o6auJApGCsv"));
                }

                if (silkaj.intersects(x)) {
                    txOpen = true;
                    Platform.runLater(() -> txOpen = TxBox.display("JT:TKS:SILKAJ", "GfKERHnJTYzKhKUma5h1uWhetbA8yHKymhVH2raf2aCP"));
                }
            }
        });

        collectables.removeIf(sakia::intersects);
        collectables.removeIf(silkaj::intersects);
    }

    @Override
    void preset() {
        gates.add(new Gate(new TheBeginning(), 0, canvas.getHeight() / 2, canvas.getWidth() - 50, canvas.getHeight() / 2));


        sakia = new Curiosity("/gui/game/img/sakia.png", "", 200, 200);
        silkaj = new Curiosity("/gui/game/img/silkaj.png", "", canvas.getWidth() - 200, 200);

        setCoins();
    }

}
