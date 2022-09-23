package dungeonmania;

import java.util.List;
import java.util.stream.Collectors;

public class ExitGoalLeaf extends GoalLeaf {
    public ExitGoalLeaf(Game game) {
        setGoalType(":exit");
        setGame(game);
    }

    @Override
    public boolean fulfilledGoals() {
        Game game = getGame();
        Character character = game.getCharacter();
        // Accounting for possibility of multiple exits.
        List<Entity> exits = game.getEntities().stream().filter(e -> e.getType().equals("exit")).collect(Collectors.toList());
        return exits.stream().anyMatch(e -> e.getPosition().equals(character.getPosition()));
    }
}
