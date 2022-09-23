package dungeonmania;

public class HealthPotion extends Items {

    public HealthPotion(String itemId, String itemType, int durability) {
        super(itemId, itemType, durability);
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
