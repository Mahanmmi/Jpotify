package logic.media;

import com.mpatric.mp3agic.Mp3File;
import graphic.MainPanel;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import logic.storage.StorageManager;

import javax.swing.*;
import java.io.InputStream;

public class PauseablePlayer {

    private final static int NOTSTARTED = 0;
    private final static int PLAYING = 1;
    private final static int PAUSED = 2;
    private final static int FINISHED = 3;
    private final AdvancedPlayer player;
    private final Object playerLock = new Object();
    private int playerStatus = NOTSTARTED;
    private int currentFrame;
    private Mp3File mp3File;

    PauseablePlayer(final InputStream inputStream, int startingFrame, Mp3File mp3File) throws JavaLayerException {
        this.player = new AdvancedPlayer(inputStream);
        this.mp3File = mp3File;
        player.play(startingFrame,startingFrame);
        currentFrame = startingFrame;
    }

    void play() {
        synchronized (playerLock) {
            switch (playerStatus) {
                case NOTSTARTED:
                    final Runnable r = this::playInternal;
                    final Thread t = new Thread(r);
                    //  t.setDaemon(true);
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

    void pause() {
        synchronized (playerLock) {
            if (playerStatus == PLAYING) {
                playerStatus = PAUSED;
            }
        }
    }

    void resume() {
        synchronized (playerLock) {
            if (playerStatus == PAUSED) {
                playerStatus = PLAYING;
                playerLock.notifyAll();
            }
        }
    }

    void stop() {
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
                } else {
                    JSlider slider = StorageManager.getInstance().getMainPanel().getMusicSlider();
                    int totalFrames = mp3File.getFrameCount();
                    currentFrame++;
                    slider.setValue(currentFrame*100/totalFrames);
                    if(slider.getValue() == 100){
                        Media.goNext();
                    }
                }
            } catch (final JavaLayerException e) {
                break;
            }
            synchronized (playerLock) {
                while (playerStatus == PAUSED) {
                    try {
                        playerLock.wait();
                    } catch (final InterruptedException e) {
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
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
