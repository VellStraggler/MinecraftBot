package org.bookreader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BookReader {
    public static final int MAX_CHARS_IN_ROW = 66;
    public static final int LINE_WIDTH = MAX_CHARS_IN_ROW * 2;
    public static final int LINE_HEIGHT = CharArrays.HEIGHT + 1;
    public static final int PIXEL_WIDTH = 3;
    public static final int ROWS = 13;
    public static final XY START_POINT = new XY(622,97);
    public static final XY ARROW = new XY(904,490);
    // Points if you were to start in non-fullscreen. Only issue is Pixel-width becomes fractional
    public static final XY NFS_START_POINT = new XY(624,100);
    public static final XY NFS_ARROW = new XY(850,415);

    public BufferedImage image;
    private int column = 0;
    private int blanks = 0;

    public BookReader() {}

    public void takeScreenshot() {
        try {
            Rectangle screenDimensions = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            image = new Robot().createScreenCapture(screenDimensions);
        } catch (AWTException e) {
            System.out.println("Image grab failed: " + e + " " + e.getMessage());
        }
    }
    public void saveImage() {
        try {
            ImageIO.write(image, "png", new File("src/main/java/org/bookreader/saved-image.png"));
        } catch (IOException e) {
            p("Unable to save image: " + e + " " + e.getMessage());
        }
    }
    public static void p(String s) {
        System.out.println(s);
    }


    public boolean hasNext() {
        return (new RGB(image.getRGB(ARROW.x,ARROW.y)).isDark());
    }
    /**
     * Click the nextPage button onscreen,
     * or return false if it isn't there
     * @return
     */
    public boolean nextPage() {
        if (hasNext()) {
            // click
            try {
                Robot clicker = new Robot();
                clicker.mouseMove(ARROW.x,ARROW.y);
                Thread.sleep(10);
                clicker.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                clicker.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                return true;
            } catch (AWTException | RuntimeException | InterruptedException e) {}
        }
        return false;
    }

    public String processPage() {
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
                column += 1;
                blanks += 1;
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
    public boolean isInkedPixel(int x, int y) {
        return new RGB(image.getRGB(x,y)).isDark();
    }
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
        return CharRecognition.recognize(array);
    }
}
