package logic.network.client;

import logic.media.Media;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String name;

    private String getIP() throws Exception {
        InetAddress ip = InetAddress.getLocalHost();
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        byte[] mac = network.getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        return sb.toString();
    }

    public Client() throws IOException {
        socket = new Socket("127.0.0.1", 18757);
    }

    public void initStreams() throws IOException {
        System.out.println("In1");

        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        System.out.println("In2");
    }

    public void sendNowPlayingSong(Media nowPlaying) {
        Object sentData = (nowPlaying.getTitle() + " : " + nowPlaying.getArtist());
        ClientResponse response = new ClientResponse(ClientResponseType.NOW_PLAYING_SONG, sentData, name);
        try {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("ERROR: sendNowPlayingSong : " + e.getMessage());
        }
    }

    public void sendCloseSocket(){
        ClientResponse response = new ClientResponse(ClientResponseType.CLOSE,null,name);
        try {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("ERROR: sendCloseSocket : " + e.getMessage());
        }
    }

}
