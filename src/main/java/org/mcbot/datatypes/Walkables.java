package org.mcbot.datatypes;

import java.util.*;

/** Y: X: [Z,Z,Z...] **/
public class Walkables {
    // A HashMap is keys with values
    // For a Y value, there is a list of X values,
    // and for each X value, there is a list of Z values
    private Map<Integer, Map<Integer, Set<Integer>>> XYZs;

    public void Walkable() {
        XYZs = new HashMap<>();
    }
    public void addXYZ(XYZ c) {
        if(c == null) return;

        int x = XYZ.toIntXOrZ(c.x);
        int y = XYZ.toIntY(c.y);
        int z = XYZ.toIntXOrZ(c.z);
        if(XYZs == null) {
            XYZs = new HashMap<>();
        }
        if(!XYZs.containsKey(y)) {
            XYZs.put(y, new HashMap<>());
        }
        if(!XYZs.get(y).containsKey(x)){
            XYZs.get(y).put(x, new HashSet<>());
        }
        // Sets don't allow duplicates
        XYZs.get(y).get(x).add(z);
    }
    public boolean isWalkable(XYZ c) {
        if(c == null) {
            return false;
        }
        int x = XYZ.toIntXOrZ(c.x);
        int y = XYZ.toIntY(c.y);
        int z = XYZ.toIntXOrZ(c.z);
        if(!XYZs.containsKey(y)) {
            return false;
        } else if(!XYZs.get(y).containsKey(x)){
            return false;
        } else if(!XYZs.get(y).get(x).contains(z)) {
            return false;
        }
        return true;
    }
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int y:XYZs.keySet()) {
            for(int x:XYZs.get(y).keySet()) {
                for(int z:XYZs.get(y).get(x)) {
                    builder.append("(" + x + ", " + y + ", " + z + ")");
                }
            }
        }
        return builder.toString();
    }
}
