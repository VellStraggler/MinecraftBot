package org.mcbot.datatypes;

public class Item {
//    private Recipe recipe;
//    private Use use;
//    private Directions howToGet;

    public final String name;
    public final boolean placeable;
    public final boolean edible;
    public Item (String name, boolean placeable, boolean edible) {
        this.name = name;
        this.placeable = placeable;
        this.edible = edible;
    }

}
