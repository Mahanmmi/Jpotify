package logic.network.server;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerData implements Serializable {
    private String username;
    private String password;
    private String lastSong;
    private Date lastOnline;
    private boolean isOnline;

    public ServerData(String s) {
        String[] data = s.split("<---->");
        username = data[0];
        password = data[1];
        lastSong = data[2];
        try {
            lastOnline = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy").parse(data[3]);
        } catch (ParseException e){
            e.printStackTrace();
        }
        isOnline = Boolean.parseBoolean(data[4]);
    }

    public ServerData(String username, String password) {
        this.username = username;
        this.password = password;
//        this.lastSong = "";
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getLastSong() {
        return lastSong;
    }

    void setLastSong(String lastSong) {
        this.lastSong = lastSong;
    }

    public Date getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
    }


    @Override
    public String toString() {
        return "ServerData{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", lastSong='" + lastSong + '\'' +
                ", lastOnline=" + lastOnline +
                ", isOnline=" + isOnline +
                '}';
    }
}
