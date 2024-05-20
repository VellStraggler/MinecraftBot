package org.mcbot.datatypes;

public class LocNode {
    public XYZ position;
    public String block;
    public LocNode north;
    public LocNode south;
    public LocNode east;
    public LocNode west;

    public LocNode(XYZ position, String block, LocNode prev, Facing facing) {
        this.position = position;
        this.block = block;
        switch(facing) {
            case NORTH:
                prev.north = this;
                this.south = prev;
                break;
            case SOUTH:
                prev.south = this;
                this.north = prev;
                break;
            case EAST:
                prev.east = this;
                this.west = prev;
                break;
            default: //WEST
                prev.west = this;
                this.east = prev;
                break;

        }
    }

}
