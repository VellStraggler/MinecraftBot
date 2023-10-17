package org.mcbot.skills;

import org.mcbot.F3DataReader;
import org.mcbot.Movement;
import org.mcbot.Utils;

public class Farming {
    private static final int HOE = 1;
    private static final int SEED = 2;
    private Movement movement;
    private F3DataReader reader;
    private int slot;
    private String crop;
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
    public void farmAndPlantCrop(String newCrop, int seconds) {
        crop = newCrop;
        //aim at the block directly in front of you
        movement.setYFacing(60);
        boolean turn = false;
        long endTime = System.currentTimeMillis() + (seconds * 1000L);
        while(System.currentTimeMillis() < endTime) {
            String targetBlock = (String)reader.data.get("Target Block");
            if (targetBlock.equals(crop)) {
                if (reader.data.get("age") != null
                        && Integer.parseInt((String)reader.data.get("age")) == 7 ) {
                    farmCrop();
                    plantCrop();
                }
            } else if (targetBlock.equals("farmland")) {
                plantCrop();
            } else if (targetBlock.equals("dirt")) {
                tillLand();
                plantCrop();
            }
            else if (!targetBlock.contains("slab")){
                // turn around and start next row
                turn = !turn;
                if(turn) {
                    movement.turnRight();
                    farmCrop();
                    plantCrop();
                    movement.moveForward(1);
                    movement.turnRight();
                } else {
                    movement.turnLeft();
                    farmCrop();
                    plantCrop();
                    movement.moveForward(1);
                    movement.turnLeft();
                }
                continue;
            }
            movement.moveForward(1);
            reader.readScreen();
//            Utils.p(reader.data.toString());
        }

    }
    public void setSlot(int num) {
        // KeyEvent.VK_1 is 49
        if (slot != num) {
            movement.pressAndReleaseKey(num + 48);
            int slot = num;
            Utils.sleep(20);
        }
    }
    private void farmCrop() {
        setSlot(HOE);
        movement.clickHere();
    }
    private void tillLand() {
        setSlot(HOE);
        movement.rightClickHere();
    }
    private void plantCrop() {
        setSlot(SEED);
        movement.rightClickHere();
    }
    private boolean thisIsReadyCrop(String blockType) {
        if (blockType.equals(crop)) {
            return (reader.data.get("age") != null
                    && Integer.parseInt((String) reader.data.get("age")) == 7);
        }
        return false;
    }
}