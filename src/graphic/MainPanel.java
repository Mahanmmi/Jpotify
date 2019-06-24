package graphic;

import ch.randelshofer.quaqua.QuaquaManager;
import javazoom.jl.decoder.JavaLayerException;
import logic.media.Media;
import logic.media.MediaData;
import logic.playlist.Playlist;
import logic.playlist.UserPlaylist;
import logic.storage.StorageManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.Timer;

public class MainPanel implements PlaylistLinkable {
    private JPanel mainPanel;
    private JPanel listsPanel;
    private JPanel middlePanel;
    private JPanel friendPanel;
    private JPanel upperPanel;
    private JPanel bottomPanel;
    private JPanel detailsPanel;
    private JPanel autoPlaylistsPanel;
    private JPanel volumePanel;
    private JPanel musicButtonsPanel;
    private JTextArea textArea3;
    private JSlider volumeSlider;
    private JButton volumeButton;
    private JButton lastTrackButton;
    private JButton nextTrackButton;
    private JButton play_pause;
    private JButton musicTitle;
    private JButton artist;
    private JButton favorite;
    private JButton shared;
    private JSlider musicSlider;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel artworkPanel;
    private JPanel albumsPanel;
    private JPanel playlistsPanel;
    private JList playlistList;
    private JList albumList;
    private JButton replayButtun;
    private JButton shuffleButton;
    private JPanel sliderPanel;
    private JPanel timepanel;
    private JLabel timeLabel;
    private JLabel artWorkLAbel;
    private JScrollPane showcasePane;
    private JPanel showcasePanel;
    private JFrame frame;

    public void setArtWorkLAbel() {
        if (Media.getNowPlaying().getIcon() == null) {
            artWorkLAbel.setIcon(new ImageIcon(new ImageIcon("./resources/New Icons/Music-icon.png").getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT)));
        } else {
            artWorkLAbel.setIcon(new ImageIcon(Media.getNowPlaying().getIcon().getImage().getScaledInstance(160, 160, Image.SCALE_DEFAULT)));
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    public JSlider getMusicSlider() {
        return musicSlider;
    }

    private void initDarkTheme() {
        System.setProperty("Quaqua.design", "lion");
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
        textArea3.setBackground(Color.lightGray);

        upperPanel.setMinimumSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight() * 4 / 6));
        upperPanel.setMaximumSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight() * 4 / 6));
        upperPanel.setPreferredSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight() * 4 / 6));
        bottomPanel.setMinimumSize(new Dimension(mainPanel.getWidth() - 5, mainPanel.getHeight() / 6));
        bottomPanel.setMaximumSize(new Dimension(mainPanel.getWidth() - 5, mainPanel.getHeight() / 6));
        bottomPanel.setPreferredSize(new Dimension(mainPanel.getWidth() - 5, mainPanel.getHeight() / 6));


        mainPanel.setBackground(Color.BLACK);
        listsPanel.setBackground(Color.BLACK);
        middlePanel.setBackground(Color.BLACK);
        friendPanel.setBackground(Color.BLACK);
    }

    private void setMusicPanelIconsAndColors() {
        favorite.setSize(favorite.getWidth(), favorite.getHeight() + 10);
        favorite.setIcon(new ImageIcon("./resources/New Icons/heart-icon.png"));

        shared.setSize(shared.getWidth(), shared.getHeight() + 10);
        shared.setIcon(new ImageIcon("./resources/New Icons/share-icon.png"));

        nextTrackButton.setSize(nextTrackButton.getWidth(), nextTrackButton.getHeight() + 10);
        nextTrackButton.setIcon(new ImageIcon("./resources/New Icons/Actions-media-seek-forward-icon.png"));

        lastTrackButton.setSize(lastTrackButton.getWidth(), lastTrackButton.getHeight() + 10);
        lastTrackButton.setIcon(new ImageIcon("./resources/New Icons/Actions-media-seek-backward-icon.png"));

        volumeButton.setSize(volumeButton.getWidth(), volumeButton.getHeight() + 10);
        volumeButton.setIcon(new ImageIcon("./resources/New Icons/speaker-icon.png"));

        play_pause.setSize(play_pause.getWidth(), play_pause.getHeight() + 10);
        play_pause.setIcon(new ImageIcon("./resources/New Icons/Actions-media-playback-start-icon.png"));

        shuffleButton.setSize(shuffleButton.getWidth(), shuffleButton.getHeight() + 10);
        shuffleButton.setIcon(new ImageIcon("./resources/New Icons/shuffle-icon.png"));

        replayButtun.setSize(replayButtun.getWidth(), replayButtun.getHeight() + 10);
        replayButtun.setIcon(new ImageIcon("./resources/New Icons/replay-icon.png"));
    }

    private void addAutoPlaylistsListeners() {
        favorite.addActionListener(event -> {
            Media nowPlaying = Media.getNowPlaying();
            if (nowPlaying.isFave()) {
                StorageManager.getInstance().getPlaylistHashMap().get("Favorite").getPlaylistMedia().remove(nowPlaying);
                favorite.setIcon(new ImageIcon("./resources/New Icons/heart-icon-disabled.png"));
            } else {
                StorageManager.getInstance().getPlaylistHashMap().get("Favorite").getPlaylistMedia().add(nowPlaying);
                favorite.setIcon(new ImageIcon("./resources/New Icons/heart-icon.png"));
            }
            StorageManager.getInstance().updateMediaData();
        });
        shared.addActionListener(event -> {
            Media nowPlaying = Media.getNowPlaying();
            if (nowPlaying.isShared()) {
                StorageManager.getInstance().getPlaylistHashMap().get("Shared").getPlaylistMedia().remove(nowPlaying);
                shared.setIcon(new ImageIcon("./resources/New Icons/share-icon-disabled.png"));
            } else {
                StorageManager.getInstance().getPlaylistHashMap().get("Shared").getPlaylistMedia().add(nowPlaying);
                shared.setIcon(new ImageIcon("./resources/New Icons/share-icon.png"));
            }
            StorageManager.getInstance().updateMediaData();
        });
    }

    private void setActionListenerToSlider() {
        musicSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Media.getNowPlaying().pauseFile();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    System.out.println();
                    System.out.println(musicSlider.getValue());
                    Media.getNowPlaying().seekTo(musicSlider.getValue());
                } catch (FileNotFoundException | JavaLayerException e1) {
                    e1.printStackTrace();
                }
            }
        });
