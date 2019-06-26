package graphic;

import logic.storage.StorageManager;

import javax.swing.*;
import java.util.ArrayList;

public class SharedPlaylistPanel {
    private JList playlistMedia;
    private JPanel panel;
    private JFrame frame;

    public SharedPlaylistPanel(String src, ArrayList<String> media) {
        frame = new JFrame(src + ":::: Shared playlist");
        MainPanel.newFrameInitialSettings(frame, panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        playlistMedia.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playlistMedia.setLayoutOrientation(JList.VERTICAL_WRAP);
        playlistMedia.setVisibleRowCount(-1);

        playlistMedia.addListSelectionListener(event ->{
            if(!playlistMedia.isSelectionEmpty()) {
                System.out.println(src);
                StorageManager.getInstance().getClient().requestGetSong(src, playlistMedia.getSelectedIndex());
                JOptionPane.showMessageDialog(frame,"Download requested");
            }
        });

        //noinspection unchecked
        playlistMedia.setListData(media.toArray());
        frame.setVisible(true);
    }
}
