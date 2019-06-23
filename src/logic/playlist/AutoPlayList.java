package logic.playlist;

import logic.media.Media;

import java.util.ArrayList;

public class AutoPlayList extends Playlist {
    public AutoPlayList(String name) {
        super(name, new ArrayList<>());
    }

    public AutoPlayList(String name, ArrayList<Media> playlistMedia) {
        super(name, playlistMedia);
    }
}
