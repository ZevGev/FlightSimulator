package server_side;

public class SolverSearcher<Problem, Solution> implements Solver<Problem, Solution> {
    private final Searcher searcher;

    public SolverSearcher(Searcher searcher) {
        this.searcher = searcher;
    }

    @Override
    public Solution Solve(Problem problem) {
        return (Solution) searcher.search((Searchable) problem);
    }

}
