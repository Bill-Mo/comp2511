package dungeonmania;

import java.util.List;
import java.util.stream.Collectors;

public class BoulderGoalLeaf extends GoalLeaf {
    public BoulderGoalLeaf(Game game) {
        setGame(game);
        setGoalType(":boulders");
    }

    @Override
    public boolean fulfilledGoals() {
        Game game = getGame();
        List<Entity> entities = game.getEntities();
        List<Entity> boulders = entities.stream().filter(e -> e.getType().equals("boulder")).collect(Collectors.toList());
        List<Entity> switches = entities.stream().filter(e -> e.getType().equals("switch")).collect(Collectors.toList());
        for (Entity s : switches) {
            if (boulders.stream().noneMatch(b -> b.getPosition().equals(s.getPosition()))) {
                return false;
            }
        }
        return true;
    }
}
