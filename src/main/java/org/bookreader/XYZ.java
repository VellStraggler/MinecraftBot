package org.bookreader;

public class XYZ extends XY{
    public int z;
    public XYZ(int x, int y, int z) {
        super(x,y);
        this.z = z;
    }
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}