package org.mcbot;

import org.mcbot.datatypes.*;
import org.mcbot.skills.*;

public class App {
    public static void main(String[] args) {
        //setup
        Items items = new Items();
        Blocks blocks = new Blocks(items);
        // time to go fullscreen
        Utils.sleep(4000);
        F3DataReader dataReader = new F3DataReader();
        Movement movement = new Movement(dataReader, blocks);
        Building building = new Building(movement, dataReader);

        //tasks
//        movement.moveForward(1,false,false);
//        building.mlg();
        //Mining mining = new Mining(movement, dataReader);
        new Farming(movement, dataReader).farmAndPlantCrop("carrots",10000);
    }
}
