package org.mcbot;

import org.mcbot.datatypes.*;
import org.mcbot.datatypes.containers.Inventory;
import org.mcbot.datatypes.containers.Items;
import org.mcbot.datatypes.containers.Slot;
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
        Movement movement = new Movement(blocks, dataReader);
        //Inventory inventory = new Inventory(items, movement);
        //inventory.readContents();
        // Initialize Task Types
        Mining mining = new Mining(movement);
        Building building = new Building(movement);
        //Farming farming = new Farming(movement);

        // Do what you want
        Utils.beep();
        mining.mineSquareArea(5);
        Utils.beep();
        //building.bridge();
    }
}
