package org.mcbot.datatypes;

/**
 * Simple, modifiable coordinate object
 */
public class XY {
    public double x;
    public double y;
    public XY() {}
    public XY(XY other) {
        this.x = other.x;
        this.y = other.y;
    }
    public XY(double x, double y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }
        XY other = (XY) obj;
        return (other.x == this.x && other.y == this.y);
    }
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
