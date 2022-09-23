package dungeonmania;

import dungeonmania.util.Position;

public class KeyEntity extends Entity {
    private int keyId;

    public KeyEntity(String id, String type, Position position, boolean isInteractable, int keyId) {
        super(id, type, position, isInteractable);
        this.keyId = keyId;
    }

    @Override
    public void update(Character character) {
        if (character.isOn(this) && character.getKey() == null) {
            character.addInventory(new Key(getId(), getType(), 1, keyId));
            character.mapRemove(this);
            character.detach(this);
        }        
    }

    public int getKeyId() {
        return keyId;
    }

}
