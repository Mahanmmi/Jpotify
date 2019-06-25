package graphic;

import logic.media.Media;
import logic.storage.playlist.Playlist;

import javax.swing.*;
import java.util.ArrayList;

public class EditPlaylist {
    private JPanel panel;
    private JButton cancelButton;
    private JButton upButton;
    private JButton downButton;
    private JButton doneButton;
    private JList mediaList;
    private JFrame frame;
    private Playlist target;


    @SuppressWarnings("unchecked")
    private void setListContent() {
        ArrayList<String> songs = new ArrayList<>();
        for (Media media : target.getPlaylistMedia()) {
            songs.add(media.getTitle() + " - " + media.getArtist());
        }
        mediaList.setListData(songs.toArray());
    }

    EditPlaylist(PlaylistLinkable parent, Playlist target) {
        this.target = target;
        frame = new JFrame("Edit playlist");
        MainPanel.newFrameInitialSettings(frame, panel);
        ArrayList<Media> mediaArrayList = target.getPlaylistMedia();
        mediaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mediaList.setLayoutOrientation(JList.VERTICAL_WRAP);
        mediaList.setVisibleRowCount(-1);
        setListContent();


        cancelButton.addActionListener(e -> {
            parent.cancelPlaylistOperation();
            frame.dispose();
        });


        upButton.addActionListener(e -> {
            int currentIndex = mediaList.getSelectedIndex();
            if (currentIndex != 0) {
                Media media = mediaArrayList.get(currentIndex);
                mediaArrayList.remove(currentIndex);
                mediaArrayList.add(currentIndex - 1, media);
                setListContent();
                mediaList.setSelectedIndex(currentIndex - 1);
            }
        });

        downButton.addActionListener(e -> {
            int currentIndex = mediaList.getSelectedIndex();
            if (currentIndex != mediaArrayList.size() - 1) {
                Media media = mediaArrayList.get(currentIndex);
                mediaArrayList.remove(currentIndex);
                mediaArrayList.add(currentIndex + 1, media);
                setListContent();
                mediaList.setSelectedIndex(currentIndex + 1);
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
