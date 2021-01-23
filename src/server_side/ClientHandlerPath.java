package server_side;

import java.io.InputStream;
import java.io.OutputStream;

public class ClientHandlerPath implements ClientHandler {
    MyClientHandler clientHandler;
    public static volatile boolean stop = false;

    public ClientHandlerPath(MyClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void handleClient(InputStream in, OutputStream out) {
        while (!stop) {
            clientHandler.handleClient(in, out);
        }
    }
}
