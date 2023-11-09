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
//        Utils.sleep(4000);
        Items items = new Items();
        Blocks blocks = new Blocks(items);
        // time to go fullscreen
        F3DataReader dataReader = new F3DataReader();
        Movement movement = new Movement(blocks, dataReader);
        Inventory inventory = new Inventory(items, movement);
        BufferedImage image = ImageWork.retrieveImage("src/main/resources/inventory_with_crafting_table");
        XY slotCoord = inventory.getSlotCoordinates(4, 0);
        Slot slot = inventory.readSlot(image, (int)(slotCoord.x), (int)(slotCoord.y));
        Utils.p("name:" + slot.item.name);
        ImageWork.saveImage(image, "src/main/resources/affected_image");
        //notMain(args);
    }

    public static void notMain(String[] args) {
        BufferedImage image = ImageWork.takeScreenshot();
        ImageWork.saveImage(image,                                "screenshots/normal");
        ImageWork.saveImage(ImageWork.convertImageToBinary(image), "screenshots/binary");
        ImageWork.saveImage(ImageWork.convertImageToGrayscale(image), "screenshots/grayscale");
        ImageWork.saveImage(ImageWork.convertImageToReduced(image), "screenshots/reduced");
        ImageWork.saveImage(ImageWork.convertImageTo4Level(image), "screenshots/4level");
        //setup
        Items items = new Items();
        Blocks blocks = new Blocks(items);
        // time to go fullscreen
//        Utils.sleep(4000);
        F3DataReader dataReader = new F3DataReader();
        Movement movement = new Movement(blocks, dataReader);
        Inventory inventory = new Inventory(items, movement);
        Building building = new Building(movement);
        Mining mining =     new Mining(movement);

        //tasks
//        movement.moveForward(1,false,false);
//        building.mlg();
//        new Farming(movement).farmAndPlantCrop("carrots",10000);
    }
}
