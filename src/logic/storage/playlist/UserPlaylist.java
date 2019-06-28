package logic.storage.playlist;

import logic.media.Media;

import java.util.ArrayList;

/**
 * this class is for userPlaylist(playList that user create by itself)
 */
public class UserPlaylist extends Playlist {
    public UserPlaylist(String name, ArrayList<Media> playlistMedia) {
        super(name, playlistMedia);
    }
}

