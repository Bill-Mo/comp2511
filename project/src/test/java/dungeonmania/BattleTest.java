package dungeonmania;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleTest {
    @Test
    public void normalCharacterOnEnemy() {
        // normal state character 
        Character character = new Character("player", "character", new Position(7,7), false, 100, 5, new ArrayList<>(), new ArrayList<>(), "Standard");
        
        Zombie zombie = new Zombie("zombie", "zombie_toast", new Position(7,6), false, 50, 5);
        Spider spider = new Spider("spider", "spider", new Position(6,6), false, 40, 3);
        Mercenary mercenary = new Mercenary("mercenary", "mercenary", new Position(6,7), true, 70, 8, 3, false);

        List<Entity> entities = new ArrayList<>(Arrays.asList(zombie, spider, mercenary, character));
        character.setEntities(entities);

        // player battles zombie
        character.move(Direction.UP);
        assertTrue(character.getHealth() > 0 && character.getHealth() < 100);
        assertTrue(zombie.getHealth() <= 0);

        // player battles spider
        character.move(Direction.LEFT);
        assertTrue(character.getHealth() > 0 && character.getHealth() < 100);
        assertTrue(spider.getHealth() <= 0);

        // player battles mercenary
        character.move(Direction.DOWN);
        assertTrue(character.getHealth() > 0 && character.getHealth() < 100);
        assertTrue(mercenary.getHealth() <= 0);
    }

    @Test
    public void invincibleCharacterOnEnemy() {
        // invincible state character
        Character character = new Character("player", "character", new Position(7,7), false, 100, 1, new ArrayList<>(), new ArrayList<>(), "Standard");
        Items invincibilityPotion = new InvincibilityPotion("invincibilityPotion", "invincibility_potion", 1);
        character.addInventory(invincibilityPotion);
        character.useItem("invincibilityPotion");
        assertEquals("Invincible", character.getStateName());
        
        Zombie zombie = new Zombie("zombie", "zombie_toast", new Position(7,6), false, 50, 5);
        Spider spider = new Spider("spider", "spider", new Position(6,6), false, 40, 3);
        Mercenary mercenary = new Mercenary("mercenary", "mercenary", new Position(6,7), true, 70, 8, 3, false);
        
        List<Entity> entities = new ArrayList<>(Arrays.asList(zombie, spider, mercenary, character));
        character.setEntities(entities);

        character.move(Direction.UP); // player battles zombie
        assertEquals(character.getHealth(), 100); // player takes no damage
        assertFalse(character.getEntities().contains(zombie)); // zombie is defeated, should be removed from map

        character.move(Direction.LEFT); // player battles spider
        assertEquals(character.getHealth(), 100); // player takes no damage
        assertFalse(character.getEntities().contains(spider)); // spider is defeated, should be detached from player

        character.move(Direction.DOWN); // player battles mercenary
        assertEquals(character.getHealth(), 100); // player takes no damage
        assertFalse(character.getEntities().contains(mercenary)); // mercenary is defeated, should be detached from player
    }

    @Test
    public void invisibleCharacterOnEnemy() {
        // invisible state character        
        Character character = new Character("player", "character", new Position(7,7), false, 100, 1, new ArrayList<>(), new ArrayList<>(), "Standard");
        Items invisibilityPotion = new InvisibilityPotion("invisibilityPotion", "invisibility_potion", 1);
        character.addInventory(invisibilityPotion);
        character.useItem("invisibilityPotion");
        assertEquals("Invisible", character.getStateName());
        
        Zombie zombie = new Zombie("zombie", "zombie_toast", new Position(7,6), false, 50, 5);
        Spider spider = new Spider("spider", "spider", new Position(6,6), false, 40, 3);
        Mercenary mercenary = new Mercenary("mercenary", "mercenary", new Position(6,7), true, 70, 8, 3, false);

        List<Entity> entities = new ArrayList<>(Arrays.asList(zombie, spider, mercenary, character));
        character.setEntities(entities);

        character.move(Direction.UP); // player moves onto same cell as zombie
        assertEquals(character.getHealth(), 100); // player takes no damage
        assertEquals(zombie.getHealth(), 50); // zombie takes no damage

        character.move(Direction.LEFT); // player moves onto same cell as spider
        assertEquals(character.getHealth(), 100); // player takes no damage
        assertEquals(spider.getHealth(), 40); // spider takes no damage

        character.move(Direction.DOWN); // player moves onto same cell as mercenary
        assertEquals(character.getHealth(), 100); // player takes no damage
        assertEquals(mercenary.getHealth(), 70); // mercenary takes no damage
    }
    @Test
    public void peacefulBattleTest() {
        Character character = new Character("player", "character", new Position(7,7), false, 100, 5, new ArrayList<>(), new ArrayList<>(), "Peaceful");
        
        Zombie zombie = new Zombie("zombie", "zombie_toast", new Position(7,6), false, 50, 5);
        Spider spider = new Spider("spider", "spider", new Position(6,6), false, 40, 3);
        Mercenary mercenary = new Mercenary("mercenary", "mercenary", new Position(6,7), true, 70, 8, 3, false);

        List<Entity> entities = new ArrayList<>(Arrays.asList(zombie, spider, mercenary, character));
        character.setEntities(entities);

        // player moves onto zombie's position, battle should not occur
        character.move(Direction.UP);
        assertEquals(100, character.getHealth());
        assertEquals(50, zombie.getHealth());

        // player moves onto spider's position
        character.move(Direction.LEFT);
        assertEquals(100, character.getHealth());
        assertEquals(40, spider.getHealth());

        // player moves onto mercenary's position
        character.move(Direction.DOWN);
        assertEquals(100, character.getHealth());
        assertEquals(70, mercenary.getHealth());
    }

    @Test
    public void characterArmourTest() {
        Character character1 = new Character("player1", "player", new Position(7,7), false, 100, 2, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character2 = new Character("player2", "player", new Position(7,7), false, 100, 2, new ArrayList<>(), new ArrayList<>(), "Standard");
        Armour armour = new Armour("armour", "armour", 2);
        character2.addInventory(armour);

        Zombie zombie1 = new Zombie("zombie1", "zombie_toast", new Position(7,6), false, 50, 10);
        Zombie zombie2 = new Zombie("zombie2", "zombie_toast", new Position(7,8), false, 50, 10);

        List<Entity> entities1 = new ArrayList<>(Arrays.asList(zombie1, character1));
        List<Entity> entities2 = new ArrayList<>(Arrays.asList(zombie2, character2));
        character1.setEntities(entities1);
        character2.setEntities(entities2);

        character1.move(Direction.UP); // player1 battles zombie1
        character2.move(Direction.DOWN); // player2 battles zombie2

        assertTrue(character1.getHealth() < character2.getHealth()); // player without armour has less health

        // assertEquals(1, armour.getDurability());
        // assertEquals(1, character2.getInventory().size());

        // character2.move(Direction.LEFT);
        // character2.move(Direction.RIGHT); // player2 battles zombie again

        // armour was used twice in battle, should be out of durability
        assertEquals(0, armour.getDurability());
    }

    @Test
    public void characterShieldTest() {
        Character character1 = new Character("player1", "player", new Position(7,7), false, 100, 2, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character2 = new Character("player2", "player", new Position(7,7), false, 100, 2, new ArrayList<>(), new ArrayList<>(), "Standard");
        Shield shield = new Shield("shield", "shield", 2);
        character2.addInventory(shield);

        Spider spider1 = new Spider("spider1", "spider", new Position(7,8), false, 50, 8);
        Spider spider2 = new Spider("spider2", "spider", new Position(7,6), false, 50, 8);


        List<Entity> entities1 = new ArrayList<>(Arrays.asList(spider1, character1));
        List<Entity> entities2 = new ArrayList<>(Arrays.asList(spider2,character2));
        character1.setEntities(entities1);
        character2.setEntities(entities2);

        character1.move(Direction.DOWN); // player1 battles spider1
        character2.move(Direction.UP); // player2 battles spider2
        assertTrue(character1.getHealth() < character2.getHealth()); // player without shield has less health
        
        // assertEquals(1, shield.getDurability());
        // assertEquals(1, character2.getInventory().size());

        // character2.move(Direction.DOWN);
        // character2.move(Direction.UP); // player2 battles spider again

        // shield was used twice in battle, should be out of durability
        assertEquals(0, shield.getDurability());
    }

    @Test
    public void characterArmourShieldTest() {
        Character character1 = new Character("player1", "player", new Position(7,7), false, 100, 3, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character2 = new Character("player2", "player", new Position(7,7), false, 100, 3, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character3 = new Character("player3", "player", new Position(7,7), false, 100, 3, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character4 = new Character("player4", "player", new Position(7,7), false, 100, 3, new ArrayList<>(), new ArrayList<>(), "Standard");
        Armour armour1 = new Armour("armour1", "armour", 2);
        Armour armour2 = new Armour("armour2", "armour", 2);
        Shield shield1 = new Shield("shield1", "shield", 2);
        Shield shield2 = new Shield("shield2", "shield", 2);
        character2.addInventory(armour1); // player2 has armour
        character3.addInventory(shield1); // player3 has shield
        character4.addInventory(armour2);
        character4.addInventory(shield2); // player4 has both armour and shield

        Mercenary mercenary1 = new Mercenary("mercenary1", "mercenary", new Position(7,6), true, 70, 8, 3, false);
        Mercenary mercenary2 = new Mercenary("mercenary2", "mercenary", new Position(7,8), true, 70, 8, 3, false);
        Mercenary mercenary3 = new Mercenary("mercenary3", "mercenary", new Position(6,7), true, 70, 8, 3, false);
        Mercenary mercenary4 = new Mercenary("mercenary4", "mercenary", new Position(8,7), true, 70, 8, 3, false);

        List<Entity> entities1 = new ArrayList<>(Arrays.asList(mercenary1, character1));
        List<Entity> entities2 = new ArrayList<>(Arrays.asList(mercenary2, character2));
        List<Entity> entities3 = new ArrayList<>(Arrays.asList(mercenary3, character3));
        List<Entity> entities4 = new ArrayList<>(Arrays.asList(mercenary4, character4));
        character1.setEntities(entities1);
        character2.setEntities(entities2);
        character3.setEntities(entities3);
        character4.setEntities(entities4);

        character1.move(Direction.UP); // player1 battles mercenary1
        character2.move(Direction.DOWN); // player2 battles mercenary2
        character3.move(Direction.LEFT); // player3 battles mercenary3
        character4.move(Direction.RIGHT); // player4 battles mercenary4
        assertTrue(character1.getHealth() < character4.getHealth()); // player with no equipment has less health
        assertTrue(character2.getHealth() < character4.getHealth()); // player with only armour has less health
        assertTrue(character3.getHealth() < character4.getHealth()); // player with only shield has less health
        
        // assertEquals(1, armour2.getDurability());
        // assertEquals(1, shield2.getDurability());
        // assertEquals(2, character4.getInventory().size());

        // character4.move(Direction.DOWN);
        // character4.move(Direction.UP); // player4 battles mercenary again

        // armour and shields were used twice in battle, should be out of durability
        assertEquals(0, armour1.getDurability());
        assertEquals(0, armour2.getDurability());
        assertEquals(0, shield1.getDurability());
        assertEquals(0, shield2.getDurability());
    }

    @Test
    public void characterSwordTest() {
        Character character1 = new Character("player1", "player", new Position(7,7), false, 100, 2, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character2 = new Character("player2", "player", new Position(7,7), false, 100, 2, new ArrayList<>(), new ArrayList<>(), "Standard");

        Sword sword = new Sword("sword", "sword", 2);
        character2.addInventory(sword); // player2 has sword

        Mercenary mercenary1 = new Mercenary("mercenary1", "mercenary", new Position(6,7), true, 85, 1, 3, false);
        Mercenary mercenary2 = new Mercenary("mercenary2", "mercenary", new Position(8,7), true, 85, 1, 3, false);

        List<Entity> entities1 = new ArrayList<>(Arrays.asList(mercenary1, character1));
        List<Entity> entities2 = new ArrayList<>(Arrays.asList(mercenary2, character2));
        character1.setEntities(entities1);
        character2.setEntities(entities2);

        character1.move(Direction.LEFT); // player1 battles mercenary1
        character2.move(Direction.RIGHT); // player2 battles mercenary2
        // assertTrue(mercenary2.getHealth() < mercenary1.getHealth()); // mercenary attacked with sword has less health
        assertTrue(character1.getHealth() < character2.getHealth()); // player with sword has more health as enemy dies faster
        
        // assertEquals(1, sword.getDurability());
        // assertEquals(1, character2.getInventory().size());

        // character2.move(Direction.DOWN);
        // character2.move(Direction.UP); // player2 battles mercenary2 again

        assertEquals(0, sword.getDurability()); // sword was used twice in battle, should be out of durability
    }

    @Test
    public void characterBowTest() {
        Character character1 = new Character("player1", "player", new Position(7,7), false, 100, 1, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character2 = new Character("player2", "player", new Position(7,7), false, 100, 1, new ArrayList<>(), new ArrayList<>(), "Standard");

        Bow bow = new Bow("bow", "bow", 2);
        character2.addInventory(bow); // player2 has bow

        Spider spider1 = new Spider("spider1", "spider", new Position(7,6), false, 45, 1);
        Spider spider2 = new Spider("spider2", "spider", new Position(7,8), false, 45, 1);

        List<Entity> entities1 = new ArrayList<>(Arrays.asList(spider1, character1));
        List<Entity> entities2 = new ArrayList<>(Arrays.asList(spider2, character2));
        character1.setEntities(entities1);
        character2.setEntities(entities2);

        character1.move(Direction.UP); // player1 battles spider1
        character2.move(Direction.DOWN); // player2 battles spider2
        // assertTrue(spider2.getHealth() < spider1.getHealth()); // spider attacked with bow has less health
        assertTrue(character1.getHealth() < character2.getHealth()); // player with bow has more health as enemy dies faster
        
        // assertEquals(1, bow.getDurability());
        // assertEquals(1, character2.getInventory().size());

        // character2.move(Direction.RIGHT);
        // character2.move(Direction.LEFT); // player2 battles spider2 again

        assertEquals(0, bow.getDurability()); // bow was used twice in battle, should be out of durability
    }

    @Test
    public void characterSwordBowTest() {
        Character character1 = new Character("player1", "player", new Position(7,7), false, 100, 1, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character2 = new Character("player2", "player", new Position(7,7), false, 100, 1, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character3 = new Character("player3", "player", new Position(7,7), false, 100, 1, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character4 = new Character("player4", "player", new Position(7,7), false, 100, 1, new ArrayList<>(), new ArrayList<>(), "Standard");

        Sword sword1 = new Sword("sword1", "sword", 2);
        Sword sword2 = new Sword("sword2", "sword", 2);
        Bow bow1 = new Bow("bow1", "bow", 2);
        Bow bow2 = new Bow("bow2", "bow", 2);
        character2.addInventory(sword1); // player2 has sword
        character3.addInventory(bow1); // player3 has bow
        character4.addInventory(sword2);
        character4.addInventory(bow2); // player4 has sword and bow

        Zombie zombie1 = new Zombie("zombie1", "zombie_toast", new Position(7,6), false, 50, 1);
        Zombie zombie2 = new Zombie("zombie2", "zombie_toast", new Position(7,8), false, 50, 1);
        Zombie zombie3 = new Zombie("zombie3", "zombie_toast", new Position(6,7), false, 50, 1);
        Zombie zombie4 = new Zombie("zombie4", "zombie_toast", new Position(8,7), false, 50, 1);

        List<Entity> entities1 = new ArrayList<>(Arrays.asList(zombie1, character1));
        List<Entity> entities2 = new ArrayList<>(Arrays.asList(zombie2, character2));
        List<Entity> entities3 = new ArrayList<>(Arrays.asList(zombie3, character3));
        List<Entity> entities4 = new ArrayList<>(Arrays.asList(zombie4, character4));
        character1.setEntities(entities1);
        character2.setEntities(entities2);
        character3.setEntities(entities3);
        character4.setEntities(entities4);

        character1.move(Direction.UP); // player1 battles zombie1
        character2.move(Direction.DOWN); // player2 battles zombie2
        character3.move(Direction.LEFT); // player3 battles zombie3
        character4.move(Direction.RIGHT); // player4 battles zombie4
        // assertTrue(zombie4.getHealth() < zombie1.getHealth()); // zombie attacked with sword and bow has less health
        // assertTrue(zombie4.getHealth() < zombie2.getHealth()); // zombie attacked with sword and bow has less health
        // assertTrue(zombie4.getHealth() < zombie3.getHealth()); // zombie attacked with sword and bow has less health

        // player with sword and bow has more health than all other players (as its enemy dies fastest)
        assertTrue(character1.getHealth() < character4.getHealth());
        assertTrue(character2.getHealth() < character4.getHealth());
        assertTrue(character3.getHealth() < character4.getHealth());
        
        // assertEquals(1, sword2.getDurability());
        // assertEquals(1, bow2.getDurability());
        // assertEquals(2, character4.getInventory().size());

        // character4.move(Direction.LEFT);
        // character4.move(Direction.RIGHT); // player4 battles zombie4 again

        assertEquals(0, sword1.getDurability());
        assertEquals(1, sword2.getDurability());
        assertEquals(0, bow1.getDurability());
        assertEquals(1, bow2.getDurability());
    }

    @Test
    public void zombieArmourTest() {
        Character character1 = new Character("player1", "player", new Position(7,7), false, 50, 2, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character2 = new Character("player2", "player", new Position(7,7), false, 50, 2, new ArrayList<>(), new ArrayList<>(), "Standard");

        Zombie zombie1 = new Zombie("zombie1", "zombie_toast", new Position(7,6), false, 80, 6);
        Zombie zombie2 = new Zombie("zombie2", "zombie_toast", new Position(7,8), false, 80, 6);
        Armour armour = new Armour("armour", "armour", 2);
        zombie2.setArmour(armour); // zombie2 has armour

        // character.attach(zombie1);
        // character.attach(zombie2);

        List<Entity> entities1 = new ArrayList<>(Arrays.asList(zombie1, character1));
        List<Entity> entities2 = new ArrayList<>(Arrays.asList(zombie2, character2));
        character1.setEntities(entities1);
        character2.setEntities(entities2);

        character1.move(Direction.UP); // player1 battles zombie1
        character2.move(Direction.DOWN); // player2 battles zombie2

        assertTrue(zombie1.getHealth() < zombie2.getHealth()); // zombie without armour has less health
        
        // assertEquals(1, armour.getDurability());

        // character.move(Direction.LEFT);
        // character.move(Direction.RIGHT); // player battles zombies again

        assertEquals(0, armour.getDurability()); // armour out of durability
        assertNull(zombie2.getArmour()); // removed from zombie2
    }

    @Test
    public void mercenaryArmourTest() {
        Character character1 = new Character("player1", "player", new Position(7,7), false, 60, 2, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character2 = new Character("player2", "player", new Position(7,7), false, 60, 2, new ArrayList<>(), new ArrayList<>(), "Standard");

        Mercenary mercenary1 = new Mercenary("mercenary1", "mercenary", new Position(7,6), true, 80, 7, 3, false);
        Mercenary mercenary2 = new Mercenary("mercenary2", "mercenary", new Position(7,8), true, 80, 7, 3, false);
        Armour armour = new Armour("armour", "armour", 2);
        mercenary2.setArmour(armour); // mercenary2 has armour

        List<Entity> entities1 = new ArrayList<>(Arrays.asList(mercenary1, character1));
        List<Entity> entities2 = new ArrayList<>(Arrays.asList(mercenary2, character2));
        character1.setEntities(entities1);
        character2.setEntities(entities2);

        character1.move(Direction.UP); // player1 battles mercenary1
        character2.move(Direction.DOWN); // player2 battles mercenary2

        assertTrue(mercenary1.getHealth() < mercenary2.getHealth()); // mercenary without armour has less health
        // assertEquals(36, mercenary1.getHealth());
        // assertEquals(48, mercenary2.getHealth());
        
        // assertEquals(1, armour.getDurability());

        // character.move(Direction.LEFT);
        // character.move(Direction.RIGHT); // player battles mercenaries again

        assertEquals(0, armour.getDurability()); // armour out of durability
        assertNull(mercenary2.getArmour()); // removed from mercenary2
    }

    @Test
    public void enemyDropArmourTest() {
        Character character = new Character("player", "player", new Position(7,7), false, 100, 3, new ArrayList<>(), new ArrayList<>(), "Standard");

        Zombie zombie = new Zombie("zombie", "zombie_toast", new Position(7,6), false, 10, 1);
        Mercenary mercenary = new Mercenary("mercenary", "mercenary", new Position(6,6), true, 10, 2, 3, false);

        Armour armour1 = new Armour("armour1", "armour", 3);
        Armour armour2 = new Armour("armour2", "armour", 3);
        zombie.setArmour(armour1); // zombie has armour
        mercenary.setArmour(armour2); // mercenary has armour

        List<Entity> entities = new ArrayList<>(Arrays.asList(zombie, mercenary, character));
        character.setEntities(entities);

        assertNull(character.getArmour());
        assertEquals(0, character.getInventory().size());

        character.move(Direction.UP); // player battles zombie
        assertTrue(zombie.getHealth() <= 0); // zombie is dead
        assertEquals(2, armour1.getDurability());
        assertNotNull(character.getArmour());
        assertTrue(character.getInventory().contains(armour1)); // player gets zombie's armour

        character.move(Direction.LEFT); // player battles mercenary
        assertTrue(mercenary.getHealth() <= 0); // mercenary is dead
        assertEquals(1, armour1.getDurability());
        assertEquals(2, armour2.getDurability());
        assertTrue(character.getInventory().contains(armour2)); // player gets mercenary's armour
    }

    @Test
    public void characterDeathTest() {
        Character character = new Character("player", "player", new Position(7,7), false, 100, 0, new ArrayList<>(), new ArrayList<>(), "Standard");
        Zombie zombie = new Zombie("zombie", "zombie_toast", new Position(7,6), false, 50, 5);

        List<Entity> entities = new ArrayList<>(Arrays.asList(zombie, character));
        character.setEntities(entities);

        assertEquals(2, character.getEntities().size()); // map contains player and zombie

        character.move(Direction.UP); // player battles zombie
        assertTrue(character.getHealth() <= 0); // player is now dead

        assertEquals(1, character.getEntities().size()); // map only contains zombie
    }

    @Test
    public void allyNoBattleTest() {
        Character character = new Character("player", "player", new Position(7,7), false, 100, 3, new ArrayList<>(), new ArrayList<>(), "Standard");
        Mercenary ally = new Mercenary("ally", "mercenary", new Position(7,6), false, 70, 5, 3, true);

        List<Entity> entities = new ArrayList<>(Arrays.asList(ally, character));
        character.setEntities(entities);

        character.move(Direction.UP); // player moves to same position as mercenary, no battle should occur
        assertEquals(100, character.getHealth()); // player health unchanged
        assertEquals(70, ally.getHealth()); // ally health unchanged
    }

    @Test
    public void allyBattleEnemyTest() {
        Character character1 = new Character("player1", "player", new Position(7,7), false, 100, 1, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character2 = new Character("player2", "player", new Position(7,7), false, 100, 1, new ArrayList<>(), new ArrayList<>(), "Standard");
        Mercenary ally = new Mercenary("ally", "mercenary", new Position(7,6), false, 70, 2, 3, true);

        Spider spider1 = new Spider("spider1", "spider", new Position(6,7), false, 60, 3);
        Spider spider2 = new Spider("spider2", "spider", new Position(8,7), false, 60, 3);

        List<Entity> entities1 = new ArrayList<>(Arrays.asList(spider1, character1));
        List<Entity> entities2 = new ArrayList<>(Arrays.asList(spider2, character2, ally)); // player2 has ally
        character1.setEntities(entities1);
        character2.setEntities(entities2);

        character1.move(Direction.LEFT); // player1 battles spider1
        character2.move(Direction.RIGHT); // player2 battles spider2 with ally
        assertTrue(character1.getHealth() < character2.getHealth()); // player with ally has more health as enemy dies faster
        assertTrue(ally.getHealth() < 70); // ally has taken damage from battle
    }

    @Test
    public void allyKillArmouredEnemyTest() {
        Character character = new Character("player", "player", new Position(7,7), false, 100, 0, new ArrayList<>(), new ArrayList<>(), "Standard");
        Mercenary ally = new Mercenary("ally", "mercenary", new Position(7,6), false, 70, 10, 3, true);
        Zombie zombie = new Zombie("zombie", "zombie_toast", new Position(6,7), false, 60, 3);
        
        Armour armour = new Armour("armour1", "armour", 4);
        zombie.setArmour(armour); // zombie has armour

        List<Entity> entities = new ArrayList<>(Arrays.asList(zombie, character, ally));
        character.setEntities(entities);

        character.move(Direction.LEFT); // player battles zombie, ally kills the zombie
        assertTrue(zombie.getHealth() <= 0); // zombie is dead
        assertEquals(2, armour.getDurability());
        assertNotNull(character.getArmour());
        assertTrue(character.getInventory().contains(armour)); // player gets zombie's armour
    }

    @Test
    public void enemyKillArmouredAllyTest() {
        Character character = new Character("player", "player", new Position(7,7), false, 100, 1, new ArrayList<>(), new ArrayList<>(), "Standard");
        Mercenary ally = new Mercenary("ally", "mercenary", new Position(7,6), false, 5, 10, 3, true);
        Mercenary mercenary = new Mercenary("mercenary", "mercenary", new Position(6,7), true, 40, 10, 3, false);
        
        Armour armour = new Armour("armour1", "armour", 6);
        ally.setArmour(armour); // ally has armour

        List<Entity> entities = new ArrayList<>(Arrays.asList(mercenary, character, ally));
        character.setEntities(entities);

        character.move(Direction.LEFT); // player battles mercenary, mercenary kills ally
        assertTrue(ally.getHealth() <= 0); // ally is dead
        assertEquals(4, armour.getDurability()); // ally used armour once, and player used armour once
        assertNotNull(character.getArmour());
        assertTrue(character.getInventory().contains(armour)); // player gets ally's armour
    }

    @Test
    public void midnightArmourTest() {
        Character character1 = new Character("player1", "player", new Position(7,7), false, 100, 2, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character2 = new Character("player2", "player", new Position(7,7), false, 100, 2, new ArrayList<>(), new ArrayList<>(), "Standard");
        MidnightArmour armour = new MidnightArmour("armour", "midnight_armour", 2);
        character2.addInventory(armour);

        Zombie zombie1 = new Zombie("zombie1", "zombie_toast", new Position(7,6), false, 60, 10);
        Zombie zombie2 = new Zombie("zombie2", "zombie_toast", new Position(7,8), false, 60, 10);

        List<Entity> entities1 = new ArrayList<>(Arrays.asList(zombie1, character1));
        List<Entity> entities2 = new ArrayList<>(Arrays.asList(zombie2, character2));
        character1.setEntities(entities1);
        character2.setEntities(entities2);

        character1.move(Direction.UP); // player1 battles zombie1
        character2.move(Direction.DOWN); // player2 battles zombie2

        assertTrue(character1.getHealth() < character2.getHealth()); // player without armour has less health
        // Midnight armour gives more attack damage, so zombie who battled with player without armour should have less health
        assertTrue(zombie1.getHealth() > zombie2.getHealth());
        assertEquals(0, armour.getDurability());
    }

    @Test
    public void andurilTest() {
        Character character1 = new Character("player1", "player", new Position(7,7), false, 100, 2, new ArrayList<>(), new ArrayList<>(), "Standard");
        Character character2 = new Character("player2", "player", new Position(7,7), false, 100, 2, new ArrayList<>(), new ArrayList<>(), "Standard");

        Sword sword = new Sword("sword", "anduril", 2);
        Sword sword2 = new Sword("sword2", "sword", 2);
        character2.addInventory(sword); // player2 has anduril
        character1.addInventory(sword2); // player1 has regular sword

        Mercenary mercenary1 = new Mercenary("mercenary1", "mercenary", new Position(6,7), true, 200, 1, 3, false);
        Mercenary mercenary2 = new Mercenary("mercenary2", "mercenary", new Position(8,7), true, 200, 1, 3, false);

        List<Entity> entities1 = new ArrayList<>(Arrays.asList(mercenary1, character1));
        List<Entity> entities2 = new ArrayList<>(Arrays.asList(mercenary2, character2));
        character1.setEntities(entities1);
        character2.setEntities(entities2);

        character1.move(Direction.LEFT); // player1 battles mercenary1
        character2.move(Direction.RIGHT); // player2 battles mercenary2
        assertTrue(character1.getHealth() < character2.getHealth()); // player with anduril has more health as enemy dies faster

        assertEquals(0, sword.getDurability()); // sword was used twice in battle, should be out of durability
        assertTrue(sword2.getDurability() <= 0); // same goes for other sword.
    
    }
}
