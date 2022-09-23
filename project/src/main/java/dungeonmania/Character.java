package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dungeonmania.util.Direction;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

public class Character extends Mob {
    
    private List<Items> inventory;
    private List<Entity> mapEntities;
    private CharacterState state;
    private String gameMode;

    private List<CharacterObserver> observers = new ArrayList<>();

    public Character(String id, String type, Position position, boolean isInteractable, int health, int attack,
            List<Items> inventory, List<Entity> mapEntities, String gameMode) {
        super(id, type, position, isInteractable, health, attack);
        this.inventory = inventory;
        this.mapEntities = mapEntities;
        this.gameMode = gameMode;

        this.state = new NormalState(this);
    }

    public List<Items> getInventory() {
        return inventory;
    }

    public void setInventory(List<Items> inventory) {
        this.inventory = inventory;
    }

    public void addInventory(Items inventoryItem) {
        this.inventory.add(inventoryItem);
    }

    public void setState(CharacterState state) {
        this.state = state;
    }

    public CharacterState getState() {
        return this.state;
    }

    public String getStateName() {
        return state.getStateName();
    }

    public String getGameMode() {
        return this.gameMode;
    }

    public void buildItem(String item) throws InvalidActionException {
        Build buildable = ItemFactory.createBuildable(item + inventory.size(), item);

        List<Map<String,Integer>> recipes = buildable.getRecipe();
        List<Items> recipeItems = new ArrayList<>();
        boolean recipeFulfilled = false;
        for (Map<String,Integer> recipe : recipes) {
            // Retrieves as many items from inventory as needed to fulfill recipe. If one of components isn't fulfilled
            // move on to next recipe.
            for (String component : recipe.keySet()) {
                List<Items> query = new ArrayList<>();
                query.addAll(inventory.stream()
                     .filter(i -> i.getItemType().equals(component))
                     .limit(recipe.get(component))
                     .collect(Collectors.toList()));

                if (query.size() != recipe.get(component)) {
                    recipeFulfilled = false;
                    recipeItems.clear();
                    break;
                }
                recipeItems.addAll(query);
                recipeFulfilled = true;
            }
            if (recipeFulfilled) {
                recipeItems.stream().forEach(i -> inventory.remove(i));
                inventory.add((Items) buildable);
                return;
            }
        }
        throw new InvalidActionException("Insufficient items to build");
        

    }

    public List<Entity> getEntities() {
        return this.mapEntities;
    }

    public void setEntities(List<Entity> mapEntities) {
        this.mapEntities = mapEntities;

        // set observers
        for (Entity mapEntity : mapEntities) {
            if (mapEntity instanceof CharacterObserver) {
                this.attach((CharacterObserver) mapEntity);
            }
        }
    }

    public void mapRemove(Entity entity) {
        mapEntities.remove(entity);
    }

    //public void PlayerMovement(Direction direction) {}
    // Under assumption argument passed is an id.
    public void useItem(String item) throws InvalidActionException {
        Items useItem = inventory.stream()
                        .filter(query -> item.equals(query.getItemId())).findAny()
                        .orElseThrow(() -> new InvalidActionException("Item does not exist"));
        
        useItem.use(this);
    }

    //public void checkRing() {}

    public void move(Direction direction) {
        //Position curPos = getPosition();
        Position newPos = getPosition().translateBy(direction);
        
        if (checkWall(mapEntities, newPos) 
        && checkMoveBoulder(mapEntities, newPos, direction)
        && checkDoor(mapEntities, newPos)
        && checkBomb(mapEntities, newPos))
        {
            setPosition(getPosition().translateBy(direction));
            notifyObservers();
            checkBattle();
            moveBoulder(mapEntities, newPos, direction);
        }

        // checkItem(mapEntities, newPos);

        goThroughPortal(mapEntities, newPos);
    }

    public void battle(Mob enemy) {
        // battles do not occur in peaceful mode
        if (!gameMode.equals("Peaceful")) {
            state.battle(enemy);
        }
    }

    public void attach(CharacterObserver observer) {
        observers.add(observer);
        
        // attach observers when generating/loading map?
    }

