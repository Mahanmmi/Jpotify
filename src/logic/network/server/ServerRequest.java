package logic.network.server;

import logic.network.client.ClientRequestType;

import java.io.Serializable;

public class ServerRequest implements Serializable {
    private ServerRequestType type;
    private int targetInPlaylist;
    private String srcName;

    public ServerRequest(ServerRequestType type, int targetInPlaylist, String srcName) {
        this.type = type;
        this.targetInPlaylist = targetInPlaylist;
        this.srcName = srcName;
    }

    public ServerRequestType getType() {
        return type;
    }

    public int getTargetInPlaylist() {
        return targetInPlaylist;
    }

    public String getSrcName() {
        return srcName;
    }
}
