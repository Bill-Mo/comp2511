package dungeonmania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shield extends Items implements Build {
    // Making it list of map here since there could be multiple recipes.
    private List<Map<String, Integer>> recipe = new ArrayList<>();

    public Shield(String itemId, String itemType, int durabilty) {
        super(itemId, itemType, durabilty);
        Map<String, Integer> recipe1 = new HashMap<>();
        Map<String, Integer> recipe2 = new HashMap<>();
        recipe1.put("wood", 2);
        recipe1.put("treasure", 1);
        recipe2.put("wood", 2);
        recipe2.put("key", 1);
        recipe.add(recipe1);
        recipe.add(recipe2);       
    }

    public List<Map<String, Integer>> getRecipe() {
        return recipe;
    }
    
}
