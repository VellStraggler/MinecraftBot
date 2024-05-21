package org.mcbot;

import org.mcbot.datatypes.*;
import org.mcbot.datatypes.containers.Items;
import org.mcbot.skills.*;
import org.mcbot.wordwork.F3DataReader;

public class App {
    public static void main(String[] args) {
        // time to go fullscreen
        Utils.sleep(4000);
        // Set up variables
        Items items = new Items();
        Blocks blocks = new Blocks(items);
        F3DataReader dataReader = new F3DataReader();
        dataReader.readScreen();
        Movement mvt = new Movement(blocks, dataReader);
        //Inventory inventory = new Inventory(items, mvt);
        //inventory.readContents();
        // Initialize Task Types
        Mining mining = new Mining(mvt);
        Building building = new Building(mvt);
        //Farming farming = new Farming(mvt);

        // Do what you want
        Utils.beep();
        long end = System.currentTimeMillis() + 60000;
        Walkables walks = new Walkables();
        walks.addXYZ(mvt.getCoordinates());
        while (System.currentTimeMillis() < end) {
            mvt.wanderStep();
            walks.addXYZ(mvt.getCoordinates());
            Block block = mvt.getTargetBlock();
            if (!block.probablyPlaced && mvt.getSurface() != Surface.FLOOR) {
                mining.mineWalkingSpace();
                mvt.turnRandom();
            }
        }
        Utils.beep();
        Utils.p(walks.toString());
    }
}
