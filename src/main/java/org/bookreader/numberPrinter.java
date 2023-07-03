package org.bookreader;

import java.util.*;

import static java.lang.System.currentTimeMillis;
import static org.bookreader.CharRecognition.recognize;

public class numberPrinter {
    public static void efficiencyTest(String[] args) {
        int time = 100000000;
        char[] digs = {'0','1','2','3','4','5','6','7','8','9'};
        long timeA = System.currentTimeMillis();
        for(int i = 0; i < time; i++) {
            for (char c: digs) {
                boolean[][] array = CharArrays.getArray(c);
                CharRecognition.recognize(array, true);
            }
        }
        long timeB = System.currentTimeMillis();
        for(int i = 0; i < time; i ++) {
            for (char c: digs) {
                boolean[][] array = CharArrays.getArray(c);
                CharRecognition.recognizeRisk(array, false);
            }
        }
        System.out.println((timeB - timeA) + " compared to risk: " + (System.currentTimeMillis() - timeB));
    }
    public static void getCoordUses(String[] args) {
        int[][] counts = new int[8][];
        for(int i = 0; i < counts.length; i++) {
            counts[i] = new int[5];
        }
        for(char chr: CharArrays.getAllChars()) {
            boolean[][] array = CharArrays.getArray(chr);
            for(int r = 0; r < array.length;r++) {
                for (int c = 0; c < array[0].length; c++) {
                    if(array[r][c]) counts[r][c] += 1;
                }
            }
        }
        for(int[] row: counts) {
            System.out.println();
            for(int i: row) {
                if (i < 10) {
                    System.out.print('0');
                }
                System.out.print(i + " ");
            }
        }

    }
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
        // Note: Capital letters are most likely found at the start of a line or sentence (after a period)
        // Note: Special characters are most likely found only on page 1 (except for dashes, commas, periods, and apostrophes)
    }
}
