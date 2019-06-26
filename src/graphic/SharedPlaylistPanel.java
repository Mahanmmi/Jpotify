package graphic;

import javax.swing.*;
import java.util.ArrayList;

public class SharedPlaylistPanel {
    private JList playlistMedia;
    private JPanel panel;
    private JFrame frame;

    public SharedPlaylistPanel(String src, ArrayList<String> media) {
        frame = new JFrame(src);
        MainPanel.newFrameInitialSettings(frame,panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //noinspection unchecked
        playlistMedia.setListData(media.toArray());
        frame.setVisible(true);
    }
}
