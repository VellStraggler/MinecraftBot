package org.mcbot.skills;

import org.mcbot.*;
import org.mcbot.datatypes.RGB;
import org.mcbot.datatypes.XY;
import org.mcbot.wordwork.CharLibrary;
import org.mcbot.wordwork.CharRecognition;

import java.awt.image.BufferedImage;

import static org.mcbot.wordwork.CharRecognition.PIXEL_WIDTH;

public class BookReader {
    public static final int MAX_CHARS_IN_ROW = 58;
    public static final int LINE_WIDTH = MAX_CHARS_IN_ROW * 2;
    public static final int LINE_HEIGHT = CharLibrary.HEIGHT + 1;
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

    private Movement movement;

    public BookReader(Movement movement) {
        this.movement = movement;
    }

    /**
     * Call this once a book is already opened. This will copy every page
     * and save it using saveBook().
     */
    public void readOneBook() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(readThisPage()).append("\n");
        while (hasNext()) {
            stringBuilder.append(readThisPage()).append("\n");
        }
        saveBook(stringBuilder.toString());
    }
    public void saveBook(String text)  {
        Utils.saveToTextFile(text, "books/book");
    }
    public void takeScreenshot() {
        // Save the last screenshot
        if (image != null) {
            lastImage = image;
        }
        image = ImageWork.takeScreenshot();
    }
    public void saveImage() {
        ImageWork.saveImage(image, "src/main/java/org/bookreader/saved-image");
    }
    public static void p(String s) {
        Utils.p(s);
    }


    public boolean hasNext() {
        if (image == null) takeScreenshot();
        return (new RGB(image.getRGB((int)ARROW.x,(int)ARROW.y)).isDark());
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
        int max = (int)START_POINT.y + (LINE_HEIGHT * PIXEL_WIDTH * ROWS);
        for(int y = (int)START_POINT.y; y < max; y+= PIXEL_WIDTH) {
            int currentRGB = image.getRGB((int)START_POINT.x, y);
            if (lastImage.getRGB((int)START_POINT.x,y) != currentRGB) {
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
            movement.clickHere(ARROW);
            return true;
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
                for (int r = 0; r < CharLibrary.HEIGHT; r++) {
                    // if found, a new character has been found
                    if (isInkedPixel((column * PIXEL_WIDTH) + (int)START_POINT.x,
                                    (((row * LINE_HEIGHT) + r) * PIXEL_WIDTH) + (int)START_POINT.y)) {
                        inkedLine = true;
                        blanks = 0;
                        break;
                    }
                }
                if(inkedLine) {
                    char chr = processChar((column * PIXEL_WIDTH) + (int)START_POINT.x,
                                 ((row * LINE_HEIGHT) * PIXEL_WIDTH) + (int)START_POINT.y);
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
        boolean[][] array = CharLibrary.emptyArray();
        int trueWidth = 0;
        for (int charC = 0; charC < CharLibrary.WIDTH; charC++) {
            boolean emptyCol = true;
            int xPixel = (charC * PIXEL_WIDTH) + startingX;
            for (int charR = 0; charR < CharLibrary.HEIGHT; charR++) {
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
