package org.mcbot.wordwork;

import org.mcbot.Utils;
import org.mcbot.datatypes.RGB;
import org.mcbot.datatypes.XY;

import java.awt.image.BufferedImage;

/**
 * This recognizes 93 particular characters based on the text font found in the game Minecraft.
 * This font is stored as a 2D array of booleans
 */
public class CharRecognition {

    public static final int HEIGHT = 8;
    public static final int NEW_LINE = HEIGHT + 1;
    public static final int WIDTH = 5;
    public static final int SPACE = WIDTH - 1;
    public static final int PIXEL_WIDTH = 3;
    // For the resolution 800 x 600, the pixel_width is 2;

    //         (x,y)
    // contains 0,3 and 0,5
    //public static final char[] levelOne = "eonihrldumywckpbHWMANOBRDCPELGF0U6QK@[|".toCharArray();
    // contains 1,2 | len 18
    private static final char[] levelTwo = "tasdgIfSqz54#={}\\<".toCharArray();
    // contains 1,7 | len 5
    private static final char[] levelThree = "yj_".toCharArray();
    // contains 0,6 | len 13
    private static final char[] levelFour = ",;.x:21!)%]ZX>/".toCharArray();
    // contains 2,6 | len 12
    private static final char[] levelFive = "v?TYJ3V87($&9".toCharArray();
    // what's left  | len 8
    private static final char[] levelSix = "-'*~`^+".toCharArray();

    // This divides levelOne again
    // contains 6,2 | len 19
    private static final char[] levelOneOne = "eoduwcbOBDCELG0U6Q[".toCharArray();
    // what's left  | len 21
    private static final char[] levelOneTwo = "nihrlmykpHWMANRPF0K@|".toCharArray();

    private BufferedImage image;
    private final RGB textColor;
    private XY startingPoint;
    // Refers to a text row
    private int row;
    // Refers to character pixel column
    private int col;

    /**
     * Instances of CharRecognition can read characters on one image using a starting point
     * as the top-left corner of the first character in a line. They will start on the same
     * x-coordinate for a new line.
     * If whiteText is not true, CharRecognition will look for black text instead.
     */
    public CharRecognition(BufferedImage image, XY startingPoint, RGB textColor){
        this.image = image;
        this.startingPoint = startingPoint;
        this.row = 0;
        this.col = 0;
        this.textColor = textColor;
    }
    public void update(BufferedImage image, XY startingPoint) {
        this.image = image;
        this.startingPoint = startingPoint;
    }

