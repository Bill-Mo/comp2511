package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.util.Position;

public class theOneRingTest {
    @Test
    public void getTheOneRingTest() {
        DungeonManiaController dc = new DungeonManiaController();
        dc.newGame("advanced", "Standard");
        Game game = dc.getCurrentGame();
        Character player = game.getCharacter();
        Position dummyPosition = new Position(1, 1);
        Mob enemy1 = new Mob("test1", "spider", dummyPosition, false, 1, 1);
        assertTrue(player.getInventory().isEmpty());
        TheOneRing.seed = 10;
        assertDoesNotThrow(() -> player.battle(enemy1));
        assertTrue(player.getInventory().isEmpty());

        Mob enemy2 = new Mob("test2", "spider", dummyPosition, false, 1, 1);
        TheOneRing.seed = 18;
        assertDoesNotThrow(() -> player.battle(enemy2));
        assertTrue(!player.getInventory().isEmpty());
    }

    @Test
    public void useTheOneRingTest() {
        DungeonManiaController dc = new DungeonManiaController();
        dc.newGame("advanced", "Standard");
        Game game = dc.getCurrentGame();
        Character player = game.getCharacter();
        TheOneRing testRing1 = new TheOneRing("testRing1", "the_one_ring", 1);
        player.addInventory(testRing1);
        player.setHealth(1);
        player.setAttack(20);
        assertEquals(player.getHealth(), 1);
        Position dummyPosition = new Position(1, 1);
        Mob enemy = new Mob("test1", "spider", dummyPosition, false, 1, 20);
        assertTrue(player.getInventory().contains(testRing1));
        assertDoesNotThrow(() -> player.battle(enemy));
        assertTrue(!player.getInventory().contains(testRing1));
        assertEquals(player.getHealth(), player.getMaxHealth());
    }
}
