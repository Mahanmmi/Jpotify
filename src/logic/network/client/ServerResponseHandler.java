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
            case UPDATE_IN_DATA: {
                serverData.replace(response.getName(),(ServerData) response.getSentData());
                StorageManager.getInstance().getMainPanel().updateGUISongDetails();
            }
        }
    }
}
