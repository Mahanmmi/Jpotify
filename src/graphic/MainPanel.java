package graphic;

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

public class MainPanel implements PlaylistLinkable {
    private JPanel mainPanel;
    private JPanel musicPanel;
    private JPanel listsPanel;
    private JPanel middlePanel;
    private JPanel friendPanel;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textArea3;
    private JTextArea refreshHandler;
    private JSlider volumeSlider;
    private JButton lastTrackIcon;
    private JButton volumeIcon;
    private JButton nextTrackIcon;
    private JButton play_pusse;
    private JSlider musicSlider;
    private JButton musicName;
    private JButton artist;
    private JButton favorite;
    private JButton shared;
    private JFrame frame;


    private void initDarkTheme() {
        UIManager.put("control", new Color(128, 128, 128));
        UIManager.put("info", new Color(128, 128, 128));
        UIManager.put("nimbusBase", new Color(18, 30, 49));
        UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
        UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
        UIManager.put("nimbusFocus", new Color(115, 164, 209));
        UIManager.put("nimbusGreen", new Color(176, 179, 50));
        UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
        UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
        UIManager.put("nimbusOrange", new Color(191, 98, 4));
        UIManager.put("nimbusRed", new Color(169, 46, 34));
        UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
        UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
        UIManager.put("text", new Color(230, 230, 230));
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    private void updatePanelSizeAndColors() {
        textArea1.setBackground(Color.lightGray);
        textArea2.setBackground(Color.lightGray);
        textArea3.setBackground(Color.lightGray);


        mainPanel.setPreferredSize(new Dimension(frame.getWidth() - frame.getWidth() / 8, frame.getHeight() - frame.getHeight() / 8));
        mainPanel.setMaximumSize(new Dimension(frame.getWidth() - frame.getWidth() / 8, frame.getHeight() - frame.getHeight() / 8));
        mainPanel.setMinimumSize(new Dimension(frame.getWidth() - frame.getWidth() / 8, frame.getHeight() - frame.getHeight() / 8));


        mainPanel.setBackground(Color.BLACK);
      //  musicPanel.setBackground(new Color(51, 51, 51));
            musicPanel.setBackground(Color.BLACK);
        listsPanel.setBackground(Color.BLACK);
        middlePanel.setBackground(Color.darkGray);
        friendPanel.setBackground(Color.BLACK);


        int width = mainPanel.getWidth() - mainPanel.getWidth() / 10;
        int height = mainPanel.getHeight() - mainPanel.getHeight() / 10;

        musicPanel.setPreferredSize(new Dimension(0, height / 5));
        musicPanel.setMaximumSize(new Dimension(0, height / 5));
        musicPanel.setMinimumSize(new Dimension(0, height / 5));

        listsPanel.setPreferredSize(new Dimension(width / 5, (height * 5) / 7));
        listsPanel.setMaximumSize(new Dimension(width / 5, (height * 5) / 7));
        listsPanel.setMinimumSize(new Dimension(width / 5, (height * 5) / 7));

        friendPanel.setPreferredSize(new Dimension(width / 5, (height * 5) / 7));
        friendPanel.setMaximumSize(new Dimension(width / 5, (height * 5) / 7));
        friendPanel.setMinimumSize(new Dimension(width / 5, (height * 5) / 7));

        middlePanel.setPreferredSize(new Dimension((width * 3) / 5, (height * 5) / 7));
        middlePanel.setMaximumSize(new Dimension((width * 3) / 5, (height * 5) / 7));
        middlePanel.setMinimumSize(new Dimension((width * 3) / 5, (height * 5) / 7));
    }

    private void setMusicPanelIconsAndColors(){
        nextTrackIcon.setIcon(new ImageIcon(new ImageIcon("./resources/nt.png").getImage().getScaledInstance(nextTrackIcon.getWidth()-5,nextTrackIcon.getHeight()+7,Image.SCALE_SMOOTH)));
        lastTrackIcon.setIcon(new ImageIcon(new ImageIcon("./resources/lt.png").getImage().getScaledInstance(lastTrackIcon.getWidth()-5,lastTrackIcon.getHeight()+7,Image.SCALE_SMOOTH)));
        volumeIcon.setIcon(new ImageIcon(new ImageIcon("./resources/v.png").getImage().getScaledInstance(volumeIcon.getWidth()-5,volumeIcon.getHeight()+7,Image.SCALE_SMOOTH)));
        play_pusse.setIcon( new ImageIcon(new  ImageIcon("./resources/play.png").getImage().getScaledInstance(play_pusse.getWidth()-5,play_pusse.getHeight()+7,Image.SCALE_SMOOTH)));
        nextTrackIcon.setBackground(Color.black);
        lastTrackIcon.setBackground(Color.black);
        play_pusse.setBackground(Color.black);
        volumeIcon.setBackground(Color.black);



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
            if (event.getNewState() == 6 || event.getOldState() == 6) {
                updatePanelSizeAndColors();
                try {
                    Robot robot = new Robot();
                    refreshHandler.grabFocus();
                    robot.delay(50);
                    robot.keyPress(KeyEvent.VK_0);
                    robot.keyRelease(KeyEvent.VK_0);
                    robot.keyPress(KeyEvent.VK_BACK_SPACE);
                    robot.keyRelease(KeyEvent.VK_BACK_SPACE);
                    refreshHandler.transferFocus();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
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
        HashMap<String, MediaData> playListMediaDataHashMap=StorageManager.getInstance().getMediaDataHashMap();
        playlistHashMap.put(name,new UserPlaylist(name,result));
        StorageManager.getInstance().updateMediaData();
        //in ja fek konam kamel shod hamaaaal
        frame.setVisible(true);
    }

    @Override
    public void cancelPlaylistOperation() {
        frame.setVisible(true);
    }

    public static void main(String[] args){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> StorageManager.getInstance().saveAndQuit(), "Shutdown-thread"));
        System.out.println(StorageManager.getInstance().getMediaArrayList());
    }
}
