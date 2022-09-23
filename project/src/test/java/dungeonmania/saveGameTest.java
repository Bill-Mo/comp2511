package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class saveGameTest {
    @Test
    public void exceptionTest() {
        DungeonManiaController d = new DungeonManiaController();
        File f = new File("src/main/java/dungeonmania/save/test1.json");
        assertTrue(f.exists());
        assertThrows(IllegalArgumentException.class, () -> d.saveGame("test1"));
    }

    @Test
    public void successfulTest() {

        DungeonManiaController d = new DungeonManiaController();
        
        // Load a game
        DungeonResponse actualDungeon = d.loadGame("test1");
        // Delete the file if it exist
        File f = new File("src/main/java/dungeonmania/save/test1.json");

        if (f.exists()) {
            f.delete();
        }
        d.saveGame("test1");
        //DungeonResponse clearDungeon = d.newGame("advanced", "peaceful");
        DungeonResponse loadSavedDungeon = d.loadGame("test1");

        assertTrue(actualDungeon.getDungeonName().equals(loadSavedDungeon.getDungeonName()));
        assertListOfEntitiesEqual(actualDungeon.getEntities(), loadSavedDungeon.getEntities());
        assertListOfInventoryEqual(actualDungeon.getInventory(), loadSavedDungeon.getInventory());
        assertTrue(actualDungeon.getBuildables().equals(loadSavedDungeon.getBuildables()));
        assertTrue(actualDungeon.getGoals().equals(loadSavedDungeon.getGoals()));

    }
    
    public void assertListOfEntitiesEqual(List<EntityResponse> a, List<EntityResponse> b) {
        for (int i = 0; i < a.size(); i += 1) {
            EntityResponse entity_a = a.get(i);
            EntityResponse entity_b = b.get(i);
            Position position_a = entity_a.getPosition();
            Position position_b = entity_b.getPosition();
            assertEquals(entity_a.getId(), entity_b.getId());
            assertEquals(position_a.getX(), position_b.getX());
            assertEquals(position_a.getY(), position_b.getY());
            assertEquals(position_a.getLayer(), position_b.getLayer());
            assertEquals(entity_a.getType(), entity_b.getType());
        }
    }

    public void assertListOfInventoryEqual(List<ItemResponse> a, List<ItemResponse> b) {
        for (int i = 0; i < a.size(); i += 1) {
            ItemResponse entity_a = a.get(i);
            ItemResponse entity_b = b.get(i);
            assertEquals(entity_a.getId(), entity_b.getId());
            assertEquals(entity_a.getType(), entity_b.getType());
        }
    }
}
