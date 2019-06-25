package logic.network.server;

import logic.network.client.ClientResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerManager {
    private static ServerManager ourInstance = new ServerManager();
    private ServerSocket server;
    private ExecutorService executorService;
    private ArrayList<ClientManager> activeSockets;
    private HashMap<String,String> allActivity;

    public static ServerManager getInstance() {
        return ourInstance;
    }

    private ServerManager() {
        try {
            server = new ServerSocket(18757);
            executorService = Executors.newCachedThreadPool();
            activeSockets = new ArrayList<>();
            allActivity = new HashMap<>();
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
        private String name;
        private String lastSong;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        public Socket getClient() {
            return client;
        }

        ClientManager(Socket client) throws IOException {
            this.client = client;
            ServerManager.getInstance().activeSockets.add(this);
        }

        private void getNotified(String song, String srcName) {
            try {
                outputStream.writeObject(new ServerResponse(ServerResponseType.NOW_PLAYING_SONG, song, srcName));
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void notifyAllClients(String song, String srcName) {
            lastSong = song;
            ServerManager.getInstance().allActivity.put(name,lastSong);
            for (ClientManager activeSocket : ServerManager.getInstance().activeSockets) {
//                if (activeSocket != this) {
                activeSocket.getNotified(song, srcName);
//                }
            }
        }

        private void handleClientResponse(ClientResponse response) {
            switch (response.getType()) {
                case NOW_PLAYING_SONG: {
                    notifyAllClients((String) response.getSentData(), response.getClientName());
                    break;
                }
                case CLOSE: {
                    try {
                        System.out.println(client + " : " + response.getClientName() + " : " + " CLOSED");
                        ServerManager.getInstance().activeSockets.remove(this);
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
                name = (String) inputStream.readObject();
                HashMap<String, String> friendActivity = new HashMap<>();
                for (Map.Entry<String, String> entry : ServerManager.getInstance().allActivity.entrySet()) {
//                    if (activeSocket != this) {
                        friendActivity.put(entry.getKey(),entry.getValue());
//                    }
                }
                outputStream.writeObject(friendActivity);
                outputStream.flush();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            while (ServerManager.getInstance().activeSockets.contains(this)) {
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
