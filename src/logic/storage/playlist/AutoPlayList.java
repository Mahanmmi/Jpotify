package logic.storage.playlist;

import logic.media.Media;

import java.util.ArrayList;

/**
 * this class is for autoPlaylists: favorite and shared
 */
public class
AutoPlayList extends Playlist {
    public AutoPlayList(String name) {
        super(name, new ArrayList<>());
    }

    public AutoPlayList(String name, ArrayList<Media> playlistMedia) {
        super(name, playlistMedia);
    }
}
