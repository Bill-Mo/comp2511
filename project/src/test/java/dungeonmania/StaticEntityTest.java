package dungeonmania;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;


public class StaticEntityTest {
    @Test
    public void teleportCorrectColourTest() {
        Character character = new Character("c1", "player", new Position(0, 0), true, 100, 10, new ArrayList<>(), new ArrayList<>(), "Standard");
        List<Entity> entities = new ArrayList<>();
        JSONObject object = new JSONObject();
        object.put("goal", "enemies");
        Game g = new Game("portals.json", "standard", entities, new ArrayList<>(), new ArrayList<>(), object, character);
        
        Portal p1 = new Portal("portal1", "portal", new Position(1, 0), false, "blue");
        entities.add(p1);

        Portal p2 = new Portal("portal2", "portal", new Position(4,0), false, "blue");
        entities.add(p2);
        character.goThroughPortal(entities, p1.getPosition());

        assertEquals(character.getPosition(), p2.getPosition());
    }

    @Test
    public void DoorTest() {
        
        List<Entity> entities = new ArrayList<>();
        List<Items> inventory = new ArrayList<>();
        JSONObject object = new JSONObject();
        object.put("goal", "enemies");
        Character character = new Character("c1", "player", new Position(0, 0), false, 100, 10, inventory, new ArrayList<>(), "Standard");
        Game g = new Game("empty.json", "standard", entities, inventory, new ArrayList<>(), object, character);
        
        Door d = new Door("d1", "door", new Position(0,1), false, false, 1);
        Key k = new Key("k1", "key", 1, 1);
        
        entities.add(d);
        inventory.add(k);
        //character.useItem("key");
        character.checkDoor(entities, new Position(0,1));
        assertTrue(d.isOpen());
        assertEquals("door_unlocked", d.getType());

    }

    
    @Test
    public void BombTest() {
        List<Entity> entities = new ArrayList<>();
        List<Items> inventory = new ArrayList<>();
        JSONObject object = new JSONObject();
        object.put("goal", "enemies");
        Character character = new Character("c1", "player", new Position(0, 0), false, 100, 10, inventory, new ArrayList<>(), "Standard");
        Game g = new Game("empty.json", "standard", entities, inventory, new ArrayList<>(), object, character);
        FloorSwitch floorSwitch = new FloorSwitch("s1", "switch", new Position(0,2), false);
        Bomb b = new Bomb("b1", "bomb", 1);
        inventory.add(b);
        Boulder b1 = new Boulder("b1", "boulder", new Position(0,1), false);
        Boulder b2 = new Boulder("b2", "boulder", new Position(0,3), false);
        entities.add(floorSwitch);
        entities.add(character);
        entities.add(b1);
        entities.add(b2);
        
        //entities.add(new Entity("bomb", "bomb", new Position(1,2), false));
        b.use(character);
        floorSwitch.checkBoulder(entities, new Position(0,1));
        floorSwitch.checkSwitchOn(entities, new Position(0,1));
        floorSwitch.explode(entities, new Position(1,2));
        assertEquals(1, entities.size());
    }
    
    
    @Test
    public void ZombieToastSpawnerTest() {
        List<Entity> entities = new ArrayList<>();
        List<Items> inventory = new ArrayList<>();
        JSONObject object = new JSONObject();
        object.put("goal", "enemies");
        Character character = new Character("c1", "player", new Position(0, 0), false, 100, 10, inventory, new ArrayList<>(), "Standard");
        Game g = new Game("empty.json", "standard", entities, inventory, new ArrayList<>(), object, character);
        entities.add(character);
        ZombieToastSpawner z1 = new ZombieToastSpawner("spawner", "zombie_toast_spawner", new Position(5,5), false);
        entities.add(z1);

        //40 ticks
        IntStream.range(0, 40).forEach(i -> {
            g.tick(null, Direction.NONE);

        });

        long count = entities
            .stream()
            .filter(e -> e.getType().equals("zombie_toast"))
            .count();
       
        assertEquals(2, count);
    }


    @Test
    public void destroySpawnerTest() {
        List<Entity> entities = new ArrayList<>();
        List<Items> inventory = new ArrayList<>();
        JSONObject object = new JSONObject();
        object.put("goal", "enemies");
        Character character = new Character("c1", "player", new Position(0, 0), false, 100, 10, inventory, new ArrayList<>(), "Standard");
        Sword s = new Sword("s1", "sword", 5);
        inventory.add(s);
        ZombieToastSpawner spawner = new ZombieToastSpawner("spawner", "zombie_toast_spawner", new Position(0, 1), true);
        entities.add(spawner);
        entities.add(character);
        Game g = new Game("empty.json", "standard", entities, inventory, new ArrayList<>(), object, character);

        g.interactSpawner("spawner");
        List<Entity> entitiesCheck = new ArrayList<>();
        entitiesCheck.add(character);
        //checks the list off entities to make sure that spawner has been removed
        assertEquals(entitiesCheck, entities);

    }

}
