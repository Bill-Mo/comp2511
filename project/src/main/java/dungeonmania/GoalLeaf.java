package dungeonmania;

public abstract class GoalLeaf implements Goals {
    private String goalType;
    private Game game;

    /**
     * Returns goaltype with colon for frontend image use.
     */
    public String getGoal() {
        if (!fulfilledGoals()) {
            return goalType;
        }
        return "";
        
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public abstract boolean fulfilledGoals();
}
