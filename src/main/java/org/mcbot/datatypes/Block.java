package org.mcbot.datatypes;

public class Block {
    public final Item item;
    public final boolean breathable;
    public final String brokenWith;

    public Block(Item item, boolean breathable, String brokenWith) {
        this.item = item;
        this.breathable = breathable;
        this.brokenWith = brokenWith;
    }
}
