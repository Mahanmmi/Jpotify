package logic.media;

import logic.playlist.PlaylistElement;

import java.io.Serializable;
import java.util.ArrayList;

public class MediaData implements Serializable {
    private String playlistName;
    private int indexInPlaylist;
    private ArrayList<PlaylistElement> elements;
}
