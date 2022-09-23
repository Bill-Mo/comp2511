package dungeonmania;

public class InvisibilityPotion extends Items {

    public InvisibilityPotion(String itemId, String itemType, int durability) {
        super(itemId, itemType, durability);
    }
    
    /**
     * On potion use give character invisible state.
     */
    @Override
    public void use(Character character) {
        character.setState(new InvisibleState(character));
        super.use(character);
    }
}
