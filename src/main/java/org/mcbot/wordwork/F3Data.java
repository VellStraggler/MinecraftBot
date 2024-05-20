package org.mcbot.wordwork;

import org.mcbot.datatypes.XY;
import org.mcbot.datatypes.XYZ;

import java.util.HashMap;
import java.util.Map;

/**
 * POJO: Stores all data deemed important from an F3 screenshot.
 */
public class F3Data {
    // Makes it easy to add other data if you want to
    // TODO: currently all dataHeadings must have different first letters.
    // This data does not need to be in order
    public static Character[] leftFirstChars = new Character[]{'X', 'F', 'L'};
    public static String[] leftDataHeadings = new String[]    {"XYZ", "Facing", "Local Difficulty"};
    //                                                                  ^         ^ day, ignore difficulty
    //                                                                  direction and facing
    public static Character[] rightFirstChars = new Character[]{'T', 'm'};
    public static String[] rightDataHeadings = new String[]    {"Targeted Block", "minecraft"};
    //                                                                             block type
    private Map<String, Object> data;

    public F3Data(Map<String, Object> data) {
        this.data = data;
    }
    public Object get(String key) {
        if(data.containsKey(key)) {
            return data.get(key);
        }
        return null;
    }
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(String key: data.keySet()) {
            builder.append(key + ": " + data.get(key) + "\n");
        }
        return builder.toString();
    }

}
