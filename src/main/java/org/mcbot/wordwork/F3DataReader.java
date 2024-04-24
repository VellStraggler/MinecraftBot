package org.mcbot.wordwork;

import org.mcbot.ImageWork;
import org.mcbot.Utils;
import org.mcbot.datatypes.RGB;
import org.mcbot.datatypes.XY;
import org.mcbot.datatypes.XYZ;

import java.awt.image.BufferedImage;
import java.util.*;

/**
 * This detects if F3 data is on, and then
 * stores everything perceived as useful from it
 * in an F3Data Object
 */
public class F3DataReader {
    // Usable data keys:
    // - Coordinates
    // - Direction
    // - Day
    // - Target Coordinates
    // - Target Block

    public static final int F3_KEY = 114;
    private static final int START_Y = 34;// + 2; //windowed mode?
    private static final int END_Y = 628 + 1;
    private static final int LEFT_X = 7;
    // This is where data reading begins on the left side
    private static final int RIGHT_START_Y = 304;
    private static final int RIGHT_END_Y = 412 + 1;
    private static final int RIGHT_X = 1006;
    // For data reading on the right side. This skips computer information and DISPLAY resolution
    private BufferedImage screen;
    public F3Data data;

    /** Writes down where everything is. This
     * should only be initialized once. **/
    public F3DataReader() {
    }
    /** Reads every character shown on screen **/
    public void PrintWholeScreenContents() {
        Map<String, Object> data = new HashMap<>();
        screen = ImageWork.takeScreenshot();
        setF3OnIfOff();

        for (int y = START_Y; y < END_Y; y+= (CharRecognition.NEW_LINE * CharRecognition.PIXEL_WIDTH)) {
            String line = new CharRecognition(screen, new XY(LEFT_X, y), RGB.F3_WHITE).readToImageEdge();
            Utils.p(line);
        }
        Utils.p("\n\n");
    }
    /** Takes a screenshot and returns the data from it **/
    public F3Data readScreen() {
        Map<String, Object> data = new HashMap<>();

        // Take a screenshot or use the current one
        screen = ImageWork.takeScreenshot();
        setF3OnIfOff();

        // read LEFT side
        for (int y = START_Y; y < END_Y; y+= (CharRecognition.NEW_LINE * CharRecognition.PIXEL_WIDTH)) {
            // read the first character of the line
            char c = new CharRecognition(screen, new XY(LEFT_X, y), RGB.F3_WHITE).readChar();
            // check for the first characters of the data keys rather than entire words
            for(int i = 0; i < F3Data.leftFirstChars.length; i++) {
                char s = F3Data.leftFirstChars[i];
                if (c == s) {
                    // this is a line we want to read
                    String line = new CharRecognition(screen, new XY(LEFT_X, y), RGB.F3_WHITE).readToThreeSpaces();
                    // if this is ACTUALLY the line we want
                    String key = F3Data.leftDataHeadings[i];
                    if(line.contains(key)) {
                        Object object;
                        switch(key) {
                            case ("XYZ"):
                                double x1 = Double.parseDouble(line.substring(line.indexOf(' ')+1,line.indexOf('/')-1));
                                line = line.substring(line.indexOf('/')+2);
                                double y1 = Double.parseDouble(line.substring(0, line.indexOf('/')-1));
                                double z1 = Double.parseDouble(line.substring(line.indexOf('/')+2));
                                data.put("Coordinates", new XYZ(x1, y1, z1));
                                break;
                            case ("Facing"):
                                // ex: "Facing: north (Towards negative Z) (-173.3 / 15.9)"
                                data.put("Direction", line.substring(line.indexOf(' ')+1, line.indexOf('(') -1)); //i.e. north
                                double x2 = Double.parseDouble(line.substring(line.indexOf(") (")+3, line.indexOf('/')-1));
                                double y2 = Double.parseDouble(line.substring(line.indexOf('/') + 2,line.length()-1));
                                data.put(key, new XY(x2, y2));
                                break;
                        }
                    }
                }
            }
        }
        // read RIGHT Side
        for (int y = RIGHT_START_Y; y < RIGHT_END_Y; y+= (CharRecognition.NEW_LINE * CharRecognition.PIXEL_WIDTH)) {
            // read the line we want to read
            String line = new CharRecognition(screen, new XY(RIGHT_X, y), RGB.F3_WHITE).readToImageEdge();
            boolean found = false;
            for(int i = 0; i < F3Data.rightFirstChars.length; i++) {
                char s = F3Data.rightFirstChars[i];
                if (line.charAt(0) == s || (line.length() > 1 && line.charAt(1) == s)) {
                    // if this is ACTUALLY the line we want
                    String key = F3Data.rightDataHeadings[i];
                    if(line.contains(key)) {
                        Object object;
                        switch(key) {
                            case ("Targeted"):
                                found = true;
                                double x1 = Integer.parseInt(line.substring(line.indexOf(':') + 2, line.indexOf(',')));
                                line = line.substring(line.indexOf(',') + 2);
                                double y1 = Integer.parseInt(line.substring(0, line.indexOf(',')));
                                double z1 = Integer.parseInt(line.substring(line.indexOf(',')+2).replaceAll(" ",""));
                                data.put("Target Coordinates", new XYZ(x1, y1, z1));
                                // the targeted block type is always right under this
                                line = new CharRecognition(screen, new XY(RIGHT_X, y + (CharRecognition.NEW_LINE * CharRecognition.PIXEL_WIDTH)), RGB.F3_WHITE).readToImageEdge();
                                data.put("Target Block", line.substring(line.indexOf(':')+1));
                                break;
                        }
                    }
                }
            }
            // We are adding every variable just in case
            line = line.replace("#","");
            line = line.replace("minecraft","");
            line = line.replaceAll(" ","");
            if (line.contains(":")) {
                data.put(line.substring(0,line.indexOf(':')),
                        line.substring(line.indexOf(':')+1));
            }
        }
        this.data = new F3Data(data);
        return this.data;
    }

    private void setF3OnIfOff() {
        // Detect if F3 mode is on. This checks the top left corner of 'Minecraft 1.20.1...etc'
        if (!RGB.F3_WHITE.equals(screen,7,7) && !RGB.F3_WHITE.equals(screen,4,4)) {
            // Press F3 if that isn't the case
            Utils.pressAndReleaseKey(F3_KEY);
            // Might be too quick
            Utils.sleep(100);
            // Obviously we need a new screenshot now
            screen = ImageWork.takeScreenshot();
        }
    }
}
