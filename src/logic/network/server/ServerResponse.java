package logic.network.server;

import java.io.Serializable;


public class ServerResponse implements Serializable {
    private ServerResponseType type;
    private Object sentData;
    private String name;

    public ServerResponse(ServerResponseType type, Object sentData, String name) {
        this.type = type;
        this.sentData = sentData;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ServerResponseType getType() {
        return type;
    }

    public Object getSentData() {
        return sentData;
    }
}
