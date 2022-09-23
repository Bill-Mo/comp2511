package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoalFactory {
    /**
     * Creates GoalComposites from JSON
     * @param object
     * @return
     */
    public static Goals createGoals(JSONObject object, Game game) {
        String goalType = object.getString("goal");
        
        switch (goalType) {
            case "AND":
                JSONArray goalArray1 = object.getJSONArray("subgoals");
                List<Goals> subgoals1 = new ArrayList<>();
                for (int i = 0; i < goalArray1.length(); i++) {
                    subgoals1.add(createGoals(goalArray1.getJSONObject(i), game));
                }
                return new AndGoalComposite(subgoals1);
            
            case "OR":
            JSONArray goalArray2 = object.getJSONArray("subgoals");
            List<Goals> subgoals2 = new ArrayList<>();
            for (int i = 0; i < goalArray2.length(); i++) {
                subgoals2.add(createGoals(goalArray2.getJSONObject(i), game));
            }
            return new OrGoalComposite(subgoals2);
            
            case "exit":
                return new ExitGoalLeaf(game);
            
            case "enemies":
                return new EnemyGoalLeaf(game);

            case "treasure":
                return new TreasureGoalLeaf(game);

            case "boulders":
                return new BoulderGoalLeaf(game);
            default:
                return null;
        }
    }
}
