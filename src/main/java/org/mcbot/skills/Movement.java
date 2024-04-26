package org.mcbot.skills;

import org.mcbot.wordwork.F3Data;
import org.mcbot.wordwork.F3DataReader;
import org.mcbot.Utils;
import org.mcbot.datatypes.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Stack;

import static org.mcbot.Utils.p;

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
    private static final int FORWARD_KEY = KeyEvent.VK_W;
    private static final int RIGHT_KEY = KeyEvent.VK_D;
    private static final int LEFT_KEY = KeyEvent.VK_A;
    public static final int BACKWARD_KEY = KeyEvent.VK_S;
    public static final int SHIFT_KEY = KeyEvent.VK_SHIFT;
    public static final int INVENTORY_KEY = KeyEvent.VK_E;
    private static final int JUMP_KEY = KeyEvent.VK_SPACE;


    private Blocks blocks;
    private Robot input;
    private int xUp;
    private int xDown;
    private int zUp;
    private int zDown;

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

    /** Extracts what it needs from the given dataset,
     * facing and coordinates. TAKES A SCREENSHOT
     */
    public F3Data update() {
        // wait to prevent compounded movement
        Utils.sleep(10);
        F3Data screenData = reader.readScreen();
        this.coordinates = (XYZ)screenData.get("Coordinates");
        this.facing = (XY)screenData.get("Facing");
        this.direction = Facing.valueOf(((String)screenData.get("Direction")).toUpperCase());
//        Utils.p(screenData.toString());
        return screenData;
    }

    public void turnLeft(){
        p("turn left");
        facingGoal.x = (getGeneralFacingNum() - 90);
        if (facingGoal.x < -180) {
            facingGoal.x = 90;
        }
        faceDirectionGoal();
    }
    public void turnRight(){
        p("turn right");
        facingGoal.x = (getGeneralFacingNum() + 90);
        if (facingGoal.x > 180) {
            facingGoal.x = -90;
        }
        faceDirectionGoal();
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
        moveMouseHere(newX, newY);
    }
    public void turnXPixels(int amount) {
        // x is between 18 and 19
        // y is between 6 and 7
        if (amount < 0) {
            moveMouseHere(18 + amount, 7);
        } else {
            moveMouseHere(19 + amount, 7);
        }
    }
    /** should NOT be used in tangent with turnXPixels **/
    public void turnYPixels(int amount) {
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

        // Set up to turn right if the goal is north and it's on your right
        if (facingGoal.x == -180 && facing.x > 0) {
            facingGoal.x = 180;
        }
        // Else turn left if the goal is north and it's on your left
        if (facingGoal.x == 180 && facing.x < 0) {
            facingGoal.x = -180;
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
        double xDiff = Math.abs(facingGoal.x - facing.x);
        double yDiff = Math.abs(facingGoal.y - facing.y);
        if (xDiff > 15) x *= 4;
        if (yDiff > 15) y *= 3;
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
        moveForward(amount, true);
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
        moveForward(amount, jumpingAllowed, true);
    }
    /**
     * Aligns where player is looking and standing,
     * and then moves the given amount forward.
     * Does not account for y-level, and will jump if need be
     * @param amount
     */
    public void moveForward(int amount, boolean jumpingAllowed, boolean withCentering) {
        p("move forward " + amount);
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
        centerOnBlock();
        coordinatesGoal.x = coordinates.x + xChange;
        coordinatesGoal.z = coordinates.z + zChange;
        moveToGoal();
        if(withCentering) {
            centerOnBlock(); // won't matter if we do it twice
        }
    }

    /**
     * Walks forward until a goal is reached.
     * Jumps if needed.
     */
    private void moveToGoal() {
        moveForward();
        while (!coordinateReached()) {
            update();
            if (shouldJumpThere()) jump();
        }
        releaseAllKeys();
    }
    public void faceDirectionGoal() {
        while(!closeEnough()) {
            correction();
            update();
        }
    }
    public void jump() {
        pressAndReleaseKey(JUMP_KEY);
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
        double factor = 2;
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
    /** Holds shift and gets centered with 80% accuracy **/
    public void centerOnBlock() {
        pressKey(SHIFT_KEY);
        double rangeOfMiddle = .1;
        // Set the goal to the center of the block
        // -100.9 -> -100.5   100.2 -> 100.5
        // -100.1 -> -100.5   100.8 -> 100.5
        coordinatesGoal.x = Math.ceil(coordinates.x) - .5;
        coordinatesGoal.y = Math.floor(coordinates.y);
        coordinatesGoal.z = Math.ceil(coordinates.z) - .5;
        Utils.p(coordinates.toString() + " GOAL: " + coordinatesGoal.toString());
        setDirectionalMovementFromFacing();
        long start = System.currentTimeMillis();
        while(!coordinateReached(rangeOfMiddle)) {
            releaseAllKeys();
            pressKey(SHIFT_KEY);
            // If centering takes too long, jump and try again.
            if (System.currentTimeMillis() > start + 2000L) {
                pressKey(JUMP_KEY);
                Utils.sleep(25);
                start += 2000L;
            }
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
        Utils.sleep(100);

    }
    /** Simply press the forward key if it
     * hasn't already been pressed**/
    private void moveForward() {
        pressKey(FORWARD_KEY);
    }

    /**
     * Presses a key and adds it to the keyStack
     * if it isn't already there.
     */
    public void pressKey(int keyValue) {
        if(!keyStack.contains(keyValue)) {
            input.keyPress(keyValue);
            keyStack.add(keyValue);
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
    private void releaseAllKeys() {
        while(!keyStack.isEmpty()) {
            releaseKey(keyStack.pop());
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
        Utils.sleep(30);
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
        Utils.sleep(30);
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
        Utils.sleep(20);
        input.keyRelease(inputEvent);
    }
    public void releaseKey(int inputEvent){
        if (keyStack.contains(inputEvent)) {
            keyStack.remove(inputEvent);
        }
        input.keyRelease(inputEvent);
    }

}
