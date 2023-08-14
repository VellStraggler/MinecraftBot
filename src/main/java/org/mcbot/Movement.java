package org.mcbot;

import org.mcbot.datatypes.Facing;
import org.mcbot.datatypes.XY;
import org.mcbot.datatypes.XYZ;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Takes rotation values 'facing' and base coordinates
 * to discover the orientation of the player. Requires
 * a new image after each use.
 */
public class Movement {
    public static XY SCREEN_CENTER = new XY(Utils.SCREEN_RESOLUTION.x/2, Utils.SCREEN_RESOLUTION.y/2);
    private static final int FORWARD_KEY = KeyEvent.VK_W;


    private double sensitivity;
    private double sensitivityAccuracy;
    private Stack<Integer> keyStack;
    private XY facingNums;
    private XYZ coordinates;

    private XY facingNumsGoal;
    private XYZ coordinatesGoal;

    /** Sets movement sensitivity to 1, and accuracy to 0.
     * Both of these will slowly be increased until the quickest,
     * most accurate movement tick is found.
     * @param coordinates
     * @param facingNums
     */
    public Movement(XYZ coordinates, XY facingNums) {
        this.coordinates = coordinates;
        this.facingNums = facingNums;
        this.coordinatesGoal = new XYZ(coordinates);
        this.facingNumsGoal = new XY(facingNums);
        this.sensitivity = 1.0;
        this.sensitivityAccuracy = 0.0;
        this.keyStack = new Stack<>();
    }

    /** Extracts what it needs from the given dataset,
     * facing and coordinates.
     * @param data
     */
    public void update(F3Data data) {
        this.coordinates = data.coordinates;
        this.facingNums = data.facing;
    }

    public boolean turnLeft(){
        return true;
    }
    public boolean turnRight(){
        return true;
    }
    public void turnPixels(int x, int y) {
        int newX; int newY;
        if (x < 0) {
            newX = 18 + x;
        } else {
            newX = 19 + x;
        }
        if (y < 0) {
            newY = 6;
        } else {
            newY = 7;
        }
        Utils.moveMouseHere(newX, newY);
    }
    public void turnXPixels(int amount) {
        // x is between 18 and 19
        // y is between 6 and 7
        if (amount < 0) {
            Utils.moveMouseHere(18 + amount, 7);
        } else {
            Utils.moveMouseHere(19 + amount, 7);
        }
    }
    /** should NOT be used in tangent with turnXPixels **/
    public void turnYPixels(int amount) {
        // x is between 18 and 19
        // y is between 6 and 7
        if (amount < 0) {
            Utils.moveMouseHere(19, 6 + amount);
        } else {
            Utils.moveMouseHere(19, 7 + amount);
        }
    }
    private void correction() {
        int x = 0;
        int y = 0;
        if(facingNumsGoal.x < facingNums.x) {
            x = -1;
        } else if (facingNumsGoal.x > facingNums.x) {
            x = 1;
        }
        if(facingNumsGoal.y < facingNums.y) {
            y = -1;
        } else if (facingNumsGoal.y > facingNums.y) {
            y = 1;
        }
        if (x == 0 && y != 0) {
            turnYPixels(y);
        } else if (y == 0 && x != 0) {
            turnXPixels(x);
        } else if (y != 0 && x != 0 ) {
            turnPixels(x, y);
        }
    }

    /**
     * Initializes moving forward.
     * @param amount
     */
    public void moveForward(int amount) {
        // Set goal
        int xChange = 0;
        int zChange = 0;
        switch(getFacing()) {
            case SOUTH:
                zChange = amount;
                break;
            case EAST:
                xChange = amount;
                break;
            case WEST:
                xChange = -amount;
                break;
            default: //NORTH
                zChange = -amount;
        }
        coordinatesGoal.x = coordinates.x + xChange;
        coordinatesGoal.z = coordinates.z + zChange;
        facingNumsGoal.x = getGeneralFacingNum();
        facingNumsGoal.y = 90;

        // Align with a compass direction
        // SLOWDOWN: This will slow down this Thread
        F3DataReader reader = new F3DataReader();
        int i = 0;
        while(!closeEnough()) {
            correction();
            update(reader.readScreen());
        }

        // Move to goal
        moveForward();
        while (!coordinateReached()) {
            correction();
            update(reader.readScreen());
        }
        releaseAllKeys();

    }
    public boolean closeEnough() {
        int factor = 15;
        if (facingNumsGoal.x == -1800) {
            if (Math.abs(1800 - facingNums.x) <= factor) {
                return true;
            }
        }
        return (Math.abs(facingNumsGoal.x - facingNums.x) <= factor);
    }
    private boolean coordinateReached() {
        return coordinates.equals(coordinatesGoal);
    }
    /** Simply press the forward key if it
     * hasn't already been pressed**/
    private void moveForward() {
        if(!keyStack.contains(FORWARD_KEY)) {
            Utils.pressKey(FORWARD_KEY);
            keyStack.add(FORWARD_KEY);
        }
    }
    private void releaseAllKeys() {
        while(!keyStack.isEmpty()) {
            Utils.releaseKey(keyStack.pop());
        }
    }

    /**
     * gives the general direction the player is facing
     * based on rotation numbers (facingNums).
     * @return
     */
    public Facing getFacing() {
        if(facingNums.x > 1350 || facingNums.x < -1351) {
            return Facing.NORTH;
        } else if (facingNums.x > 450) {
            return Facing.WEST;
        } else if (facingNums.x > -451) {
            return Facing.SOUTH;
        } else {
            return Facing.EAST;
        }
    }
    /**
     * Gives the rotational number of the general direction
     * the player is facing.
     */
    public int getGeneralFacingNum() {
        if (facingNums.x < 0) {
            return ((facingNums.x - 450) / 900) * 900;
        } else {
            return ((facingNums.x + 450) / 900) * 900;
        }
    }

}
