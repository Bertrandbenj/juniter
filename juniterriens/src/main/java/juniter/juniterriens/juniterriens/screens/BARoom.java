package juniter.juniterriens.juniterriens.screens;

import juniter.juniterriens.juniterriens.engine.Gate;

public class BARoom extends Room {


    public BARoom() {

    }


    @Override
    void setGates() {
        gates.add(new Gate(BBRoom.singleton(BARoom.class), canvas.getWidth() / 2, 50));
    }
}
