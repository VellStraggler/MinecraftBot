package org.mcbot.datatypes.containers;

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

    public void addItem(Item newItem) {
        items.put(newItem.name, newItem);
    }

    public boolean contains(String key) {
        if(items.containsKey(key)) return true;
        return false;
    }
}
