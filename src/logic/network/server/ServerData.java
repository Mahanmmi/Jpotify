package logic.network.server;

import logic.storage.playlist.AutoPlayList;

import java.io.Serializable;
import java.util.Date;

public class ServerData implements Serializable {
    private String username;
    private String password;
    private String lastSong;
    private Date lastOnline;
    private AutoPlayList sharedPlaylist;

    public ServerData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getLastSong() {
        return lastSong;
    }

    public void setLastSong(String lastSong) {
        this.lastSong = lastSong;
    }

    public Date getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
    }

    public AutoPlayList getSharedPlaylist() {
        return sharedPlaylist;
    }

    public void setSharedPlaylist(AutoPlayList sharedPlaylist) {
        this.sharedPlaylist = sharedPlaylist;
    }
}
