package dungeonmania;

import dungeonmania.util.Position;

public class FloorSwitch extends Entity implements BoulderObserver {
    private boolean isTriggered = false;

    public FloorSwitch(String id, String type, Position position, boolean isInteractable) {
        super(id, type, position, isInteractable);
    }

    public boolean isTriggered() {
        return isTriggered;
    }

    @Override
    public void update(Position boulderOldPosition, Position boulderNewPosition) {
        if (getPosition().equals(boulderOldPosition)) {
            isTriggered = false;
        } else if (getPosition().equals(boulderNewPosition)) {
            isTriggered = true;
        }
    }
}
