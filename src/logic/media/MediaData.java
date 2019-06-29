package logic.media;

import logic.storage.playlist.PlaylistElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * this class use for saving data in files have a playList
 * that contain playlistElement and lastPlayed
 */
public class MediaData implements Serializable {
    //its array list because a song might be in several play list
    private ArrayList<PlaylistElement> elements;
   //its declare last time that song  played
    private Date lastPlayed;

    public Date getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    /**
     * because our sorting is comparative we declare a specified time for all song
     * and compare them by distance of this date
     * @param address this is path of our song
     * @param elements this declare that media is in which playList
     */
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
