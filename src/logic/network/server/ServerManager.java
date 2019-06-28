package logic.network.server;

import logic.network.client.ClientRequest;
import logic.network.client.ClientResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerManager {
    private static ServerManager ourInstance;
    private ServerSocket server;
    private ExecutorService executorService;
    private ArrayList<ClientManager> activeSockets;
    private HashMap<String, ServerData> allActivity;

    ArrayList<ClientManager> getActiveSockets() {
        return activeSockets;
    }

    HashMap<String, ServerData> getAllActivity() {
        return allActivity;
    }

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

    static class ClientManager implements Runnable {
        private Socket client;
        private String name;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Socket getClient() {
            return client;
        }

        ClientManager(Socket client) {
            this.client = client;
            ServerManager.getInstance().activeSockets.add(this);
        }

        void getNotified(String name, ServerData data) {
            try {
                String send = data.getUsername() + "<---->" + data.getPassword() + "<---->"
                        + data.getLastSong() + "<---->" + data.getLastOnline() + "<---->"
                        + data.isOnline();
                outputStream.writeObject(new ServerResponse(ServerResponseType.UPDATE_IN_DATA, send, name));
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void sendRequest(ServerRequest request){
            try {
                outputStream.writeObject(request);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void sendResponse(ServerResponse response){
            try {
                outputStream.writeObject(response);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() {
            try {
                inputStream = new ObjectInputStream(client.getInputStream());
                outputStream = new ObjectOutputStream(client.getOutputStream());

                //Initial data transfers
                outputStream.writeObject(ServerManager.getInstance().getAllActivity());
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }


            while (ServerManager.getInstance().activeSockets.contains(this)) {
                try {
                    Object input = inputStream.readObject();
                    if (input instanceof ClientResponse) {
                        new ClientResponseHandler(this, (ClientResponse) input).handle();
                    } else if(input instanceof ClientRequest){
                        new ClientRequestHandler(this,(ClientRequest) input).handle();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        ourInstance = new ServerManager();
        System.out.println(ourInstance);
        try {
            Thread.sleep(500);
            ServerManager.getInstance().runServer();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}
