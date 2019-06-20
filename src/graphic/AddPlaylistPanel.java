package graphic;

import logic.media.Media;
import logic.storage.StorageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            addedList.setListData(mediaTitles.toArray());
        }
    }

    public AddPlaylistPanel(PlaylistLinkable parent) {
        initFrame();
        generateAllSongsList();

        frame.setVisible(true);
        DONEButton.addActionListener(event -> {
            String name = playlistName.getText();
            if (!name.equals("") && !StorageManager.getInstance().getPlaylistHashMap().containsKey(name) && result.size() != 0) {
                parent.doAddPlaylistLink(playlistName.getText(), result);
                frame.dispose();
            } else {
                if (name.equals("") || StorageManager.getInstance().getPlaylistHashMap().containsKey(name)) {
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
        ImageIcon frameIcon = new ImageIcon("./resources/JpotifyIcon.png");
        frame.setIconImage(frameIcon.getImage());
        frame.setContentPane(panel);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width / 2 - frame.getWidth() / 2;
        int y = screenSize.height / 2 - frame.getHeight() / 2;
        frame.setLocation(x, y);
    }

}
