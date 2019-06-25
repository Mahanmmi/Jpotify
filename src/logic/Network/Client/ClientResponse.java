package logic.Network.Client;

import java.io.Serializable;

public class ClientResponse implements Serializable {
    private ClientResponseType type;
    private Object sentData;
    private String clientName;

    public ClientResponse(ClientResponseType type, Object sentData, String clientName) {
        this.type = type;
        this.sentData = sentData;
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public ClientResponseType getType() {
        return type;
    }

    public Object getSentData() {
        return sentData;
    }
}
