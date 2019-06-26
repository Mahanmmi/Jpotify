package logic.network.client;

import logic.media.Media;
import logic.network.server.ServerManager;
import logic.network.server.ServerRequest;
import logic.storage.StorageManager;

import java.util.ArrayList;

public class ServerRequestHandler {
    private ServerRequest request;

    public ServerRequestHandler(ServerRequest request) {
        this.request = request;
    }

    void handle(){
        switch (request.getType()){
            case PLAYLIST:{
                ArrayList<String> playlist = new ArrayList<>();
                for (Media media : StorageManager.getInstance().getPlaylistHashMap().get("Shared").getPlaylistMedia()) {
                    playlist.add(media.getTitle() + " - " + media.getArtist());
                }
                ClientResponse response = new ClientResponse(ClientResponseType.PLAYLIST,playlist, StorageManager.getInstance().getClient().getName(),request.getSrcName());
                StorageManager.getInstance().getClient().sendResponse(response);
            }
        }
    }
}
