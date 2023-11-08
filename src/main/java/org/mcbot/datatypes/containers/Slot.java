package org.mcbot.datatypes.containers;

import org.mcbot.datatypes.XY;

public class Slot {
    public final static byte PIXELS_WIDE = 54;

    public Item item;
    public byte amount;
    public XY location;

    public Slot(Item item, byte amount, XY location) {
        this.item = item;
        this.amount = amount;
        this.location = location;
    }

    public Item getItem() {
        return item;
    }
}