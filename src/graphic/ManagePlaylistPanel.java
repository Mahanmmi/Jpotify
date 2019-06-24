package graphic;

import com.sun.tools.javac.Main;
import logic.playlist.Playlist;
import logic.playlist.UserPlaylist;
import logic.storage.StorageManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class ManagePlaylistPanel {
    private JButton editButton;
    private JButton removeButton;
    private JButton cancelButton;
    private JList playlistList;
    private JPanel panel;
    private JFrame frame;

    @SuppressWarnings("unchecked")
    public ManagePlaylistPanel(PlaylistLinkable parent) {
        frame = new JFrame("Manage playlists...");
        MainPanel.newFrameInitialSettings(frame,panel);
        HashMap<String, Playlist> playlists = StorageManager.getInstance().getPlaylistHashMap();
        ArrayList<String> listData = new ArrayList<>();
        for (String s : playlists.keySet()) {
            if(playlists.get(s) instanceof UserPlaylist){
                listData.add(s);
            }
        }
        playlistList.setListData(listData.toArray());
        cancelButton.addActionListener(event -> parent.cancelPlaylistOperation());
        removeButton.addActionListener(event -> parent.doRemovePlaylist((String) playlistList.getSelectedValue()));
        frame.setVisible(true);
    }
}
