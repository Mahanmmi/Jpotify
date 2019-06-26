package logic.network.client;

import java.io.Serializable;

public class ClientRequest implements Serializable {
    private ClientRequestType type;
    private String targetName;
    private String clientName;
    private int indexInPlaylist;

    public ClientRequest(ClientRequestType type, String targetName, String clientName, int indexInPlaylist) {
        this.type = type;
        this.targetName = targetName;
        this.clientName = clientName;
        this.indexInPlaylist = indexInPlaylist;
    }

    public String getClientName() {
        return clientName;
    }

    public ClientRequestType getType() {
        return type;
    }

    public String getTargetName() {
        return targetName;
    }

    public int getIndexInPlaylist() {
        return indexInPlaylist;
    }
}
