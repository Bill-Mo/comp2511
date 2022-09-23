package dungeonmania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MidnightArmour extends Armour implements Build {
    private List<Map<String, Integer>> recipe = new ArrayList<>();
    public MidnightArmour(String id, String type, int durability) {
        super(id, type, durability);
        Map<String, Integer> recipe = new HashMap<>();
        recipe.put("armour", 1);
        recipe.put("sun_stone", 1);
        this.recipe.add(recipe);
    }

    public List<Map<String, Integer>> getRecipe() {
        return recipe;
    }
}
