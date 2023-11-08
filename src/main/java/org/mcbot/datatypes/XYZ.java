package org.mcbot.datatypes;

public class XYZ extends XY{
    public double z;
    public XYZ(XYZ other) {
        if(other == null) {
            x = 0;
            y = 0;
            z = 0;
        } else {
            this.x = other.x;
            this.y = other.y;
            this.z = other.z;
        }
    }
    public XYZ(double x, double y, double z) {
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
