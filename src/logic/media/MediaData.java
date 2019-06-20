package logic.media;

import logic.playlist.PlaylistElement;

import java.io.Serializable;
import java.util.ArrayList;

public class MediaData implements Serializable {
    //its array list because a song might be in several array list
    private ArrayList<PlaylistElement> elements;
    //needs another fields for lastPlayTime and number of play (should be static)


    public MediaData(ArrayList<PlaylistElement> elements) {
        this.elements = elements;
    }
}
