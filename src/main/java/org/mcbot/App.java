package org.mcbot;

import java.awt.*;
import java.awt.image.BufferedImage;

public class App {
    public static void main(String[] args) {
        //setup
        Utils.sleep(4000);
        F3DataReader dataReader = new F3DataReader();
        F3Data screenData = dataReader.readScreen();

        Movement movement = new Movement(screenData.coordinates, screenData.facing);
        movement.moveForward(5);
    }
    public static void readOneBook() {
        BookReader reader = new BookReader();
        Utils.sleep(4000);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(reader.readThisPage()).append("\n");
        while (reader.hasNext()) {
            stringBuilder.append(reader.readThisPage()).append("\n");
        }
        reader.saveBook(stringBuilder.toString());
    }
    public static void p(String s) {
        System.out.println(s);
    }
}
