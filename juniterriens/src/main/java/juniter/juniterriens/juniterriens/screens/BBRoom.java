package juniter.juniterriens.juniterriens.screens;

import juniter.juniterriens.juniterriens.engine.Gate;

public class BBRoom extends Room {


    public BBRoom() {

    }

    @Override
    void setGates() {
        gates.add(new Gate(BARoom.singleton(BARoom.class), canvas.getWidth() / 2, 50));
    }


}
