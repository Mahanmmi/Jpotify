package logic.network.server;

import logic.network.client.ClientResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerManager {
    private static ServerManager ourInstance = new ServerManager();
    private ServerSocket server;
    private ExecutorService executorService;
    private ArrayList<Socket> activeSockets;

    public static ServerManager getInstance() {
        return ourInstance;
    }

    private ServerManager() {
        try {
            server = new ServerSocket(18757);
            executorService = Executors.newCachedThreadPool();
            activeSockets = new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Can't create server: " + e.getMessage());
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void runServer() {
        System.out.println("logic.network.server running");
        while (true) {
            try {
                Socket client = server.accept();
                activeSockets.add(client);
                System.out.println(client + " JOINED");
                ClientManager manager = new ClientManager(client);
                executorService.submit(manager);
            } catch (IOException e) {
                System.out.println("Can't accept client: " + e.getMessage());
            }
        }
    }

    private static class ClientManager implements Runnable {

        private Socket client;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        ClientManager(Socket client) throws IOException {
            this.client = client;
        }

        private void handleClientResponse(ClientResponse response) {
            switch (response.getType()) {
                case NOW_PLAYING_SONG: {
                    System.out.println(response.getSentData());
                    break;
                }
                case CLOSE: {
                    try {
                        System.out.println(client + " CLOSED");
                        ServerManager.getInstance().activeSockets.remove(client);
                        client.close();
                    } catch (IOException e) {
                        System.out.println("Cant close socket on server");
                    }
                    break;
                }
            }
        }

        @Override
        public void run() {
            try {
                inputStream = new ObjectInputStream(client.getInputStream());
                outputStream = new ObjectOutputStream(client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (ServerManager.getInstance().activeSockets.contains(client)) {
                try {
                    Object input = inputStream.readObject();
                    if (input instanceof ClientResponse) {
                        handleClientResponse((ClientResponse) input);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        try {
            Thread.sleep(500);
            ServerManager.getInstance().runServer();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}
