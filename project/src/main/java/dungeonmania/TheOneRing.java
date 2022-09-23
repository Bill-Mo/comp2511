package dungeonmania;

import java.util.Random;

public class TheOneRing extends Items {
    public static int seed = 0;

    public TheOneRing(String itemId, String type, int durability) {
        super(itemId, type, durability);
    }

    /**
     * Determine whether the enemy drop the one ring.
     * @return true if it drops
     */
    public static Boolean doesDropRing() {
        Random r = new Random();
        if (seed != 0) {
            r.setSeed(seed);
            seed = 0;
        }
        if (r.nextInt(100) < 10) {
            return true;
        }
        return false;
    }

    /**
     * Restores character's health to its initial value, before reducing its uses through super.
     */
    @Override
    public void use(Character character) {
        character.setHealth(character.getMaxHealth());
        super.use(character);
    }
}
