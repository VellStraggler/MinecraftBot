package org.mcbot.datatypes;

import java.util.List;

public class Point {
    public XY xy;
    public List<Character> numbers;
    public Point(XY xy, List<Character> numbers) {
        this.xy = xy;
        this.numbers = numbers;
    }
    // ignores xy
    public boolean equals(Object other) {
        if (other == null || other.getClass() != Point.class) {
            return false;
        }
        Point otherPoint = (Point) other;
        return (otherPoint.numbers.equals(this.numbers));
    }
    public String toString() {
        if (numbers.size() > 0) {
            return xy.toString() + ": " + numbers.toString() + "\n";
        } return "";
    }
}