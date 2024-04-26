package org.mcbot;

import org.mcbot.datatypes.RGB;
import org.mcbot.datatypes.XY;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;

/**
 * Static-only class that holds functions for things like screenshots
 * and saving data to the hard drive
 */
public class Utils {
    // There is no perceivable difference in frame-rate if you screenshot the whole window or half the window
    public static final XY SCREEN_RESOLUTION = new XY(1600,900);
    /**
     * Saves only to txt files. The path should not include ".txt"
     * @param text
     * @param path
     */
    public static void saveToTextFile(String text, String path)  {
        File file = new File(path + ".txt");
        try {
            FileWriter writer = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(text);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println(e + " " + e.getMessage());
        }
    }

    /**
     * Printing shortcut.
     * @param s
     */
    public static void p(String s) {
        System.out.println(s);
    }


    /**
     * Simply checks to see if a given pixel is white on the given screen.
     * @param image
     * @param x
     * @param y
     * @return
     */
    public static boolean isWhite(BufferedImage image, int x, int y) {
        return new RGB(image.getRGB(x,y)).isWhite();
    }
    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            p("Unable to sleep? " + e.getMessage());
        }
    }
    public static void sleepOneFrame() {
        sleep(1000/30);
    }

    /**
     * Only use this in cases where the Movement class has not been instantiated
     * (such as when F3DataReader needs to turn on the data screen)
     * @param inputEvent
     */
    public static void pressAndReleaseKey(int inputEvent) {
        try {
            Robot input = new Robot();
            input.keyPress(inputEvent);
            sleep(40);
            input.keyRelease(inputEvent);
        } catch (AWTException ignored) {}
    }
    public static int distanceFrom(double here, double there) {
        int ans = (int)(Math.floor(here) - Math.floor(there));
        return Math.abs(ans);
    }
}
