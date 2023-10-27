import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mcbot.ImageWork;
import org.mcbot.Utils;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** You will receive 4 seconds of prep time to open the game **/
public class PerformanceTests {
    @BeforeAll
    public static void main() {
        Utils.sleep(4000);
    }
    @Test
    public void BaseFramerateForScreenshots() {
        long start = System.currentTimeMillis();
        double count = 0;
        while(System.currentTimeMillis() - start < 10000) {
            BufferedImage image = ImageWork.takeScreenshot();
            count++;
        }
        assertTrue(count >= 200, "screenshots should at least run at 20 fps. This ran at " + count/10);
        Utils.p(count/10 + " screenshots per second");
    }
}
