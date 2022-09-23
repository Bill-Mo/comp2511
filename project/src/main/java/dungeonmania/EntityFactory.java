package dungeonmania;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.util.Position;

public class EntityFactory {
    private Character character;
    public EntityFactory() {

    }
    /**
     * Takes a JSONArray of entities and a gameMode(to account for player health) and produces
     * a list of entity objects.
     * @param entities
     * @param gameMode
     * @return List of entity objects
     */
    public List<Entity> createEntity(JSONArray entities, String gameMode) {
        List<Entity> entityList = new ArrayList<>();
        Pattern specialPattern = Pattern.compile("key|door|portal|player|swamp_tile");
        for (int i = 0; i < entities.length(); i++) {
            JSONObject entity = entities.getJSONObject(i);
            Position position = new Position(entity.getInt("x"), entity.getInt("y"));
            String type = entity.getString("type");
            Matcher matcher = specialPattern.matcher(type);
            String id = type + String.valueOf(i);
            if (matcher.find()) {
                switch (matcher.group()) {
                    case "door":
                    case "key":
                        entityList.add(createEntityInt(id, position, type, matcher.group(), entity.getInt("key")));
                        break;
                    case "swamp_tile":
                        entityList.add(createEntityInt(id, position, type, matcher.group(), entity.getInt("movement_factor")));
                        break;
                    case "portal":
                        entityList.add(createPortal(id, position, type, entity.getString("colour")));
                        break;
    
                    case "player":
                        entityList.add(createPlayer(id, position, type, gameMode, entityList));
                        break;
                }
            } else {
                entityList.add(createEntity(id, position, type));
            }
            
        }
        return entityList;
    }
    /**
     * Takes in an id, position and type of entity and produces entity object.
     * @param id
     * @param position
     * @param type
     * @return an Entity object of requested position and type.
     */
    public static Entity createEntity(String id, Position position, String type) {
        Pattern entPattern = Pattern.compile("zombie_toast|spider|mercenary|boulder|zombie_toast_spawner|swamp_tile|hydra");
        Matcher matcher = entPattern.matcher(type);
        if (matcher.find()) {
            switch (type) {
                case "zombie_toast":
                    Zombie z = new Zombie(id, type, position, false, 15, 4);
                    // random chance of armour for zombie
                    if (Armour.doesDropArmour()) {
                        String armourId = "armour" + id;
                        Armour a = (Armour) ItemFactory.createItem(armourId, "armour");
                        z.setArmour(a);
                    }
                    return z;
                
                case "spider":
                    return new Spider(id, type, position, false, 10, 3);
                
                case "mercenary":
                    int damage = 5;

                    Random random = new Random();
                    if (random.nextInt(10) < 3) {
                        type = "assassin";
                        damage = 10;
                    }

                    Mercenary m = new Mercenary(id, type, position, true, 18, damage, 1, false);
                    // random chance of armour for mercenary
                    if (Armour.doesDropArmour()) {
                        String armourId = "armour" + id;
                        Armour a = (Armour) ItemFactory.createItem(armourId, "armour");
                        m.setArmour(a);
                    }
                    return m;
    
                case "boulder":
                    return new Boulder(id, type, position, false);
                
                case "zombie_toast_spawner":
                    return new ZombieToastSpawner(id, type, position, true);
                
                case "hydra":
                    return new Zombie(id, "hydra", position, false, 50, 8);
            }
        } 
        return new Entity(id, type, position, false);
    }
    /**
     * Creates a Character object that will represent the player. Health will change based on given gamemode.
     * @param id
     * @param position
     * @param type
     * @param gameMode
     * @param entities
     * @return a Character entity
     */
    public Character createPlayer(String id, Position position, String type, String gameMode, List<Entity> entities) {
        int health = (gameMode.equals("Hard")) ? (10):(20);
        int attack = 5;
        List<Items> inventory = new ArrayList<>();
        character = new Character(id, type, position, false, health, attack, inventory, entities, gameMode);
        return character;
    }

    public Character getCharacter() {
        return character;
    }

    public static Entity createPortal(String id, Position position, String type, String colour) {
        return new Portal(id, colour + "_" + type, position, false, colour);
    }


    public static Entity createEntityInt(String id, Position position, String fullType, String type, int special) {
        switch (type) {
            case "door":
                return new Door(id, fullType, position, false, false, special);
            
            case "key":
                return new KeyEntity(id, fullType, position, false, special);
            
            case "swamp_tile":
                return new SwampTile(id, fullType, position, false, special);
                
            default:
                return null;
        }
    }
}
