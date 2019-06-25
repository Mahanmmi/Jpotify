package logic.storage.playlist;

import logic.media.Media;

import java.util.ArrayList;

public class UserPlaylist extends Playlist {
    public UserPlaylist(String name, ArrayList<Media> playlistMedia) {
        super(name, playlistMedia);
    }
}

