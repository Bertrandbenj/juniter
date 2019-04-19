package juniter.juniterriens.juniterriens.screens;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import juniter.juniterriens.include.I18N;
import juniter.juniterriens.juniterriens.Game;
import juniter.juniterriens.juniterriens.characters.Player;
import juniter.juniterriens.juniterriens.engine.Boat;
import juniter.juniterriens.juniterriens.engine.Curiosity;
import juniter.juniterriens.juniterriens.engine.Gate;

import java.util.ArrayList;
import java.util.List;

public class Market extends Room {

    private Curiosity π, shop;

    private List<String> rules = List.of(
            I18N.get("game.pi.5rules"),
            I18N.get("game.pi.rule1"),
            I18N.get("game.pi.rule2"),
            I18N.get("game.pi.rule3"),
            I18N.get("game.pi.rule4"),
            Game.foundRule5?I18N.get("game.pi.rule5"):I18N.get("game.pi.helpMe"));

    private List<Boat> boats = new ArrayList<>();

    public Market() {

    }


    @Override
    void roomSpecific() {
        gc().setFill(Color.LIGHTBLUE);
        gc().fillRect(0,130, gc().getCanvas().getWidth(),200);
        π.render(gc(), π.intersects(Player.get()));
        boats.forEach(b -> b.update(0.01));
        boats.forEach(b -> b.render(gc(), b.intersects(Player.get())));
        boats.forEach(b -> {
            if(b.intersects(Player.get())){
                Player.get().setVelocity(b.left?-b.speed:b.speed,0);
            }
        });
    }


    @Override
    void preset() {
        gates.add(new Gate(new Neighbourhood(), canvas.getWidth() / 2, canvas.getHeight() - 50, canvas.getWidth() / 2, 100));
        gates.add(new Gate(new SavingMarcel(), canvas.getWidth() - 50, canvas.getHeight() / 2, 100, canvas.getHeight() / 2));

        π = new Curiosity("/juniterriens/game/img/blueWizard.png", rules, 200, 0);
        shop = new Curiosity("/juniterriens/game/img/shop.png", rules, 200, 0);
        //π.setLabelPos(new Point2D(canvas.getWidth() / 2 + 50, 50));

        boats.add(new Boat(" ", 800, canvas.getHeight() / 2 - 100, false));
        boats.add(new Boat(" ", 400, canvas.getHeight() / 2 - 35, true));
        boats.add(new Boat(" ", 600, canvas.getHeight() / 2 + 35, false));
    }
}
