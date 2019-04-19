package juniter.juniterriens.juniterriens.screens;

import juniter.core.model.dbo.index.CINDEX;
import juniter.core.model.dbo.tx.Transaction;
import juniter.juniterriens.include.JuniterBindings;
import juniter.juniterriens.juniterriens.characters.Player;
import juniter.juniterriens.juniterriens.engine.Curiosity;
import juniter.juniterriens.juniterriens.engine.Gate;

import java.util.ArrayList;
import java.util.List;

public class Neighbourhood extends Room {

    private List<Curiosity> buddies = new ArrayList<>();

    public Neighbourhood() {

    }

    @Override
    void roomSpecific() {
        buddies.forEach(b-> b.render(gc(), Player.get().intersects(b)));
    }

    @Override
    void preset() {
        gates.add(new Gate(new Market(), canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight()-100));
        gates.add(new Gate(new SandOfKronos(), canvas.getWidth() / 2, canvas.getHeight() - 50, canvas.getWidth() / 2, 100));
        gates.add(new Gate(new BBRoom(), canvas.getWidth() - 50, canvas.getHeight() / 2, 100, canvas.getHeight() / 2));

        int i=0;

        for (Transaction t : JuniterBindings.txRelated) {
            var x = 50 + (i*60);
            var y = 50 + (i*60);
            buddies.add(new Curiosity("/juniterriens/game/img/blueWizard.png", "tx", x, y));
            i++;
        }

        for (CINDEX t : JuniterBindings.certsRelated) {
            var x = 50 + (i*60);
            var y = 50 + (i*60);
            buddies.add(new Curiosity("/juniterriens/game/img/blueWizard.png", "cert", x, y));
            i++;
        }

    }

}
