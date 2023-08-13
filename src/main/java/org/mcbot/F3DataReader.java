package org.mcbot;

import org.mcbot.datatypes.RGB;
import org.mcbot.datatypes.XY;
import org.mcbot.datatypes.XYZ;

import java.awt.image.BufferedImage;
import java.util.function.Supplier;

/**
 * This detects if F3 data is on, and then
 * stores everything perceived as useful from it
 * in an F3Data Object
 */
public class F3DataReader {
    public final int VK_F3 = 114;
    private F3Data data;
    private BufferedImage screen;

    /** Used to extract data from screenshots. **/
    public F3DataReader() {}

    /** Takes a screenshot and returns the data from it **/
    public F3Data readScreen() {
        // Take a screenshot or use the current one
        screen = Utils.takeScreenshot();

        // Detect if F3 mode is on. This checks the top left corner of 'Minecraft ver...etc'
        if (!RGB.F3_WHITE.equals(screen,7,7) && !RGB.F3_WHITE.equals(screen,4,4)) {
            // Press F3 if that isn't the case
            Utils.pressAndReleaseKey(VK_F3);
            // Might be too quick
            Utils.sleep(100);
            // Obviously we need a new screenshot now
            screen = Utils.takeScreenshot();
        }
        // Check if there is a targeted block
        boolean targetedBlock = (!Utils.isWhite(screen, 1186,301) && Utils.isWhite(screen,1189,304));
        // Read the resolution first. Read the whole line and find the numbers within it
        String resolutionLine = new CharRecognition(screen, F3Data.RESOLUTION, RGB.F3_WHITE)
                                    .readToImageEdge();
        if (!resolutionLine.contains(Utils.SCREEN_RESOLUTION.x + "") ||
            !resolutionLine.contains(Utils.SCREEN_RESOLUTION.y + "")) {
            // Throw an error if the resolution is incorrect
            throw new RuntimeException("Screen resolution is not what it should be. Text read: " + resolutionLine);
        }
        Utils.p(resolutionLine);
        // Read data starting at all given points
        for (XY dataLineStart: F3Data.RIGHT_SIDE) {
            Utils.p(new CharRecognition(screen, dataLineStart, RGB.F3_WHITE).readToImageEdge());
        }
        for (XY dataLineStart: F3Data.LEFT_SIDE) {
            Utils.p(new CharRecognition(screen, dataLineStart, RGB.F3_WHITE).readToThreeSpaces());
        }

// ------------- LAMBDAS ------------------------------------------
        Supplier<XYZ> getCoordinates = () -> {
            String init = new CharRecognition(screen, F3Data.COORDINATES, RGB.F3_WHITE).readToThreeSpaces();
            int x = Integer.parseInt(init.substring(0, init.indexOf(" ")));
            init = init.substring(init.indexOf(" ") + 1);
            int y = Integer.parseInt(init.substring(0, init.indexOf(" ")));
            init = init.substring(init.indexOf(" ") + 1);
            int z = Integer.parseInt(init.substring(0, init.indexOf(" ")));
            return new XYZ(x,y,z);
        };
        Supplier<Integer> getDay = () -> {
            String init = new CharRecognition(screen, F3Data.DAY, RGB.F3_WHITE).readToThreeSpaces();
            return Integer.parseInt(init.substring(init.indexOf("(") + 5, init.indexOf(")")));
        };
        Supplier<XY> getFacing = () -> {
            String init = new CharRecognition(screen, F3Data.FACING, RGB.F3_WHITE).readToThreeSpaces();
            init = init.substring(init.indexOf(") (")+3);
            double x = Double.parseDouble(init.substring(0, init.indexOf(" ")));
            double y = Double.parseDouble(init.substring(init.indexOf("/ ")+2,init.indexOf(")")));
            return new XY((int)(x*10),(int)(y*10));
        };
        Supplier<XYZ> getTargetedBlock = () -> {
            String init = new CharRecognition(screen, F3Data.TARGETED_BLOCK, RGB.F3_WHITE).readToImageEdge();
            init = init.replaceAll(" ","");
            String[] ints = init.split(",");
            return new XYZ(Integer.parseInt(ints[0]),Integer.parseInt(ints[1]),Integer.parseInt(ints[2]));
        };
        Supplier<Integer> getLightLevel = () -> {
            String init = new CharRecognition(screen, F3Data.LIGHT_LEVEL, RGB.F3_WHITE).readToThreeSpaces();
            return Integer.parseInt(init.substring(0, init.indexOf(" ")));
        };
// ------------ END OF LAMBDAS -----------------------------------

        // Save it all to an F3Data object
        F3Data data = new F3Data.Builder()
                .withBiome(new CharRecognition(screen, F3Data.BIOME, RGB.F3_WHITE).readToThreeSpaces())
                .withMineableHow(new CharRecognition(screen, F3Data.MINEABLE_HOW, RGB.F3_WHITE).readToImageEdge())
                .withTargetedBlockType(new CharRecognition(screen, F3Data.BLOCK_TYPE, RGB.F3_WHITE).readToImageEdge())
                .withCoordinates(getCoordinates.get())
                .withDay(getDay.get())
                .withFacing(getFacing.get())
                .withTargetedBlock(getTargetedBlock.get())
                .withLightLevel(getLightLevel.get())
                .build();
        // return data
        return data;
    }
}
