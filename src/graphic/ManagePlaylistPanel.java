package graphic;

import logic.storage.playlist.Playlist;
import logic.storage.playlist.UserPlaylist;
import logic.storage.StorageManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ManagePlaylistPanel {
    private JButton editButton;
    private JButton removeButton;
    private JButton cancelButton;
    private JList playlistList;
    private JPanel panel;
    private JButton addSongButton;
    private JFrame frame;

    @SuppressWarnings("unchecked")
    ManagePlaylistPanel(PlaylistLinkable parent) {
        frame = new JFrame("Manage playlists...");
        MainPanel.newFrameInitialSettings(frame,panel);
        HashMap<String, Playlist> playlists = StorageManager.getInstance().getPlaylistHashMap();
        ArrayList<String> listData = new ArrayList<>();
        for (String s : playlists.keySet()) {
            if(playlists.get(s) instanceof UserPlaylist){
                listData.add(s);
            }
        }
        playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playlistList.setLayoutOrientation(JList.VERTICAL_WRAP);
        playlistList.setVisibleRowCount(-1);
        playlistList.setListData(listData.toArray());
        cancelButton.addActionListener(event -> {
            parent.cancelPlaylistOperation();
            frame.dispose();
        });
        removeButton.addActionListener(event -> {
            parent.doRemovePlaylist((String) playlistList.getSelectedValue());
            frame.dispose();
        });
        editButton.addActionListener(event -> {
            new EditPlaylist(parent,playlists.get(playlistList.getSelectedValue()));
            frame.dispose();
        });
        addSongButton.addActionListener(e -> {
            new AddSongPlaylistPanel(parent,playlists.get(playlistList.getSelectedValue()));
            frame.dispose();
        });
        frame.setVisible(true);
    }
}
