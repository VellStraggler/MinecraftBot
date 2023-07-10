package org.bookreader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Holds functions for things like keystrokes, screenshots,
 * and saving data to the hard drive
 */
public class Utils {
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

    public static BufferedImage takeScreenshot() {
        try {
            Rectangle screenDimensions = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            return new Robot().createScreenCapture(screenDimensions);
        } catch (AWTException e) {
            System.out.println("Image grab failed: " + e + " " + e.getMessage());
            return null;
        }
    }

    /**
     * Save an image to the given path. PNG only. Do not include ".png" in the path parameter.
     * @param image
     * @param path
     */
    public static void saveImage(BufferedImage image, String path) {
        try {
            ImageIO.write(image, "png", new File(path + ".png"));
        } catch (IOException e) {
            System.out.println("Unable to save image: " + e + " " + e.getMessage());
        }
    }

    /**
     * Move the mouse to a particular point on screen. Returns true if successful
     * @param x
     * @param y
     * @return
     */
    public static boolean clickHere(int x, int y) {
        try {
            Robot clicker = new Robot();
            clicker.mouseMove(x,y);
            clicker.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            clicker.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            return true;
        } catch (AWTException | RuntimeException e) { return false;}
    }
    public static boolean clickHere(XY xy) {
        return clickHere(xy.x, xy.y);
    }

    public static boolean pressAndReleaseKey(int inputEvent) {
        try {
            Robot clicker = new Robot();
            clicker.keyPress(inputEvent);
            clicker.keyRelease(inputEvent);
            return true;
        } catch (AWTException | RuntimeException e) {return false;}
    }
    public static boolean pressKey(int inputEvent){
        try {
            Robot clicker = new Robot();
            clicker.keyPress(inputEvent);
            return true;
        } catch (AWTException | RuntimeException e) {return false;}
    }
    public static boolean releaseKey(int inputEvent){
        try {
            Robot clicker = new Robot();
            clicker.keyRelease(inputEvent);
            return true;
        } catch (AWTException | RuntimeException e) {return false;}
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
}
