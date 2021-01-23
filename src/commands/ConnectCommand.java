package commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;


public class ConnectCommand implements Command, Observer {
    public static volatile boolean stop = false;
    public static PrintWriter printWriter;

    @Override
    public void update(final Observable o, final Object arg) {
        if (arg.getClass() == String.class) {
            printWriter.println("set " + o.toString() + " " + arg);
            printWriter.flush();
            System.out.println("set " + o.toString() + " " + arg);
        }
    }

    @Override
    public void executeCommand(final String[] commandRow) {

        stop = false;

        new Thread(() -> {
            try {
                try {
                    synchronized (OpenDataServer.wait) {
                        OpenDataServer.wait.wait();
                    }
                    Thread.sleep(10000);
                    final Socket socket = new Socket(commandRow[1], Integer.parseInt(commandRow[2]));
                    printWriter = new PrintWriter(socket.getOutputStream());

                    while (!stop) {
                        Thread.onSpinWait();
                    }

                    printWriter.close();
                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
