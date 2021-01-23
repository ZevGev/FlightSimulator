package server_side;

import java.util.Comparator;
import java.util.PriorityQueue;

public abstract class CommonSearcher<Solution> implements Searcher<Solution> {

    protected PriorityQueue<State> openList;
    protected int evaluateNodes;

    public CommonSearcher() {
        Comparator<State> stateComparator = new StateComparator();
        openList = new PriorityQueue<>(stateComparator);
        evaluateNodes = 0;
    }

    protected State popOpenList() {
        evaluateNodes++;
        return openList.poll();
    }

    protected Solution backTrace(final State goalState, final State initialState) {
        if (goalState.equals(initialState))
            return (Solution) initialState.getState();
        return (Solution) (backTrace(goalState.getLastLocation(), initialState) + "->" + goalState.getState());
    }
}
