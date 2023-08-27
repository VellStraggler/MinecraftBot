package org.mcbot.datatypes;

import java.util.HashMap;
import java.util.Map;

public class Items {
    private Map<String, Item> items;
    public Items() {
        items = new HashMap<>();
        items.put("Carrot", new Item("Carrot",true, true));
        items.put("Dirt", new Item("Dirt",true, false));
        items.put("Potato",new Item("Potato", true, true));
        items.put("Wheat",new Item("Potato", true, true));
    }

    public Item get(String key) {
        return items.get(key);
    }
}
