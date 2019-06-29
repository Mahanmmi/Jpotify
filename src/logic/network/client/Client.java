package logic.network.client;

import logic.media.Media;
import logic.network.server.ServerData;
import logic.network.server.ServerRequest;
import logic.network.server.ServerResponse;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

/**
 * Local client class sends and receives data
 * to/from server and handle it
 */
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

    public Client() throws IOException {
        serverData = new HashMap<>();
        socket = new Socket("127.0.0.1", 18757);
    }

    /**
     * Initialize client input and output streams
     * and receive initial data from server
     * @throws IOException Stream exception
     */
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

    /**
     * Create a new user on server and log in to it
     * @param username new user username
     * @param password new user password
     */
    public void createNewUser(String username, String password) {
        name = username;
        ServerData data = new ServerData(name, password);
        data.setOnline(true);
        data.setLastOnline(new Date());
        serverData.put(name, data);
        ClientResponse response = new ClientResponse(ClientResponseType.NEW_USER, data, name);
        try {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("ERROR: createNewUser : " + e.getMessage());
        }
    }

    /**
     * Login into a previously created user on server
     * @param username username on server
     */
    public void setNameAndLogin(String username) {
        name = username;
        ClientResponse response = new ClientResponse(ClientResponseType.LOGIN, null, name);
        try {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("ERROR: setNameAndLogin : " + e.getMessage());
        }
    }

    /**
     * Notify server to change last played shared playlist song
     * @param nowPlaying A shared playlist song
     */
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

    /**
     * Send request to server to get targetName shared playlist
     * @param targetName username of that user we want to get it's shared playlist
     */
    public void requestGetPlaylist(String targetName) {
        ClientRequest request = new ClientRequest(ClientRequestType.PLAYLIST, targetName, name, -1);
        try {
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("ERROR: requestGetPlaylist : " + e.getMessage());
        }
    }

    /**
     * Send request to server to get targetName shared playlist song
     * @param targetName username of that user we want to get it's shared playlist song
     * @param index index of song in target's shared playlist
     */
    public void requestGetSong(String targetName, int index) {
        System.out.println("targetName: " + targetName);
        ClientRequest request = new ClientRequest(ClientRequestType.SONG, targetName, name, index);
        try {
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("ERROR: requestGetSong : " + e.getMessage());
        }
    }

    /**
     * Send a response to getSong or getPlaylist requests
     * @param response response we want to send to server
     */
    void sendResponse(ClientResponse response) {
        try {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("ERROR: sendResponse : " + e.getMessage());
        }
    }

    /**
     * Inform server to set this client offline
     */
    public void sendCloseSocket() {
        ClientResponse response = new ClientResponse(ClientResponseType.CLOSE, null, name);
        try {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("ERROR: sendCloseSocket : " + e.getMessage());
        }
    }

    /**
     * As long as client is online check for requests and responses from server in a thread and handle them
     */
    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            try {
                Object input = inputStream.readObject();
                if (input instanceof ServerResponse) {
                    new ServerResponseHandler((ServerResponse) input).handle();
                } else if (input instanceof ServerRequest) {
                    new ServerRequestHandler((ServerRequest) input).handle();
                }
            } catch (EOFException ignore) {
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
