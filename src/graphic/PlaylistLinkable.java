package graphic;

import logic.media.Media;

import java.util.ArrayList;

public interface PlaylistLinkable {
    void doAddPlaylistLink(String name, ArrayList<Media> result);
    void doRemovePlaylist(String name);
    void cancelPlaylistOperation();
}
