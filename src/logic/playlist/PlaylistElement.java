package logic.playlist;

import logic.storage.StorageManager;

import java.io.Serializable;

public class PlaylistElement implements Serializable {
    private StorageManager manager = StorageManager.getInstance();
    private String playlistName;
    private String songAddress;
    private int index;

    public PlaylistElement(String playlistName, String songAddress, int index) {
        this.playlistName = playlistName;
        this.songAddress = songAddress;
        this.index = index;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public String getSongAddress() {
        return songAddress;
    }

    public int getIndex() {
        return index;
    }
}
