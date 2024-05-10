package org.mcbot.skills;

import org.mcbot.wordwork.F3DataReader;
import org.mcbot.ImageWork;
import org.mcbot.Utils;
import org.mcbot.datatypes.XYZ;

public class Building {
    private static final int BRIDGING_DEGREE = 81;
    private Movement mvt;
    private F3DataReader reader;

    public Building(Movement mvt, F3DataReader reader) {
        this.mvt = mvt;
        this.reader = reader;
    }
    public Building(Movement mvt) {
        this(mvt, mvt.getReader());
    }

    /**
     * Assumes you are on a block-slot and that you have enough
     * blocks to get to the other side.
     * You start out facing the precipice.
     */
    public void bridge(int amt, boolean fast) {
        //setup
        mvt.setYFacingGoal(BRIDGING_DEGREE);
//        mvt.turnAround();
        mvt.centerOnBlock();
        mvt.faceDirectionGoal();

        if(!fast) {
            mvt.pressKey(Movement.SHIFT_KEY);
        } else {
            mvt.pressKey(Movement.JUMP_KEY);
        }
        mvt.pressKey(Movement.BACKWARD_KEY);
        int placed = 0;
        XYZ startCoords = mvt.getSimplifiedCoordinates();
        XYZ newCoords = mvt.getSimplifiedCoordinates();
        while(placed <= amt) {
            mvt.rightClickHere();
            mvt.update();
            newCoords = mvt.getSimplifiedCoordinates();
            if (!newCoords.equals(startCoords)) {
                placed++;
                startCoords = mvt.getSimplifiedCoordinates();
            }
        }
        mvt.update(); // redundancy
        mvt.releaseKey(Movement.BACKWARD_KEY);
        if(fast) {
            for(int i = 0; i < 10; i++) {
                mvt.rightClickHere();
            }
        }
        mvt.update();
    }
    /** It is assumed that you are already falling
     *  and have a bucket in-hand. **/
    public void mlg() {
        //setup
        mvt.lookDown();
        while(mvt.getTargetCoordinates() == null
           || mvt.getTargetCoordinates().y < mvt.getCoordinates().y - 10) {
            mvt.update();
        }
        // This relies on the top-left pixel of "Bucket", which shows up above the toolbar
        // once the water is released
        while(!Utils.isWhite(ImageWork.takeScreenshot(), 751, 724)) {
            mvt.rightClickHere();
        }
    }
}
