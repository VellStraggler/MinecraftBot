package org.bookreader;

import java.util.*;

import static java.lang.System.currentTimeMillis;
import static org.bookreader.CharRecognition.recognize;

public class numberPrinter {
    public static void main(String[] args) {
        for(char c: CharArrays.getAllChars()) {
            System.out.println("    " + c + "\n" + CharArrays.toString(c));
        }

    }
    // TODO: write a program that finds the best encoding for these 10 digits
    public static void omain(String[] args) {
        // step 1: ignore coordinates that have duplicate sets of digits that share it
        List<Point> points = new ArrayList<>();
        char[] nums = {'0','1','2','3','4','5','6','7','8','9'};
        for(int r = 0; r <CharArrays.HEIGHT; r++) {
            for(int c = 0; c < CharArrays.WIDTH; c++ ) {
                List<Character> numbers = new ArrayList<>();
                for(char n: nums) {
                    boolean[][] chr = CharArrays.getArray(n);
                    if (chr[r][c]) {
                        //numbers will always be in order
                        numbers.add(n);
                    }
                }
                if (numbers.size() != 0) {
                    Point point = new Point(new XY(c,r), numbers);
                    if(!points.contains(point)) {
                        points.add(point);
                    }
                }
            }
        }
        System.out.println(points);
        // step 2: loop through each point, where you remove the digits it has from every single point
        int max = nums.length;
        List<Point> attackers = new ArrayList<>();
        attackers.add(new Point(new XY(0,2), List.of('0','5','6','8','9')));
        attackers.add(new Point(new XY(2,4), List.of('1','4','7')));
        for (Point attacker: attackers) {
            if (attacker.numbers.size() < (max / 2) + 2 && attacker.numbers.size() > (max / 2) - 2) {
                System.out.print("attacker: " + attacker.toString());
                List<Point> newPoints = new ArrayList<>();
                int newMax = attacker.numbers.size();
                for (Point point : points) {
                    if (!newPoints.contains(point)) {
                        newPoints.add(point);
                        if (point.numbers.size() > newMax) {
                            newMax = point.numbers.size();
                        }
                    }
                }
                max = newMax;
                points = newPoints;
                for (int preyIndex = 0; preyIndex < points.size(); preyIndex++) {
                    List<Character> removeFromThis = points.get(preyIndex).numbers;
                    for (Character c : attacker.numbers) {
                        if (removeFromThis.contains(c)) {
                            removeFromThis.remove(c);
                        }
                    }
                }
                System.out.println("\n" + points);
            }
        }
        //




        // step 3: search for the smallest number of nested loops to remove all digits (our goal is 4)
        // step 4: store these points for use in quickly reading digits
        // step 5: Make a version of this for all characters

        // Note: Capital letters are most likely found at the start of a line or sentence (after a period)
        // Note: Special characters are most likely found only on page 1 (except for dashes, commas, periods, and apostrophes)
    }
}
