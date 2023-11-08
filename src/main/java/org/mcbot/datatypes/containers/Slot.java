package org.mcbot.datatypes.containers;

import org.mcbot.datatypes.XY;

public class Slot {
    public final static byte PIXELS_WIDE = 54;

    Item item;
    byte amount;
    XY location;

    public Slot(Item item, byte amount, XY location) {
        this.item = item;
        this.amount = amount;
        this.location = location;
    }
}