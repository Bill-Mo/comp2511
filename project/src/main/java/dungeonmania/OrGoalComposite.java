package dungeonmania;

import java.util.List;
import java.util.stream.Collectors;

public class OrGoalComposite implements Goals {
    private List<Goals> children;

    public OrGoalComposite(List<Goals> children) {
        this.children = children;
    }
    /**
     * Returns goal string depending on how many goals are fulfilled in composite
     */
    public String getGoal() {
        List<Goals> unfulfilled = children.stream().filter(g -> !g.fulfilledGoals()).collect(Collectors.toList());
        int size = unfulfilled.size();
        String returnString = "";
        if (size == children.size()) {
            returnString = "( " + unfulfilled.get(0).getGoal();
            for (int i = 1; i < size; i++) {
                returnString += " OR " + unfulfilled.get(i).getGoal();
            }
            returnString += " )";
        }
        return returnString;
    
    }
    /**
     * Checks children for any fulfilled goals, returning true if it finds any()
     */
    public boolean fulfilledGoals() {
        for (Goals goal : children) {
            if (goal.fulfilledGoals()) {
                return true;
            }
        }
        return false;
    }
}
