package graphic;

import logic.storage.StorageManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;

public class MainPanel {
    private JPanel mainPanel;
    private JPanel musicPanel;
    private JPanel listsPanel;
    private JPanel middlePanel;
    private JPanel friendPanel;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textArea3;
    private JTextArea textArea4;
    private JTextArea refreshHandler;
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

    private void updatePanelSizeAndColors() {
        textArea1.setBackground(Color.lightGray);
        textArea2.setBackground(Color.lightGray);
        textArea3.setBackground(Color.lightGray);
        textArea4.setBackground(Color.lightGray);


        mainPanel.setPreferredSize(new Dimension(frame.getWidth() - frame.getWidth() / 8, frame.getHeight() - frame.getHeight() / 8));
        mainPanel.setMaximumSize(new Dimension(frame.getWidth() - frame.getWidth() / 8, frame.getHeight() - frame.getHeight() / 8));
        mainPanel.setMinimumSize(new Dimension(frame.getWidth() - frame.getWidth() / 8, frame.getHeight() - frame.getHeight() / 8));


        mainPanel.setBackground(Color.BLACK);
        musicPanel.setBackground(new Color(51, 51, 51));
        listsPanel.setBackground(Color.BLACK);
        middlePanel.setBackground(Color.darkGray);
        friendPanel.setBackground(Color.BLACK);


        int width = mainPanel.getWidth() - mainPanel.getWidth() / 10;
        int height = mainPanel.getHeight() - mainPanel.getHeight() / 10;

        musicPanel.setPreferredSize(new Dimension(width, height / 6));
        musicPanel.setMaximumSize(new Dimension(width, height / 6));
        musicPanel.setMinimumSize(new Dimension(width, height / 6));

        listsPanel.setPreferredSize(new Dimension(width / 5, (height * 6) / 7));
        listsPanel.setMaximumSize(new Dimension(width / 5, (height * 6) / 7));
        listsPanel.setMinimumSize(new Dimension(width / 5, (height * 6) / 7));

        friendPanel.setPreferredSize(new Dimension(width / 5, (height * 6) / 7));
        friendPanel.setMaximumSize(new Dimension(width / 5, (height * 6) / 7));
        friendPanel.setMinimumSize(new Dimension(width / 5, (height * 6) / 7));

        middlePanel.setPreferredSize(new Dimension((width * 3) / 5, (height * 6) / 7));
        middlePanel.setMaximumSize(new Dimension((width * 3) / 5, (height * 6) / 7));
        middlePanel.setMinimumSize(new Dimension((width * 3) / 5, (height * 6) / 7));
    }

    private JMenuBar initMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        JMenu submenu = new JMenu("Sub Menu");


        JMenuItem addSongMenuItem = new JMenuItem("Add song...");
        addSongMenuItem.addActionListener(event -> {
            JFileChooser songChooser = new JFileChooser();
            songChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Songs", "mp3");
            songChooser.setFileFilter(filter);
            int returnVal = songChooser.showOpenDialog(frame);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                StorageManager.getInstance().addMedia(songChooser.getSelectedFile());
            }
        });



        JMenuItem addDirectoryMenuItem = new JMenuItem("Add directory...");
        addDirectoryMenuItem.addActionListener(event -> {
            JFileChooser directoryChooser = new JFileChooser();
            directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = directoryChooser.showOpenDialog(frame);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                StorageManager.getInstance().addDirectory(directoryChooser.getSelectedFile());
            }
        });



        JMenuItem i3 = new JMenuItem("Item 3");
        JMenuItem i4 = new JMenuItem("Item 4");
        JMenuItem i5 = new JMenuItem("Item 5");
        menu.add(addSongMenuItem);
        menu.add(addDirectoryMenuItem);
        menu.add(i3);
        submenu.add(i4);
        submenu.add(i5);
        menu.add(submenu);
        menuBar.add(menu);

        return menuBar;
    }

    private void initFrame() {
        frame = new JFrame("Jpotify");
        ImageIcon frameIcon = new ImageIcon("./resources/JpotifyIcon.png");
        frame.setIconImage(frameIcon.getImage());
        frame.setContentPane(mainPanel);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width / 2 - frame.getWidth() / 2;
        int y = screenSize.height / 2 - frame.getHeight() / 2;
        frame.setLocation(x, y);


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
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new MainPanel();
        System.out.println(StorageManager.getInstance().getMediaArrayList());
    }
}
