package server_side;

public interface CacheManager<Problem, Solution> {
    Boolean Check(final Problem in);

    Solution Extract(final Problem in);

    void Save(final Problem in, final Solution out);
}
