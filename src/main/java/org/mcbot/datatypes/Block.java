package org.mcbot.datatypes;

import org.mcbot.datatypes.containers.Item;

public class Block {
    public final Item item;
    public final boolean breathable;
    public final String brokenWith;
    public final boolean probablyPlaced;

    public Block(Item item, boolean breathable, String brokenWith, boolean probablyPlaced) {
        this.item = item;
        this.breathable = breathable;
        this.brokenWith = brokenWith;
        this.probablyPlaced = probablyPlaced;
    }

    public class Builder {
        private Item item;
        private boolean breathable;
        private String brokenWith;
        private boolean probablyPlaced;

        public Builder() {
            // Initialize variables with default values
            this.item = null;
            this.breathable = false;
            this.brokenWith = "";
            this.probablyPlaced = false;
        }

        // Setters for each variable
        public Builder setItem(Item item) {
            this.item = item;
            return this;
        }

        public Builder setBreathable(boolean breathable) {
            this.breathable = breathable;
            return this;
        }

        public Builder setBrokenWith(String brokenWith) {
            this.brokenWith = brokenWith;
            return this;
        }

        public Builder setProbablyPlaced(boolean probablyPlaced) {
            this.probablyPlaced = probablyPlaced;
            return this;
        }

        // Method to build the object
        public Block build() {
            return new Block(item, breathable, brokenWith, probablyPlaced);
        }
    }

}
