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

    /** Sets movement sensitivity to 1, and accuracy to 0.
     * Both of these will slowly be increased until the quickest,
     * most accurate movement tick is found.
     * @param coordinates
     * @param facing
     */
    public Movement(XYZ coordinates, XY facing) {
        this.coordinates = coordinates;
        this.facing = facing;
        this.sensitivity = 1.0;
        this.sensitivityAccuracy = 0.0;
        this.keyStack = new Stack<>();
    }

    /** Extracts what it needs from the given dataset,
     * facing and coordinates.
     * @param data
     */
    public void update(F3Data data) {
        this.coordinates = coordinates;
        this.facing = facing;
    }

    public boolean turnLeft(){
        return true;
    }
    public boolean turnRight(){
        return true;
    }
    public boolean turn(int amount) {
        return true;
    }
    private boolean correction() {
        return true;
    }
    /** Simply pressed the forward key**/
    public boolean moveForward() {
        if(!keyStack.contains(FORWARD_KEY)) {
            Utils.pressKey(FORWARD_KEY);
            keyStack.add(FORWARD_KEY);
        }
        return false;
    }
    public void releaseAllKeys() {
        while(!keyStack.isEmpty()) {
            Utils.releaseKey(keyStack.pop());
        }
    }

    /**
     * gives the general direction the player is facing.
     * @return
     */
    public Facing getFacing() {
        return Facing.NORTH;
    }

}
