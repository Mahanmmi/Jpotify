package logic.network.client;

import graphic.SharedPlaylistPanel;
import logic.network.server.ServerData;
import logic.network.server.ServerResponse;
import logic.storage.StorageManager;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerResponseHandler {
    private ServerResponse response;

    public ServerResponseHandler(ServerResponse response) {
        this.response = response;
    }

    void handle() {
        switch (response.getType()) {
            case UPDATE_IN_DATA: {
                System.out.println("Client update data in process");
                System.out.println(response.getName() + "   " + response.getSentData());
                StorageManager.getInstance().getClient().getServerData().put(response.getName(), new ServerData((String) response.getSentData()));
                try {
                    StorageManager.getInstance().getMainPanel().updateGUISongDetails();
                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("Client update data Done");
                break;
            }
            case PLAYLIST:{
                //noinspection unchecked
                new SharedPlaylistPanel(response.getName(),(ArrayList<String>) response.getSentData());
                break;
            }
        }
    }
}
