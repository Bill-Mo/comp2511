package dungeonmania;

public class InvincibilityPotion extends Items {

    public InvincibilityPotion(String itemId, String itemType, int durability) {
        super(itemId, itemType, durability);
    }
    /**
     * On potion use, set character's state to invincible
     */
    @Override
    public void use(Character character) {
        // invincibility potions have no effect in hard mode
        if (!character.getGameMode().equals("Hard")) {
            character.setState(new InvincibleState(character));
        }
        
        super.use(character);
    }

    
    
}
