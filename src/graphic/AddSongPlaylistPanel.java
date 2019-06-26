package graphic;

import logic.media.Media;
import logic.storage.StorageManager;
import logic.storage.playlist.Playlist;

import javax.swing.*;
import java.util.ArrayList;

public class AddSongPlaylistPanel {
    private JFrame frame;
    private Playlist target;
    private JPanel panel;
    private JList playlistSongs;
    private JButton addSongButton;
    private JButton doneButton;
    private JList songList;

    @SuppressWarnings("unchecked")
    private void setListsContent() {
        ArrayList<String> songs = new ArrayList<>();
        for (Media media : target.getPlaylistMedia()) {
            songs.add(media.getTitle() + " - " + media.getArtist());
        }
        playlistSongs.setListData(songs.toArray());
        songs = new ArrayList<>();
        for (Media media : StorageManager.getInstance().getMediaArrayList()) {
            songs.add(media.getTitle() + " - " + media.getArtist());
        }
        songList.setListData(songs.toArray());
    }

    AddSongPlaylistPanel(PlaylistLinkable parent, Playlist target) {
        frame = new JFrame("Add song to playlist");
        MainPanel.newFrameInitialSettings(frame, panel);
        this.target = target;
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songList.setLayoutOrientation(JList.VERTICAL_WRAP);
        songList.setVisibleRowCount(-1);
        playlistSongs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playlistSongs.setLayoutOrientation(JList.VERTICAL_WRAP);
        playlistSongs.setVisibleRowCount(-1);
        setListsContent();


        addSongButton.addActionListener(e -> {
            if (!songList.isSelectionEmpty()) {
                int index = songList.getSelectedIndex();
                Media media = StorageManager.getInstance().getMediaArrayList().get(index);
                if (!target.getPlaylistMedia().contains(media)) {
                    target.getPlaylistMedia().add(media);
                }
                setListsContent();
            }
        });

        doneButton.addActionListener(e -> {
            parent.doRemovePlaylist(target.getName());
            parent.doAddPlaylistLink(target.getName(), target.getPlaylistMedia());
            frame.dispose();
        });

        frame.setVisible(true);
    }

}
