package graphic;

import ch.randelshofer.quaqua.QuaquaManager;
import logic.media.Media;
import logic.media.MediaData;
import logic.playlist.Playlist;
import logic.playlist.UserPlaylist;
import logic.storage.StorageManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MainPanel implements PlaylistLinkable {
    private JPanel mainPanel;
    private JPanel listsPanel;
    private JPanel middlePanel;
    private JPanel friendPanel;
    private JPanel upperPannel;
    private JPanel bottomPanel;
    private JPanel detailsPanel;
    private JPanel autoPlaylistsPanel;
    private JPanel volumePanel;
    private JPanel musicButtonsPanel;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textArea3;
    private JSlider volumeSlider;
    private JButton lastTrackButton;
    private JButton volumeButtun;
    private JButton nextTrackButton;
    private JButton play_pause;
    private JButton musicName;
    private JButton artist;
    private JButton favorite;
    private JButton shared;
    private JSlider musicSlider;
    private JFrame frame;


    private void initDarkTheme() {
        System.setProperty(
                "Quaqua.design", "lion"

        );
        Set<String> excludes = new HashSet<>();
        excludes.add("RootPane");
        excludes.add("TextField");
        excludes.add("FileChooser");
        QuaquaManager.setExcludedUIs(excludes);
        try {
            UIManager.setLookAndFeel(QuaquaManager.getLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    private void updatePanelSizeAndColors() {
        textArea1.setBackground(Color.lightGray);
        textArea2.setBackground(Color.lightGray);
        textArea3.setBackground(Color.lightGray);

        upperPannel.setMinimumSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight() * 4 / 6));
        upperPannel.setMaximumSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight() * 4 / 6));
        upperPannel.setPreferredSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight() * 4 / 6));
        bottomPanel.setMinimumSize(new Dimension(mainPanel.getWidth() - 5, mainPanel.getHeight() / 6));
        bottomPanel.setMaximumSize(new Dimension(mainPanel.getWidth() - 5, mainPanel.getHeight() / 6));
        bottomPanel.setPreferredSize(new Dimension(mainPanel.getWidth() - 5, mainPanel.getHeight() / 6));


        mainPanel.setBackground(Color.BLACK);
        listsPanel.setBackground(Color.BLACK);
        middlePanel.setBackground(Color.darkGray);
        friendPanel.setBackground(Color.BLACK);
    }

    private void setMusicPanelIconsAndColors() {
        nextTrackButton.setSize(nextTrackButton.getWidth(), nextTrackButton.getHeight() + 10);
        nextTrackButton.setIcon(new ImageIcon("./resources/New Icons/Actions-media-seek-forward-icon.png"));
//        nextTrackButton.setIcon(new ImageIcon(new ImageIcon("./resources/New Icons/Actions-media-seek-forward-icon.png").getImage().getScaledInstance(nextTrackButton.getHeight() + 4, nextTrackButton.getHeight() + 4, Image.SCALE_DEFAULT)));

        lastTrackButton.setSize(lastTrackButton.getWidth(), lastTrackButton.getHeight() + 10);
        lastTrackButton.setIcon(new ImageIcon("./resources/New Icons/Actions-media-seek-backward-icon.png"));
//        lastTrackButton.setIcon(new ImageIcon(new ImageIcon("./resources/New Icons/Actions-media-seek-backward-icon.png").getImage().getScaledInstance(lastTrackButton.getHeight() + 4, lastTrackButton.getHeight() + 4, Image.SCALE_DEFAULT)));

        volumeButtun.setSize(volumeButtun.getWidth(), volumeButtun.getHeight() + 10);
        volumeButtun.setIcon(new ImageIcon("./resources/New Icons/speaker-icon.png"));
//        volumeButtun.setIcon(new ImageIcon(new ImageIcon("./resources/New Icons/speaker-icon.png").getImage().getScaledInstance(volumeButtun.getHeight() + 4, volumeButtun.getHeight() + 4, Image.SCALE_DEFAULT)));

        play_pause.setSize(play_pause.getWidth(), play_pause.getHeight() + 10);
        play_pause.setIcon(new ImageIcon("./resources/New Icons/Actions-media-playback-start-icon.png"));
//        play_pause.setIcon(new ImageIcon(new ImageIcon("./resources/New Icons/Actions-media-playback-start-icon.png").getImage().getScaledInstance(play_pause.getHeight() + 4, play_pause.getHeight() + 4, Image.SCALE_DEFAULT)));
    }

    private JMenuBar initMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");


        JMenuItem addSongMenuItem = new JMenuItem("Add song...");
        addSongMenuItem.addActionListener(event -> {
            JFileChooser songChooser = new JFileChooser();
            songChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Songs", "mp3");
            songChooser.setFileFilter(filter);
            int returnVal = songChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                StorageManager.getInstance().addMedia(songChooser.getSelectedFile());
            }
        });


        JMenuItem addDirectoryMenuItem = new JMenuItem("Add directory...");
        addDirectoryMenuItem.addActionListener(event -> {
            JFileChooser directoryChooser = new JFileChooser();
            directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = directoryChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                StorageManager.getInstance().addDirectory(directoryChooser.getSelectedFile());
            }
        });


        JMenuItem addPlaylistMenuItem = new JMenuItem("Add playlist...");
        addPlaylistMenuItem.addActionListener(event -> {
            new AddPlaylistPanel(this);
            frame.setVisible(false);
        });


        menu.add(addSongMenuItem);
        menu.add(addDirectoryMenuItem);
        menu.add(addPlaylistMenuItem);

        menuBar.add(menu);

        return menuBar;
    }

    static void newFrameInitialSettings(JFrame frame, JPanel panel) {
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

    private void initFrame() {
        frame = new JFrame("Jpotify");
        newFrameInitialSettings(frame, mainPanel);

        frame.addWindowStateListener(event -> {
            frame.setVisible(false);
            updatePanelSizeAndColors();
            mainPanel.repaint();
            listsPanel.repaint();
            middlePanel.repaint();
            friendPanel.repaint();
            upperPannel.repaint();
            bottomPanel.repaint();
            detailsPanel.repaint();
            autoPlaylistsPanel.repaint();
            volumePanel.repaint();
            musicButtonsPanel.repaint();
            frame.repaint();
            mainPanel.validate();
            listsPanel.validate();
            middlePanel.validate();
            friendPanel.validate();
            upperPannel.validate();
            bottomPanel.validate();
            detailsPanel.validate();
            autoPlaylistsPanel.validate();
            volumePanel.validate();
            musicButtonsPanel.validate();
            frame.validate();
            frame.setVisible(true);
        });

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                updatePanelSizeAndColors();
            }
        });


        updatePanelSizeAndColors();
        frame.setJMenuBar(initMenus());
    }

    public MainPanel() {

        initDarkTheme();
        initFrame();
        setMusicPanelIconsAndColors();
        frame.setVisible(true);
    }


    @Override
    public void doAddPlaylistLink(String name, ArrayList<Media> result) {
        System.out.print("HI ");
        System.out.println(result);

        HashMap<String, Playlist> playlistHashMap = StorageManager.getInstance().getPlaylistHashMap();
        HashMap<String, MediaData> playListMediaDataHashMap = StorageManager.getInstance().getMediaDataHashMap();
        playlistHashMap.put(name, new UserPlaylist(name, result));
        StorageManager.getInstance().updateMediaData();
        //in ja fek konam kamel shod hamaaaal
        frame.setVisible(true);
    }

    @Override
    public void cancelPlaylistOperation() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> StorageManager.getInstance().saveAndQuit(), "Shutdown-thread"));
        System.out.println(StorageManager.getInstance().getMediaArrayList());
    }
}
