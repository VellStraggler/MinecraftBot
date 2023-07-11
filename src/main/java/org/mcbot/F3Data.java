package org.mcbot;

/**
 * Stores all data deemed important from an F3 screenshot.
 * The most boilerplatiest code there ever was.
 * Constants only work with a 1600 x 900 resolution.
 */
public class F3Data {
    public static XY COORDINATES =  new XY(103,304);
    public static XY FACING =       new XY(121,358);
    public static XY LIGHT_LEVEL =  new XY(190,385);
    public static XY BIOME =        new XY(250,466);
    public static XY TARGETED_BLOCK=new XY(1435,304);
    public static XY RESOLUTION =   new XY(1006,196);
    public static XY BLOCK_TYPE =   new XY(1441,331);
    public static XY MINEABLE_HOW = new XY(1486,358);
    public static XY DAY =          new XY(523,493);
    public static XY[] LEFT_SIDE = {COORDINATES, FACING, LIGHT_LEVEL, BIOME, DAY};
    // RESOLUTION is omitted because if it is wrong, the rest of the list is not worth reading
    public static XY[] RIGHT_SIDE = {TARGETED_BLOCK, BLOCK_TYPE, MINEABLE_HOW};
    public final XYZ coordinates;
    public final XY facing;
    public final int lightLevel;
    public final String biome;
    public final XYZ targetedBlock;
    public final String targetedBlockType;
    public final String mineableHow;
    public final int day;

    public F3Data(Builder b) {
        this.coordinates = b.coordinates;
        this.facing = b.facing;
        this.lightLevel = b.lightLevel;
        this.biome = b.biome;
        this.targetedBlock = b.targetedBlock;
        this.targetedBlockType = b.targetedBlockType;
        this.mineableHow = b.mineableHow;
        this.day = b.day;
    }
    public Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private XYZ coordinates;
        private XY facing;
        private int lightLevel;
        private String biome;
        private XYZ targetedBlock;
        private String targetedBlockType;
        private String mineableHow;
        private int day;

        public F3Data build() {
            return new F3Data(this);
        }
        public Builder withCoordinates(XYZ c) {
            this.coordinates = c;
            return this;
        }
        public Builder withFacing(XY f) {
            this.facing = f;
            return this;
        }
        public Builder withLightLevel(int l) {
            this.lightLevel = l;
            return this;
        }
        public Builder withBiome(String b) {
            biome = b;
            return this;
        }
        public Builder withTargetedBlock(XYZ t) {
            targetedBlock = t;
            return this;
        }
        public Builder withTargetedBlockType(String t) {
            targetedBlockType = t;
            return this;
        }
        public Builder withMineableHow(String m) {
            mineableHow = m;
            return this;
        }
        public Builder withDay(int d) {
            day = d;
            return this;
        }
    }
    public String toString() {
        return "\nCoordinates: " + coordinates.toString() + "\n"
                +"Facing: " + facing.toString() + "\n"
                +"Light level: " + lightLevel + "\n"
                +"Biome: " + biome + "\n"
                +"Targeted block: " + targetedBlock.toString() + "\n"
                +"Targeted block type: " + targetedBlockType + "\n"
                +"Mineable how: " + mineableHow + "\n"
                +"Day: " + day;
    }

}
