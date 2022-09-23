package dungeonmania;

import java.util.List;

public class Items {
    
    private String itemId;
    private String itemType; 
    private int durability;

    public Items(String itemId, String itemType, int durability) {
        this.itemId = itemId;
        this.itemType = itemType;
        this.durability = durability;
    }

    public String getItemId() {
        return itemId;
    }
    
    public String getItemType() {
        return itemType;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
    /**
     * Uses item and if durability reaches 0, get rid of the item from inventory.
     * @param character
     */
    public void use(Character character) {
        this.durability = this.durability - 1;
        if (this.durability == 0) {
            List<Items> inventory = character.getInventory();
            inventory.remove(this); 
        }
    }
    
    /**
     * Uses armour and if durability reaches 0, set zombie's armour to null.
     * @param zombie
     */
    public void use(Zombie zombie) {
        this.durability = this.durability - 1;
        if (this.durability == 0) {
            zombie.setArmour(null);
        }
    }

    /**
     * Uses armour and if durability reaches 0, set mercenary's armour to null.
     * @param mercenary
     */
    public void use(Mercenary mercenary) {
        this.durability = this.durability - 1;
        if (this.durability == 0) {
            mercenary.setArmour(null);
        }
    }
}
