package org.bookreader;

public class numberPrinter {
    public static void main(String[] args) {
        System.out.println(CharArrays.toString(CharArrays.getChar('0')));
        System.out.println(CharArrays.toString(CharArrays.getChar('1')));
        System.out.println(CharArrays.toString(CharArrays.getChar('2')));
        System.out.println(CharArrays.toString(CharArrays.getChar('3')));
        System.out.println(CharArrays.toString(CharArrays.getChar('4')));
        System.out.println(CharArrays.toString(CharArrays.getChar('5')));
        System.out.println(CharArrays.toString(CharArrays.getChar('6')));
        System.out.println(CharArrays.toString(CharArrays.getChar('7')));
        System.out.println(CharArrays.toString(CharArrays.getChar('8')));
        System.out.println(CharArrays.toString(CharArrays.getChar('9')));

    }
    // TODO: write a program that finds the best encoding for these 10 digits
    // step 1: ignore coordinates that have duplicate sets of digits that share it
    // step 2: loop through each point, where you remove the digits it has from every single point
    // step 3: search for the smallest number of nested loops to remove all digits (our goal is 4)
    // step 4: store these points for use in quickly reading digits
    // step 5: Make a version of this for all characters

    // Note: Capital letters are most likely found at the start of a line or sentence (after a period)
    // Note: Special characters are most likely found only on page 1 (except for dashes, commas, periods, and apostrophes)
}
