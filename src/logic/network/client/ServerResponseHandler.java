package logic.network.client;

import graphic.SharedPlaylistPanel;
import logic.network.server.ServerData;
import logic.network.server.ServerResponse;
import logic.storage.StorageManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
            case PLAYLIST: {
                //noinspection unchecked
                new SharedPlaylistPanel(response.getName(), (ArrayList<String>) response.getSentData());
                break;
            }
            case SONG: {
                File downloadDirectory = new File("./downloads");
                int name = downloadDirectory.list().length + 1;
                File downloadFile = new File("./downloads/" + name + ".mp3");
                try {
                    FileOutputStream out = new FileOutputStream(downloadFile);
                    out.write((byte[]) response.getSentData());
                    out.flush();
                    out.close();
                    StorageManager.getInstance().addMedia(downloadFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
