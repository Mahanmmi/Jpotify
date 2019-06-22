package logic.media;

import logic.playlist.PlaylistElement;

import java.io.Serializable;
import java.util.ArrayList;

public class MediaData implements Serializable {
    private String address;
    //its array list because a song might be in several array list
    private ArrayList<PlaylistElement> elements;
    //needs another fields for lastPlayTime and number of play (should be static)


    public MediaData(String address, ArrayList<PlaylistElement> elements) {
        this.address = address;
        this.elements = elements;
    }

    public ArrayList<PlaylistElement> getElements() {
        return elements;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
