package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class loadGameTest {
    @Test
    public void exceptionTest() {
        DungeonManiaController d = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> d.loadGame("invalid name"));
    }

    @Test
    public void successfulTest() {
        File f = new File("src/main/java/dungeonmania/save/test1.json");
        assertTrue(f.exists());
        
        String dungeonId = "test1";
            
        String dungeonName = "advanced";

        List<EntityResponse> entities = new ArrayList<>();
        EntityResponse entity1 = new EntityResponse("wall0", "wall", new Position(0, 0), false);
        EntityResponse entity2 = new EntityResponse("wall1", "wall", new Position(0, 1), false);
        EntityResponse entity3 = new EntityResponse("wall2", "wall", new Position(0, 2), false);
        EntityResponse entity4 = new EntityResponse("wall3", "wall", new Position(0, 3), false);
        EntityResponse entity5 = new EntityResponse("wall4", "wall", new Position(0, 4), false);
        EntityResponse entity6 = new EntityResponse("treasure5", "treasure", new Position(0, 6), false);
        EntityResponse entity7 = new EntityResponse("zombie_toast6", "zombie_toast", new Position(3, 5), false);
        EntityResponse entity8 = new EntityResponse("player7", "player", new Position(1, 1), false);

        entities.add(entity1);
        entities.add(entity2);
        entities.add(entity3);
        entities.add(entity4);
        entities.add(entity5);
        entities.add(entity6);
        entities.add(entity7);
        entities.add(entity8);
        
        List<ItemResponse> inventory = new ArrayList<>();
        ItemResponse item1 = new ItemResponse("wood1", "wood");
        ItemResponse item2 = new ItemResponse("treasure2", "treasure");
        ItemResponse item3 = new ItemResponse("key3", "key");
        ItemResponse item4 = new ItemResponse("wood4", "wood");
        inventory.add(item1);
        inventory.add(item2);
        inventory.add(item3);
        inventory.add(item4);

        List<String> buildables = new ArrayList<>();
        buildables.add("shield");

        String goals = "( :enemies AND :treasure )";

        DungeonManiaController d = new DungeonManiaController();
        DungeonResponse loadedDungeon = d.loadGame("test1");

        assertTrue(loadedDungeon.getDungeonName().equals(dungeonName));
        assertListOfEntitiesEqual(loadedDungeon.getEntities(), entities);
        assertListOfInventoryEqual(loadedDungeon.getInventory(), inventory);
        assertTrue(loadedDungeon.getBuildables().equals(buildables));
        assertTrue(loadedDungeon.getGoals().equals(goals));
    }

    public void assertListOfEntitiesEqual(List<EntityResponse> a, List<EntityResponse> b) {
        for (int i = 0; i < a.size(); i++) {
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

    public boolean assertListOfInventoryEqual(List<ItemResponse> a, List<ItemResponse> b) {
        for (int i = 0; i < a.size(); i++) {
            ItemResponse entity_a = a.get(i);
            ItemResponse entity_b = b.get(i);
            assertEquals(entity_a.getId(), entity_b.getId());
            assertEquals(entity_a.getType(), entity_b.getType());
            return true;
        }
        return false;
    }
}
