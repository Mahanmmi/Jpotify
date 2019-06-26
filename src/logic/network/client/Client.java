package logic.network.client;

import logic.media.Media;
import logic.network.server.ServerData;
import logic.network.server.ServerResponse;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

public class Client implements Runnable {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String name;
    private HashMap<String, ServerData> serverData;

    public String getName() {
        return name;
    }

    public HashMap<String, ServerData> getServerData() {
        return serverData;
    }

    public void setServerData(HashMap<String, ServerData> serverData) {
        this.serverData = serverData;
    }

    public Client() throws IOException {
        serverData = new HashMap<>();
        socket = new Socket("127.0.0.1", 18757);
    }

    public void initStreams() throws IOException {
        System.out.println("In1");
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        try {
            //noinspection unchecked
            serverData = (HashMap<String, ServerData>) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("In2");
    }

    public void createNewUser(String username, String password){
        name = username;
        ServerData data = new ServerData(name,password);
        data.setOnline(true);
        data.setLastOnline(new Date());
        serverData.put(name,data);
        ClientResponse response = new ClientResponse(ClientResponseType.NEW_USER, data, name);
        try {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("ERROR: setNameAndLogin : " + e.getMessage());
        }
    }

    public void setNameAndLogin(String username){
        name = username;
        ClientResponse response = new ClientResponse(ClientResponseType.LOGIN, null, name);
        try {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("ERROR: setNameAndLogin : " + e.getMessage());
        }
    }

    public void sendNowPlayingSong(Media nowPlaying) {
        Object sentData = (nowPlaying.getTitle() + " - " + nowPlaying.getArtist());
        ClientResponse response = new ClientResponse(ClientResponseType.NOW_PLAYING_SONG, sentData, name);
        try {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("ERROR: sendNowPlayingSong : " + e.getMessage());
        }
    }

    public void sendCloseSocket() {
        ClientResponse response = new ClientResponse(ClientResponseType.CLOSE, null, name);
        try {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("ERROR: sendCloseSocket : " + e.getMessage());
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            try {
                Object input = inputStream.readObject();
                if (input instanceof ServerResponse) {
                    new ServerResponseHandler((ServerResponse) input).handle();
                }
            } catch (EOFException ignore) {
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
