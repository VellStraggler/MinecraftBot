package org.mcbot.skills;

import org.mcbot.Utils;
import org.mcbot.wordwork.F3DataReader;
import org.mcbot.datatypes.XYZ;

public class Mining {
    private static final int STRIP_MINE_DEGREE = 32;
    private Movement mvt;
    private F3DataReader reader;

    public Mining(Movement mvt, F3DataReader reader) {
        this.mvt = mvt;
        this.reader = reader;
    }
    public Mining(Movement mvt) {
        this(mvt, mvt.getReader());
    }

    /**
     * Mines the block we are currently facing.
     * This assumes we have both a pickaxe in hand and a reachable block
     */
    public void mineBlock() {
        // record current target coordinates
        mvt.update();
        //wait to catch up a bit
        XYZ currentBlock = (XYZ) reader.data.get("Target Coordinates");
        // cancel if it's too far away
        if (mvt.blockInRange()) {
            //left-click until targeted-block changes
            mvt.holdClick();
            while(currentBlock.equals( (XYZ)reader.data.get("Target Coordinates"))) {
                mvt.update();
            }
            mvt.releaseClick();
            Utils.sleepOneFrame();
        }
    }

    /** Checks if there is a block in the walking space in front
     * of the player.
     * @return
     */
    private boolean blockInFront(XYZ targBlock, XYZ coords) {
        if (Utils.distanceFrom(targBlock.x, coords.x) == 1 ||
            Utils.distanceFrom(targBlock.z, coords.z) == 1) {
            double yDist = targBlock.y - Math.floor(coords.y); // Sometimes you're in the air??
            if (yDist >= 0 && yDist <= 1) {
                return true;
            }
        }
        return false;
    }
    /**
     * Mines the two blocks directly in front of the player.
     * Sets correct facing direction.
     */
    public void mineWalkingSpace() {
        mvt.setYFacingGoal(STRIP_MINE_DEGREE);
        mvt.faceFacingGoal();
        mvt.centerOnBlock(.5);
        XYZ targ = mvt.getTargetCoordinates();
        XYZ curr = mvt.getCoordinates();
        while(blockInFront(targ, curr)) {
            mvt.faceFacingGoal(); // redundancy
            targ = mvt.getTargetCoordinates();
            curr = mvt.getCoordinates();
            if (blockInFront(targ, curr)) {
                mineBlock();
            }
        }
        mvt.centerOnBlock();
    }
    /**
     * Assumes we have a pickaxe in hand and at least one reachable block.
     * No safeguards in place
     */
    public void simpleStripMine(int length) {
        for(int i = 0; i < length; i ++) {
            mineWalkingSpace();
            mvt.moveForward(1,false,.5);
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
        for(int col = 0; col < length-1; col++) {
            // Mine the length of the column
            simpleStripMine(length);
            // Turn, mine, move forward one, and turn again
            if (turnRight) {
                mvt.turnRight();
                mineWalkingSpace();
                mvt.moveForward(1);
                mvt.turnRight();
            } else {
                mvt.turnLeft();
                mineWalkingSpace();
                mvt.moveForward(1);
                mvt.turnLeft();
            }
            turnRight = !turnRight;
        }
        simpleStripMine(length);
    }
}
