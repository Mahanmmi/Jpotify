package logic.network.server;

import logic.network.client.ClientRequestType;

import java.io.Serializable;

public class ServerRequest implements Serializable {
    private ServerRequestType type;
    private String name;

    public ServerRequest(ServerRequestType type, String name) {
        this.type = type;
        this.name = name;
    }

    public ServerRequestType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
