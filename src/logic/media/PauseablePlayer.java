package logic.media;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.InputStream;

public class PauseablePlayer {
    private final static int NOTSTARTED = 0;
    private final static int PLAYING = 1;
    private final static int PAUSED = 2;
    private final static int FINISHED = 3;

    private final Player player;

    private final Object playerLock = new Object();

    private int playerStatus = NOTSTARTED;

    public PauseablePlayer(final InputStream inputStream) throws JavaLayerException {
        this.player = new Player(inputStream);
    }

    public void play() {
        synchronized (playerLock) {
            switch (playerStatus) {
                case NOTSTARTED:
                    Runnable r = this::playInternal;
                    final Thread t = new Thread(r);
                    t.setPriority(Thread.MAX_PRIORITY);
                    playerStatus = PLAYING;
                    t.start();
                    break;
                case PAUSED:
                    resume();
                    break;
                default:
                    break;
            }
        }
    }

    public void pause() {
        synchronized (playerLock) {
            if (playerStatus == PLAYING) {
                playerStatus = PAUSED;
            }
        }
    }

    public void resume() {
        synchronized (playerLock) {
            if (playerStatus == PAUSED) {
                playerStatus = PLAYING;
                playerLock.notifyAll();
            }
        }
    }

    public void stop() {
        synchronized (playerLock) {
            playerStatus = FINISHED;
            playerLock.notifyAll();
        }
    }

    private void playInternal() {
        while (playerStatus != FINISHED) {
            try {
                if (!player.decodeFrame()) {
                    break;
                }
            } catch (final JavaLayerException e) {
                break;
            }
            synchronized (playerLock) {
                while (playerStatus == PAUSED) {
                    try {
                        playerLock.wait();
                    } catch (InterruptedException e) {
                        System.out.println("System f***ed up");
                        break;
                    }
                }
            }
        }
        close();
    }

    public void close() {
        synchronized (playerLock) {
            playerStatus = FINISHED;
        }
        try {
            player.close();
        } catch (Exception e) {
            //Ignore
        }
    }

}