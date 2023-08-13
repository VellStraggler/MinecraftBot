package org.mcbot;

import org.mcbot.datatypes.RGB;
import org.mcbot.datatypes.XY;

import java.awt.image.BufferedImage;

import static org.mcbot.CharRecognition.PIXEL_WIDTH;

public class BookReader {
    public static final int MAX_CHARS_IN_ROW = 58;
    public static final int LINE_WIDTH = MAX_CHARS_IN_ROW * 2;
    public static final int LINE_HEIGHT = CharArrays.HEIGHT + 1;
    public static final int ROWS = 14;
    public static final XY START_POINT = new XY(622,97);
    public static final XY ARROW = new XY(904,490);
    // Points if you were to start in non-fullscreen. Only issue is Pixel-width becomes fractional
    public static final XY NFS_START_POINT = new XY(624,100);
    public static final XY NFS_ARROW = new XY(850,415);

    public BufferedImage image;
    public BufferedImage lastImage;
    private int column = 0;
    private int blanks = 0;

    public BookReader() {}

    public void saveBook(String text)  {
        Utils.saveToTextFile(text, "books/book");
    }
    public void takeScreenshot() {
        // Save the last screenshot
        if (image != null) {
            lastImage = image;
        }
        image = Utils.takeScreenshot();
    }
    public void saveImage() {
        Utils.saveImage(image, "src/main/java/org/bookreader/saved-image");
    }
    public static void p(String s) {
        Utils.p(s);
    }


    public boolean hasNext() {
        if (image == null) takeScreenshot();
        return (new RGB(image.getRGB(ARROW.x,ARROW.y)).isDark());
    }

    /**
     * Reads a page and tries to go to the next one
     * @return
     */
    public String readThisPage() {
        takeScreenshot();
        int tries = 0;
        while(shouldRetakeScreenshot() && tries < 50) {
            takeScreenshot();
            tries++;
        }
        nextPage();
        return processScreenshot();
    }

    /**
     * Should return true if the current image is a duplicate
     * of the previous or if the current image is empty
     * (such as when a page is loading)
     * @return
     */
    public boolean shouldRetakeScreenshot() {
        if (lastImage == null) return false;
        // check a line in the first column of text
        // this assumes the column has text
        boolean blank = true;
        boolean duplicate = true;
        int max = START_POINT.y + (LINE_HEIGHT * PIXEL_WIDTH * ROWS);
        for(int y = START_POINT.y; y < max; y+= PIXEL_WIDTH) {
            int currentRGB = image.getRGB(START_POINT.x, y);
            if (lastImage.getRGB(START_POINT.x,y) != currentRGB) {
                duplicate = false;
            }
            if (new RGB(currentRGB).isDark()) blank = false;
        } if (blank || duplicate) {
            return true;
        } else return false;
    }
    /**
     * Click the nextPage button onscreen,
     * or return false if it isn't there
     * @return
     */
    public boolean nextPage() {
        if (hasNext()) {
            return Utils.clickHere(ARROW);
        }
        return false;
    }

    public String processScreenshot() {
        StringBuilder stringBuilder = new StringBuilder();
        // go through each row
        for (int row = 0; row < ROWS; row++) {
            column = 0;
            blanks = 0;
            // If 5 spaces are found, a space is assumed
            while (column < LINE_WIDTH) {
                // check this pixel column for ink
                boolean inkedLine = false;
                for (int r = 0; r < CharArrays.HEIGHT; r++) {
                    // if found, a new character has been found
                    if (isInkedPixel((column * PIXEL_WIDTH) + START_POINT.x,
                                    (((row * LINE_HEIGHT) + r) * PIXEL_WIDTH) + START_POINT.y)) {
                        inkedLine = true;
                        blanks = 0;
                        break;
                    }
                }
                if(inkedLine) {
                    char chr = processChar((column * PIXEL_WIDTH) + START_POINT.x,
                                 ((row * LINE_HEIGHT) * PIXEL_WIDTH) + START_POINT.y);
                    stringBuilder.append(chr);
                }
                if (blanks == 4) {
                    stringBuilder.append(" ");
                    blanks = 0;
                }
                blanks += 1;
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
    public boolean isInkedPixel(int x, int y) {
        return new RGB(image.getRGB(x,y)).isDark();
    }

    /**
     * CharRecognition has added functionality similar to this, but I don't want to break this code at all.
     * @param startingX
     * @param startingY
     * @return
     */
    public char processChar(int startingX, int startingY) {
        // map the character to an array
        boolean[][] array = CharArrays.emptyArray();
        int trueWidth = 0;
        for (int charC = 0; charC < CharArrays.WIDTH; charC++) {
            boolean emptyCol = true;
            int xPixel = (charC * PIXEL_WIDTH) + startingX;
            for (int charR = 0; charR < CharArrays.HEIGHT; charR++) {
                int yPixel = (charR * PIXEL_WIDTH) + startingY;
                if (isInkedPixel(xPixel, yPixel)) {
                    array[charR][charC] = true;
                    emptyCol = false;
                }
            }
            if (!emptyCol) trueWidth++;
            // You don't want to start reading from subsequent characters.
            else break;
        }
        column += trueWidth;
        return CharRecognition.recognize(array, false);
    }
}
