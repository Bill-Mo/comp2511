package dungeonmania;

public interface CharacterState {
    
    //public void battle(Mob mob);
    public String getStateName();
    /**
     * Normal state does not have duration, so these are defaulted.
     * @return
     */
    default public int getStateDuration() {
        return -1;
    }
    default public void tickStateDuration() {};

    public void battle(Mob enemy);
}
