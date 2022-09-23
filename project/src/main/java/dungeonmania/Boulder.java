package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Boulder extends Entity {
    List<BoulderObserver> observers = new ArrayList<>();

    public Boulder(String id, String type, Position position, boolean isInteractable) {
        super(id, type, position, isInteractable);
    }
    
    // public void moveBoulder(Position position) {}

    public void move(Direction direction) {
        Position oldPosition = getPosition();
        Position newPosition = oldPosition.translateBy(direction);

        setPosition(newPosition);

        notifyObservers(oldPosition, newPosition);
    }

    public void attach(BoulderObserver observer) {
        observers.add(observer);

        // attach observers when generating/loading map?
    }

    public void detach(BoulderObserver observer) {
        observers.remove(observer);
        
        // detach when bombs destroy observers?
    }

    public void notifyObservers(Position oldPosition, Position newPosition) {
        // iterate over copy of observers to prevent ConcurrentModificationException
        for (BoulderObserver observer : new ArrayList<>(observers)) {
            observer.update(oldPosition, newPosition);
        }
    }
}
