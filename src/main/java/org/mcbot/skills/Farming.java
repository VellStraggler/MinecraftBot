package org.mcbot.skills;

import org.mcbot.F3DataReader;
import org.mcbot.Movement;
import org.mcbot.Utils;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Farming {
    private static final int HOE = 1;
    private static final int SEED = 2;
    private Movement movement;
    private F3DataReader reader;
    private int slot;
    public Farming(Movement movement, F3DataReader reader) {
        this.movement = movement;
        this.reader = reader;
        slot = 0;
        setSlot(HOE);
    }

    /**
     * It is assumed that you are holding the hoe in SLOT 1.
     * It is assumed that you are holding the crop in SLOT 2.
     * This will go down a line at a time in the field, so start at
     * one corner with the farm to your right
     * @param crop
     * @param seconds
     */
    public void farmAndPlantCrop(String crop, int seconds) {
        //aim at the block directly in front of you
        movement.setYFacing(60);
        boolean turn = false;
        long endTime = System.currentTimeMillis() + (seconds * 1000L);
        while(System.currentTimeMillis() < endTime) {
            String targetBlock = (String)reader.data.get("Target Block");
            if (targetBlock.equals(crop)) {
                if (reader.data.get("age") != null
                        && Integer.parseInt((String)reader.data.get("age")) == 7 ) {
                    // break it
                    setSlot(HOE);
                    Utils.clickHere();
                    // replant it
                    setSlot(SEED);
                    Utils.rightClickHere();
                }
                // move forward
                movement.moveForward(1);
            } else if (targetBlock.equals("farmland")) {
                // plant crop
                setSlot(SEED);
                Utils.rightClickHere();
                movement.moveForward(1);
            } else if (targetBlock.equals("dirt")) {
                // till the earth and plant crop
                setSlot(HOE);
                Utils.rightClickHere();
                setSlot(SEED);
                Utils.rightClickHere();
                movement.moveForward(1);
            }
            else if (targetBlock.equals("grass_block")){
                // turn around and start next row
                movement.moveForward(1);
                turn = !turn;
                if(turn) {
                    movement.turnRight();
                    movement.moveForward(1);
                    movement.turnRight();
                } else {
                    movement.turnLeft();
                    movement.moveForward(1);
                    movement.turnLeft();
                }
            }
            else {
                movement.moveForward(1);
            }
            reader.readScreen();
            Utils.p(reader.data.toString());
        }

    }
    public void setSlot(int num) {
        // KeyEvent.VK_1 is 49
        if (slot != num) {
            Utils.pressAndReleaseKey(num + 48);
            int slot = num;
            Utils.sleep(20);
        }
    }
}
