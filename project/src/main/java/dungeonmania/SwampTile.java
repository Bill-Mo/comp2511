package dungeonmania;

import dungeonmania.util.Position;

public class SwampTile extends Entity {
    private int movementFactor;
    public SwampTile(String id, String type, Position position, boolean isInteractable, int movement_factor) {
        super(id, type, position, isInteractable);
        this.movementFactor = movement_factor;
    }
    public int getMovementFactor() {
        return movementFactor;
    }
}
