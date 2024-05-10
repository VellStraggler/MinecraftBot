package org.mcbot.skills;

import org.mcbot.wordwork.F3Data;
import org.mcbot.wordwork.F3DataReader;
import org.mcbot.Utils;
import org.mcbot.datatypes.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Stack;

/**
 * Takes rotation values 'facing' and base coordinates
 * to discover the orientation of the player. Requires
 * a new image after each use.
 */
public class Movement {
    //            North: -Z 180/-180
    //                   |
    // West: -X 90 ------+------ East: X -90
    //                   |
    //               South: Z 0
    public static XY SCREEN_CENTER = new XY(Utils.SCREEN_RESOLUTION.x/2, Utils.SCREEN_RESOLUTION.y/2);
    public static final int FORWARD_KEY = KeyEvent.VK_W;
    public static final int RIGHT_KEY = KeyEvent.VK_D;
    public static final int LEFT_KEY = KeyEvent.VK_A;
    public static final int BACKWARD_KEY = KeyEvent.VK_S;
    public static final int SHIFT_KEY = KeyEvent.VK_SHIFT;
    public static final int INVENTORY_KEY = KeyEvent.VK_E;
    public static final int JUMP_KEY = KeyEvent.VK_SPACE;

    private static final double DEFAULT_ACCURACY = 0.8;


    private Blocks blocks;
    private Robot input;
    private int xUp;
    private int xDown;
    private int zUp;
    private int zDown;

    private Stack<Integer> keyStack;
    private XY facing;
    private XYZ coordinates;
    private XYZ targetCoordinates;
    private Facing direction;

    private XY facingGoal;
    // facingGoal.y is widely ignored rn
    private XYZ coordinatesGoal;
    private F3DataReader reader;

    private boolean jumpingAllowed = false;

    /** Sets movement sensitivity to 1, and accuracy to 0.
     * Both of these will slowly be increased until the quickest,
     * most accurate movement tick is found.
     */
    public Movement(Blocks blocks, F3DataReader dataReader) {
        F3Data screenData = dataReader.data;
        if(screenData != null) {
            this.coordinates = (XYZ) screenData.get("Coordinates");
            this.facing = (XY) screenData.get("Facing");
            String compassDirection = ((String) screenData.get("Direction"));
            if (compassDirection != null) {
                compassDirection = compassDirection.toUpperCase();
                this.direction = Facing.valueOf(compassDirection);
            }

            this.coordinatesGoal = new XYZ(coordinates);
            if (facing != null) {
                this.facingGoal = new XY(facing.x, 55);
            }
        }
        this.keyStack = new Stack<>();
        this.reader = dataReader;
        this.blocks = blocks;

        try {
            this.input = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Keyboard input not working for some reason. " + e.getMessage());
        }
    }
    public Blocks getBlocks() { return blocks; }
    public F3DataReader getReader() {
        return reader;
    }

    /** Takes a screenshot, and then extracts what it needs from the
     *  given dataset, facing and coordinates.
     */
    public F3Data update() {
        // wait to prevent compounded movement
        Utils.sleepOneFrame();
        F3Data screenData = reader.readScreen();
        this.coordinates = (XYZ)screenData.get("Coordinates");
        this.facing = (XY)screenData.get("Facing");
        this.direction = Facing.valueOf(((String)screenData.get("Direction")).toUpperCase());
        this.targetCoordinates = (XYZ)screenData.get("Target Coordinates");
        return screenData;
    }

    public void turnLeft(){
        facingGoal.x = (getGeneralFacingNum() - 90);
        if (facingGoal.x < -180) {
            facingGoal.x = 90;
        }
        faceDirectionGoal();
    }
    public void turnRight(){
        facingGoal.x = (getGeneralFacingNum() + 90);
        if (facingGoal.x > 180) {
            facingGoal.x = -90;
        }
        faceDirectionGoal();
    }
    public void turnAround() {
        turnRight();
        turnRight(); // ;D
    }
    private void turnPixels(int x, int y) {
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
        moveMouseHere(newX, newY);
    }
    private void turnXPixels(int amount) {
        // x is between 18 and 19
        // y is between 6 and 7
        if (amount < 0) {
            moveMouseHere(18 + amount, 7);
        } else {
            moveMouseHere(19 + amount, 7);
        }
    }
    /** should NOT be used in tangent with turnXPixels **/
    private void turnYPixels(int amount) {
        // x is between 18 and 19
        // y is between 6 and 7
        if (amount < 0) {
            moveMouseHere(19, 6 + amount);
        } else {
            moveMouseHere(19, 7 + amount);
        }
    }
    private void correction() {
        int x = 0; int y = 0;
        // Set up to turn right if the goal is NORTH and it's on your right
        if (facingGoal.x == -180 && facing.x > 0) facingGoal.x = 180;
        // Else turn left if the goal is NORTH and it's on your left
        else if (facingGoal.x == 180 && facing.x < 0) facingGoal.x = -180;

        double xDiff = Math.abs(facingGoal.x - facing.x);
        double yDiff = Math.abs(facingGoal.y - facing.y);

        if(facingGoal.x < facing.x) x = -1;
        else if (facingGoal.x > facing.x) x = 1;
        if(xDiff > 180) x *= -1;

        if(facingGoal.y < facing.y) y = -1;
        else if (facingGoal.y > facing.y) y = 1;

        // speed multiplier
        boolean negX = x < 0;
        x = (int)Math.min(Math.floor(.045*(xDiff*xDiff)+1),22);
        if(negX) x *= -1;
        if (yDiff > 5) y *= 2;
        if (y == -1) y = -2;

        if (x == 0 && y != 0) {
            turnYPixels(y);
        } else if (y == 0 && x != 0) {
            turnXPixels(x);
        } else if (y != 0) {
            turnPixels(x, y);
        }
    }

