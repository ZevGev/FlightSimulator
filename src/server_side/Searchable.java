package server_side;

import java.util.List;

public interface Searchable {
    State getInitialState();

    State getGoalState();

    List<State> getAllPossibleStates(State S);
}
