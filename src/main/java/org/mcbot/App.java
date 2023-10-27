package org.mcbot;

import org.mcbot.datatypes.*;
import org.mcbot.skills.*;

import java.awt.image.BufferedImage;

public class App {
    public static void main(String[] args) {
        Utils.sleep(4000);
        notMain(args);
    }

    public static void notMain(String[] args) {
        BufferedImage image = ImageWork.takeScreenshot();
        ImageWork.saveImage(image,                                "screenshots/normal");
        ImageWork.saveImage(ImageWork.convertImageToBinary(image), "screenshots/binary");
        ImageWork.saveImage(ImageWork.convertImageToGrayscale(image), "screenshots/grayscale");
        ImageWork.saveImage(ImageWork.convertImageToReduced(image), "screenshots/reduced");
        //setup
        Items items = new Items();
        Blocks blocks = new Blocks(items);
        // time to go fullscreen
        Utils.sleep(4000);
        F3DataReader dataReader = new F3DataReader();
        Movement movement = new Movement(blocks, dataReader);
        Building building = new Building(movement);
        Mining mining =     new Mining(movement);

        //tasks
//        movement.moveForward(1,false,false);
//        building.mlg();
        new Farming(movement).farmAndPlantCrop("carrots",10000);
    }
}
