package dungeonmania;

import java.util.ArrayList;
import java.util.List;

public interface Enemies {
    
    /**
     * Move a mob based on its supposed behavior
     * @param entities
     * @param character
     */
    public void move(List<Entity> entities, Character character);

    /**
     * Determine can an enemy can move or not
     * @return true if can move
     */
    public boolean canMove();

    public void isOnSwampTile(ArrayList<SwampTile> swampTilePosition);
}
