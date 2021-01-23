package commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import expressions.ShuntingYard;
import interpreter.CompParser;
import server_side.MySerialServer;
import server_side.Server;

public class OpenDataServer implements Command {
    public static final String SPLITTER = ",";
    public static volatile boolean stop = false;
    public static final Object wait = new Object();
    private Server server;

    @Override
    public void executeCommand(final String[] commandRow) {
        stop = false;
        server = new MySerialServer();
        server.open(Integer.parseInt(commandRow[1]), (in, out) -> {
            BufferedReader Bin = new BufferedReader(new InputStreamReader(in));
            synchronized (OpenDataServer.wait) {
                OpenDataServer.wait.notifyAll();
            }

            while (!stop) {
                try {
                    final String line = Bin.readLine();
                    final String[] vars = line.split(SPLITTER);

                    for (int i = 0; i < vars.length; i++) {
                        if (Double.parseDouble(vars[i]) != CompParser.symbolTable.get(CompParser.vars.get(i)).getValue())
                            CompParser.symbolTable.get(CompParser.vars.get(i)).setValue(Double.parseDouble(vars[i]));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    final long num = (long) ShuntingYard.calc("1000/" + commandRow[2]);
                    Thread.sleep(num);
                } catch (InterruptedException ignored) {
                }
            }
            server.stop();
        });
    }
}
