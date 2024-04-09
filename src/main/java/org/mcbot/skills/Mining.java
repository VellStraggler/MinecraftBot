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
     * Mines the block we are currently facing.
     * This assumes we have both a pickaxe in hand and a reachable block
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
     * Mines the two blocks directly in front of the player.
     * Sets correct facing direction.
     */
    public void mineWalkingSpace() {
        movement.setYFacingGoal(40);
        movement.centerOnBlock();
        movement.faceDirectionGoal();
        XYZ targ = (XYZ) reader.data.get("Target Coordinates");
        XYZ curr = (XYZ) reader.data.get("Coordinates");
        if (targ.y >= curr.y) {
            mineBlock();
        }
        targ = (XYZ) reader.data.get("Target Coordinates");
        curr = (XYZ) reader.data.get("Coordinates");
        if (targ.y == curr.y) {
            mineBlock();
        }
    }
    /**
     * Assumes we have a pickaxe in hand and at least one reachable block.
     * No safeguards in place
     */
    public void simpleStripMine(int length) {
        for(int i = 0; i < length; i ++) {
            mineWalkingSpace();
            movement.moveForward(1, false);
        }
    }

    /**
     * Mines a square area with the specified length.
     * Starts in the bottom-left corner.
     * No safeguards in place. Assumes the pickaxe is in hand.
     */
    public void mineSquareArea(int length){
        //^>v>^>V>
        boolean turnRight = true;
        for(int col = 0; col < length; col++) {
            // Mine the length of the column
            simpleStripMine(length-1);
            // Turn, mine, move forward one, and turn again
            if (turnRight) {
                movement.turnRight();
                mineWalkingSpace();
                movement.moveForward(1);
                movement.turnRight();
            } else {
                movement.turnLeft();
                mineWalkingSpace();
                movement.moveForward(1);
                movement.turnLeft();
            }
            turnRight = !turnRight;
        }
    }
}
