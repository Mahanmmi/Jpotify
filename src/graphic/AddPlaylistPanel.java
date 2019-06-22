package graphic;

import logic.media.Media;
import logic.storage.StorageManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AddPlaylistPanel {
    private JPanel panel;
    private JList allSongsList;
    private JList addedList;
    private JButton removeSongButton;
    private JButton addSongButton;
    private JButton DONEButton;
    private JTextField playlistName;
    private JButton cancelButton;
    private JFrame frame;
    private ArrayList<Media> result = new ArrayList<>();
    private ArrayList<Media> mediaArrayList = StorageManager.getInstance().getMediaArrayList();


    private void generateAllSongsList() {
        addedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addedList.setLayoutOrientation(JList.VERTICAL_WRAP);
        addedList.setVisibleRowCount(-1);
        ArrayList<String> mediaTitles = new ArrayList<>();
        allSongsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        allSongsList.setLayoutOrientation(JList.VERTICAL_WRAP);
        allSongsList.setVisibleRowCount(-1);
        for (Media media : mediaArrayList) {
            mediaTitles.add(media.getTitle() + " - " + media.getArtist());
        }
        //noinspection unchecked
        allSongsList.setListData(mediaTitles.toArray());
    }

    private void addToChosenList() {
        if(!allSongsList.isSelectionEmpty()) {
            int index = allSongsList.getSelectedIndex();
            Media media = mediaArrayList.get(index);
            if (!result.contains(media)) {
                result.add(media);
                ArrayList<String> mediaTitles = new ArrayList<>();
                for (Media addedMedia : result) {
                    mediaTitles.add(addedMedia.getTitle() + " - " + addedMedia.getArtist());
                }
                //noinspection unchecked
                addedList.setListData(mediaTitles.toArray());
            }
        }
    }

    private void removeFromChosenList() {
        if(!addedList.isSelectionEmpty()) {
            int index = addedList.getSelectedIndex();
            result.remove(index);
            ArrayList<String> mediaTitles = new ArrayList<>();
            for (Media addedMedia : result) {
                mediaTitles.add(addedMedia.getTitle() + " - " + addedMedia.getArtist());
            }
            //noinspection unchecked
            addedList.setListData(mediaTitles.toArray());
        }
    }

    AddPlaylistPanel(PlaylistLinkable parent) {
        initFrame();
        generateAllSongsList();

        frame.setVisible(true);
        DONEButton.addActionListener(event -> {
            String name = playlistName.getText();
            if (!name.equals("Favorite") && !name.equals("Shared") && !name.equals("") && !StorageManager.getInstance().getPlaylistHashMap().containsKey(name) && result.size() != 0) {
                parent.doAddPlaylistLink(playlistName.getText(), result);
                frame.dispose();
            } else {
                if (name.equals("Favorite") || name.equals("Shared") || name.equals("") || StorageManager.getInstance().getPlaylistHashMap().containsKey(name)) {
                    playlistName.setBackground(Color.RED);
                } else {
                    playlistName.setBackground(new Color(18, 30, 49));
                }
                if(result.size()==0){
                    addedList.setBackground(Color.RED);
                } else {
                    addedList.setBackground(new Color(18, 30, 49));
                }
            }

        });
        addSongButton.addActionListener(event -> addToChosenList());
        removeSongButton.addActionListener(event -> removeFromChosenList());
        cancelButton.addActionListener(event -> {
            frame.dispose();
            parent.cancelPlaylistOperation();
        });
    }

    private void initFrame() {
        frame = new JFrame("Add Playlist");
        MainPanel.newFrameInitialSettings(frame, panel);
    }

}
