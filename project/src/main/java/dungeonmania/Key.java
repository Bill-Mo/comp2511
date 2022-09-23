package dungeonmania;

public class Key extends Items {
    
    private int keyId;

    public Key(String itemId, String itemType, int durability, int keyId) {
        super(itemId, itemType, durability);
        this.keyId = keyId;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }
}
