package server_side;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Astar<Solution, heuristic> extends CommonSearcher<Solution> {

    public interface Heuristic {
        double cost(State s, State goalState);
    }

    Heuristic heuristic;

    public Astar(final Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public Solution search(final Searchable s) {
        openList.add(s.getInitialState());
        Set<State> closedSet = new HashSet<>();

        while (openList.size() > 0) {
            State n = popOpenList();
            closedSet.add(n);
            List<State> successors = s.getAllPossibleStates(n); //however it is implemented
            n.setCost(n.getCost() + heuristic.cost(n, s.getGoalState()));

            if (n.equals(s.getGoalState())) {
                return backTrace(n, s.getInitialState());
            }

            for (State state : successors) {
                state.setCost(state.getCost() + heuristic.cost(state, s.getGoalState()));

                if (!closedSet.contains(state) && !openList.contains(state)) {
                    state.setLastLocation(n);
                    openList.add(state);
                } else if (n.getCost() + (state.getCost() - state.getLastLocation().getCost()) < state.getCost())
                    if (openList.contains(state)) {
                        state.setLastLocation(n);
                    } else {
                        state.setLastLocation(n);
                        closedSet.remove(state);
                        openList.add(state);
                    }
            }
        }
        return backTrace(s.getGoalState(), s.getInitialState());
    }

    @Override
    public int getNumberOfNodesEvaluated() {
        return evaluateNodes;
    }

}
