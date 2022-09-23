package dungeonmania;

public class InvincibleState implements CharacterState {
    private int duration = 10;
    private Character character;

    public InvincibleState(Character character) {
        this.character = character;
    }

    public String getStateName() {
        return "Invincible";
    }
    
    @Override
    public int getStateDuration() {
        return this.duration;
    }

    @Override
    public void tickStateDuration() {
        this.duration -= 1;
    }

    @Override
    public void battle(Mob enemy) {
        // defeat enemy immediately
        // remove enemy from game

        Armour eArmour = null;

        if (enemy instanceof Zombie) {
            Zombie zombie = (Zombie) enemy;
            eArmour = zombie.getArmour();
        } else if (enemy instanceof Mercenary) {
            Mercenary mercenary = (Mercenary) enemy;
            eArmour = mercenary.getArmour();
        }

        character.removeEnemy(enemy, eArmour);
    }
}
