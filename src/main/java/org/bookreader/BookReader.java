package org.bookreader;

/**
 * MINECRAFT SETTINGS:
 * - 1080p
 * - no resource pack
 * - fullscreen
 */
public class BookReader {
    public static final int MAX_CHARS_IN_ROW = 67;
    public static final int LINE_WIDTH = MAX_CHARS_IN_ROW * 2;
    public static final int PIXEL_WIDTH = 4;
    public static final int ROWS = 13;
    public static final XY TOP_LEFT_CORNER = new XY(0,0);
    public static final XY ARROW = new XY(0,0);

    public static void main(String[] args) {
        // await command to begin
        // /readbook bookName
        // Wait for a slash
//        while(!key.isPressed("/")) {
//            Thread.sleep(10);
//        }
        while(true) {
//            if(command.equals("readbook\n")) {
//
//            } else break;

            Book book = new Book();
            book.setTitle("");

            boolean endOfBook = false;
            StringBuilder stringBuilder = new StringBuilder();
            // TODO: Create an app that tells you where your mouse is on-screen
            while (!endOfBook) {
                // take a screenshot

                // go through each row
                for (int row = 0; row < ROWS; row++) {
                    char[] charLine = new char[MAX_CHARS_IN_ROW];
                    int lineX = 0;
                    int blanks = 0;
                    // If three spaces are found, a new line is assumed
                    while (lineX < LINE_WIDTH && blanks < 12) {
                        // check the column of this character for the topleft-most pixel
                        for (int r = 0; r < CharArrays.HEIGHT; r++) {
                            // if found, a new character has been found
                            if (true) {
                                blanks = 0;
                                break;
                            }
                        }
                        // map the character to an array
                        boolean[][] array = CharArrays.emptyArray();
                        for (int charR = 0; charR < CharArrays.HEIGHT; charR++) {
                            for (int charC = 0; charC < CharArrays.WIDTH; charC++) {
                                //array[charR][charC] = if color is black
                            }
                        }

                        // Characters are always at least one spot away from each other
                        lineX += 2;
                        blanks += 2;
                    }
                    // Convert the char array to a String, then add to stringBuilder with a \n
                }
                // check for arrow
                // if no arrow, endOfBook is found
                // otherwise, click on arrow
            }
            // Save Book to a text document under the proper title

        }
    }
}
