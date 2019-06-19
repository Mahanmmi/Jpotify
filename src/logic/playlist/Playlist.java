package logic.playlist;

import logic.media.Media;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Playlist implements Serializable {
    private String name;
    private ArrayList<Media> playlistMedia;

    public void addMedia(Media media) {
        playlistMedia.add(media);
    }
}
