package logic.network.server;

import logic.network.client.ClientResponse;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * Handles client responses and update other clients status
 * MOST IMPORTANT CLASS IN SERVER
 */
class ClientResponseHandler {
    private ServerManager.ClientManager source;
    private ClientResponse response;
    private HashMap<String, ServerData> allActivity;

    ClientResponseHandler(ServerManager.ClientManager source, ClientResponse response) {
        this.source = source;
        this.response = response;
        allActivity = ServerManager.getInstance().getAllActivity();
    }

    /**
     * Notify all clients of a change in ServerData
     * @param name key in ServerData HashMap
     * @param data new value in ServerData HashMap
     */
    private void notifyAllClients(String name, ServerData data) {
        System.out.println(ServerManager.getInstance().getActiveSockets());
        for (ServerManager.ClientManager activeSocket : ServerManager.getInstance().getActiveSockets()) {
            activeSocket.getNotified(name, data);
        }
    }

    /**
     * Update last played shared playlist song of a client
     */
    private void setClientLastSong() {
        String song = (String) response.getSentData();
        String name = response.getClientName();
        allActivity.get(name).setLastSong(song);
        System.out.println(allActivity.get(name));
        notifyAllClients(name, allActivity.get(name));
    }

    /**
     * Create new user in server
     */
    private void createNewUser(){
        source.setName(response.getClientName());
        allActivity.put(response.getClientName(),(ServerData) response.getSentData());
        notifyAllClients(response.getClientName(),(ServerData) response.getSentData());
    }

    /**
     * Notify clients that a user came online
     */
    private void setUserOnline(){
        source.setName(response.getClientName());
        allActivity.get(response.getClientName()).setLastOnline(new Date());
        allActivity.get(response.getClientName()).setOnline(true);
        notifyAllClients(response.getClientName(), allActivity.get(response.getClientName()));
    }

    /**
     * Send a received playlist to it's requester
     */
    private void sendPlaylist(){
        for (ServerManager.ClientManager activeSocket : ServerManager.getInstance().getActiveSockets()) {
            if(activeSocket.getName().equals(response.getRequester())){
                ServerResponse serverResponse = new ServerResponse(ServerResponseType.PLAYLIST,response.getSentData(),response.getClientName());
                activeSocket.sendResponse(serverResponse);
            }
        }
    }

    /**
     * Send a received song to it's requester
     */
    private void sendSong(){
        for (ServerManager.ClientManager activeSocket : ServerManager.getInstance().getActiveSockets()) {
            if(activeSocket.getName().equals(response.getRequester())){
                ServerResponse serverResponse = new ServerResponse(ServerResponseType.SONG,response.getSentData(),response.getClientName());
                activeSocket.sendResponse(serverResponse);
            }
        }
    }

    /**
     * Handle response by it's type
     */
    void handle() {
        switch (response.getType()) {
            case NOW_PLAYING_SONG: {
                System.out.println("Update NPS");
                setClientLastSong();
                System.out.println("Update NPS Done");
                break;
            }
            case NEW_USER: {
                System.out.println("Create NU");
                createNewUser();
                System.out.println("Create NU Done");
                break;
            }
            case LOGIN:{
                System.out.println("Login User");
                setUserOnline();
                System.out.println("Login User Done");
                break;
            }
            case PLAYLIST:{
                System.out.println("Send Playlist");
                sendPlaylist();
                System.out.println("Send Playlist Done");
                break;
            }
            case SONG:{
                System.out.println("Send Song");
                sendSong();
                System.out.println("Send Song Done");
                break;
            }
            case CLOSE: {
                try {
                    System.out.println(source.getClient() + " : " + response.getClientName() + " : " + " CLOSED");
                    allActivity.get(response.getClientName()).setLastOnline(new Date());
                    allActivity.get(response.getClientName()).setOnline(false);
                    notifyAllClients(response.getClientName(),allActivity.get(response.getClientName()));
                    ServerManager.getInstance().getActiveSockets().remove(source);
                    source.getClient().close();
                } catch (IOException e) {
                    System.out.println("Cant close socket on server");
                }
                break;
            }
        }
    }
}
