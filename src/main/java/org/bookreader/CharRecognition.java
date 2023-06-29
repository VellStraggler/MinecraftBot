package org.bookreader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This recognizes 93 particular characters based on the text font found in the game Minecraft.
 * This font is stored as a 2D array of booleans
 */
public class CharRecognition {
    private char c;
    private final boolean[][] array;

    public static final int HEIGHT = CharRecognition.HEIGHT;
    public static final int WIDTH = CharRecognition.WIDTH;
    public static final String SORTED_CHARACTERS = "etoansihrldumyg.wcI,fkpbv?jHWTMAS-'xNqzYOBRDCJPELG:2F0U513!4V;*86QK7)(%~`@#$^&9_+=[{]}\\|ZX<>/";
    //         (x,y)
    // contains 0,3 and 0,5
    //public static final char[] levelOne = "eonihrldumywckpbHWMANOBRDCPELGF0U6QK@[|".toCharArray();
    // contains 1,2 | len 18
    private static final char[] levelTwo = "tasdgIfSqz54#={}\\<".toCharArray();
    // contains 0,7 | len 5
    private static final char[] levelThree = "y,j;_".toCharArray();
    // contains 0,6 | len 13
    private static final char[] levelFour = ".x:21!)%]ZX>/".toCharArray();
    // contains 2,6 | len 12
    private static final char[] levelFive = "v?TYJ3V87$&9".toCharArray();
    // what's left  | len 8
    private static final char[] levelSix = "-'*(~`^+".toCharArray();

    // This divides levelOne again
    // contains 6,2 | len 19
    private static final char[] levelOneOne = "eoduwcbOBDCELG0U6Q[".toCharArray();
    // what's left  | len 21
    private static final char[] levelOneTwo = "nihrlmykpHWMANRPF0K@|".toCharArray();

    /**
     * This constructor discovers the character automatically.
     * @param array
     */
    public CharRecognition(boolean[][] array) {
        this.array = array;
        checkDimensions();
        char[] level = getLevel();
        for(char cInLevel: level){
            if (CharArrays.compare(array, CharArrays.getChar(cInLevel))) {
                c = cInLevel;
                break;
            }
        }
    }

    public char getChar() {
        return c;
    }

    /**
     * ONLY PUBLIC FOR TESTING
     * This checks for points that certain characters have in common.
     * It returns a sublist from the list of all possible characters
     * @return
     */
    public char[] getLevel() {
        if (array[3][0] && array[5][0]) {
            if (array[2][6]) {
                return levelOneOne;
            } else return levelOneTwo;
        } else if (array[2][1]) {
            return levelTwo;
        } else if (array[7][0]) {
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
    private void checkDimensions() {
        if (array.length != WIDTH) {
            throw new IllegalArgumentException("array must have a width of " + WIDTH);
        } else if (array[0].length != HEIGHT) {
            throw new IllegalArgumentException("array must have a height of " + HEIGHT);
        }
    }

}
