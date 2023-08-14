package org.mcbot.datatypes;

public class XYZ extends XY{
    public int z;
    public XYZ(XYZ other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }
    public XYZ(int x, int y, int z) {
        super(x,y);
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }
        XYZ other = (XYZ) obj;
        return (other.x == this.x && other.y == this.y && other.z == this.z);
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
