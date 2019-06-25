package logic.Network.Client;

import java.io.Serializable;

public class ClientRequest implements Serializable {
    private ClientRequestType type;
    private String name;
    private String clientName;

    public ClientRequest(ClientRequestType type, String name, String clientName) {
        this.type = type;
        this.name = name;
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public ClientRequestType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
