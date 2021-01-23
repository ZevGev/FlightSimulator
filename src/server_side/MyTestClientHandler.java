package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MyTestClientHandler<Problem, Solution> implements ClientHandler {

    @SuppressWarnings("rawtypes")
    private final Solver<Problem, Solution> solver;
    @SuppressWarnings("rawtypes")
    private final CacheManager<Problem, Solution> cacheManager;

    public MyTestClientHandler(CacheManager<Problem, Solution> cacheManager, Solver<Problem, Solution> solver) {
        this.solver = solver;
        this.cacheManager = cacheManager;
    }

    @Override
    public void handleClient(InputStream in, OutputStream out) {
        BufferedReader Bin = new BufferedReader(new InputStreamReader(in));
        PrintWriter Bout = new PrintWriter(new OutputStreamWriter(out));
        try {
            Problem Line;
            Solution Solved;

            while (!(Line = (Problem) Bin.readLine()).equals("end")) {

                if (cacheManager.Check(Line)) {
                    Solved = cacheManager.Extract(Line);
                } else {
                    Solved = solver.Solve(Line);
                    cacheManager.Save(Line, Solved);
                }
                Bout.println(Solved);
                Bout.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Bin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bout.close();
    }

}
