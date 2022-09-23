package dungeonmania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sceptre extends Items implements Build {
    private List<Map<String, Integer>> recipe = new ArrayList<>();
    // The recipe system is in place, because what if new ways to build the item use entirely different components?
    public Sceptre(String itemId, String itemType, int durability) {
        super(itemId, itemType, durability);
        Map <String, Integer> recipe1 = new HashMap<>();
        Map <String, Integer> recipe2 = new HashMap<>();
        Map <String, Integer> recipe3 = new HashMap<>();
        Map <String, Integer> recipe4 = new HashMap<>();
        recipe1.put("wood", 1);
        recipe1.put("key", 1);
        recipe1.put("sun_stone", 1);
        this.recipe.add(recipe1);
        recipe2.put("wood", 1);
        recipe2.put("treasure", 1);
        recipe2.put("sun_stone", 1);
        this.recipe.add(recipe2);
        recipe3.put("arrow", 2);
        recipe3.put("key", 1);
        recipe3.put("sun_stone", 1);
        this.recipe.add(recipe3);
        recipe4.put("arrow", 2);
        recipe4.put("treasure", 1);
        recipe4.put("sun_stone", 1);
        this.recipe.add(recipe4);
    }

    @Override
    public List<Map<String,Integer>> getRecipe() {
        return recipe;
    }
}
