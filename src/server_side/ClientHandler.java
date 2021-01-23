package server_side;

import java.io.InputStream;
import java.io.OutputStream;

public interface ClientHandler {
    void handleClient(final InputStream in, final OutputStream out);
}
