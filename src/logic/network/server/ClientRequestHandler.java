package logic.network.server;

import logic.network.client.ClientRequest;

import java.util.ArrayList;

public class ClientRequestHandler {
    private ServerManager.ClientManager source;
    private ClientRequest request;
    private ArrayList<ServerManager.ClientManager> activeSockets;

    public ClientRequestHandler(ServerManager.ClientManager source, ClientRequest request) {
        this.source = source;
        this.request = request;
        activeSockets = ServerManager.getInstance().getActiveSockets();
    }

    void handle(){
        switch (request.getType()){
            case PLAYLIST:{
                for (ServerManager.ClientManager activeSocket : activeSockets) {
                    if(activeSocket.getName().equals(request.getTargetName())){
                        ServerRequest serverRequest = new ServerRequest(ServerRequestType.PLAYLIST,-1,request.getClientName());
                        activeSocket.sendRequest(serverRequest);
                        break;
                    }
                }
                break;
            }
        }
    }
}
