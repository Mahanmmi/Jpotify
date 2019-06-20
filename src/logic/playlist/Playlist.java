package logic.playlist;

import logic.media.Media;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Playlist  implements Serializable {
    private String name;
    private ArrayList<Media> playlistMedia;

    public Playlist(String name, ArrayList<Media> playlistMedia) {
        this.name = name;
        this.playlistMedia = playlistMedia;
    }

    private void addMedia(Media media){
        playlistMedia.add(media);
    }

    public ArrayList<Media> getPlaylistMedia() {
        return playlistMedia;
    }
}
