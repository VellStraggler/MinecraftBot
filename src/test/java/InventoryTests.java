import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mcbot.ImageWork;
import org.mcbot.datatypes.Blocks;
import org.mcbot.datatypes.containers.Inventory;
import org.mcbot.datatypes.containers.Items;
import org.mcbot.skills.Movement;
import org.mcbot.wordwork.F3DataReader;

import java.awt.image.BufferedImage;

public class InventoryTests {

    @BeforeAll
    public void setup() {
        Items items = new Items();
        Blocks blocks = new Blocks(items);
        F3DataReader dataReader = new F3DataReader();
        Movement movement = new Movement(blocks, dataReader);
        Inventory inventory = new Inventory(items, movement);
    }

    @Test
    public void readInventoryWithCraftingTablePngImage() {
        BufferedImage image = ImageWork.retrieveImage("src/main/resources/inventory_with_chest.png");

    }
}
