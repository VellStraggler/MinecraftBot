package org.mcbot;

import org.mcbot.datatypes.*;

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
    private static final int RIGHT_KEY = KeyEvent.VK_D;
    private static final int LEFT_KEY = KeyEvent.VK_A;
    private static final int BACKWARD_KEY = KeyEvent.VK_S;
    private static final int SHIFT_KEY = KeyEvent.VK_SHIFT;
    private static final int JUMP_KEY = KeyEvent.VK_SPACE;
    private Blocks blocks;

    private Stack<Integer> keyStack;
    private XY facing;
    private XYZ coordinates;
    private Facing direction;

    private XY facingGoal;
    // facingGoal.y is widely ignored rn
    private XYZ coordinatesGoal;
    private F3DataReader reader;

    /** Sets movement sensitivity to 1, and accuracy to 0.
     * Both of these will slowly be increased until the quickest,
     * most accurate movement tick is found.
     */
    public Movement(F3DataReader dataReader, Blocks blocks) {
        F3Data screenData = dataReader.data;
        this.coordinates = (XYZ)screenData.get("Coordinates");
        this.facing = (XY)screenData.get("Facing");
        this.direction = Facing.valueOf(((String)screenData.get("Direction")).toUpperCase());

        this.coordinatesGoal = new XYZ(coordinates);
        this.facingGoal = new XY(facing.x, 55);
        this.keyStack = new Stack<>();
        this.reader = dataReader;
        this.blocks = blocks;
    }

    /** Extracts what it needs from the given dataset,
     * facing and coordinates. Takes a screenshot on its OWN.
     */
    public void update() {
        Utils.sleep(10);
        // This wait allows movements to not compound
        F3Data screenData = reader.readScreen();
        this.coordinates = (XYZ)screenData.get("Coordinates");
        this.facing = (XY)screenData.get("Facing");
        this.direction = Facing.valueOf(((String)screenData.get("Direction")).toUpperCase());
    }

    public void turnLeft(){
        facingGoal.x = (getGeneralFacingNum() - 90);
        if (facingGoal.x < -180) {
            facingGoal.x = 90;
        }
        while(!closeEnough()) {
            correction();
            update();
        }
    }
    public void turnRight(){
        facingGoal.x = (getGeneralFacingNum() + 90);
        if (facingGoal.x > 180) {
            facingGoal.x = -90;
        }
        while(!closeEnough()) {
            correction();
            update();
        }
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
        int x = 0; int y = 0;

        // Set up to turn right if the goal is north and it's on your right
        if (facingGoal.x == -180 && facing.x > 0) {
            facingGoal.x = 180;
        }

        if(facingGoal.x < facing.x) {
            x = -1;
        } else if (facingGoal.x > facing.x) {
            x = 1;
        } if(facingGoal.y < facing.y) {
            y = -1;
        } else if (facingGoal.y > facing.y) {
            y = 1;
        }
//        double xMult = Math.max(Math.abs(facingGoal.x - facing.x)/45, 1);
//        double yMult = Math.max(Math.abs(facingGoal.y - facing.y)/5, 1);
//        x *= (int)(Math.pow(xMult,2));
//        y *= (int)(Math.pow(yMult,2));
        double xDiff = Math.abs(facingGoal.x - facing.x);
        double yDiff = Math.abs(facingGoal.y - facing.y);
        if (xDiff > 15) x *= 4;
        if (yDiff > 15) y *= 3;
        if (x == 0 && y != 0) {
            turnYPixels(y);
        } else if (y == 0 && x != 0) {
            turnXPixels(x);
        } else if (y != 0 && x != 0 ) {
            turnPixels(x, y);
        }
    }
    public void setYFacing(int y) {
        facingGoal.y = y;
    }

    /**
     * aligns where player is looking and standing,
     * and then moves the given amount forward.
     * Does not account for y-level, and will jump if need be
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
        facingGoal.x = getGeneralFacingNum();
        //facingGoal.y = 55; //This points to the 3 blocks directly in front of you

        // Align with a compass direction
        while(!closeEnough()) {
            correction();
            update();
        }
        // Align to center of block
        centerOnBlock();
        // Stay on target!
        coordinatesGoal.x = coordinates.x + xChange;
        coordinatesGoal.z = coordinates.z + zChange;

        // Move to goal
        moveForward();
        while (!coordinateReached()) {
            update();
            if (shouldJumpThere()) jump();
        }
        releaseAllKeys();
        centerOnBlock(); // won't matter if we do it twice

    }
    private void jump() {
        Utils.pressAndReleaseKey(JUMP_KEY);
    }
    private boolean shouldJumpThere() {
        double y = ((XYZ)(reader.data.get("Target Coordinates"))).y;
        // same y actually means there is a block to jump
        boolean facingBreathable = blocks.get((String)reader.data.get("Target Block")).breathable;
        if (y == (int)(coordinates.y + .1)) {
            return !facingBreathable;
        } else if (y == (int)(coordinates.y + 1.1)) {
            return facingBreathable;
        }
        return false;
    }
    private boolean xCloseEnough() {
        double factor = 1;
        if (facingGoal.x == -180) {
            if (Math.abs(180 - facing.x) < factor) {
                return true;
            }
        }
        return (Math.abs(facingGoal.x - facing.x) < factor);
    }
    private boolean yCloseEnough() {
        double factor = 1;
        return (Math.abs(facingGoal.y - facing.y) < factor);
    }
    public boolean closeEnough() {
        return xCloseEnough() && yCloseEnough();
    }
    // The default accuracy is within .5
    // Ignores y-level
    private boolean coordinateReached() {
        return coordinateReached(.5);
    }
    private boolean coordinateReached(double factor) {
        if (Math.abs(coordinatesGoal.x - coordinates.x) < factor &&
            Math.abs(coordinatesGoal.z - coordinates.z) < factor) {
            return true;
        } else {
            return coordinates.equals(coordinatesGoal);
        }
    }
    public void centerOnBlock() {
        // hold shift and get mostly centered
        Utils.pressKey(SHIFT_KEY);
        keyStack.add(SHIFT_KEY);
        double rangeOfMiddle = .2;
        // Set the goal
        coordinatesGoal.x = ((int)coordinates.x) - .5;
        coordinatesGoal.y = coordinates.y;
        coordinatesGoal.z = ((int)coordinates.z) - .5;
        // how will the coordinates change if you go forward or right
        int xUp; int xDown;
        int zUp; int zDown;
        switch (direction) {
            case NORTH:
                zDown = FORWARD_KEY;
                zUp = BACKWARD_KEY;
                xDown = LEFT_KEY;
                xUp = RIGHT_KEY;
                break;
            case EAST:
                zDown = LEFT_KEY;
                zUp = RIGHT_KEY;
                xDown = BACKWARD_KEY;
                xUp = FORWARD_KEY;
                break;
            case SOUTH:
                zUp = FORWARD_KEY;
                zDown = BACKWARD_KEY;
                xUp = LEFT_KEY;
                xDown = RIGHT_KEY;
                break;
            default: // WEST
                zUp = LEFT_KEY;
                zDown = RIGHT_KEY;
                xUp = BACKWARD_KEY;
                xDown = FORWARD_KEY;
                break;
        }
        long start = System.currentTimeMillis();
        while(!coordinateReached(rangeOfMiddle)) {
            releaseAllKeys();
            pressKey(SHIFT_KEY);
            if (System.currentTimeMillis() > start + 1000L) {
                pressKey(JUMP_KEY);
                Utils.sleep(25);
                start += 2000L;
            };
            // I don't know why xDown and xUp are switched, really
            if (coordinatesGoal.x > coordinates.x + rangeOfMiddle)
                pressKey(xUp);
            else if (coordinatesGoal.x < coordinates.x - rangeOfMiddle)
                pressKey(xDown);
            if (coordinatesGoal.z > coordinates.z + rangeOfMiddle)
                pressKey(zUp);
            else if (coordinatesGoal.z < coordinates.z - rangeOfMiddle)
                pressKey(zDown);
            update();
        }
        releaseAllKeys();
        Utils.sleep(50);

    }
    /** Simply press the forward key if it
     * hasn't already been pressed**/
    private void moveForward() {
        pressKey(FORWARD_KEY);
    }
    private void pressKey(int keyValue) {
        if(!keyStack.contains(keyValue)) {
            Utils.pressKey(keyValue);
            keyStack.add(keyValue);
        }
    }
    private void releaseLatestKey() {
        if (!keyStack.isEmpty()) {
            Utils.releaseKey(keyStack.pop());
        }
    }
    private void releaseAllKeys() {
        while(!keyStack.isEmpty()) {
            Utils.releaseKey(keyStack.pop());
        }
    }

    /**
     * gives the general direction the player is facing
     */
    public Facing getFacing() {
        return direction;
    }
    /**
     * Gives the rotational number of the general direction
     * the player is facing. -180 to 90
     */
    public int getGeneralFacingNum() {
        if (facing.x < 0) {
            return (int)((facing.x - 45) / 90) * 90;
        } else {
            return (int)((facing.x + 45) / 90) * 90;
        }
    }

}
