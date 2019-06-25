package logic.network.client;

import java.io.Serializable;


public enum ClientResponseType implements Serializable {
    SONG,
    PLAYLIST,
    NOW_PLAYING_SONG,
    CLOSE
}
