package logic.storage;

import graphic.Showable;
import logic.media.Media;

import javax.swing.*;
import java.util.ArrayList;

public class Album implements Showable {
    private String albumName;
    private String Artist;
    private ArrayList<Media> albumSongs = new ArrayList<>();

    public Album(String albumName) {
        this.albumName = albumName;
    }

    public void addSong(Media media) {
        albumSongs.add(media);

    }

    @Override
    public ImageIcon getIcon() {
        for (Media albumSong : albumSongs) {
            if (albumSong.getIcon() != null) {
                return albumSong.getIcon();
            }
        }
        return null;
    }

    @Override
    public void getClicked() {
        StorageManager.getInstance().getMainPanel().setShowcaseContent(new ArrayList<>(albumSongs));
    }

    @Override
    public String getTitle() {
        return albumName;
    }
}