    public void detach(CharacterObserver observer) {
        observers.remove(observer);

        // detach observers when they are destroyed?
    }

    public void notifyObservers() {
        // iterate over copy of observers to prevent ConcurrentModificationException
        for (CharacterObserver observer : new ArrayList<>(observers)) {
            observer.update(this);
        }
    }

    public boolean isObserver(CharacterObserver observer) {
        return observers.contains(observer);
    }


    /**
     * Checks if a position contains a wall
     * @param entities
     * @param position
     * @return
     */
    public boolean checkWall(List<Entity> entities, Position position) {
        for (Entity entity : entities) {

            Position entPos = entity.getPosition();

            if (entity.getType().equals("wall") && position.equals(entPos)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a position contains a bomb
     * @param character
     * @return
     */
    public boolean checkBomb(List<Entity> entities, Position position) {
        for (Entity entity : entities) {
            Position entPos = entity.getPosition();

            if (entity.getType().equals("bomb") && !this.isObserver(entity) && position.equals(entPos)) {
                return false;
            }
        }
        return true;
    }

    // /**
    //  * Checks if a position contains an item removes the item when picked up
    //  * @param entities
    //  * @param position
    //  */
    // public void checkItem(List<Entity> entities, Position position) {
    //     for (Entity entity : new ArrayList<>(entities)) {

    //         Position entPos = entity.getPosition();

    //         if (entity.getType().equals("treasure")
    //         || entity.getType().equals("arrow")
    //         || entity.getType().equals("wood")
    //         || entity.getType().equals("armour")
    //         ) {
                
    //             if (position.equals(entPos)) {
    //                 entities.remove(entity);
    //             }
    //         }

    //         if (entity.getType().equals("bomb") && position.equals(entPos)) {
    //             entities.remove(entity);
    //         }

    //         if (entity.getType().equals("sword") && position.equals(entPos)) {
    //             entities.remove(entity);
    //         }

    //         if (entity.getType().equals("key") && position.equals(entPos)) {
    //             entities.remove(entity);
    //         }

    //         if (entity.getType().equals("health_potion") && position.equals(entPos)) {
    //             entities.remove(entity);
    //         }

    //         if (entity.getType().equals("invisibility_potion") && position.equals(entPos)) {
    //             entities.remove(entity);
    //         }

    //         if (entity.getType().equals("invincibility_potion") && position.equals(entPos)) {
    //             entities.remove(entity);
    //         }
    //     }
    // }

    // /**
    //  * Checks if a position contains an item removes the item when picked up
    //  * @param entities
    //  * @param position
    //  */
    // public void checkItem(List<Entity> entities, Position position) {
    //     for (Entity entity : new ArrayList<>(entities)) {

    //         Position entPos = entity.getPosition();
    //         if (entity.isCollectable() && position.equals(entPos)) {
    //             entities.remove(entity);
    //         }
    //     }
    // }

    /**
     * Checks if a position contains a boulder and moves the boulder
     * @param entities
     * @param position
     * @param direction
     */
    public void moveBoulder(List<Entity> entities, Position position, Direction direction) {
        for (Entity entity : entities) {

            Position entPos = entity.getPosition();
            if (entity.getType().equals("boulder") && position.equals(entPos)) {
                Position newPos = entPos.translateBy(direction);
                if(checkWall(entities, newPos)) {
                    entity.setPosition(newPos);
                }
            }
        }
    }

    public boolean checkMoveBoulder(List<Entity> entities, Position position, Direction direction) {
        for (Entity entity : entities) {
            Position entPos = entity.getPosition();
            if (entity.getType().equals("boulder") && position.equals(entPos)) {
                for (Entity entity2 : entities) {
                    Position entPos2 = entity2.getPosition();
                    if (entity2.getType().equals("boulder") && position.translateBy(direction).equals(entPos2)) {
                        return false;
                    }
                    if (entity2.getType().equals("wall") && position.translateBy(direction).equals(entPos2)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean checkDoor(List<Entity> entities, Position position) {
        for (Entity entity : entities) {
            Position entPos = entity.getPosition();
            if (entity.getType().equals("door") && position.equals(entPos)) {
                Door door = (Door) entity;
                if (door.isOpen() == false) {
                    // First check for sunstone. If sunstone exists, unlock door, and don't use.
                    if (inventory.stream().anyMatch(e -> e.getItemType().equals("sun_stone"))) {
                        door.setOpen(true);
                        door.setType("door_unlocked");
                        return true;
                    }
                    //check if character have the right key
                    for(Items items : inventory) {
                        if (items.getItemType().equals("key")) {
                            Key key = (Key) items;
                            if (key.getKeyId() == (door.getKeyId())) {
                                door.setOpen(true);
                                door.setType("door_unlocked");
                                key.use(this);
                                return true;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public void checkBattle() {
        for (Entity entity : new ArrayList<>(mapEntities)) {
            if (!entity.getId().equals(this.getId()) && 
                this.isOn(entity) && entity instanceof Mob && 
                (!(entity instanceof Mercenary) || !((Mercenary) entity).isAlly())) {
                
                Mob enemy = (Mob) entity;
                this.battle(enemy);
            }
        }
    }

    public void goThroughPortal(List<Entity> entities, Position position) {
        for (Entity entity : new ArrayList<>(entities)) {

            Position entPos = entity.getPosition();

            if (entity instanceof Portal && position.equals(entPos)) {
                Portal portal = (Portal) entity;
                String portalColour = portal.getColour();

                for (Entity entity2 : new ArrayList<>(entities)) {

                    Position entPos2 = entity2.getPosition();
        
                    if (entity2 instanceof Portal && !entPos.equals(entPos2)) {
                        Portal portal2 = (Portal) entity2;
                        String portal2Colour = portal2.getColour();

                        if (portalColour.equals(portal2Colour)) {
                            this.setPosition(entPos2);
                        }
                    }
                }
            }
        }
    }

    public Sword getSword() {
        Sword sword = null;
        for (Items item : inventory) {
            if (item instanceof Sword) {
                if (item.getItemType().equals("anduril")) {
                    return (Sword) item;
                }
                sword = (Sword) item;
            }
        }
        return sword;
    }

    public Bow getBow() {
        for (Items item : inventory) {
            if (item instanceof Bow) {
                return (Bow) item;
            }
        }
        return null;
    }

    public Armour getArmour() {
        Armour armour = null;
        for (Items item : inventory) {
            if (item instanceof MidnightArmour) {
                return (Armour) item;
            }
            if (item instanceof Armour && armour == null) {
                armour = (Armour) item;
            }
        }
        return armour;
    }

    public Shield getShield() {
        for (Items item : inventory) {
            if (item instanceof Shield) {
                return (Shield) item;
            }
        }
        return null;
    }

    public TheOneRing getTheOneRing() {
        for (Items item : inventory) {
            // if (item.getItemType().equals("one_ring")) {
            if (item instanceof TheOneRing) {
                return (TheOneRing) item;
            }
        }

        return null;
    }

    public Key getKey() {
        for (Items item : inventory) {
            if (item instanceof Key) {
                return (Key) item;
            }
        }
        return null;
    }

    /**
     * Remove enemy from the game, and check for armour and One Ring.
     * @param enemy
     * @param eArmour
     */
    public void removeEnemy(Mob enemy, Armour eArmour) {
        mapRemove(enemy);
        // check enemy armour
        if (eArmour != null) {
            // give to character
            addInventory(eArmour);
        }

        // check enemy theOneRing
        if (TheOneRing.doesDropRing()) {
            String id = "one_ring" + getEntities().size();
            TheOneRing ring = (TheOneRing) ItemFactory.createItem(id, "one_ring");
            addInventory(ring);
        }
    }


    public List<Mercenary> getAllies() {
        List<Mercenary> allies = new ArrayList<>();

        for (Entity e : mapEntities) {
            if (e instanceof Mercenary && ((Mercenary) e).isAlly()) {
                allies.add((Mercenary) e);
            }
        }

        return allies;
    }
}


 