package logic.playlist;

import logic.media.Media;

import java.util.ArrayList;

public abstract class Playlist {
    private String name;
    private ArrayList<Media> playlistMedia;

    private void addMedia(Media media){
        playlistMedia.add(media);
    }
}
