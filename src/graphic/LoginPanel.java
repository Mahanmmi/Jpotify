package graphic;

import logic.network.client.Client;
import logic.network.server.ServerData;
import logic.storage.StorageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.util.ArrayList;

public class LoginPanel {
    private JPanel panel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton newUserButton;
    private JButton loginButton;
    private JFrame frame;

    LoginPanel() {
        frame = new JFrame("Login");
        MainPanel.newFrameInitialSettings(frame, panel);

        loginButton.addActionListener(event -> {
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

            if (username != null && password != null) {
                if (StorageManager.getInstance().getClient().getServerData().containsKey(username) && StorageManager.getInstance().getClient().getServerData().get(username).getPassword().equals(password)) {
                    StorageManager.getInstance().getClient().setNameAndLogin(username);
                    frame.dispose();
                    StorageManager.getInstance().setMainPanel(new MainPanel());
                    try {
                        Thread.sleep(1500);
                        StorageManager.getInstance().getMainPanel().setShowcaseContent(new ArrayList<>(StorageManager.getInstance().getDefaultPlaylist().getPlaylistMedia()));
                        StorageManager.getInstance().getMainPanel().updateGUISongDetails();
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted");
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Username or password incorrect!!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        newUserButton.addActionListener(event -> {
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

            if (username != null && password != null){
                if(StorageManager.getInstance().getClient().getServerData().containsKey(username)){
                    JOptionPane.showMessageDialog(panel, "Username is already used!!", "Error", JOptionPane.ERROR_MESSAGE);
                }{
                    StorageManager.getInstance().getClient().createNewUser(username,password);
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
        });
        frame.setVisible(true);
    }
}