    /** Useful y-rotations -
     * 40: strip-mining
     * 81: bridging
     * 55: This points to the 3 blocks in the spot in front of you
     * @param y
     */
    public void setYFacingGoal(int y) {
        facingGoal.y = y;
    }

    public void moveForward(int amount) {
        moveForward(amount, false);
    }

    public void pathFinding(XYZ goal) {
        // get the z change
        double zChange = goal.z - coordinates.z;
        if (zChange < 0) {

        }
        // determine north or south
        // get the x change
        // determine east or west
    }
    public void destructivePathFinding(XYZ goal) {

    }

    /** Snaps the camera up with frightening speed. */
    public void lookUp() {
        // We know it will immediately hit the top after 80 runs
        // without screenshots
        int i = 0;
        while (i < 80) {
            turnYPixels(-6);
            i++;
        }
    }
    /** Snaps camera down with frightening speed. */
    public void lookDown() {
        turnYPixels(300);
    }
    public void moveForward(int amount, boolean jumpingAllowed) {
        moveForward(amount, jumpingAllowed, DEFAULT_ACCURACY);
    }
    /**
     * Aligns where player is looking and standing,
     * and then moves the given amount forward.
     * Does not account for y-level, and will jump if need be
     * @param amount
     */
    public void moveForward(int amount, boolean jumpingAllowed, double centeringAccuracy) {
        this.jumpingAllowed = jumpingAllowed;
        // Set goal
        int xChange = 0;
        int zChange = 0;
        switch(direction) {
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

        faceDirectionGoal();
        // Stay on target!
        centerOnBlock(centeringAccuracy);
        coordinatesGoal.x = coordinates.x + xChange;
        coordinatesGoal.z = coordinates.z + zChange;
        moveToGoal();
        centerOnBlock(centeringAccuracy); // won't matter if we do it twice
    }
    /**
     * Walks forward until a goal is reached.
     * Jumps if needed.
     */
    private void moveToGoal() {
        moveForward();
        while (!coordinateReached()) {
            update();
            if (this.jumpingAllowed && shouldJumpThere()) jump();
        }
        releaseAllKeys();
        this.jumpingAllowed = false;
    }
    public void faceDirectionGoal() {
        update();
        while(!closeEnough()) {
            correction();
            update();
        }
        Utils.sleepOneFrame(); //redundancy
        correction(); //redundancy
    }
    public XYZ getTargetCoordinates() {
        return ((XYZ)reader.data.get("Target Coordinates"));
    }
    public XYZ getCoordinates() { return (XYZ)reader.data.get("Coordinates");}
    /** Only applies to something on the same plane as the player **/
    public void moveToWhereLooking() {
        // Take the x and z of the block before where you're looking at
        update();
        setCoordinatesGoal(getTargetCoordinates());
        XYZ curr = getSimplifiedCoordinates(coordinates);
        // at this point, both the goal and where we are are simplified
        if (coordinatesGoal.y >= curr.y) {
            //one block closer
            if(curr.x == coordinatesGoal.x) {
                coordinatesGoal.z += (curr.z - coordinatesGoal.z) / Math.abs(curr.z - coordinatesGoal.z);
            }
            else {
                coordinatesGoal.x += (curr.x - coordinatesGoal.x) / Math.abs(curr.x - coordinatesGoal.x);
            }
        }
        coordinatesGoal.y = curr.y;
        moveToGoal();
    }
    public void jump() {
        pressAndReleaseKey(JUMP_KEY);
    }
    private boolean shouldJumpThere() {
        double y = getTargetCoordinates().y;
        // same y actually means there is a block to jump
        boolean facingBreathable = blocks.get((String)reader.data.get("Target Block")).breathable;
        double yDist = y - Math.floor(coordinates.y);
        if (yDist >= 0 && yDist < 1) {
            return !facingBreathable;
        } else if (yDist >= 1 && yDist < 2) {
            return facingBreathable;
        }
        return false;
    }
    private boolean xCloseEnough() {
        double factor = 3;
        if (facingGoal.x == -180) {
            if (Math.abs(180 - facing.x) < factor) return true;
        }
        return (Math.abs(facingGoal.x - facing.x) < factor);
    }
    private boolean yCloseEnough() {
        double factor = 3;
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
    private void setDirectionalMovementFromFacing() {
        // how the coordinates will change if you go forward or right
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
    }
    /** Simplifies to the exact bottom center of a block
     * that holds the given coordinates */
    public void setCoordinatesGoal(XYZ newGoal) {
        coordinatesGoal = getSimplifiedCoordinates(newGoal);
    }

    /** Simplifies to the exact bottom center of a block
     * that holds the given coordinates */
    public void setCoordinatesGoal(double x, double y, double z) {
        setCoordinatesGoal(new XYZ(x,y,z));
    }
    /** adds .01 on account of if you are given an integer amount,
     * such as from a target block. **/
    public XYZ getSimplifiedCoordinates(XYZ coordinate) {
        return new XYZ(
            Math.ceil(coordinate.x + .01) - .5,
               Math.floor(coordinate.y),
            Math.ceil(coordinate.z + .01) - .5);
    }
    public XYZ getSimplifiedCoordinates() {
        return getSimplifiedCoordinates(getCoordinates());
    }
    public void centerOnBlock() {
        centerOnBlock(.8);
    }
    /** Holds shift and gets centered with a certain amount of accuracy.
     * Centers on the coordinatesGoal, not the current block **/
    public void centerOnBlock(double accuracy) {
        pressKey(SHIFT_KEY);
        double rangeOfMiddle = (1 - accuracy)/2;
        //setCoordinatesGoal(coordinatesGoal);
        setCoordinatesGoal(getSimplifiedCoordinates());
        setDirectionalMovementFromFacing();
        long start = System.currentTimeMillis();
        while(!coordinateReached(rangeOfMiddle)) {
            releaseAllKeys();
            pressKey(SHIFT_KEY);
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

    }
    /** Simply press the forward key if it
     * hasn't already been pressed**/
    private void moveForward() {
        pressKey(FORWARD_KEY);
        Utils.sleepOneFrame();
    }

    /**
     * Presses a key and adds it to the keyStack
     * if it isn't already there.
     */
    public void pressKey(int keyValue) {
        Utils.p("Pressing " + keyValue);
        if(!keyStack.contains(keyValue)) {
            Utils.p(" pressing successful");
            input.keyPress(keyValue);
            keyStack.add(keyValue);
            Utils.p(" current stack: " + keyStack.toString());
        }
        else {
            Utils.p(" pressing failed");
        }
    }

    /**
     * Releases the latest key found in the keyStack
     */
    private void releaseLatestKey() {
        if (!keyStack.isEmpty()) {
            releaseKey(keyStack.pop());
        }
    }

    /**
     * Releases all keys stored to the keyStack
     */
    public void releaseAllKeys() {
        Utils.p("releasing ALL\n current stack: " + keyStack.toString());
        while(!keyStack.isEmpty()) {
            releaseKey(keyStack.pop());
            Utils.sleepOneFrame();
        }
        input.keyRelease(BACKWARD_KEY);
        input.keyRelease(SHIFT_KEY);
        input.keyRelease(FORWARD_KEY);
        input.keyRelease(LEFT_KEY);
        input.keyRelease(RIGHT_KEY);
        input.keyRelease(JUMP_KEY);
        Utils.sleepOneFrame();
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

    /**
     * Move the mouse to a particular point on screen.
     * @param x : pixel x coordinate on screen
     * @param y : pixel y coordinate on screen
     */
    public void clickHere(int x, int y) {
        moveMouseHere(x,y);
        clickHere();
    }
    public void clickHere() {
        holdClick();
        Utils.sleepTwoFrames();
        releaseClick();
    }
    public void holdClick() {
        input.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    }
    public void releaseClick() {
        input.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
    public void rightClickHere() {
        input.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        Utils.sleepOneFrame();
        input.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }
    public void clickHere(XY xy) {
        clickHere((int)xy.x, (int)xy.y);
    }
    public void moveMouseHere(int x, int y) {
        input.mouseMove(x,y);
    }
    public void moveMouseHere(XY xy) { moveMouseHere((int)xy.x, (int)xy.y); }

    public void pressAndReleaseKey(int inputEvent) {
        input.keyPress(inputEvent);
        Utils.sleepTwoFrames();
        input.keyRelease(inputEvent);
    }
    public void releaseKey(int inputEvent){
        Utils.p("Removing " + inputEvent);
        Utils.p(" current stack: " + keyStack.toString());
        if (keyStack.contains(inputEvent)) {
            Utils.p(" Removing " + inputEvent);
            keyStack.removeElement(inputEvent);
            input.keyRelease(inputEvent);
            Utils.sleepTwoFrames();
        } else {
            Utils.p(" Removal FAILED");
        }
    }

}
