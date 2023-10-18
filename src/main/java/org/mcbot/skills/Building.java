package org.mcbot.skills;

import org.mcbot.F3DataReader;
import org.mcbot.Movement;
import org.mcbot.Utils;
import org.mcbot.datatypes.XYZ;

public class Building {
    private Movement movement;
    private F3DataReader reader;

    public Building(Movement movement, F3DataReader reader) {
        this.movement = movement;
        this.reader = reader;
    }

    /**
     * Assumes you are on a block-slot and that you have enough
     * blocks to get to the other side.
     */
    public void bridge() {
        //setup
        movement.setYFacingGoal(81);
        movement.turnRight();
        movement.turnRight();
        movement.centerOnBlock();

        movement.pressKey(Movement.SHIFT_KEY);
        movement.pressKey(Movement.BACKWARD_KEY);
        while(true) {
            movement.rightClickHere();
        }
    }
    /** It is assumed that you are already falling
     *  and have a bucket in-hand. **/
    public void mlg() {
        //setup
        movement.lookDown();
        while(reader.data.get("Target Coordinates") == null
              || ((XYZ)reader.data.get("Target Coordinates")).y < ((XYZ)reader.data.get("Coordinates")).y - 10) {
            movement.update();
        }
        // This relies on the top-left pixel of "Bucket", which shows up above the toolbar
        // once the water is released
        while(!Utils.isWhite(Utils.takeScreenshot(), 751, 724)) {
            movement.rightClickHere();
        }
    }
}
