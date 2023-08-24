package org.mcbot;

import org.mcbot.datatypes.XY;
import org.mcbot.datatypes.XYZ;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores all data deemed important from an F3 screenshot.
 */
public class F3Data {
    // Makes it easy to add other data if you want to
    // TODO: currently all dataHeadings must have different first letters.
    // This data does not need to be in order
    public static String[] leftDataHeadings = new String[]{"XYZ", "Facing", "Local Difficulty"};
    //                                                             ^         day, ignore difficulty
    //                                                             direction and facing
    public static Character[] leftFirstChars = new Character[]{'X', 'F', 'L'};
    public static String[] rightDataHeadings = new String[]{"Targeted Block", "minecraft"};
    //                                                                         block type
    public static Character[] rightFirstChars = new Character[]{'T', 'm'};
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
        return "";
    }

}
