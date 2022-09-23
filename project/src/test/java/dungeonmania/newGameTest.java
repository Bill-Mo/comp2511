package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;

public class newGameTest {
    @Test
    public void exceptionTest() {
        DungeonManiaController d = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> d.newGame("invalid dungeon name", "peaceful"));
        assertThrows(IllegalArgumentException.class, () -> d.newGame("advanced", "invalid game mode"));
    }

    @Test
    public void successfulTest() {
        DungeonManiaController d = new DungeonManiaController();
        DungeonResponse newDungeon = d.newGame("advanced", "Peaceful");
        List<EntityResponse> entities = newDungeon.getEntities();
        EntityResponse lowerRightCorner = entities.get(entities.size() - 1);
        assertEquals(lowerRightCorner.getType(), "wall");
        assertEquals(lowerRightCorner.getPosition().getX(), 17);
        assertEquals(lowerRightCorner.getPosition().getY(), 15);
        assertTrue(newDungeon.getInventory().isEmpty());
        assertTrue(newDungeon.getBuildables().isEmpty());
    }
    
}
