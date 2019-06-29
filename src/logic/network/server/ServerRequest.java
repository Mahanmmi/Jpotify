package logic.network.server;

import java.io.Serializable;

/**
 * This class models a server request
 */
public class ServerRequest implements Serializable {
    private ServerRequestType type;
    private int targetInPlaylist;
    private String srcName;

    ServerRequest(ServerRequestType type, int targetInPlaylist, String srcName) {
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
