package logic.network.client;

import java.io.Serializable;

public class ClientResponse implements Serializable {
    private ClientResponseType type;
    private Object sentData;
    private String clientName;
    private String requester;

    public ClientResponse(ClientResponseType type, Object sentData, String clientName) {
        this.type = type;
        this.sentData = sentData;
        this.clientName = clientName;
        requester = "";
    }

    public ClientResponse(ClientResponseType type, Object sentData, String clientName, String requester) {
        this.type = type;
        this.sentData = sentData;
        this.clientName = clientName;
        this.requester = requester;
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

    public String getRequester() {
        return requester;
    }
}
