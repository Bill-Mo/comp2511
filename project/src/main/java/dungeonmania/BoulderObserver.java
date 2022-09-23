package dungeonmania;

import dungeonmania.util.Position;

// public class BoulderObserver {
public interface BoulderObserver {
    
    //public void update(Subject) {}

    public void update(Position boulderOldPosition, Position boulderNewPosition);

}
