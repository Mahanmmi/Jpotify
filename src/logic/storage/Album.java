package logic.storage;

import logic.media.Media;

import java.util.ArrayList;

public class Album {
    private String albumName;
    private String Artist;
    private ArrayList<Media> albumSongs = new ArrayList<>();

    public Album(String albumName) {
        this.albumName = albumName;
    }

    public void addSong(Media media) {
        albumSongs.add(media);

    }


}