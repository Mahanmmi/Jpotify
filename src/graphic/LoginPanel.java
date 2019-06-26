package graphic;

import logic.network.client.Client;
import logic.network.server.ServerData;
import logic.storage.StorageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;

public class LoginPanel {
    private JPanel panel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton newUserButton;
    private JButton loginButton;
    private JFrame frame;

    public LoginPanel() {
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
                }

            }
        });
    }
}
