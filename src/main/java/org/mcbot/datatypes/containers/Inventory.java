package org.mcbot.datatypes.containers;

import org.mcbot.ImageWork;
import org.mcbot.Utils;
import org.mcbot.datatypes.*;
import org.mcbot.skills.Movement;
import org.mcbot.wordwork.CharRecognition;

import java.awt.image.BufferedImage;

public class Inventory implements Container {
    // This is the top-left corner of the top-left slot.
    private static final XY CORNER_WITH_SHORTCUTS_OPEN = new XY(792,443);
    // This is the same for almost any container that is opened, regardless of a potion effect
    private static final XY CORNER = new XY(561, 453);
    private static final XY CORNER_WITH_DOUBLE_CHEST_OPEN = new XY(561,534);
//    public static final XY TEXT_OFFSET = new XY(61, -38);
    public static final XY TEXT_OFFSET = new XY(34, -35);
    private static final XY TEXT_CHECK_FOR_BLACK = new XY(77, -38);
    private static final short WIDTH = 9;
    private static final short HEIGHT = 3;
    private static final short HOTBAR_PADDING = 12;


    private Slot[][] storage;
    private Slot[] hotbar;
    public Movement movement;
    public Blocks blocks;
    public Items items;
    public CharRecognition charRecognition;
    private boolean recipeShortcutsOpen;
    private Container containerOpened;

    public Inventory(Items items, Movement movement) {
        this.movement = movement;
        this.blocks = movement.getBlocks();
        this.items = items;

        charRecognition = new CharRecognition(null, null, RGB.HAND_WHITE);

        storage = new Slot[HEIGHT+1][WIDTH];
        hotbar = storage[HEIGHT];
    }


    /** Reads through entire inventory excluding armor and shield slots. **/
    public void readContents() {
        //move to the first corner
        int x = (int) CORNER.x;
        int y = (int) CORNER.y;

        //iterate through all slots
        for(int r = 0; r < HEIGHT; r++) {
            readRow(x, y, r);
            // set up next row
            y += Slot.PIXELS_WIDE;
            x = (int) CORNER.x;
        }
        readHotBar();
    }
    public XY getSlotCoordinates(int x, int y) {
        return new XY(CORNER.x + (Slot.PIXELS_WIDE * x), CORNER.y + (Slot.PIXELS_WIDE * y));
    }
    public void readRow(int x, int y, int r) {
        for(int c = 0; c < WIDTH; c++) {
            // hover to the slot
            movement.moveMouseHere(x, y);
            // allow screen to refresh;
            Utils.sleepOneFrame();
            // take a photo
            BufferedImage image = ImageWork.takeScreenshot();
            // check for text box
            storage[r][c] = readSlot(image, x, y);
            x += Slot.PIXELS_WIDE;
        }
    }
    /** x and y refer to pixel coordinates. **/
    public Slot readSlot(BufferedImage image, int x, int y) {
        if(!ImageWork.getRGB(image, x + (int)TEXT_CHECK_FOR_BLACK.x, y + (int)TEXT_CHECK_FOR_BLACK.y).isDark()) {
            return null;
        }
        // read the text found
        XY slotLoc = new XY(x+ TEXT_OFFSET.x, y + TEXT_OFFSET.y);
        charRecognition.update(image, slotLoc);
        String itemName = charRecognition.readToThreeSpaces();
        // FIXME: read the amount
        // if there isn't an amount, put it in as 1
        byte amount = 1;

        // check if the item exists in the Items database
        Item item;
        if(!items.contains(itemName)) {
            item = new Item(itemName, null, null);
            // if not, add it to the database
            items.addItem(item);

        }
        item = items.get(itemName);
        return new Slot(item, amount, slotLoc);
    }

    public void readHotBar() {
        int x = (int) CORNER.x;
        int y = (int) CORNER.y + (Slot.PIXELS_WIDE*HEIGHT) + HOTBAR_PADDING;
        readRow(x, y, HEIGHT);
    }

    /** Checks if the inventory is open or not through a screenshot. **/
    private boolean isOpen() {
        return false;
    }
    @Override
    public XY getStartingCorner() {
        return CORNER;
    }

    @Override
    public XY getDimensions() {
        return new XY(WIDTH, HEIGHT);
    }

    @Override
    public Slot[][] getSlots() {
        return storage;
    }
}
