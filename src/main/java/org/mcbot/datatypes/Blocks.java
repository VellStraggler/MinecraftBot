package org.mcbot.datatypes;

import org.mcbot.datatypes.containers.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blocks {
    private Map<String, Block> blocks = new HashMap<>();
    private Items items;
    public Blocks(Items items) {
        this.items = items;
        addToMap("carrots", "Carrot", true, "hoe",true);
        addToMap("dirt", "Dirt", "shovel");
        addToMap("wheat","Wheat", true,"hoe",true);
        addToMap("oak_log", "Oak Log", "axe");
        addToMap("birch_planks", "Birch Planks", false, "axe", true);
        addToMap("oak_planks", "Oak Planks", false, "axe", true);
        addToMap("grass_block", "Grass Block", "shovel");
        addToMap("gravel", "Gravel", "shovel");
        addToMap("sand", "Sand", "shovel");
        addToMap("polished_stone_slab", "Polished Stone Slab", false, "pickaxe", true);
        addToMap("stone_bricks", "Stone Bricks", false, "pickaxe",true);
        addToMap("cobblestone", "Cobblestone", false, "pickaxe", true);
        addToMap("cobblestone_stairs", "Cobblestone Stairs", false, "pickaxe", true);
        addToMap("polished_diorite", "Polished Diorite", false, "pickaxe", true);
        addToMap("polished_andesite", "Polished Andesite", false, "pickaxe", true);
        addRocks(new ArrayList<>(List.of(
                "Stone","Diorite","Andesite","Granite", "Deepslate", "Tuff",
                "Deepslate Redstone Ore", "Deepslate Iron Ore", "Deepslate Diamond Ore", "Deepslate Copper Ore",
                "Redstone Ore", "Iron Ore", "Diamond Ore", "Copper Ore", "Coal Ore")));

    }
    public boolean contains(String name) {
        return blocks.containsKey(name);
    }
    public Block get(String name) {
        if (!contains(name)) {
            addToMap(name, "unknown", "unknown");
        }
        return blocks.get(name);
    }
    /** Does not include whether it was placed. This is a privatized action**/
    public void addToMap(String name, String itemName, String brokenWith){
        blocks.put(name, new Block(items.get(itemName),
                false, brokenWith, false));
    }
    private void addToMap(String name, String itemName, boolean breathable, String brokenWith, boolean probablyPlaced){
        blocks.put(name, new Block(items.get(itemName),
                breathable, brokenWith, probablyPlaced));
    }
    public void addToMap(String name, Block block) {
        blocks.put(name, block);
    }
    /** Takes a list of item names, NOT block names **/
    public void addRocks(List<String> rocks) {
        for (String rock: rocks) {
            String block = rock.toLowerCase().replaceAll(" ","_");
            addToMap(block, rock, false, "pickaxe", false);
        }
    }
}
