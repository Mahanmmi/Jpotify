package logic.network.client;

import logic.media.Media;
import logic.network.server.ServerRequest;
import logic.storage.StorageManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class handles server requests and send responses
 */
class ServerRequestHandler {
    private ServerRequest request;

    ServerRequestHandler(ServerRequest request) {
        this.request = request;
    }

    /**
     * Handles request by it's type
     */
    void handle(){
        switch (request.getType()){
            case PLAYLIST:{
                ArrayList<String> playlist = new ArrayList<>();
                for (Media media : StorageManager.getInstance().getPlaylistHashMap().get("Shared").getPlaylistMedia()) {
                    playlist.add(media.getTitle() + " - " + media.getArtist());
                }
                ClientResponse response = new ClientResponse(ClientResponseType.PLAYLIST,playlist, StorageManager.getInstance().getClient().getName(),request.getSrcName());
                StorageManager.getInstance().getClient().sendResponse(response);
                break;
            }
            case SONG:{
                try {
                    File myFile = new File(StorageManager.getInstance().getPlaylistHashMap().get("Shared").getPlaylistMedia().get(request.getTargetInPlaylist()).getAddress());
                    byte[] myByteArray = new byte[(int) myFile.length()];
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
                    bis.read(myByteArray, 0, myByteArray.length);
                    ClientResponse response = new ClientResponse(ClientResponseType.SONG,myByteArray,StorageManager.getInstance().getClient().getName(),request.getSrcName());
                    StorageManager.getInstance().getClient().sendResponse(response);
                } catch (IOException e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
