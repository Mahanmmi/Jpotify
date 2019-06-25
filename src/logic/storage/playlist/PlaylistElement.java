package logic.storage.playlist;

import java.io.Serializable;

public class PlaylistElement implements Serializable {
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

    @Override
    public String toString() {
        return "PlaylistElement{" +
                "playlistName='" + playlistName + '\'' +
                ", songAddress='" + songAddress + '\'' +
                ", index=" + index +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PlaylistElement){
            return playlistName.equals(((PlaylistElement) obj).playlistName) && songAddress.equals(((PlaylistElement) obj).songAddress);
        }
        return false;
    }
}
