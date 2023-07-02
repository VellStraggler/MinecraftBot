package org.bookreader;

public class XY {
    public int x;
    public int y;
    public XY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
