package server_side;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BFS<Solution> extends CommonSearcher<Solution> {

    @Override
    public Solution search(final Searchable s) {
        openList.add(s.getInitialState());
        Set<State> closedSet = new HashSet<>();

        while (openList.size() > 0) {
            State n = popOpenList();
            closedSet.add(n);
            List<State> successors = s.getAllPossibleStates(n);

            if (n.equals(s.getGoalState())) {
                return backTrace(n, s.getInitialState());
            }

            for (State state : successors) {
                if (!closedSet.contains(state) && !openList.contains(state)) {
                    state.setLastLocation(n);
                    openList.add(state);
                } else if (n.getCost() + (state.getCost() - state.getLastLocation().getCost()) < state.getCost())
                    if (openList.contains(state))
                        state.setLastLocation(n);
                    else {
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
