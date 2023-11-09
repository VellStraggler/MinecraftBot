import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mcbot.ImageWork;
import org.mcbot.datatypes.Blocks;
import org.mcbot.datatypes.XY;
import org.mcbot.datatypes.containers.*;
import org.mcbot.datatypes.containers.Items;
import org.mcbot.datatypes.containers.Slot;
import org.mcbot.skills.Movement;
import org.mcbot.wordwork.F3DataReader;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mcbot.datatypes.containers.Inventory.TEXT_OFFSET;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InventoryTests {

    public Items items;
    public Blocks blocks;
    public F3DataReader dataReader;
    public Movement movement;
    public Inventory inventory;
    @BeforeAll
    public void setup() {
        Items items = new Items();
        Blocks blocks = new Blocks(items);
        F3DataReader dataReader = new F3DataReader();
        Movement movement = new Movement(blocks, dataReader);
        inventory = new Inventory(items, movement);
    }

    @Test
    public void readInventoryWithCraftingTablePngImage() {
        BufferedImage image = ImageWork.retrieveImage("src/main/resources/inventory_with_crafting_table");
        XY slotCoord = inventory.getSlotCoordinates(4, 0);
        Slot slot = inventory.readSlot(image, (int)(slotCoord.x), (int)(slotCoord.y));
        assertEquals("Coal", slot.getItem().name);
    }
}
