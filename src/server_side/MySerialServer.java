package server_side;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MySerialServer implements Server {

    private int port;
    private ClientHandler clientHandler;
    private volatile boolean stop;


    public MySerialServer() {
        this.stop = false;
    }

    @Override
    public void open(int port, ClientHandler c) {
        this.port = port;
        this.clientHandler = c;
        new Thread(() -> {
            try {
                runServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void stop() {
        stop = true;
    }

    private void runServer() throws Exception {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server is open. waiting for clients...");
        server.setSoTimeout(300000000);
        while (!stop) {
            try {
                Socket socket = server.accept();
                System.out.println("Client connected to the server");

                try {
                    clientHandler.handleClient(socket.getInputStream(), socket.getOutputStream());;
                    socket.close();
                } catch (IOException e) {
                    System.out.println("invalid input2-output");
                    e.printStackTrace();
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Time Out");
                e.printStackTrace();
            }
        }
        server.close();
    }
}
