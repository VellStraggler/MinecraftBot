package org.bookreader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This recognizes 93 particular characters based on the text font found in the game Minecraft.
 * This font is stored as a 2D array of booleans
 */
public class CharRecognition {

    public static final int HEIGHT = 8;
    public static final int WIDTH = 5;
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

    public CharRecognition(){}
    public static char recognize(boolean[][] array, boolean surelyDigit) {
        if (surelyDigit) {
            return getDigit(array);
        } else {
            char[] level = getLevel(array);
            for(char cInLevel: level){
                if (CharArrays.compare(array, CharArrays.getArray(cInLevel))) {
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
                if (CharArrays.riskyCompare(array, CharArrays.getArray(cInLevel))) {
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
    private static boolean matchesDimensions(boolean[][] array) {
        if (array.length != HEIGHT) {
            return false;
        } else return array[0].length == WIDTH;
    }

}
