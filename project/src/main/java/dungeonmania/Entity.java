package dungeonmania;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeonmania.util.Position;

public class Entity implements CharacterObserver {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;
    
    public Entity(String id, String type, Position position, boolean isInteractable) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public boolean isInteractable() {
        return isInteractable;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setInteractable(boolean isInteractable) {
        this.isInteractable = isInteractable;
    }

    public boolean isOn(Entity entity) {
        return this != entity && position.equals(entity.getPosition());
        // return position.equals(entity.getPosition());
    }

    @Override
    public void update(Character character) {
        // if (character.isOn(this) && isCollectable() && character.isObserver(this)) {
        if (character.isOn(this) && isCollectable()) {
            // add collectable to inventory and remove from map
            collectItem(character);
        }

        List<Entity> entities = character.getEntities();

        for(Entity entity : new ArrayList<>(entities)) {
            if (entity.getType().equals("bomb") && checkSwitchOn(entities, entity.getPosition())) {
                explode(entities, entity.getPosition());
            }
        }

    }

    // Checks if current entity is a collectable entity
    public boolean isCollectable() {
        List<String> collectables = Arrays.asList("treasure", "key", "health_potion", "invincibility_potion", 
        "invisibility_potion", "wood", "arrow", "bomb", "sword", "armour", "sun_stone");

        return collectables.contains(type);
    }


    // Adds collectable item to character's inventory
    private void collectItem(Character character) {
        character.addInventory(ItemFactory.createItem(id, type));
        character.mapRemove(this);
        character.detach(this);
    }

    public void explode(List<Entity> entities, Position position) {
        List<Position> adjacentPositions = position.getAdjacentPositions();

        for (Entity entity : new ArrayList<>(entities)) {
            Position entPos = entity.getPosition();

            if (!entity.getType().equals("player") && adjacentPositions.contains(entPos)) {
                entities.remove(entity);
            }

            if (!entity.getType().equals("player") && position.equals(entPos)) {
                entities.remove(entity);
            }

        }
    }

    public boolean checkSwitchOn(List<Entity> entities, Position position) {
        List<Position> adjacentPositions = position.getAdjacentPositions();

        for (Entity entity : new ArrayList<>(entities)) {
            Position entPos = entity.getPosition();

            if (entity.getType().equals("switch") && adjacentPositions.contains(entPos) && checkBoulder(entities, entPos)) {
                return true;
            }

        }
        return false;
    }

    public boolean checkBoulder(List<Entity> entities, Position position) {
        for (Entity entity : new ArrayList<>(entities)) {
            Position entPos = entity.getPosition();

            if (entity.getType().equals("boulder") &&  position.equals(entPos)) {
                return true;
            }

        }
        return false;
    }

}
