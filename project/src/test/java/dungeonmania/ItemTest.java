package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ItemTest {
    // Checking item is now present in inventory of player when added
    @Test
    public void itemAdd() {
        Position start = new Position(10, 10);
        List<Items> items = new ArrayList<>();
        Character c1 = new Character("player", "Character", start, false, 100, 10, items, new ArrayList<>(), "Standard");
        Items i1 = new Sword("s1", "sword", 4);
        c1.addInventory(i1);
        assertEquals(new ArrayList<Items>(Arrays.asList(i1)), c1.getInventory());
    }
    
    // Using an item should cause it to lose a point of durability
    @Test
    public void useItem() {
        Position start = new Position(10, 10);
        List<Items> items = new ArrayList<>();
        Character c1 = new Character("player", "Character", start, false, 100, 10, items, new ArrayList<>(), "Standard");
        Items i1 = new Sword("s1", "sword", 4);
        c1.addInventory(i1);
        c1.useItem("s1");
        assertEquals(3, i1.getDurability());
    }
    
    // When Item runs out of durability, it should 'break' disappearing from inventory
    @Test
    public void breakItem() {
        Position start = new Position(10, 10);
        List<Items> items = new ArrayList<>();
        Character c1 = new Character("player", "Character", start, false, 100, 10, items, new ArrayList<>(), "Standard");
        Items i1 = new Items("i1", "sword", 1);
        c1.addInventory(i1);
        assertEquals(new ArrayList<Items>(Arrays.asList(i1)), c1.getInventory());
        c1.useItem("i1");
        assertEquals(0, c1.getInventory().size());
        assertEquals(new ArrayList<Items>(), c1.getInventory());
    }
    
    // Testing Potion usage, as they have different effects that just decreasing durability
    @Test
    public void usePotion() {
        Position start = new Position(10, 10);
        List<Items> items = new ArrayList<>();
        Character c1 = new Character("player", "Character", start, false, 100, 10, items, new ArrayList<>(), "Standard");
        Items i1 = new InvincibilityPotion("i1", "invincibility_potion", 1);
        c1.addInventory(i1);
        c1.useItem("i1");
        assertEquals(new ArrayList<Items>(), c1.getInventory());
        assertEquals("Invincible", c1.getStateName());
        c1.addInventory(new InvisibilityPotion("i2", "invisibility_potion", 1));
        c1.useItem("i2");
        assertEquals(new ArrayList<Items>(), c1.getInventory());
        assertEquals("Invisible", c1.getStateName());
        c1.setHealth(20);
        c1.addInventory(new HealthPotion("i3", "health_potion", 1));
        c1.useItem("i3");
        assertEquals(new ArrayList<Items>(), c1.getInventory());
        assertEquals(c1.getMaxHealth(), c1.getHealth());
    }

    // Testing Invincibility Potion usage in Hard mode
    @Test
    public void invincibilityHardMode() {
        Character character = new Character("player", "player", new Position(10, 10), false, 100, 10, new ArrayList<>(), new ArrayList<>(), "Hard");

        assertEquals("Normal", character.getStateName());

        character.addInventory(new InvincibilityPotion("invincibility_potion1", "invincibility_potion", 1));
        character.useItem("invincibility_potion1");
        assertEquals(new ArrayList<>(), character.getInventory()); // invincibility potion used
        assertEquals("Normal", character.getStateName()); // state has not changed

        character.addInventory(new InvisibilityPotion("invisibility_potion1", "invisibility_potion", 1));
        character.useItem("invisibility_potion1");
        assertEquals(new ArrayList<>(), character.getInventory()); // invisibility potion used
        assertEquals("Invisible", character.getStateName()); // character now invisible

        character.addInventory(new InvincibilityPotion("invincibility_potion2", "invincibility_potion", 1));
        character.useItem("invincibility_potion2");
        assertEquals(new ArrayList<>(), character.getInventory()); // invincibility potion used
        assertEquals("Invisible", character.getStateName()); // state has not changed
    }
    
    // Testing building items with Character
    @Test
    public void buildTest() {
        Position start = new Position(10, 10);
        List<Items> items = new ArrayList<>();
        Character c1 = new Character("player", "Character", start, false, 100, 10, items, new ArrayList<>(), "Standard");
        // build a bow
        Items a = new Items("a", "wood", 1);
        Items b = new Items("b", "arrow", 1);
        Items c = new Items("c", "arrow", 1);
        Items d = new Items("d", "arrow", 1);
        c1.addInventory(a);
        c1.addInventory(b);
        c1.addInventory(c);
        c1.addInventory(d);
        List<Items> test = new ArrayList<>(Arrays.asList(a,b,c,d));
        assertEquals(test, c1.getInventory());
        c1.buildItem("bow");
        assertEquals(1, c1.getInventory().size());
        Items e = c1.getInventory().get(0);
        assertEquals("bow", e.getItemType());

        // build a shield with a key
        Items f = new Items("f", "wood", 1);
        Items g = new Items("g", "wood", 1);
        // not really a key, just testing with the type
        Items h = new Items("h", "key", 1);
        c1.addInventory(f);
        c1.addInventory(g);
        c1.addInventory(h);
        c1.buildItem("shield");
        assertEquals(2, c1.getInventory().size());
        Items i = c1.getInventory().get(1);
        assertEquals("shield",i.getItemType());

        // build a shield with a treasure 
        Items j = new Items("f", "wood", 1);
        Items k = new Items("g", "wood", 1);
        Items l = new Items("h", "treasure", 1);
        c1.addInventory(j);
        c1.addInventory(k);
        c1.addInventory(l);
        c1.buildItem("shield");
        assertEquals(3, c1.getInventory().size());
        Items m = c1.getInventory().get(2);
        assertEquals("shield",m.getItemType());

        c1.getInventory().clear();
        Items wood = new Items ("wood", "wood", 1);
        Items arrow1 = new Items("arrow1", "arrow", 1);
        Items arrow2 = new Items("arrow2", "arrow", 1);
        Items key = new Items("key", "key", 1);
        Items treasure = new Items("treasure", "treasure", 1);
        Items sun1 = new Items("sun1", "sun_stone", 1);
        Items sun2 = new Items("sun2", "sun_stone", 1);
        c1.addInventory(wood);
        c1.addInventory(arrow1);
        c1.addInventory(arrow2);
        c1.addInventory(key);
        c1.addInventory(treasure);
        c1.addInventory(sun1);
        c1.addInventory(sun2);
        assertEquals(7, c1.getInventory().size());
        c1.buildItem("sceptre");
        assertEquals(5, c1.getInventory().size());
        assertTrue(c1.getInventory().stream().anyMatch(t -> t.getItemType().equals("sceptre")));
        c1.buildItem("sceptre");
        assertEquals(2, c1.getInventory().size());
        assertEquals(2, c1.getInventory().stream().filter(y -> y.getItemType().equals("sceptre")).count());
    }
    
    @Test
    public void useBomb() {
        // Creating a new game just to have access to entity list
        List<Entity> entities = new ArrayList<>();
        Position cPosition = new Position(10,11);
        Character c1 = new Character("player", "Character", cPosition, false, 100, 10, new ArrayList<>(), entities, "Standard");
        entities.add(c1);
        c1.addInventory(new Bomb("a", "bomb", 1));
        c1.useItem("a");
        assertEquals(0, c1.getInventory().size());
        assertEquals(2, entities.size());
        Entity testBomb = entities.get(1);
        assertEquals("bomb", testBomb.getType());
        Position bPosition = testBomb.getPosition();
        assertEquals(cPosition, bPosition); // character and bomb on same position

        // attempt to move character back to original position, but move is blocked by the placed bomb
        c1.move(Direction.LEFT);
        c1.move(Direction.RIGHT);
        assertEquals(0, c1.getInventory().size()); // character has not picked up the bomb
        assertEquals(2, entities.size());
        assertNotEquals(cPosition, c1.getPosition()); // character is not back to original position

    }
    @Test
    public void zombieDropArmourTest() {
        DungeonManiaController dc = new DungeonManiaController();
        dc.newGame("advanced", "Standard");
        Game game = dc.getCurrentGame();
        Character player = game.getCharacter();
        Position dummyPosition = new Position(1, 1);
        Armour.seed = 11;
        Zombie zombie1 = (Zombie) EntityFactory.createEntity("zombie1", dummyPosition, "zombie_toast");
        assertTrue(zombie1.getArmour() == null);
        assertDoesNotThrow(() -> player.battle(zombie1));
        assertEquals(player.getArmour(), null);

        Armour.seed = 10;
        Zombie zombie2 = (Zombie) EntityFactory.createEntity("zombie2", dummyPosition, "zombie_toast");
        assertTrue(zombie2.getArmour() != null);
        zombie2.setHealth(1);
        assertDoesNotThrow(() -> player.battle(zombie2));
        assertTrue(!player.getInventory().isEmpty());
        assertNotEquals(player.getArmour(), null);
    }
    @Test
    public void mercenaryDropArmourTest() {
        DungeonManiaController dc = new DungeonManiaController();
        dc.newGame("advanced", "Standard");
        Game game = dc.getCurrentGame();
        Character player = game.getCharacter();
        Position dummyPosition = new Position(1, 1);
        Armour.seed = 11;
        Mercenary mercenary1 = (Mercenary) EntityFactory.createEntity("mercenary1", dummyPosition, "mercenary");
        assertTrue(mercenary1.getArmour() == null);
        assertDoesNotThrow(() -> player.battle(mercenary1));
        assertEquals(player.getArmour(), null);

        Armour.seed = 10;
        Mercenary mercenary2 = (Mercenary) EntityFactory.createEntity("mercenary2", dummyPosition, "mercenary");
        assertTrue(mercenary2.getArmour() != null);
        mercenary2.setHealth(1);
        assertDoesNotThrow(() -> player.battle(mercenary2));
        assertTrue(!player.getInventory().isEmpty());
        assertNotEquals(player.getArmour(), null);
    }

    @Test
    public void sunStoneTest() {
        List<Entity> entities = new ArrayList<>();
        List<Items> inventory = new ArrayList<>();
        Character character = new Character("c1", "player", new Position(0, 0), false, 100, 10, inventory, new ArrayList<>(), "Standard");
        Door d = new Door("d1", "door", new Position(0,1), false, false, 1);
        Items k = new Items("s1", "sun_stone", 1);
        
        entities.add(d);
        inventory.add(k);
        //character.useItem("key");
        character.checkDoor(entities, new Position(0,1));
        assertTrue(d.isOpen());
        assertEquals("door_unlocked", d.getType());
    }

    @Test
    public void buildMidnightTest() {
        List<Entity> entities = new ArrayList<>();
        List<Items> inventory = new ArrayList<>();
        Character character = new Character("c1", "player", new Position(0, 0), false, 100, 10, inventory, entities, "Standard");
        JSONObject object = new JSONObject();
        object.put("goal", "enemies");
        Game g = new Game("empty.json", "standard", entities, inventory, new ArrayList<>(), object, character);
        entities.add(new Zombie("3", "zombie_toast", new Position(10,10), false, 3, 4));
        entities.add(character);
        inventory.add(new Armour("1", "armour", 1));
        inventory.add(new Items("sun_stone", "sun_stone", 1));
        g.tick(null, Direction.DOWN);
        // Can't build midnight with zombie.
        assertEquals(0, g.getBuildables().size());
        entities.remove(0);
        // Now that the zombie is gone, we should be able to build the armour.
        g.tick(null, Direction.DOWN);
        assertEquals(1, g.getBuildables().size());
        assertTrue(g.getBuildables().stream().anyMatch(e -> e.equals("midnight_armour")));
        g.build("midnight_armour");
        assertEquals(0, g.getBuildables().size());
        assertEquals(1, g.getInventory().size());
        assertTrue(g.getInventory().stream().anyMatch(e -> e.getItemType().equals("midnight_armour")));


        
    }

}
