package logic.network.client;

import logic.media.Media;
import logic.network.server.ServerResponse;
import logic.storage.StorageManager;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Client implements Runnable {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String name;
    private HashMap<String, String> friendsActivity;

    public HashMap<String, String> getFriendsActivity() {
        return friendsActivity;
    }

    private String getIP() {
        try {
            return InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Client() throws IOException {
        this.name = getIP();
        friendsActivity = new HashMap<>();
        socket = new Socket("127.0.0.1", 18757);
    }

    public void initStreams() throws IOException {
        System.out.println("In1");
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream.writeObject(name);
        try {
            //noinspection unchecked
            friendsActivity = (HashMap<String, String>) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("In2");
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

    private void handleServerResponse(ServerResponse response) {
        switch (response.getType()) {
            case NOW_PLAYING_SONG: {
                friendsActivity.put(response.getName(), response.getSentData().toString());
                StorageManager.getInstance().getMainPanel().updateGUISongDetails();
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            try {
                Object input = inputStream.readObject();
                if (input instanceof ServerResponse) {
                    handleServerResponse((ServerResponse) input);
                }
            } catch (EOFException e) {
                //Ignore
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
