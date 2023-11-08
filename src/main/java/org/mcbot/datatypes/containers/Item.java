package org.mcbot.datatypes.containers;

public class Item {
//    private Recipe recipe;
//    private Use use;
//    private Directions howToGet;

    public final String name;
    public final Boolean placeable;
    public final Boolean edible;
    public Item (String name, Boolean placeable, Boolean edible) {
        this.name = name;
        this.placeable = placeable;
        this.edible = edible;
    }

}
