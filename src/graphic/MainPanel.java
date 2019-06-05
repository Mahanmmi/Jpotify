package graphic;

import javax.swing.*;
import java.awt.*;

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
    private JFrame frame;

    private void initDarkTheme(){
        UIManager.put( "control", new Color( 128, 128, 128) );
        UIManager.put( "info", new Color(128,128,128) );
        UIManager.put( "nimbusBase", new Color( 18, 30, 49) );
        UIManager.put( "nimbusAlertYellow", new Color( 248, 187, 0) );
        UIManager.put( "nimbusDisabledText", new Color( 128, 128, 128) );
        UIManager.put( "nimbusFocus", new Color(115,164,209) );
        UIManager.put( "nimbusGreen", new Color(176,179,50) );
        UIManager.put( "nimbusInfoBlue", new Color( 66, 139, 221) );
        UIManager.put( "nimbusLightBackground", new Color( 18, 30, 49) );
        UIManager.put( "nimbusOrange", new Color(191,98,4) );
        UIManager.put( "nimbusRed", new Color(169,46,34) );
        UIManager.put( "nimbusSelectedText", new Color( 255, 255, 255) );
        UIManager.put( "nimbusSelectionBackground", new Color( 104, 93, 156) );
        UIManager.put( "text", new Color( 230, 230, 230) );
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

    private void initPanelSizes(){
        int width = mainPanel.getWidth() - mainPanel.getWidth()/10;
        int height = mainPanel.getHeight() - mainPanel.getHeight()/10;


        mainPanel.setBackground(Color.darkGray);
        musicPanel.setBackground(Color.darkGray);
        listsPanel.setBackground(Color.darkGray);
        middlePanel.setBackground(Color.darkGray);
        friendPanel.setBackground(Color.darkGray);

        musicPanel.setPreferredSize(new Dimension (width,height/7));
        musicPanel.setMaximumSize(new Dimension (width,height/7));
        musicPanel.setMinimumSize(new Dimension (width,height/7));

        listsPanel.setPreferredSize(new Dimension(width/5,(height*6)/7));
        listsPanel.setMaximumSize(new Dimension(width/5,(height*6)/7));
        listsPanel.setMinimumSize(new Dimension(width/5,(height*6)/7));

        friendPanel.setPreferredSize(new Dimension(width/5,(height*6)/7));
        friendPanel.setMaximumSize(new Dimension(width/5,(height*6)/7));
        friendPanel.setMinimumSize(new Dimension(width/5,(height*6)/7));

        middlePanel.setPreferredSize(new Dimension((width*3)/5,(height*6)/7));
        middlePanel.setMaximumSize(new Dimension((width*3)/5,(height*6)/7));
        middlePanel.setMinimumSize(new Dimension((width*3)/5,(height*6)/7));
    }

    private JMenuBar initMenus(){
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        JMenu submenu = new JMenu("Sub Menu");

        JMenuItem i1 = new JMenuItem("Item 1");
        JMenuItem i2 = new JMenuItem("Item 2");
        JMenuItem i3 = new JMenuItem("Item 3");
        JMenuItem i4 = new JMenuItem("Item 4");
        JMenuItem i5 = new JMenuItem("Item 5");
        menu.add(i1);
        menu.add(i2);
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

        initPanelSizes();
        frame.setJMenuBar(initMenus());
    }

    public MainPanel() {
        initDarkTheme();
        initFrame();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new MainPanel();
    }
}
