package juniter.juniterriens.juniterriens.screens;

import juniter.juniterriens.juniterriens.engine.Gate;

public class BARoom extends Room {

    public BARoom() {
        //gates.add(new Gate(null, canvas.getWidth() / 2, 50));
    }

    public static Room get() {
        return new BARoom();
    }
}
