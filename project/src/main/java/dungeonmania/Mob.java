package dungeonmania;

import dungeonmania.util.Position;

public class Mob extends Entity {
    private int health;
    private int attack;
    private int maxHealth;

    public Mob(String id, String type, Position position, boolean isInteractable, int health, int attack) {
        super(id, type, position, isInteractable);
        this.health = health;
        this.maxHealth = health;
        this.attack = attack;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }
    
    public void takeDamage(int damage) {
        health = health - damage;
    }

    public void increaseHealth(int increaseAmount) {
        health = Math.min(health + increaseAmount, maxHealth);
    }
}
