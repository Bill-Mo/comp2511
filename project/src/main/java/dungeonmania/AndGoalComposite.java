package dungeonmania;

import java.util.List;
import java.util.stream.Collectors;

public class AndGoalComposite implements Goals{
    private List<Goals> children;

    public AndGoalComposite(List<Goals> children) {
        this.children = children;
    }
    /**
     * Returns goal string depending on how many goals there are left to fulfill.
     */
    public String getGoal() {
        List<Goals> unfulfilled = children.stream().filter(g -> !g.fulfilledGoals()).collect(Collectors.toList());
        int size = unfulfilled.size();
        String returnString = "";
        if (size > 1) {
            returnString = "( " + unfulfilled.get(0).getGoal();
            for (int i = 1; i < size; i++) {
                returnString += " AND " + unfulfilled.get(i).getGoal();
            }
            returnString += " )";
            
        } else if (size == 1) {
            returnString = unfulfilled.get(0).getGoal();
        }
        return returnString;
    }
    /**
     * Checks children if all goals are fulfilled. If it spots one unfulfilled, returns false.
     */
    public boolean fulfilledGoals() {
        for (Goals goal : children) {
            if (!goal.fulfilledGoals()) {
                return false;
            }
        }
        return true;
    }
}
