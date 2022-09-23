package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dungeonmania.util.Direction;

public class SwampTileTest {
    @Test
    public void slowEnemyTest() {
        DungeonManiaController dc = new DungeonManiaController();
        dc.newGame("swampTile", "Standard");
        Game game = dc.getCurrentGame();
        ArrayList<SwampTile> sw = game.getSwampTilePosition();
        assertEquals(sw.size(), 5);
        assertEquals(sw.get(0).getMovementFactor(), 3);
        int x = sw.get(0).getPosition().getX();
        int y = sw.get(0).getPosition().getY();
        assertEquals(x, 2);
        assertEquals(y, 4);

        Mercenary m = null;
        for (Entity e : game.getEntities()) {
            if (e instanceof Mercenary) {
                m = (Mercenary) e;
            }
        }

        assertEquals(m.getDelayMovementCount(), 0);
        game.tick(null, Direction.RIGHT);
        x = m.getPosition().getX();
        y = m.getPosition().getY();
        assertEquals(x, 2);
        assertEquals(y, 4);
        assertEquals(sw.get(0).getMovementFactor(), 3);
        assertEquals(m.getDelayMovementCount(), 3);
        game.tick(null, Direction.RIGHT);
        game.tick(null, Direction.RIGHT);
        game.tick(null, Direction.RIGHT);
        game.tick(null, Direction.RIGHT);
        x = m.getPosition().getX();
        y = m.getPosition().getY();
        assertEquals(x, 3);
        assertEquals(y, 4);
    }
}
