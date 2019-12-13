package juniter.gui.game.screens;

import javafx.scene.image.Image;
import juniter.gui.game.engine.Collectable;
import juniter.gui.game.engine.Curiosity;
import juniter.gui.game.engine.Gate;
import juniter.gui.business.popup.TxBox;

public class Snaky extends Room {

    private Curiosity sakia, silkaj;

    private Image sc = new Image("/gui/game/img/python.png");

    public Snaky() {

    }

    @Override
    void roomSpecific() {

        var displayW = canvas.getHeight() * sc.getWidth() / sc.getHeight();
        gc().drawImage(sc, 350, 0, displayW, canvas.getHeight());


        sakia.render();
        silkaj.render();



        for (Collectable moneybag : collectables)
            moneybag.render(gc());

        collectables.forEach(x -> {

            if (!popupOpen) {
                if (sakia.intersects(x)) {
                    popupOpen = true;
                    popupOpen = TxBox.displayLater("JT:TKS:SAKIA", "5cnvo5bmR8QbtyNVnkDXWq6n5My6oNLd1o6auJApGCsv");
                }

                if (silkaj.intersects(x)) {
                    popupOpen = true;
                    popupOpen = TxBox.displayLater("JT:TKS:SILKAJ", "GfKERHnJTYzKhKUma5h1uWhetbA8yHKymhVH2raf2aCP");
                }
            }
        });

        collectables.removeIf(sakia::intersects);
        collectables.removeIf(silkaj::intersects);
    }

    @Override
    void preset() {
        gates.add(new Gate(new TheBeginning(), 0, canvas.getHeight() / 2, canvas.getWidth() - 50, canvas.getHeight() / 2));


        sakia = new Curiosity("/gui/game/img/sakia.png", "Sakia", 485, 28);
        silkaj = new Curiosity("/gui/game/img/silkaj.png", "Silkaj", 602, canvas.getHeight()-80);

        setCoins();
    }

}
