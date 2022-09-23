package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class EnemyMovementTest {
    @Test
    public void SpiderMovement() {
        Spider spider = new Spider("1", "Spider", new Position(1, 1), true, 10, 10);
        List<Entity> entities = new ArrayList<>();
        Wall wall = new Wall("2", "wall", new Position(2, 2), false);
        entities.add(wall);
        Character character = new Character("2", "Character", new Position(10, 10), true, 10, 10, new ArrayList<>(), new ArrayList<>(), "Standard");
        entities.add(character);

        /*Position spiderPos = spider.getPosition();
        int x = spiderPos.getX();
        int y = spiderPos.getY();

        assertEquals(x, 1);
        assertEquals(y, 0);*/
        
        //Checks if spider makes full rotation with walls in place
        spider.move(entities, character);
        Position spiderPos = spider.getPosition();
        int x = spiderPos.getX();
        int y = spiderPos.getY();

        assertEquals(x, 1);
        assertEquals(y, 0);
        assertTrue(spider.getPosition().equals(new Position(1, 0)));

        spider.move(entities, character);
        assertTrue(spider.getPosition().equals(new Position(2, 0)));
        
        spider.move(entities, character);
        assertTrue(spider.getPosition().equals(new Position(2 ,1)));

        spider.move(entities, character);
        assertTrue(spider.getPosition().equals(new Position(2, 2)));

        spider.move(entities, character);
        assertTrue(spider.getPosition().equals(new Position(1, 2)));

        spider.move(entities, character);
        assertTrue(spider.getPosition().equals(new Position(0, 2)));

        spider.move(entities, character);
        assertTrue(spider.getPosition().equals(new Position(0, 1)));

        spider.move(entities, character);
        assertTrue(spider.getPosition().equals(new Position(0 ,0)));

        spider.move(entities, character);
        assertTrue(spider.getPosition().equals(new Position(1 ,0)));

        //checks if spider change direction after seeing boulder
        Boulder boulder = new Boulder("3", "boulder", new Position(2, 1), true);
        Boulder boulder2 = new Boulder("4", "boulder", new Position(0, 1), true);
        entities.add(boulder);
        entities.add(boulder2);

        spider.move(entities, character);
        assertTrue(spider.getPosition().equals(new Position(2, 0)));

        spider.move(entities, character);
        assertTrue(spider.getPosition().equals(new Position(1, 0)));
        
        spider.move(entities, character);
        assertTrue(spider.getPosition().equals(new Position(0, 0)));

        spider.move(entities, character);
       
        assertTrue(spider.getPosition().equals(new Position(1, 0)));
        spider.move(entities, character);
        assertTrue(spider.getPosition().equals(new Position(2, 0)));

    }

    @Test
    public void ZombieMovement() {
        Zombie zombie = new Zombie("1", "zombie_toast", new Position(5, 5), true, 10, 10);
        List<Entity> entities = new ArrayList<>();
        Character character = new Character("2", "Character", new Position(10, 10), true, 10, 10, new ArrayList<>(), new ArrayList<>(), "Standard");
        entities.add(character);

        zombie.move(entities, character);

        //checks if zombie moves
        assertFalse(zombie.getPosition().equals(new Position(5, 5)));
        
        //checks where zombie goes if only 1 option
        Zombie zombie2 = new Zombie("1", "zombie_toast", new Position(1, 1), true, 10, 10);
        Wall wall = new Wall("2", "wall", new Position(1, 0), true);
        entities.add(wall);
        Wall wall1 = new Wall("2", "wall", new Position(0, 1), true);
        entities.add(wall1);
        Wall wall2 = new Wall("2", "wall", new Position(1, 2), true);
        entities.add(wall2);

        zombie2.move(entities, character);
        assertTrue(zombie2.getPosition().equals(new Position(2,1)));
    }

    @Test
    public void MercenaryMovement() {
        Mercenary mercenary = new Mercenary("1", "Mercenary", new Position(1, 1), true, 10, 10, 10, false);
        List<Entity> entities = new ArrayList<>();
        Character character = new Character("2", "Character", new Position(1, 5), true, 10, 10, new ArrayList<>(), new ArrayList<>(), "Standard");
        entities.add(character);
        entities.add(mercenary);
        character.setEntities(entities);

        mercenary.move(entities, character);
        assertTrue(mercenary.getPosition().equals(new Position(1, 2)));

        mercenary.move(entities, character);
        assertTrue(mercenary.getPosition().equals(new Position(1, 3)));

        mercenary.move(entities, character);
        assertTrue(mercenary.getPosition().equals(new Position(1, 4)));

        character.move(Direction.RIGHT);

        // mercenary moves onto adjacent position of character
        mercenary.move(entities, character);
        assertTrue(Position.isAdjacent(character.getPosition(), mercenary.getPosition()));

        // mercenary moves onto same position as character
        mercenary.move(entities, character);
        assertEquals(character.getPosition(), mercenary.getPosition());
    }
}
