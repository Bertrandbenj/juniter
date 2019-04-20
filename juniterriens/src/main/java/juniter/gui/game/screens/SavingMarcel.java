package juniter.gui.game.screens;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;
import juniter.gui.include.I18N;
import juniter.gui.game.characters.Player;
import juniter.gui.game.engine.Collectable;
import juniter.gui.game.engine.Curiosity;
import juniter.gui.game.engine.Gate;
import juniter.gui.game.objects.Coins;

import java.util.List;

public class SavingMarcel extends Room {
    private Curiosity puits, trmPage;


    public SavingMarcel() {

    }


    @Override
    void roomSpecific() {
        trmPage.render(gc(), trmPage.intersects(Player.get()));
        puits.render(gc(), puits.intersects(Player.get()));
        collectables.removeIf(puits::intersects);


        for (Collectable moneybag : collectables)
            moneybag.render(gc());
        if (collectables.isEmpty() && Player.get().score == 0) {
            timeline.stop();
            savedMarcel(gc());
        }


    }

    @Override
    void preset() {
        gates.add(new Gate(new TheBeginning(), canvas.getWidth() / 2, canvas.getHeight() - 50, canvas.getWidth() / 2, 80));
        var dialog = List.of(I18N.get("game.savingM.hi"), I18N.get("game.savingM.hi2"));

        puits = new Curiosity("/gui/game/img/rockwell.png", dialog, canvas.getWidth() / 2, canvas.getHeight() / 2);
        puits.getDisplaySize().add(30,0);
        trmPage = new Curiosity("/gui/game/img/blankpage.png", I18N.get("game.savingM.clue1"), 600, 20);

        collectables.clear();
        for (int i = 0; i < 15; i++) {
            Coins coin = new Coins();

            double px = (canvas.getWidth() - 50) * Math.random() + 25;
            double py = (canvas.getHeight() - 50) * Math.random() + 25;
            coin.setPosition(px, py);
            collectables.add(coin);
        }

    }


    private void savedMarcel(GraphicsContext gc) {
        gc.strokeText(I18N.get("game.savingM.greatSuccess"), 550, canvas.getHeight() / 2 - 100);
        gc.fillText(I18N.get("game.savingM.moral"), 500, canvas.getHeight() / 2 - 70);

        puits.render(gc, false);
        Player.get().render(gc);
        Player marcel = new Player();
        marcel.currentClip = 14;
        marcel.setPosition(Player.get().getPos().getX() + 70, Player.get().getPos().getY() + 70);
        marcel.render(gc);


        var anim = new Timeline();
        anim.getKeyFrames().add(new KeyFrame(Duration.millis(16), event -> {
            marcel.currentClip = 14;
            marcel.setPosition(Player.get().getPos().getX() + 70, Player.get().getPos().getY() + 70);
            marcel.render(gc);
            puits.render(gc, false);
            Player.get().render(gc);
        }));
        anim.setCycleCount(10);
        anim.play();

        LOG.info("Finished savingMarcel Mission");

    }
}
