package dungeonmania;

public class NormalState implements CharacterState{
    private Character character;

    public NormalState(Character character) {
        this.character = character;
    }
    public String getStateName() {
        return "Normal";
    }

    @Override
    public void battle(Mob enemy) {
        // get enemy initial health so enemy and player can take damage at "same time"
        int enemyHealth = enemy.getHealth();

        // // for testing!!!
        // System.out.println("\n---Battle against " + enemy.getId() + "---");
        // System.out.println("Player Health before: " + character.getHealth());
        // System.out.println("Enemy Health before: " + enemy.getHealth());

        int characterDamage = character.getHealth() * character.getAttack() / 5;
        int enemyDamage = enemyHealth * enemy.getAttack() / 10;

        Shield shield = character.getShield();
        Armour cArmour = character.getArmour();
        Sword sword = character.getSword();
        Bow bow = character.getBow();

        if (shield != null) { // if player has shield
            enemyDamage = enemyDamage * 3 / 4; // decrease damage by 25%
            shield.use(character);
        }

        if (cArmour != null) { // if player has armour
            enemyDamage = enemyDamage / 2; // halve damage
            // If armour is midnight armour, add damage.
            if (cArmour instanceof MidnightArmour) {
                characterDamage += 12;
            }
            cArmour.use(character);
        }

        if (sword != null) { // if player has sword
            // Triple damage if anduril
            if (sword.getItemType().equals("anduril")) {
                characterDamage = characterDamage * 3;
            } else {
                characterDamage = characterDamage * 2; // double damage
            }
            
            sword.use(character);
        }

        if (bow != null) { // if player has bow
            enemy.takeDamage(characterDamage); // allows second attack
            bow.use(character);
        }

        Armour eArmour = null;

        if (enemy instanceof Zombie) {
            //put hydra battle here
            Zombie zombie = (Zombie) enemy;
            eArmour = zombie.getArmour();

            if (eArmour != null) { // if zombie has armour
                characterDamage = characterDamage / 2; // halve damage
                eArmour.use(zombie);
            }
        
        } else if (enemy instanceof Mercenary) {
            Mercenary mercenary = (Mercenary) enemy;
            eArmour = mercenary.getArmour();

            if (eArmour != null) { // if mercenary has armour
                characterDamage = characterDamage / 2; // halve damage
                eArmour.use(mercenary);
            }
        } else if (enemy.getType().equals("hydra")) {
            if (sword.getItemType().equals("anduril")) {
                enemy.takeDamage(characterDamage);
                return;
            }
            else if (Math.random() < 0.5) {
                enemy.takeDamage(characterDamage);
                return;
            }
            else {
                enemy.increaseHealth(characterDamage);
                return;
            }
        }

        enemy.takeDamage(characterDamage);
        character.takeDamage(enemyDamage);

        // // for testing!!!
        // System.out.println("Player Health after: " + character.getHealth());
        // System.out.println("Enemy Health after: " + enemy.getHealth());

        // if enemy health <= 0, remove from game
        if (enemy.getHealth() <= 0) {
            character.removeEnemy(enemy, eArmour);
        }

        // if player health <= 0
        if (character.getHealth() <= 0) {
            // use the one ring if exist
            TheOneRing ring = character.getTheOneRing();
            if (ring != null) {
                ring.use(character);
            } else {
                // remove from game
                character.mapRemove(character);
                return;
            }
        }

        if (enemy.getHealth() > 0) {
            // ally battles
            for (Mercenary ally : character.getAllies()) {
                allyBattle(ally, enemy);
            }
        }

        if ((enemy.getHealth() > 0)) {
            // battle again
            battle(enemy);
        }
    }
    
    public void allyBattle(Mercenary ally, Mob enemy) {
        // get enemy initial health so enemy and mercenary can take damage at "same time"
        int enemyHealth = enemy.getHealth();

        // // for testing!!!
        // System.out.println("Ally Health before: " + ally.getHealth());
        // System.out.println("Enemy Health before: " + enemy.getHealth());

        int allyDamage = ally.getHealth() * ally.getAttack() / 5;
        int enemyDamage = enemyHealth * enemy.getAttack() / 10;

        Armour aArmour = ally.getArmour();

        if (aArmour != null) { // if ally has armour
            enemyDamage = enemyDamage / 2; // halve damage
            aArmour.use(ally);
        }

        Armour eArmour = null;

        if (enemy instanceof Zombie) {
            Zombie zombie = (Zombie) enemy;
            eArmour = zombie.getArmour();

            if (eArmour != null) { // if zombie has armour
                allyDamage = allyDamage / 2; // halve damage
                eArmour.use(zombie);
            }
        
        } else if (enemy instanceof Mercenary) {
            Mercenary mercenary = (Mercenary) enemy;
            eArmour = mercenary.getArmour();

            if (eArmour != null) { // if mercenary has armour
                allyDamage = allyDamage / 2; // halve damage
                eArmour.use(mercenary);
            }
        } else if (enemy.getType().equals("hydra")) {

            if (Math.random() < 0.5) {
                enemy.takeDamage(allyDamage);
                return;
                
            }
            else {
                enemy.increaseHealth(allyDamage);
                return;
            }
        }

        enemy.takeDamage(allyDamage);
        ally.takeDamage(enemyDamage);

        // // for testing!!!
        // System.out.println("Ally Health after: " + ally.getHealth());
        // System.out.println("Enemy Health after: " + enemy.getHealth());

        // if enemy health <= 0, remove from game
        if (enemy.getHealth() <= 0) {
            character.removeEnemy(enemy, eArmour);
        }

        // if ally health <= 0
        if (ally.getHealth() <= 0) {
            // remove from game
            character.removeEnemy(ally, aArmour);
        }
    }
}
