package org.mcbot.skills;

import org.mcbot.F3DataReader;
import org.mcbot.Movement;

public class Building {
    private Movement movement;
    private F3DataReader reader;

    public Building(Movement movement, F3DataReader reader) {
        this.movement = movement;
        this.reader = reader;
    }

    /**
     * Assumes you are on a block-slot and that you have enough
     * to get to the other side.
     */
    public void bridge() {
        //setup
        movement.setYFacing(81);
        movement.turnRight();
        movement.turnRight();
        movement.centerOnBlock();

        movement.pressKey(Movement.SHIFT_KEY);
        movement.pressKey(Movement.BACKWARD_KEY);
        while(true) {
            movement.rightClickHere();
        }
    }
}
