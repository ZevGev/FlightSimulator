package server_side;

public class boot_server {
    public static void main(String[] args) {
        Server server = new MySerialServer();
        CacheManager cacheManager = new FileCacheManager();
        MyClientHandler clientHandler = new MyClientHandler(cacheManager);
        server.open(2030, new ClientHandlerPath(clientHandler));
    }
}
