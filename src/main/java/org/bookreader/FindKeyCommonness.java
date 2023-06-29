package org.bookreader;

import java.io.*;
import java.util.*;

public class FindKeyCommonness {
    private static final String textPath = "src/main/resources/text.txt";
    private static final String skipThese = " \t";

    public static void other(String[] args) {
        File file = new File(textPath);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist");
        } catch (IOException e) {
            System.out.println("File cannot be read");
        }
        String text = stringBuilder.toString();
        // Count up characters
        Map<Character, Integer> charCounter = new HashMap<>();
        for (char c: text.toCharArray()) {
            if (charCounter.containsKey(c)) {
                charCounter.put(c, charCounter.get(c) + 1);
            } else if (!skipThese.contains(Character.toString(c))) {
                charCounter.put(c, 1);
            }
        }
        // Sort characters by popularity (like in high school)
        List<String> sorted = new ArrayList<>();
        // Assume all characters are found under 10000 times (they are in the given text)
        for (char e: charCounter.keySet()) {
            // {E : 45} ---> 45 E
            String count = "" + charCounter.get(e);
            count = "0".repeat(4 - count.length()) + count;
            sorted.add(count + " " + e);
        }
        Collections.sort(sorted);
        Collections.reverse(sorted);
//        System.out.println(sorted.toString());
        for (String s: sorted) {
            System.out.print(s.substring(5,6));
        }
    }
}
