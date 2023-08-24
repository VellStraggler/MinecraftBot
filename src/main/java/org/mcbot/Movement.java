package org.mcbot;

import org.mcbot.datatypes.Facing;
import org.mcbot.datatypes.XY;
import org.mcbot.datatypes.XYZ;

import java.awt.event.KeyEvent;
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
    private XY facing;
    private XYZ coordinates;
    private String direction;

    private XY facingGoal;
    private XYZ coordinatesGoal;

    /** Sets movement sensitivity to 1, and accuracy to 0.
     * Both of these will slowly be increased until the quickest,
     * most accurate movement tick is found.
     */
    public Movement(F3Data screenData) {
        this.coordinates = (XYZ)screenData.get("Coordinates");
        this.facing = (XY)screenData.get("Facing");
        this.direction = (String)screenData.get("Direction");

        this.coordinatesGoal = new XYZ(coordinates);
        this.facingGoal = new XY(facing);
        this.sensitivity = 1.0;
        this.sensitivityAccuracy = 0.0;
        this.keyStack = new Stack<>();
    }

    /** Extracts what it needs from the given dataset,
     * facing and coordinates.
     */
    public void update(F3Data screenData) {
        this.coordinates = (XYZ)screenData.get("Coordinates");
        this.facing = (XY)screenData.get("Facing");
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
        if(facingGoal.x < facing.x) {
            x = -1;
        } else if (facingGoal.x > facing.x) {
            x = 1;
        }
        if(facingGoal.y < facing.y) {
            y = -1;
        } else if (facingGoal.y > facing.y) {
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
        facingGoal.x = getGeneralFacingNum();
        facingGoal.y = 90;

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
            update(reader.readScreen());
        }
        releaseAllKeys();

    }
    public boolean closeEnough() {
        double factor = .5;
        if (facingGoal.x == -180) {
            if (Math.abs(180 - facing.x) <= factor) {
                return true;
            }
        }
        return (Math.abs(facingGoal.x - facing.x) <= factor);
    }
    private boolean coordinateReached() {
        if (Math.abs(coordinatesGoal.x - coordinates.x) < .5 &&
            Math.abs(coordinatesGoal.y - coordinates.y) < .5 &&
            Math.abs(coordinatesGoal.z - coordinates.z) < .5) {
            return true;
        } else {
            return coordinates.equals(coordinatesGoal);
        }
    }
    public void centerOnBlock() {
        // hold shift and get enough in the center of this coordinate to
        // fit through a corridor.
        // TODO
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
        return Facing.valueOf(direction.toUpperCase());
    }
    /**
     * Gives the rotational number of the general direction
     * the player is facing.
     */
    public int getGeneralFacingNum() {
        if (facing.x < 0) {
            return (int)((facing.x - 45) / 90) * 90;
        } else {
            return (int)((facing.x + 45) / 90) * 90;
        }
    }

}
