package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class CharacterMovementTest {

    @Test
    public void BasicMovement() {
        Character character = new Character("1", "player", new Position(5, 5), true, 10, 10, new ArrayList<>(), new ArrayList<>(), "Standard");
        
        character.move(Direction.UP);
        assertTrue(character.getPosition().equals(new Position(5, 4)));

        character.move(Direction.DOWN);
        assertTrue(character.getPosition().equals(new Position(5, 5)));

        character.move(Direction.LEFT);
        assertTrue(character.getPosition().equals(new Position(4, 5)));

        character.move(Direction.RIGHT);
        assertTrue(character.getPosition().equals(new Position(5, 5)));
    }
}
