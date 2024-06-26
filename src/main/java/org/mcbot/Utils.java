package org.mcbot;

import org.mcbot.datatypes.RGB;
import org.mcbot.datatypes.XY;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
    /** It's actually 2**/
    public static void sleepOneFrame() {
        sleep(1000/45);
    }
    public static void sleepTwoFrames() {
        sleepOneFrame();
        sleepOneFrame();
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
            sleepTwoFrames();
            input.keyRelease(inputEvent);
        } catch (AWTException ignored) {}
    }
    public static void type(String s) {
        try {
            Robot input = new Robot();
            sleepTwoFrames();
            input.keyPress(KeyEvent.VK_T);
            sleepTwoFrames();
            input.keyRelease(KeyEvent.VK_T);
            for(char c: s.toCharArray()) {
                int key = c;
                boolean cap = false;
                if(c >= 'A' && c <= 'Z') {
                    cap = true;
                    sleepTwoFrames();
                    input.keyPress(KeyEvent.VK_SHIFT);
                } else if(c >= 'a' && c <= 'z'){
                    key = c - 32;
                } else if (c == '.') {
                    key = KeyEvent.VK_PERIOD;
                } else if (c == ':') {
                    key = KeyEvent.VK_COLON;
                } else {
                    // unknown characters become a space.
                    key = KeyEvent.VK_SPACE;
                }
                sleepTwoFrames();
                input.keyPress(key);
                sleepTwoFrames();
                input.keyRelease(key);
                if (cap) {
                    sleepTwoFrames();
                    input.keyRelease(KeyEvent.VK_SHIFT);
                }
            }
            input.keyPress(KeyEvent.VK_ENTER);
            sleep(200);
        } catch (AWTException ignored) {}
    }
    /** This only applies to X and Z coordinates!! **/
    public static int distanceFrom(double here, double there) {
        int ans = (int)(Math.floor(here) - Math.floor(there));
        return Math.abs(ans);
    }
    public static void beep() {
        Toolkit.getDefaultToolkit().beep();
    }
}
