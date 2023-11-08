package org.mcbot.datatypes;

import org.mcbot.datatypes.containers.Items;

import java.util.HashMap;
import java.util.Map;

public class Blocks {
    private Map<String, Block> blocks = new HashMap<>();
    private Items items;
    public Blocks(Items items) {
        this.items = items;
        buildToMap("carrots", "Carrot", true, "hoe");
        buildToMap("dirt", false, "shovel");
        buildToMap("wheat",true,"hoe");
        buildToMap("potatoes", "Potato", true,"hoe");

    }
    public boolean contains(String name) {
        return blocks.containsKey(name);
    }
    public Block get(String name) {
        if (!contains(name)) {
            buildToMap(name, false, "unknown");
        }
        return blocks.get(name);
    }
    private void buildToMap(String name, boolean breathable, String brokenWith){
        String itemName = name.substring(0,1).toUpperCase() + name.substring(1);
        buildToMap(name,itemName,breathable,brokenWith);
    }
    private void buildToMap(String name, String itemName, boolean breathable, String brokenWith){
        blocks.put(name, new Block(items.get(itemName),
                breathable, brokenWith));
    }
}
