package dungeonmania;

import java.util.List;

public class TreasureGoalLeaf extends GoalLeaf {
    public TreasureGoalLeaf(Game game) {
        setGame(game);
        setGoalType(":treasure");
    }

    @Override
    public boolean fulfilledGoals() {
        Game game = getGame();
        List<Entity> entities = game.getEntities();
        return entities.stream().noneMatch(e -> e.getType().equals("treasure"));
    }
}
