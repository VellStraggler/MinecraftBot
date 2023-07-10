package org.bookreader;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

/**
 * This detects if F3 data is on, and then
 * stores everything perceived as useful from it
 * in an F3Data Object
 */
public class F3DataReader {
    public final int VK_F3 = 114;
    private F3Data data;
    private BufferedImage screen;

    public F3DataReader() {}
    public F3DataReader(BufferedImage screen) {
        this.screen = screen;
    }
    public F3Data readScreen() {
        // Take a screenshot or use the current one
        if (screen == null) {
            screen = Utils.takeScreenshot();
        }
        // Detect if F3 mode is on
        if (Utils.isWhite(screen,4,4) || !Utils.isWhite(screen, 7,7)) {
            // Press F3 if that isn't the case
            Utils.pressKey(VK_F3);
        }
        // Check if there is a targeted block
        boolean targetedBlock = (!Utils.isWhite(screen, 1186,301) && Utils.isWhite(screen,1189,304));
        // Read the resolution first. Read the whole line and find the numbers within it
        XY startingPoint = new XY(1006,196);
        // Throw an error if the resolution is incorrect
        // Read data starting at all given points
        // Save it all to an F3Data object
        // return data
        return null;
    }
}
