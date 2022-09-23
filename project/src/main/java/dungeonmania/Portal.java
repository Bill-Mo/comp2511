package dungeonmania;

import dungeonmania.util.Position;

public class Portal extends Entity {
    private String colour;
    public Portal(String id, String type, Position position, boolean isInteractable, String colour) {
        super(id, type, position, isInteractable);
        this.colour = colour;
    }
    public String getColour() {
        return colour;
    }
}
