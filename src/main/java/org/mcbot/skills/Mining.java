package org.mcbot.skills;

import org.mcbot.wordwork.F3DataReader;
import org.mcbot.datatypes.XYZ;

public class Mining {
    private Movement movement;
    private F3DataReader reader;

    public Mining(Movement movement, F3DataReader reader) {
        this.movement = movement;
        this.reader = reader;
    }
    public Mining(Movement movement) {
        this(movement, movement.getReader());
    }

    /**
     * Assumes we have a pickaxe in hand and a reachable block
     */
    public void mineBlock() {
        // record current target coordinates
        reader.readScreen();
        XYZ currentBlock = (XYZ) reader.data.get("Target Coordinates");
        //left-click until targeted-block changes
        movement.holdClick();
        while( currentBlock.equals( (XYZ)reader.data.get("Target Coordinates"))) {
            movement.update();
        }
        movement.releaseClick();
    }
    /**
     * Assumes we have a pickaxe in hand and at least one reachable block
     */
    public void simpleStripMine(int length) {
        for(int i = 0; i < length; i ++) {
            movement.setYFacingGoal(40);
            movement.centerOnBlock();
            movement.faceDirectionGoal();
            XYZ targ = (XYZ)reader.data.get("Target Coordinates");
            XYZ curr = (XYZ)reader.data.get("Coordinates");
            if (targ.y >= curr.y) {
                mineBlock();
            }
            targ = (XYZ)reader.data.get("Target Coordinates");
            curr = (XYZ)reader.data.get("Coordinates");
            if (targ.y == curr.y) {
                mineBlock();
            }
            movement.moveForward(1, false);
        }
    }
}
