package graphic;

import logic.storage.StorageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * this class create a frame for sign in or sign up
 * gets  a user name and a password
 */
public class LoginPanel {
    private JPanel panel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton newUserButton;
    private JButton loginButton;
    private JFrame frame;

    /**
     * gets username and password and checks them for log in if user is not new user
     */
    private void login(){
        colorNull();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username != null && password != null) {
            if (StorageManager.getInstance().getClient().getServerData().containsKey(username) && StorageManager.getInstance().getClient().getServerData().get(username).getPassword().equals(password)) {
                StorageManager.getInstance().getClient().setNameAndLogin(username);
                disposeAndLogin();
            } else {
                JOptionPane.showMessageDialog(panel, "Username or password incorrect!!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * change the backGround of textField to show that textFiled is empty
     */
    private void colorNull(){
        String username = usernameField.getText();
        if (username == null) {
            usernameField.setBackground(Color.RED);
        } else {
            usernameField.setBackground(Color.WHITE);
        }

        String password = passwordField.getText();
        if (password == null) {
            passwordField.setBackground(Color.RED);
        } else {
            passwordField.setBackground(Color.WHITE);
        }
    }

    /**
     * sets actionListener for loginButton and newUser and set frame visible true
     */
    LoginPanel() {
        StorageManager.getInstance().getClient();
        frame = new JFrame("Login");
        MainPanel.newFrameInitialSettings(frame, panel);

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER){
                    login();
                }
            }
        });

        loginButton.addActionListener(event -> login());

        newUserButton.addActionListener(event -> {
            colorNull();
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username != null && password != null){
                if(StorageManager.getInstance().getClient().getServerData().containsKey(username) || username.contains("<---->") || username.contains("::::")){
                    JOptionPane.showMessageDialog(panel, "Username is already used!!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }{
                    StorageManager.getInstance().getClient().createNewUser(username,password);
                    disposeAndLogin();
                }

            }
        });
        frame.setVisible(true);
    }

    /**
     * frame dispose and log in to jpotify by a specific userName
     */
    private void disposeAndLogin() {
        frame.dispose();
        StorageManager.getInstance().setMainPanel(new MainPanel());
        try {
            Thread.sleep(1500);
            StorageManager.getInstance().getMainPanel().setShowcaseContent(new ArrayList<>(StorageManager.getInstance().getDefaultPlaylist().getPlaylistMedia()));
            StorageManager.getInstance().getMainPanel().updateGUISongDetails();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}
