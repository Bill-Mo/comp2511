package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

public class MiscTest {
    @Test
    public void testDungeons() {
        assertTrue(DungeonManiaController.dungeons().size() > 0);
        assertTrue(DungeonManiaController.dungeons().contains("maze"));
    }
}