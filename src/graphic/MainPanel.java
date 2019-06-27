package graphic;

import ch.randelshofer.quaqua.QuaquaManager;
import javazoom.jl.decoder.JavaLayerException;
import logic.media.Media;
import logic.media.MediaData;
import logic.network.server.ServerData;
import logic.storage.playlist.Playlist;
import logic.storage.playlist.PlaylistElement;
import logic.storage.playlist.UserPlaylist;
import logic.storage.StorageManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.*;

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
    private JSlider volumeSlider;
    private JButton lyricsButton;
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
    private JLabel artWorkLabel;
    private JScrollPane showcasePane;
    private JPanel showcasePanel;
    private JLabel allSongLabel;
    private JLabel playlistLabel;
    private JLabel albumLabel;
    private JList friendActivityList;
    private JProgressBar bar1;
    private JProgressBar bar2;
    private JProgressBar bar3;
    private JProgressBar bar4;
    private JProgressBar bar5;
    private JProgressBar bar6;
    private JProgressBar bar7;
    private JProgressBar bar8;
    private ArrayList<JProgressBar> jProgressBars = new ArrayList<>();
    private JFrame frame;

    private void addBars(){
        jProgressBars.add(bar1);
        jProgressBars.add(bar2);
        jProgressBars.add(bar3);
        jProgressBars.add(bar4);
        jProgressBars.add(bar5);
        jProgressBars.add(bar6);
        jProgressBars.add(bar7);
        jProgressBars.add(bar8);
    }

    public ArrayList<JProgressBar> getJProgressBars() {
        return jProgressBars;
    }

    private void setArtWorkLAbel() {
        if (Media.getNowPlaying().getIcon() == null) {
            artWorkLabel.setIcon(new ImageIcon(new ImageIcon("./resources/New Icons/Music-icon.png").getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT)));
        } else {
            artWorkLabel.setIcon(new ImageIcon(Media.getNowPlaying().getIcon().getImage().getScaledInstance(160, 160, Image.SCALE_DEFAULT)));
        }
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

        lyricsButton.setSize(lyricsButton.getWidth(), lyricsButton.getHeight() + 10);
        lyricsButton.setIcon(new ImageIcon("./resources/New Icons/icon_info.png"));

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
            updateGUISongDetails();
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
            updateGUISongDetails();
        });
    }

    private void setActionListenerToVolumeSlider() {
        volumeSlider.putClientProperty("JSlider.isFilled", Boolean.TRUE);
        volumeSlider.addChangeListener(e -> volumeSlider.repaint());
        volumeSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("" + volumeSlider.getValue());
                Media.getNowPlaying().adjustVolume(volumeSlider.getValue());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("" + volumeSlider.getValue());
                Media.getNowPlaying().adjustVolume(volumeSlider.getValue());
            }
        });


    }

    private void setActionListenerToSlider() {
        musicSlider.addChangeListener(e -> musicSlider.repaint());
        musicSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Media.getNowPlaying().pauseFile();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
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

    @SuppressWarnings("unchecked")
    private void setListsComponents() {
        ArrayList<String> albumNames = new ArrayList<>(StorageManager.getInstance().getAlbumHashMap().keySet());
        albumList.setListData(albumNames.toArray());

        ArrayList<String> playlistNames = new ArrayList<>(StorageManager.getInstance().getPlaylistHashMap().keySet());
        playlistList.setListData(playlistNames.toArray());
    }

    private void setFriendActivityListComponents() {
        ArrayList<String> listComps = new ArrayList<>();
        for (Map.Entry<String, ServerData> entry : StorageManager.getInstance().getClient().getServerData().entrySet()) {
            String name = entry.getKey();
            ServerData data = entry.getValue();
//            if(!name.equals(StorageManager.getInstance().getClient().getTargetName())){
            String online;
            if (StorageManager.getInstance().getClient().getServerData().get(name).isOnline()) {
                online = ":::: is online and played: ";
            } else {
                online = ":::: was online at " + data.getLastOnline().toString() + " and played: ";
            }
            listComps.add(entry.getKey() + online + data.getLastSong());
//            }
        }
        //noinspection unchecked
        friendActivityList.setListData(listComps.toArray());
        friendActivityList.repaint();
    }

    private void setNetworkPanelListeners() {
        friendActivityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        friendActivityList.setLayoutOrientation(JList.VERTICAL_WRAP);
        friendActivityList.setVisibleRowCount(-1);
        friendActivityList.addListSelectionListener(event -> {
            if(!friendActivityList.isSelectionEmpty()){
                String selectedName = ((String)friendActivityList.getSelectedValue()).split("::::")[0];
                System.out.println(selectedName);
                if(StorageManager.getInstance().getClient().getServerData().get(selectedName).isOnline()) {
                    StorageManager.getInstance().getClient().requestGetPlaylist(selectedName);
                    System.out.println(selectedName);
                }
                friendActivityList.clearSelection();
            }
        });
    }

    public synchronized void updateGUISongDetails() {
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
//            setShowcaseContent(new ArrayList<>(Media.getCurrentPlaylist().getPlaylistMedia()));
            musicTitle.setText(nowPlaying.getTitle());
            artist.setText(nowPlaying.getArtist());
            setArtWorkLAbel();
            setListsComponents();
            setFriendActivityListComponents();
        }
    }

    public synchronized void setShowcaseContent(ArrayList<Showable> content) {
        showcasePanel.removeAll();
        showcasePanel.setLayout(new GridLayout((content.size() + 1) / 2, 2));
        for (Showable showable : content) {
            ShowcaseButton showcase = new ShowcaseButton(showable);
            showcasePanel.add(showcase);
        }
        showcasePanel.revalidate();
        showcasePanel.repaint();
    }

    private void addMusicPanelListeners() {
        setActionListenerToVolumeSlider();
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
            updateGUISongDetails();
        });
        replayButtun.addActionListener(event -> {
            if (Media.isReplaying()) {
                Media.setReplaying(false);
                replayButtun.setIcon(new ImageIcon("./resources/New Icons/replay-icon.png"));
            } else {
                Media.setReplaying(true);
                replayButtun.setIcon(new ImageIcon("./resources/New Icons/replay-icon-active.png"));
            }
            updateGUISongDetails();
        });
        lyricsButton.addActionListener(event -> {
            new LyricsPanel(Media.getNowPlaying());
        });
    }

    private void setListsPanelSetting() {
        setListPanelListener();
        searchButton.setIcon(new ImageIcon("./resources/New Icons/magnifying-glass-icon.png"));
        searchField.setBackground(Color.LIGHT_GRAY);



        albumList.addListSelectionListener(event -> {
            if (!albumList.isSelectionEmpty()) {
                StorageManager.getInstance().getAlbumHashMap().get(albumList.getSelectedValue()).getClicked();
                albumList.clearSelection();
            }
        });



        playlistList.addListSelectionListener(event -> {
            if (!playlistList.isSelectionEmpty()) {
                StorageManager.getInstance().getPlaylistHashMap().get(playlistList.getSelectedValue()).getClicked();
                playlistList.clearSelection();
            }
        });
        allSongLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Media.setCurrentPlaylist(StorageManager.getInstance().getDefaultPlaylist());
                setShowcaseContent(new ArrayList<>(StorageManager.getInstance().getDefaultPlaylist().getPlaylistMedia()));
                updateGUISongDetails();
            }
        });
        playlistLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setShowcaseContent(new ArrayList<>(StorageManager.getInstance().getPlaylistHashMap().values()));
                updateGUISongDetails();
            }
        });
        albumLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setShowcaseContent(new ArrayList<>(StorageManager.getInstance().getAlbumHashMap().values()));
                updateGUISongDetails();
            }
        });
    }

    private ArrayList<Media> findSongBySearch() {
        String textFieldConcept = searchField.getText().trim().toLowerCase();
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

    private void setListPanelListener() {
        albumList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        albumList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        albumList.setVisibleRowCount(-1);
        playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playlistList.setLayoutOrientation(JList.VERTICAL_WRAP);
        playlistList.setVisibleRowCount(-1);
        searchField.setFocusable(true);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                setShowcaseContent(new ArrayList<>(findSongBySearch()));
            }
        });
        searchButton.addActionListener(e -> setShowcaseContent(new ArrayList<>(findSongBySearch())));
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

        JMenuItem managePlaylistMenuItem = new JMenuItem("Manage playlists...");
        managePlaylistMenuItem.addActionListener(event -> {
            new ManagePlaylistPanel(this);
            frame.setVisible(false);
        });


        menu.add(addSongMenuItem);
        menu.add(addDirectoryMenuItem);
        menu.add(addPlaylistMenuItem);
        menu.add(managePlaylistMenuItem);

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
        frame = new JFrame("Jpotify::::"+StorageManager.getInstance().getClient().getName());
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
        setNetworkPanelListeners();
        addBars();
        frame.setVisible(true);

    }

    @Override
    public void doAddPlaylistLink(String name, ArrayList<Media> result) {
        HashMap<String, Playlist> playlistHashMap = StorageManager.getInstance().getPlaylistHashMap();
        playlistHashMap.put(name, new UserPlaylist(name, result));
        StorageManager.getInstance().updateMediaData();
        updateGUISongDetails();
        frame.setVisible(true);
    }

    @Override
    public void doRemovePlaylist(String name) {
        for (MediaData data : StorageManager.getInstance().getMediaDataHashMap().values()) {
            for (int i = 0; i < data.getElements().size(); i++) {
                PlaylistElement element = data.getElements().get(i);
                if (element.getPlaylistName().equals(name)) {
                    data.getElements().remove(i);
                    i--;
                }
            }
        }
        StorageManager.getInstance().getPlaylistHashMap().remove(name);
        updateGUISongDetails();
        frame.setVisible(true);
    }

    @Override
    public void cancelPlaylistOperation() {
        updateGUISongDetails();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> StorageManager.getInstance().saveAndQuit(), "Shutdown-thread"));
//        System.out.println(StorageManager.getInstance().getDefaultPlaylist().getPlaylistMedia());
        new LoginPanel();

    }

}
