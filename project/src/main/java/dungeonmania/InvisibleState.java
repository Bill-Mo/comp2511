package dungeonmania;

public class InvisibleState implements CharacterState{
    private int duration = 15;
    private Character character;
    
    public InvisibleState(Character character) {
        this.character = character;
    }

    public String getStateName() {
        return "Invisible";
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
        // do nothing
    }

}
