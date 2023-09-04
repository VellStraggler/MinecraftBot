package org.mcbot;

import org.mcbot.datatypes.Blocks;
import org.mcbot.datatypes.Items;
import org.mcbot.skills.BookReader;
import org.mcbot.skills.Farming;
import org.mcbot.skills.Mining;

public class App {
    public static void main(String[] args) {
        //setup
        Items items = new Items();
        Blocks blocks = new Blocks(items);
        // time to go fullscreen
        Utils.sleep(2000);
        F3DataReader dataReader = new F3DataReader();
        Movement movement = new Movement(dataReader, blocks);

        //tasks
        Mining mining = new Mining(movement, dataReader);
        boolean turnRight = true;
        for(int i = 0;i < 3;i++) {
            mining.simpleStripMine(10);
            if (turnRight) {
                movement.turnRight();
                mining.simpleStripMine(1);
                movement.turnRight();
            } else {
                movement.turnLeft();
                mining.simpleStripMine(1);
                movement.turnLeft();
            }
            turnRight = !turnRight;
        }
        //new Farming(movement, dataReader).farmAndPlantCrop("carrots",1000);
    }

    public static void p(String s) {
        System.out.println(s);
    }
}