    public int readDigit() {
        boolean[][] array = getArrayFromImagePoint();
        char digitChar = CharRecognition.getDigit(array);
        return digitChar - '0';
    }
    public String readToImageEdge() {
        StringBuilder stringBuilder = new StringBuilder();
        // It's easier to try to read a whole character
        // than to check if every column is in bounds.
        int spaces = 0;
        col = 0;
//        Utils.p("Starting point: " + startingPoint.toString());
        while(true) {
            try {
                char currentChar = readChar();
                if (currentChar == ' ') {
                    spaces ++;
                    if (spaces == SPACE) {
                        stringBuilder.append(currentChar);
                    }
                } else {
                    spaces = 0;
                    stringBuilder.append(currentChar);
                }
            } catch (IndexOutOfBoundsException e) {
                // End of the image has been found
                row++;
                col = 0;
                break;
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Good for reading left-hand-side text
     * @return
     */
    public String readToThreeSpaces() {
        StringBuilder stringBuilder = new StringBuilder();
        int spaces = 0;
        col = 0;
        while (spaces != 10) {
            char currentChar = readChar();
            if (currentChar == ' ') {
                spaces++;
                if (spaces == SPACE) {
                    stringBuilder.append(currentChar);
                }
            } else {
                spaces = 0;
                stringBuilder.append(currentChar);
            }
        }
        // check for unnecessary whitespace at the end and remove it
        if(stringBuilder.charAt(stringBuilder.length()-1) == ' ') {
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        return stringBuilder.toString();
    }

    public char readChar() {
        boolean[][] array = getArrayFromImagePoint();
        return CharRecognition.recognize(array);
    }
    public void newLine() {
        col = 0;
        row = 1;
    }

    /**
     * Does not check if we are out of image bounds.
     * Moves the column col to the right based on the width of the character.
     * @return 2D array of booleans
     */
    public boolean[][] getArrayFromImagePoint() throws IndexOutOfBoundsException {
        // map the character to an array
        boolean[][] array = CharLibrary.emptyArray();
        // Not all characters are 5 pixels wide. 'i' for example is 1 pixel wide
        int trueWidth = 0;
        for (int charC = 0; charC < CharLibrary.WIDTH; charC++) {
            // Assume the column is empty until proven otherwise
            boolean emptyCol = true;
            int xPixel = ((col + charC) * PIXEL_WIDTH) + (int)startingPoint.x;
            for (int charR = 0; charR < CharLibrary.HEIGHT; charR++) {
                // We have the base starting point row plus the new line row we're on plus the pixel row of the char
                int yPixel = (int)startingPoint.y + (((row * NEW_LINE) + charR) * PIXEL_WIDTH);
                if(charR == 0 && charC == 0) {
                }
                RGB rgb = new RGB(image.getRGB(xPixel, yPixel));
                if (rgb.equals(textColor)) {
                    array[charR][charC] = true;
                    emptyCol = false;
                }
            }
            if (!emptyCol) trueWidth++;
            // An empty column means we've reached the end of a thinner character.
            else break;
        }
        col += trueWidth + 1;
        return array;
    }


    public static char recognize(boolean[][] array, boolean surelyDigit) {
        if (surelyDigit) {
            return getDigit(array);
        } else {
            char[] level = getLevel(array);
            for(char cInLevel: level){
                if (CharLibrary.compare(array, CharLibrary.getArray(cInLevel))) {
                    return cInLevel;
                }
            }
        }
        return ' ';
    }
    public static char recognizeRisk(boolean[][] array, boolean surelyDigit) {
        if (surelyDigit) {
            return getDigit(array);
        } else {
            char[] level = getLevel(array);
            for(char cInLevel: level){
                if (CharLibrary.riskyCompare(array, CharLibrary.getArray(cInLevel))) {
                    return cInLevel;
                }
            }
        }
        return ' ';
    }
    public static char recognize(boolean[][] array) {
        return recognize(array, false);
    }

    private static char getDigit(boolean[][] array) {
        if (array[2][0]) {
            if (array[4][0]) {
                if (array[3][1]) {
                    if (array[1][1]) {
                        return '6';
                    }   return '8';
                }   return '0';
            } else if (array[5][3]) {
                return '9';
            }   return '5';
        } else if (array[4][2]) {
            if (array[5][2]) {
                if (array[1][1]) {
                    return '1';
                }   return '7';
            }   return '4';
        } else if (array[4][1]) {
            return '2';
        }   return '3';
    }

    /**
     * This checks for points that certain characters have in common.
     * It returns a sublist from the list of all possible characters
     * @return
     */
    private static char[] getLevel(boolean[][] array) {
        if (array[3][0] && array[5][0]) {
            if (array[6][2]) {
                return levelOneOne;
            } else return levelOneTwo;
        } else if (array[2][1]) {
            return levelTwo;
        } else if (array[7][1]) {
            return levelThree;
        } else if (array[6][0]) {
            return levelFour;
        } else if (array[6][2]) {
            return levelFive;
        } else return levelSix;
    }
    /**
     * Make sure the dimensions of this array matches the static variables WIDTH and HEIGHT,
     * which respectively refers to columns and rows.
     */
    private static boolean matchesDimensions(boolean[][] array) {
        if (array.length != HEIGHT) {
            return false;
        } else return array[0].length == WIDTH;
    }

}
