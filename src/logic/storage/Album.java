package logic.storage;

import graphic.Showable;
import logic.media.Media;
import logic.playlist.AutoPlayList;

import javax.swing.*;
import java.util.ArrayList;

public class Album implements Showable {
    private String albumName;
    private ArrayList<Media> albumSongs = new ArrayList<>();

    public Album(String albumName) {
        this.albumName = albumName;
    }

    public void addSong(Media media) {
        System.out.println(albumName + " lol " + media);
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
        System.out.println(albumName + " " + albumSongs);
        Media.setCurrentPlaylist(new AutoPlayList("Album",albumSongs));
        StorageManager.getInstance().getMainPanel().setShowcaseContent(new ArrayList<>(albumSongs));
    }

    @Override
    public String getTitle() {
        return albumName;
    }
}