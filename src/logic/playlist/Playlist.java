package logic.playlist;

import graphic.Showable;
import logic.media.Media;
import logic.storage.StorageManager;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Playlist implements Serializable, Showable {
    private String name;
    private ArrayList<Media> playlistMedia;

    public Playlist(String name, ArrayList<Media> playlistMedia) {
        this.name = name;
        this.playlistMedia = playlistMedia;
    }

    @Override
    public ImageIcon getIcon() {
        for (Media media : playlistMedia) {
            if (media.getIcon() != null) {
                return media.getIcon();
            }
        }
        return null;
    }

    @Override
    public void getClicked() {
        Media.setCurrentPlaylist(this);
        StorageManager.getInstance().getMainPanel().setShowcaseContent(new ArrayList<>(playlistMedia));
    }

    @Override
    public String getTitle() {
        return name;
    }

    public void addMedia(Media media) {
        playlistMedia.add(media);
    }

    public ArrayList<Media> getPlaylistMedia() {
        return playlistMedia;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", playlistMedia=" + playlistMedia +
                '}';
    }
}
