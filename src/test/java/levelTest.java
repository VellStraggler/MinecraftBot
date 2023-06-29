import org.bookreader.CharRecognition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class levelTest {
    public boolean[][] e;

    @BeforeAll
    public void main() {
        e = new boolean[CharRecognition.HEIGHT][];
        e[3] = new boolean[]{true};
        e[5] = new boolean[]{true};
        e[2] = new boolean[]{false, false, false, false, false, false, true};
    }
    @Test
    public void levelOneTest() {
        assert(CharRecognition.getLevel(e)[1] == CharRecognition.levelOneOne[1]);

    }
}