//        musicSlider.addChangeListener(e -> {
//
//        });
    }

    public void updateGUISongDetails() {
        Media nowPlaying = Media.getNowPlaying();
        if (nowPlaying != null) {
            if (Media.isPlaying()) {
                play_pause.setIcon(new ImageIcon("./resources/New Icons/Actions-media-playback-pause-icon.png"));
            } else {
                play_pause.setIcon(new ImageIcon("./resources/New Icons/Actions-media-playback-start-icon.png"));
            }
            if (nowPlaying.isFave()) {
                favorite.setIcon(new ImageIcon("./resources/New Icons/heart-icon.png"));
            } else {
                favorite.setIcon(new ImageIcon("./resources/New Icons/heart-icon-disabled.png"));
            }
            if (nowPlaying.isShared()) {
                shared.setIcon(new ImageIcon("./resources/New Icons/share-icon.png"));
            } else {
                shared.setIcon(new ImageIcon("./resources/New Icons/share-icon-disabled.png"));
            }
            if (Media.isShuffling()) {
                shuffleButton.setIcon(new ImageIcon("./resources/New Icons/shuffle-icon-active.png"));
            } else {
                shuffleButton.setIcon(new ImageIcon("./resources/New Icons/shuffle-icon.png"));
            }
            if (Media.isReplaying()) {
                replayButtun.setIcon(new ImageIcon("./resources/New Icons/replay-icon-active.png"));
            } else {
                replayButtun.setIcon(new ImageIcon("./resources/New Icons/replay-icon.png"));
            }
            setShowcaseContent(new ArrayList<>(Media.getCurrentPlaylist().getPlaylistMedia()));
            musicTitle.setText(nowPlaying.getTitle());
            artist.setText(nowPlaying.getArtist());
            setArtWorkLAbel();
        }
    }

    public void setShowcaseContent(ArrayList<Showable> content) {
        showcasePanel.removeAll();
        showcasePanel.setLayout(new GridLayout(content.size(), 1));
        System.out.println(content);
        for (Showable showable : content) {
            ShowcaseButton showcase = new ShowcaseButton(showable);
            System.out.println(showcase);
            showcasePanel.add(showcase);
        }
        showcasePanel.revalidate();
    }

    private void addMusicPanelListeners() {
        addAutoPlaylistsListeners();
        setActionListenerToSlider();
        play_pause.addActionListener(event -> {
            Media nowPlaying = Media.getNowPlaying();
            if (Media.isPlaying()) {
                nowPlaying.pauseFile();
                Media.setPlaying(false);
                play_pause.setIcon(new ImageIcon("./resources/New Icons/Actions-media-playback-start-icon.png"));
            } else {
                nowPlaying.resumeFile();
                Media.setPlaying(true);
                play_pause.setIcon(new ImageIcon("./resources/New Icons/Actions-media-playback-pause-icon.png"));
            }
            updateGUISongDetails();
        });
        nextTrackButton.addActionListener(event -> {
            ArrayList<Media> currentPlaylist = Media.getCurrentPlaylist().getPlaylistMedia();
            int next;
            if (Media.isShuffling()) {
                Random random = new Random();
                next = random.nextInt(currentPlaylist.size());
            } else {
                next = currentPlaylist.indexOf(Media.getNowPlaying()) + 1;
                if (next == currentPlaylist.size()) {
                    next = 0;
                }
            }
            Media.setNowPlaying(currentPlaylist.get(next));
            Media.getNowPlaying().playFile();
            updateGUISongDetails();
        });
        lastTrackButton.addActionListener(event -> {
            ArrayList<Media> currentPlaylist = Media.getCurrentPlaylist().getPlaylistMedia();
            int last = currentPlaylist.indexOf(Media.getNowPlaying()) - 1;
            if (last == -1) {
                last = currentPlaylist.size() - 1;
            }
            Media.setNowPlaying(currentPlaylist.get(last));
            if (Media.isPlaying()) {
                Media.getNowPlaying().playFile();
            }
            updateGUISongDetails();
        });
        shuffleButton.addActionListener(event -> {
            if (Media.isShuffling()) {
                Media.setShuffling(false);
                shuffleButton.setIcon(new ImageIcon("./resources/New Icons/shuffle-icon.png"));
            } else {
                Media.setShuffling(true);
                shuffleButton.setIcon(new ImageIcon("./resources/New Icons/shuffle-icon-active.png"));
            }
        });
        replayButtun.addActionListener(event -> {
            if (Media.isReplaying()) {
                Media.setReplaying(false);
                replayButtun.setIcon(new ImageIcon("./resources/New Icons/replay-icon.png"));
            } else {
                Media.setReplaying(true);
                replayButtun.setIcon(new ImageIcon("./resources/New Icons/replay-icon-active.png"));
            }
        });
    }

    private void setListsPanelSetting() {
        setListPanelListener();
        searchButton.setIcon(new ImageIcon("./resources/New Icons/magnifying-glass-icon.png"));
        searchField.setBackground(Color.LIGHT_GRAY);
    }

    private ArrayList<Media> findSongBySearch() {
        String textFieldConcept = searchField.getText().trim().toLowerCase();
        System.out.println(textFieldConcept);
        ArrayList<Media> searchedSong = new ArrayList<>();
        for (Media media : StorageManager.getInstance().getMediaArrayList()) {
            if ((media.getArtist() != null && media.getArtist().trim().toLowerCase().contains(textFieldConcept))
                    || (media.getAlbum() != null && media.getAlbum().trim().toLowerCase().contains(textFieldConcept))
                    || (media.getTitle() != null && media.getTitle().trim().toLowerCase().contains(textFieldConcept))) {
                searchedSong.add(media);
            }
        }
        return searchedSong;

    }

    public void setListPanelListener() {
        searchField.setFocusable(true);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    setShowcaseContent(new ArrayList<>(findSongBySearch()));
                }
            }
        });
        searchButton.addActionListener(e -> {
            setShowcaseContent(new ArrayList<>(findSongBySearch()));
        });

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
            upperPanel.repaint();
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
            upperPanel.validate();
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
        musicSlider.setValue(0);
        addMusicPanelListeners();
        setMusicPanelIconsAndColors();
        setListsPanelSetting();
        frame.setVisible(true);
    }

    @Override
    public void doAddPlaylistLink(String name, ArrayList<Media> result) {
        HashMap<String, Playlist> playlistHashMap = StorageManager.getInstance().getPlaylistHashMap();
        playlistHashMap.put(name, new UserPlaylist(name, result));
        StorageManager.getInstance().updateMediaData();
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
