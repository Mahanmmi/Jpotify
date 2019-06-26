package logic.network.server;

import logic.network.client.ClientResponse;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

class ClientResponseHandler {
    private ServerManager.ClientManager source;
    private ClientResponse response;
    private HashMap<String,ServerData> allActivity;

    ClientResponseHandler(ServerManager.ClientManager source, ClientResponse response) {
        this.source = source;
        this.response = response;
        allActivity = ServerManager.getInstance().getAllActivity();
    }

    private void notifyAllClients() {
        String song = (String) response.getSentData();
        String name = response.getClientName();
        ServerData data = allActivity.get(name);
        data.setLastSong(song);
        for (ServerManager.ClientManager activeSocket : ServerManager.getInstance().getActiveSockets()) {
//                if (activeSocket != source) {
            activeSocket.getNotified(song, name);
//                }
        }
    }

    void handle(){
        switch (response.getType()) {
            case NOW_PLAYING_SONG: {
                notifyAllClients();
                break;
            }
            case CLOSE: {
                try {
                    System.out.println(source.getClient() + " : " + response.getClientName() + " : " + " CLOSED");
                    allActivity.get(response.getClientName()).setLastOnline(new Date());
                    ServerManager.getInstance().getActiveSockets().remove(source);
                    source.getClient().close();
                } catch (IOException e) {
                    System.out.println("Cant close socket on server");
                }
                break;
            }
        }
    }
}
