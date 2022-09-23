package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
/**
 * Note all tests test goal factory in the process
 */
public class GoalTests {
    @Test
    public void exitGoalTest() {
        List<Items> inventory = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity("2", "exit", new Position(1, 2), false));
        Character character = new Character("1", "character", new Position(1, 1), false,
             2, 1, inventory, null, "Standard");
        entities.add(character);
        character.setEntities(entities);
        JSONObject jGoal = new JSONObject();
        jGoal.put("goal", "exit");
        Game game = new Game("stuff", "Peaceful", entities, inventory, new ArrayList<>(), jGoal, character);
        ExitGoalLeaf goal = new ExitGoalLeaf(game);
        assertEquals(":exit", goal.getGoal());
        assertFalse(goal.fulfilledGoals());
        game.tick(null, Direction.DOWN);
        assertTrue(goal.fulfilledGoals());
        assertEquals("", goal.getGoal());
    }

    @Test
    public void dungeonExitGoalTest() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("exit", "Standard");

        // Move character towards exit
        DungeonResponse dungeonResponse = controller.tick(null, Direction.DOWN);
        assertEquals(dungeonResponse.getGoals(), ":exit");

        // Move character onto exit
        dungeonResponse = controller.tick(null, Direction.RIGHT);
        // Game finished - dungeon response's goals should be empty
        assertEquals(dungeonResponse.getGoals(), "");
    }

    @Test
    public void treasureTest() {
        List<Items> inventory = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity("2", "treasure", new Position(1, 2), false));
        Character character = new Character("1", "character", new Position(1, 1), false,
             2, 1, inventory, null, "Standard");
        entities.add(character);
        character.setEntities(entities);
        JSONObject jGoal = new JSONObject();
        jGoal.put("goal", "treasure");
        Game game = new Game("stuff", "Peaceful", entities, inventory, new ArrayList<>(), jGoal, character);
        TreasureGoalLeaf goal = new TreasureGoalLeaf(game);
        assertFalse(goal.fulfilledGoals());
        assertEquals(":treasure", goal.getGoal());
        game.tick(null, Direction.DOWN);
        assertTrue(goal.fulfilledGoals());
        assertEquals("", goal.getGoal());
    }

    @Test
    public void boulderTest() {
        List<Items> inventory = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity("3", "switch", new Position(1, 3), false));
        entities.add(new Boulder("2", "boulder", new Position(1, 2), false));
        Character character = new Character("1", "character", new Position(1, 1), false,
             2, 1, inventory, null, "Standard");
        entities.add(character);
        character.setEntities(entities);
        JSONObject jGoal = new JSONObject();
        jGoal.put("goal", "boulders");
        Game game = new Game("stuff", "Peaceful", entities, inventory, new ArrayList<>(), jGoal, character);
        BoulderGoalLeaf goal = new BoulderGoalLeaf(game);
        assertFalse(goal.fulfilledGoals());
        assertEquals(":boulders", goal.getGoal());
        game.tick(null, Direction.DOWN);
        assertTrue(goal.fulfilledGoals());
        assertEquals("", goal.getGoal());
    }

    @Test
    public void enemyTest() {
        List<Items> inventory = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        Spider spider = new Spider("3", "spider", new Position(1, 2), false, 2, 1);
        entities.add(spider);
        Character character = new Character("1", "character", new Position(1, 1), false,
             6, 4, inventory, null, "Standard");
        entities.add(character);
        character.setEntities(entities);
        JSONObject jGoal = new JSONObject();
        jGoal.put("goal", "enemies");
        Game game = new Game("stuff", "Peaceful", entities, inventory, new ArrayList<>(), jGoal, character);
        EnemyGoalLeaf goal = new EnemyGoalLeaf(game);
        assertFalse(goal.fulfilledGoals());
        assertEquals(":enemies", goal.getGoal());
        entities.remove(spider);
        assertTrue(goal.fulfilledGoals());
        assertEquals("", goal.getGoal());
    }

    @Test
    public void AndGoalTest() {
        List<Items> inventory = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity("2", "exit", new Position(1, 3), false));
        entities.add(new Entity("3", "treasure", new Position(1,2),false));
        Character character = new Character("1", "character", new Position(1, 1), false,
             2, 1, inventory, null, "Standard");
        entities.add(character);
        character.setEntities(entities);
        JSONObject jGoal = new JSONObject();
        jGoal.put("goal", "AND");
        JSONArray subgoals = new JSONArray();
        JSONObject goal1 = new JSONObject();
        JSONObject goal2 = new JSONObject();
        goal1.put("goal", "exit");
        goal2.put("goal", "treasure");
        subgoals.put(goal1);
        subgoals.put(goal2);
        jGoal.put("subgoals", subgoals);
        Game game = new Game("stuff", "Peaceful", entities, inventory, new ArrayList<>(), jGoal, character);
        assertEquals("( :exit AND :treasure )", game.getGoals());
        game.tick(null, Direction.DOWN);
        assertEquals(":exit", game.getGoals());
        game.tick(null, Direction.DOWN);
        assertEquals("", game.getGoals());

    }

    @Test
    public void OrGoalTest() {
        List<Items> inventory = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity("2", "exit", new Position(1, 3), false));
        entities.add(new Entity("3", "treasure", new Position(1,2),false));
        Character character = new Character("1", "character", new Position(1, 1), false,
             2, 1, inventory, null, "Standard");
        entities.add(character);
        character.setEntities(entities);
        JSONObject jGoal = new JSONObject();
        jGoal.put("goal", "OR");
        JSONArray subgoals = new JSONArray();
        JSONObject goal1 = new JSONObject();
        JSONObject goal2 = new JSONObject();
        goal1.put("goal", "exit");
        goal2.put("goal", "treasure");
        subgoals.put(goal1);
        subgoals.put(goal2);
        jGoal.put("subgoals", subgoals);
        Game game = new Game("stuff", "Peaceful", entities, inventory, new ArrayList<>(), jGoal, character);
        assertEquals("( :exit OR :treasure )", game.getGoals());
        game.tick(null, Direction.DOWN);
        assertEquals("", game.getGoals());
    }


    @Test
    public void complexGoal() {
        List<Items> inventory = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity("2", "exit", new Position(1, 3), false));
        entities.add(new Entity("3", "treasure", new Position(1,2),false));
        entities.add(new Spider("4", "spider", new Position (2,4), false, 1, 1));
        Character character = new Character("1", "character", new Position(1, 1), false,
             2, 1, inventory, null, "Standard");
        entities.add(character);
        character.setEntities(entities);
        JSONObject jGoal = new JSONObject();
        jGoal.put("goal", "AND");
        JSONArray subgoals = new JSONArray();
        JSONObject goal1 = new JSONObject();
        JSONObject goal2 = new JSONObject();
        goal1.put("goal", "exit");
        goal2.put("goal", "OR");
        JSONArray subgoals2 = new JSONArray();
        JSONObject orGoal1 = new JSONObject();
        JSONObject orGoal2 = new JSONObject();
        orGoal1.put("goal", "treasure");
        orGoal2.put("goal", "enemies");
        subgoals2.put(orGoal1);
        subgoals2.put(orGoal2);
        goal2.put("subgoals", subgoals2);
        subgoals.put(goal1);
        subgoals.put(goal2);
        jGoal.put("subgoals", subgoals);
        Game game = new Game("stuff", "Peaceful", entities, inventory, new ArrayList<>(), jGoal, character);
        assertEquals("( :exit AND ( :treasure OR :enemies ) )", game.getGoals());
        game.tick(null, Direction.DOWN);
        assertEquals(":exit", game.getGoals());
        game.tick(null, Direction.DOWN);
        assertEquals("", game.getGoals());
        
        
    }
}
