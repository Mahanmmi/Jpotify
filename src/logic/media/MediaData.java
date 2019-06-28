package logic.media;

import logic.storage.playlist.PlaylistElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MediaData implements Serializable {
    //its array list because a song might be in several play list
    private ArrayList<PlaylistElement> elements;

    private Date lastPlayed;

    public Date getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public MediaData(String address, ArrayList<PlaylistElement> elements) {
        this.elements = elements;
        lastPlayed = new Date(2000, Calendar.JANUARY,1);
    }

    public ArrayList<PlaylistElement> getElements() {
        return elements;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
