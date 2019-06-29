package graphic;

import logic.media.Media;
import logic.storage.playlist.Playlist;

import javax.swing.*;
import java.util.ArrayList;

/**
 * this class contain 2 methods that contain edditing playList's Gui and logic
 */
public class EditPlaylist {
    private JPanel panel;
    private JButton cancelButton;
    private JButton upButton;
    private JButton downButton;
    private JButton doneButton;
    private JList mediaList;
    private JButton removeButton;
    private JFrame frame;
    private Playlist target;


    /**
     * sets content of playList in a jlist
     */
    @SuppressWarnings("unchecked")
    private void setListContent() {
        ArrayList<String> songs = new ArrayList<>();
        for (Media media : target.getPlaylistMedia()) {
            songs.add(media.getTitle() + " - " + media.getArtist());
        }
        mediaList.setListData(songs.toArray());
    }

    /**
     * change the position of a song in playList step by step by click on up or down and
     * can remove a song from playList
     * @param parent  @param parent this is instance of playListLinkable that we can give it object
     * of classes that implements this interface
     * @param target this is the playList that we want to edit.
     */
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
        removeButton.addActionListener(e -> {
            int currentIndex = mediaList.getSelectedIndex();
            mediaArrayList.remove(currentIndex);
            mediaList.clearSelection();
            setListContent();
        });

        doneButton.addActionListener(e -> {
            parent.doRemovePlaylist(target.getName());
            parent.doAddPlaylistLink(target.getName(), target.getPlaylistMedia());
            frame.dispose();
        });

        frame.setVisible(true);
    }
}
