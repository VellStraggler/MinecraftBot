package org.mcbot;

import org.mcbot.datatypes.*;
import org.mcbot.datatypes.containers.Inventory;
import org.mcbot.datatypes.containers.Items;
import org.mcbot.datatypes.containers.Slot;
import org.mcbot.skills.*;
import org.mcbot.wordwork.F3DataReader;

import java.awt.image.BufferedImage;

public class App {
    public static void main(String[] args) {
        // Set up variables
        Utils.sleep(4000);
        Items items = new Items();
        Blocks blocks = new Blocks(items);
        // time to go fullscreen
        F3DataReader dataReader = new F3DataReader();
        dataReader.readScreen();
        Movement movement = new Movement(blocks, dataReader);
        //Inventory inventory = new Inventory(items, movement);
        //inventory.readContents();
        // Initialize Task Types
        Mining mining = new Mining(movement);
        //Building building = new Building(movement);
        //Farming farming = new Farming(movement);

        // Do what you want
        long total = System.currentTimeMillis();
        for(int i = 0; i < 15; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < 4; j++) {
                movement.turnRight();
            }

            for (int j = 0; j < 4; j++) {
                movement.turnLeft();
            }
            Utils.p("Complete at " + (System.currentTimeMillis() - start));
        }
        Utils.p("Average of " + ((System.currentTimeMillis()-total)/15));
    }
}
