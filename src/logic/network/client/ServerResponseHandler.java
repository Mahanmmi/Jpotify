package logic.network.client;

import logic.network.server.ServerData;
import logic.network.server.ServerResponse;
import logic.storage.StorageManager;

import java.util.HashMap;

public class ServerResponseHandler {
    private ServerResponse response;
    private HashMap<String, ServerData> serverData;

    public ServerResponseHandler(ServerResponse response) {
        this.response = response;
        serverData = StorageManager.getInstance().getClient().getServerData();
    }

    void handle(){
        switch (response.getType()) {
            case NOW_PLAYING_SONG: {
                serverData.get(response.getName()).setLastSong(response.getSentData().toString());
                StorageManager.getInstance().getMainPanel().updateGUISongDetails();
            }
        }
    }
}
