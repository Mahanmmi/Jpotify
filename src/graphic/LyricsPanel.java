package graphic;

import logic.media.LyricsGatherer;
import logic.media.Media;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class LyricsPanel {
    private JPanel panel;
    private JTextArea lyricsArea;

    LyricsPanel(Media song) {
        JFrame frame = new JFrame(song.getTitle() + " - " + song.getArtist());
        MainPanel.newFrameInitialSettings(frame, panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        try {
            List<String> lyrics = LyricsGatherer.getSongLyrics(song.getArtist(), song.getTitle());
            for (String lyric : lyrics) {
                lyricsArea.append(lyric);
            }
            frame.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Can't find lyrics");
            frame.dispose();
        }
    }
}
