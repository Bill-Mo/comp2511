package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Bow extends Items implements Build{
    // Making it list of map here since there could be multiple recipes.
    private List<Map<String, Integer>> recipe = new ArrayList<>();

    public Bow(String itemId, String itemType, int durability) {
        super(itemId, itemType, durability);
        Map <String, Integer> recipe = new HashMap<>();
        recipe.put("wood", 1);
        recipe.put("arrow", 3);
        this.recipe.add(recipe);
    }

    public List<Map<String, Integer>> getRecipe() {
        return recipe;
    }

}
