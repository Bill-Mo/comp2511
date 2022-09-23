package dungeonmania;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Game {
    
    private String dungeonName;
    private String gameMode;
    private List<Entity> entities;
    private List<Items> inventory;
    private List<String> buildables;
    private Goals goals;
    private Position entryPos;
    private JSONObject jGoals;
    private Character character;
    private int gameTick;
    
    public Game(String dungeonName, String gameMode, List<Entity> entities, List<Items> inventory,
            List<String> buildables, JSONObject jGoals, Character character) {
        this.dungeonName = dungeonName;
        this.gameMode = gameMode;
        this.entities = entities;
        this.inventory = inventory;
        this.buildables = buildables;
        this.jGoals = jGoals;
        this.character = character;
        this.goals = GoalFactory.createGoals(jGoals, this);
        this.gameTick = 0;
        // So Entry Pos does not change along with character position.
        this.entryPos = new Position(character.getPosition().getX(), character.getPosition().getY());
    }

    public String getDungeonName() {
        return dungeonName;
    }

    public String getGameMode() {
        return gameMode;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Items> getInventory() {
        return inventory;
    }

    public List<String> getBuildables() {
        return buildables;
    }

    public String getGoals() {
        return goals.getGoal();
    }

    public JSONObject getJGoals() {
        return jGoals;
    }

    public Character getCharacter() {
        return character;
    }

    public void build(String buildable) {
        if (buildables.contains(buildable)) {
            character.buildItem(buildable);
            buildCheck();
        }
    }

    private void buildCheck() {
        int numWood = 0;
        int numArrows = 0;
        int numTreasure = 0;
        int numStone = 0;
        int numKey = 0;
        int numArmour = 0;
        List<String> canBuild = new ArrayList<>();
        for (Items items : new ArrayList<>(inventory)) {

            if (items.getItemType().equals("wood")) {
                numWood++;
            } else if (items.getItemType().equals("arrow")) {
                numArrows++;
            } else if (items.getItemType().equals("treasure")) {
                numTreasure++;
            } else if (items.getItemType().equals("key")) {
                numKey++;
            } else if (items.getItemType().equals("sun_stone")) {
                numStone++;
            } else if (items.getItemType().equals("armour")) {
                numArmour++;
            }

        }

        if (numWood > 0 && numArrows > 2) {
            canBuild.add("bow");
        } 
        if (numWood > 1 && numTreasure > 0) {
            canBuild.add("shield");
        } else if (numWood > 1 && numKey > 0) {
            canBuild.add("shield");
        }
        if (numWood > 0 || numArrows > 1) {
            if (numKey + numTreasure > 0 && numStone > 0) {
                canBuild.add("sceptre");
            }
        }
        if (numArmour > 0 && numStone > 0 && entities.stream().noneMatch(e -> e.getType().equals("zombie_toast"))) {
            canBuild.add("midnight_armour");
        }
        buildables = canBuild;
    }

    public void tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        character.move(movementDirection);
        // Won't bother checking if item is not uses(null)
        int itemExists = itemUsed == null ? -1 : 0;
        
        List<String> validUse = new ArrayList<>(Arrays.asList("health_potion","invisibility_potion",
                                                              "invincibility_potion","bomb"));
        for (Items items : new ArrayList<>(inventory)) {
            if (items.getItemId().equals(itemUsed)) {
                if (!validUse.stream().anyMatch(e -> e.equals(items.getItemType()))) {
                    throw new IllegalArgumentException("Not a valid use item");
                }
                itemExists = 1;
                items.use(character);
            }

        }
        // Item id is not null, but does not exist in inventory
        if (itemExists == 0) {
            throw new InvalidActionException("Item does not exist");
        }

        buildCheck();

        for (Entity entity : new ArrayList<>(entities)) {
            if (entity instanceof Enemies) {
                Enemies enemy = (Enemies) entity;
                if (enemy.canMove()) {
                    enemy.move(entities, character);
                    enemy.isOnSwampTile(getSwampTilePosition());
                }
            }
            if (entity instanceof Mercenary) {
                Mercenary merc = (Mercenary) entity;
                merc.controlTick();
            }
        }


        //zombieToastSpawner
        int spawnTick;
        if (gameMode.equals("Hard")) {
            spawnTick = 15;    
        } else {
            spawnTick = 20;
        }

        if (gameTick % spawnTick == 0) {
            for (Entity entity : new ArrayList<>(entities)) {
                if (entity instanceof ZombieToastSpawner) {
                    Zombie zombie = new Zombie(System.currentTimeMillis()+"zombie_toast", "zombie_toast", entity.getPosition(), false, 15, 4);
                    entities.add(zombie);
                }
            }
            // So mercenaries don't spawn immediately. Not even Dark Souls would be that cruel.
            if (gameTick > 0 && entities.stream().anyMatch(e -> e instanceof Enemies)) {
                entities.add(EntityFactory.createEntity(System.currentTimeMillis()+"mercenary", entryPos, "mercenary"));
            }
            
        }    

        //spider spawning
        // int maxX = 0;
        // int maxY = 0;
        // int minX = 0;
        // int minY = 0;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int spiderNum = 0;
        
        for (Entity entity : entities) {
            if (entity.getPosition().getX() > maxX) {
                maxX = entity.getPosition().getX();
            }
            if (entity.getPosition().getY() > maxY) {
                maxY = entity.getPosition().getY();
            }
            if (entity.getPosition().getX() < minX) {
                minX = entity.getPosition().getX();
            }
            if (entity.getPosition().getY() < minY) {
                minY = entity.getPosition().getY();
            }
            if (entity instanceof Spider) {
                spiderNum++;
            }

        }

        //System.out.println(maxX +"|"+ maxY +"|"+ minX +"|"+ minY);

        Random random = new Random();
        //generate random percentage
        if (random.nextInt(5) == 0 && spiderNum < 4) {
            //generate random position
            Position randPos = new Position(random.nextInt((maxX - minX + 1) + minY), random.nextInt((maxY - minY + 1)) + minY);
            Spider spider = new Spider(System.currentTimeMillis()+"spider", "spider", randPos, false, 10, 3);
            entities.add(spider);
        }

        if (gameMode.equals("Hard") && gameTick%50 == 0 && gameTick != 0) {
            Position randPos = new Position(random.nextInt((maxX - minX + 1) + minY), random.nextInt((maxY - minY + 1)) + minY);
            Zombie hydra = new Zombie(System.currentTimeMillis()+"hydra", "hydra", randPos, false, 50, 8);
            entities.add(hydra);
        }   

        gameTick++;
    }

    public void interact(String entityId) throws InvalidActionException {
        // Entity not existing exception already handled at DunegonManiaController, so this is safe.
        Entity ent = entities.stream().filter(e -> e.getId().equals(entityId) && e.isInteractable()).findFirst().get();
        if (!ent.getPosition().getAdjacentPositions2().contains(character.getPosition())) {
            throw new InvalidActionException("Not in range to do this!");
        }
        if (ent instanceof Mercenary) {
            Mercenary mercenary = (Mercenary) ent;
            // Prioritise sceptre usage
            if (inventory.stream().anyMatch(e -> e.getItemType().equals("sceptre"))) {
                mercenary.control(character, inventory.stream().filter(e -> e.getItemType().equals("sceptre")).findFirst().get());
            } else {
                List<Items> bribeItems = inventory.stream().filter(e ->
                            e.getItemType().equals("treasure") || e.getItemType().equals("sun_stone"))
                            .limit(mercenary.getBribeAmount())
                            .collect(Collectors.toList());
                // Need the ring in order to bribe an assassin
                if (bribeItems.size() < mercenary.getBribeAmount()) {
                    throw new InvalidActionException("Come back when you're a little hmmm... richer!");
                } 

                if (mercenary.getType().equals("assassin")) {
                    // If ring does not exist in the player's inventory, cannot bribe assassin.
                    try {
                        Items ring = inventory.stream().filter(e -> e.getItemType().equals("one_ring")).findFirst().get();
                        bribeItems.add(ring);
                    } catch (NoSuchElementException e) {
                        throw new InvalidActionException("Petty tinkets won't satisfy me you heathen.");
                    }
                    
                }

                bribeItems.stream().forEach((e) -> 
                {
                    // Do not use sun stone in bribery, but still counts towards total.
                    if (!e.getItemType().equals("sun_stone")) {
                        inventory.remove(e);
                    } 
                });
                // Set the mercenary to be a permanent ally, and turn off their interactability.
                mercenary.bribe();
                mercenary.setInteractable(false);
            }
        }
    }



    public void interactSpawner(String entityId) throws InvalidActionException {
       
        for (Entity entity : new ArrayList<>(entities)) {

            if (entity.getId().equals(entityId) && entity instanceof ZombieToastSpawner) {
                boolean destroyed = false;
                Position spawnerPos = entity.getPosition();
                List<Position> adjacentPos = spawnerPos.getCardinallyAdjacentPosition();

                for (Position p : adjacentPos) {
                    if (character.getPosition().equals(p)){
                        if (!inventory.stream().anyMatch(e -> e.getItemType().equals("sword") 
                                                        || e.getItemType().equals("anduril"))) {
                            throw new InvalidActionException("You need a weapon to destroy this");
                        }
                        entities.remove(entity);
                        destroyed = true;
                        break;                   
                    }

                }
                if (!destroyed) {
                    throw new InvalidActionException("You need to be adjacent to the spawner to destroy it");
                }
                
            }
        }

    }

    public ArrayList<SwampTile> getSwampTilePosition() {
        ArrayList<SwampTile> swampTiles = new ArrayList<SwampTile>();
        for (Entity e : entities) {
            if (e.getType().equals("swamp_tile")) {
                swampTiles.add((SwampTile) e);
            }
        }
        return swampTiles;
    }
}
