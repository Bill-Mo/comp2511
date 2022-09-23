package dungeonmania;

import java.util.Random;

public class Armour extends Items {
    public static int seed = 0;
    
    public Armour(String itemId, String itemType, int durability) {
        super(itemId, itemType, durability);
    }
   
    /**
     * Determine whether the enemy drop an armour or not.
     * @return true if it drops
     */
    public static Boolean doesDropArmour() {
        Random r = new Random();
        if (seed != 0) {
            r.setSeed(seed);
            seed = 0;
        }
        if (r.nextInt(100) < 30) {
            return true;
        }
        return false;
    }
}
