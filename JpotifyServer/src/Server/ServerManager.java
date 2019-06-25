package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerManager {
    private static ServerManager ourInstance = new ServerManager();
    private ServerSocket server;
    private ExecutorService executorService;
    private boolean isActive;

    public static ServerManager getInstance() {
        return ourInstance;
    }

    private ServerManager() {
        try {
            server = new ServerSocket(18757);
            executorService = Executors.newCachedThreadPool();
        } catch (IOException e) {
            System.out.println("Can't create server: " + e.getMessage());
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void runServer(){
        while (true){
            try {
                ClientManager manager = new ClientManager(server.accept());
                executorService.submit(manager);
            } catch (IOException e){
                System.out.println("Can't accept client");
            }
        }
    }

    private static class ClientManager implements Runnable {

        private Socket client;
        private InputStream inputStream;
        private OutputStream outputStream;
        ClientManager(Socket client) throws IOException {
            this.client = client;
            inputStream = client.getInputStream();
            outputStream = client.getOutputStream();
        }

        @Override
        public void run() {
            //TODO
        }

    }

    public static void main(String[] args) {
        try {
            Thread.sleep(500);
            ServerManager.getInstance().runServer();
        } catch (InterruptedException e){
            System.out.println("Interrupted");
        }
    }
}
