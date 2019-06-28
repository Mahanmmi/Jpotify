package logic.storage;

import graphic.Showable;
import logic.media.Media;
import logic.storage.playlist.AutoPlayList;

import javax.swing.*;
import java.util.ArrayList;

/**
 * this class just use for generating albums
 * implement showable interface
 */
public class Album implements Showable {
    private String albumName;
    private ArrayList<Media> albumSongs = new ArrayList<>();

    Album(String albumName) {
        this.albumName = albumName;
    }

    /**
     * add a media to album aerialist that we call albumSongs
     * @param media this is a media that we want to add to our album
     */
    void addSong(Media media) {
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