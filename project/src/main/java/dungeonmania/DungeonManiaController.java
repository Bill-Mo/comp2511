package dungeonmania;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

public class DungeonManiaController {
    private Game currentGame;
    private List<String> savedGames = new ArrayList<>();
    

    public DungeonManiaController() {
    }

    public String dungeonId() {
        return currentGame.getGameMode() + System.currentTimeMillis();
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("Standard", "Peaceful", "Hard");
    }

    /**
     * /dungeons
     * 
     * Done for you.
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {
        List<String> modes = getGameModes();
        
        // Check validity of arguments
        if (!DungeonManiaController.dungeons().contains(dungeonName) || !modes.contains(gameMode)) {
            throw new IllegalArgumentException();
        }

        // Read the json file
        char charBuf[] = new char[1000000];
        File f = new File("src/main/resources/dungeons/" + dungeonName + ".json");
        try {
            InputStreamReader input = new InputStreamReader(new FileInputStream(f),"UTF-8");
            int len = input.read(charBuf);
            String text = new String(charBuf,0,len);
            JSONObject game = new JSONObject(text);
            input.close();

            // Create entity
            JSONArray JSONEntities = game.getJSONArray("entities");
            EntityFactory factory = new EntityFactory();
            List<Entity> entities = factory.createEntity(JSONEntities, gameMode);
            Character character = factory.getCharacter();
            List<String> buildables = new ArrayList<>();
            
            if (character != null) {
                // Not entirely sure why, but unless I manually set the inventory instead of letting factory do it
                // inventory does not show up in front end.
                character.setInventory(new ArrayList<>());
                character.setEntities(entities);
                currentGame = new Game(dungeonName, gameMode, entities, character.getInventory(),
                                       buildables, game.getJSONObject("goal-condition"), character);
            } else {
                currentGame = new Game(dungeonName, gameMode, entities, new ArrayList<>(),
                 buildables, game.getJSONObject("goal-condition"), character);
            }
            
            return getDungeonResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        File f = new File("src/main/java/dungeonmania/save/" + name + ".json");
        if (f.exists()) {
            throw new IllegalArgumentException("File already exist.");
        }
        try {
            f.createNewFile();
            JSONObject JSONDungeon = new JSONObject();

            // Save name
            savedGames.add(name);

            // Transform dungeonName
            JSONDungeon.put("dungeonName", currentGame.getDungeonName());

            // Transform gameMode
            JSONDungeon.put("gameMode", currentGame.getGameMode());

            // Transform entities
            JSONArray JSONEntities = new JSONArray();
            for (Entity e : currentGame.getEntities()) {
                JSONObject entity = new JSONObject();
                entity.put("id", e.getId());
                Position p = e.getPosition();
                entity.put("x", p.getX());
                entity.put("y", p.getY());
                entity.put("type", e.getType());
                entity.put("isinteractable", e.isInteractable());
                if (e instanceof Portal) {
                    Portal portal = (Portal) e;
                    entity.put("colour", portal.getColour());
                }
                if (e instanceof KeyEntity) {
                    KeyEntity key = (KeyEntity) e;
                    entity.put("key", key.getKeyId());
                }
                if (e instanceof Door) {
                    Door door = (Door) e;
                    entity.put("key", door.getKeyId());
                }
                if (e instanceof SwampTile) {
                    SwampTile swamp = (SwampTile) e;
                    entity.put("movement_factor", swamp.getMovementFactor());
                }
                JSONEntities.put(entity);
            }
            JSONDungeon.put("entities", JSONEntities);

            // Transform inventory
            JSONArray JSONInventory = new JSONArray();
            for (Items i : currentGame.getInventory()) {
                JSONObject item = new JSONObject();
                item.put("id", i.getItemId());
                item.put("type", i.getItemType());
                item.put("durability", i.getDurability());
                if (i instanceof Key) {
                    Key key = (Key) i;
                    item.put("key", key.getKeyId());
                }
                JSONInventory.put(item);
            }
            JSONDungeon.put("inventory", JSONInventory);

            // Transform buildables
            JSONArray JSONBuildables = new JSONArray();
            for (String b : currentGame.getBuildables()) {
                JSONBuildables.put(b);
            }
            JSONDungeon.put("buildables", JSONBuildables);

            // Transform goals
            JSONDungeon.put("goal-condition", currentGame.getJGoals());

            FileWriter fw = new FileWriter(f);
            fw.write(JSONDungeon.toString());
            fw.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getDungeonResponse();
    }

    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        // Read a JSON file
        char charBuf[] = new char[1000000];
        File f = new File("src/main/java/dungeonmania/save/" + name + ".json");

        if (!f.exists()) {
            throw new IllegalArgumentException();
        }

        try {
            InputStreamReader input = new InputStreamReader(new FileInputStream(f),"UTF-8");
            int len = input.read(charBuf);
            String text = new String(charBuf,0,len);
            JSONObject game = new JSONObject(text);
            input.close();
            
            // Load dungeonName
            String dungeonName = game.getString("dungeonName");

            // Load gameMode
            String gameMode = game.getString("gameMode");
            
            // Load entities
            JSONArray JSONEntities = game.getJSONArray("entities");
            EntityFactory eFactory = new EntityFactory();
            List<Entity> entities = eFactory.createEntity(JSONEntities, gameMode);
            Character character = eFactory.getCharacter();

            // Load inventory
            JSONArray JSONInventory = game.getJSONArray("inventory");
            List<Items> inventory = ItemFactory.createInventory(JSONInventory);
            
            // Load buildables
            JSONArray JSONBuildables = game.getJSONArray("buildables");
            List<String> buildables = new ArrayList<>();
            for (int i = 0; i < JSONBuildables.length(); i += 1) {
                String buildableEntity = JSONBuildables.getString(i);
                buildables.add(buildableEntity);
            }

            if (character != null) {
                character.setEntities(entities);
                character.setInventory(inventory);
            }

            currentGame = new Game(dungeonName, gameMode, entities, inventory, buildables, game.getJSONObject("goal-condition"), character);

            return getDungeonResponse();

        } catch (UnsupportedEncodingException e) {
            e.getStackTrace();
        } catch (IOException e1) {
            e1.getStackTrace();
        }
        return null;
    }

    // Get DungeonResponse from currentGame
    public DungeonResponse getDungeonResponse() {
        String dungeonName = currentGame.getDungeonName();
        List<AnimationQueue> animations = new ArrayList<>();

        List<EntityResponse> entitiesR = new ArrayList<>();
        for (Entity e : currentGame.getEntities()) {
            EntityResponse entityR = new EntityResponse(e.getId(), e.getType(), e.getPosition(), e.isInteractable());
            entitiesR.add(entityR);

            if (e instanceof Mob) {
                Mob m = (Mob) e;
                String health = String.valueOf(((double) m.getHealth() / m.getMaxHealth()));
                animations.add(new AnimationQueue("PostTick", m.getId(), Arrays.asList("healthbar set " + health, "healthbar tint 0x00ff00"), false, -1));
            }
        }

        List<ItemResponse> inventoryR = new ArrayList<>();
        for (Items i : currentGame.getInventory()) {
            ItemResponse itemR = new ItemResponse(i.getItemId(), i.getItemType());
            inventoryR.add(itemR);
        }
        currentGame.getGoals();        
        DungeonResponse currentDungeon = new DungeonResponse(dungeonId(), dungeonName, entitiesR, inventoryR, currentGame.getBuildables(), currentGame.getGoals(), animations);

        return currentDungeon;
    }

    public List<String> allGames() {
        return savedGames;
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection)
            throws IllegalArgumentException, InvalidActionException {

        currentGame.tick(itemUsed, movementDirection);

        return getDungeonResponse();
    }

    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        if (!currentGame.getEntities().stream().anyMatch(e -> e.getId().equals(entityId))) {
            throw new IllegalArgumentException("Entity does not exist");
        }
        currentGame.interact(entityId);
        currentGame.interactSpawner(entityId);
        

        return getDungeonResponse();
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        List<String> validBuilds = new ArrayList<>(Arrays.asList("bow", "shield", "sceptre", "midnight_armour"));
        if (!validBuilds.stream().anyMatch(e -> e.equals(buildable))) {
            throw new IllegalArgumentException("Not a valid build item");
        }
        currentGame.build(buildable);
        return getDungeonResponse();
    }

    public Game getCurrentGame() {
        return currentGame;
    }
}

