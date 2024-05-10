import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mcbot.ImageWork;
import org.mcbot.Utils;
import org.mcbot.datatypes.Blocks;
import org.mcbot.datatypes.containers.Items;
import org.mcbot.skills.Movement;
import org.mcbot.wordwork.F3DataReader;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** You will receive 4 seconds of prep time to open the game **/
public class PerformanceTests {
    public static Blocks blocks;
    public static Movement movement;
    @BeforeAll
    public static void main() {
        Utils.sleep(4000);
        blocks = new Blocks(new Items());
        movement = new Movement(blocks, new F3DataReader());
    }
    @Test
    public void BaseFrameRateForScreenshots() {
        long start = System.currentTimeMillis();
        double count = 0;
        while(System.currentTimeMillis() - start < 10000) {
            BufferedImage image = ImageWork.takeScreenshot();
            count++;
        }
        assertTrue(count >= 200, "screenshots should at least run at 20 fps.");
        Utils.p(count/10 + " screenshots per second");
        Utils.beep();
    }
    @Test
    public void FrameRateIncludingDataUpdates() {
        long time = System.currentTimeMillis();
        long finalTime = time + 20000;
        int count = 0;
        while(time < finalTime) {
            movement.update();
            count++;
            time = System.currentTimeMillis();
        }
        assertTrue(count >= 500, "screenshots should at least run at 20 fps.");
        Utils.p(count/20 + " screenshots per second");
        Utils.beep();
    }
    @Test
    public void TurningIsFunctional() {
        for(int i = 0 ; i < 10; i ++ ) {
            movement.turnLeft();
        }
        for(int i = 0 ; i < 5; i ++ ) {
            movement.turnAround();
        }
    }
}
