package logic.media;

import com.mpatric.mp3agic.Mp3File;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import logic.storage.StorageManager;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Random;

/**
 * this class helps Media class to pause,resume,setVolume and play file
 * 4 final static int field help to understand state of our player
 */
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
        player.play(startingFrame, startingFrame);
        currentFrame = startingFrame;
    }



    void pause() {
        synchronized (playerLock) {
            if (playerStatus == PLAYING) {
                playerStatus = PAUSED;
            }
        }
    }

    private void resume() {
        synchronized (playerLock) {
            if (playerStatus == PAUSED) {
                playerStatus = PLAYING;
                playerLock.notifyAll();
            }
        }
    }

    /*
    void stop() {
        synchronized (playerLock) {
            playerStatus = FINISHED;
            playerLock.notifyAll();
        }
    }
    */

    /**
     * gets an int as second and change it to minute
     * @param secs this an int that shows number of second
     * @return Strng that shows time
     */
    private String secToMinConverter(long secs) {
        String min = Long.toString(secs / 60);
        String sec = Long.toString(secs % 60);
        if (secs / 60 < 10) {
            min = "0" + min;
        }
        if (secs % 60 < 10) {
            sec = "0" + sec;
        }
        return min + ":" + sec;
    }

    /**
     *plays the media use a maxPriority teared
     * and give playInternal as Runnable to thread
     */
    void play() {
        synchronized (playerLock) {
            switch (playerStatus) {
                case NOTSTARTED:
                    final Runnable r = this::playInternal;
                    final Thread t = new Thread(r);
                    //  t.setDaemon(true);
                    t.setPriority(Thread.MAX_PRIORITY);
                    playerStatus = PLAYING;
                    StorageManager.getInstance().getMainPanel().adjustVolume();
                    for (JProgressBar jProgressBar : StorageManager.getInstance().getMainPanel().getJProgressBars()){
                        jProgressBar.setValue(0);
                    }
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

    /**
     *  plays file frame by frame and update musicSlider
     *  by second and sets the equalizer
     */
    private void playInternal() {
        while (playerStatus != FINISHED) {
            try {
                if (!player.decodeFrame()) {
                    //its mean our media finished.
                    Media.goNext();
                    break;
                } else {
                    JSlider slider = StorageManager.getInstance().getMainPanel().getMusicSlider();
                    JLabel time = StorageManager.getInstance().getMainPanel().getTimeLabel();
                    JLabel totalTimeLable=StorageManager.getInstance().getMainPanel().getMusicTotalTime();
                    int totalFrames = mp3File.getFrameCount();
                    long totalTime = mp3File.getLengthInSeconds();
                    currentFrame++;
                    time.setText(secToMinConverter(currentFrame * totalTime / totalFrames));
                    totalTimeLable.setText(secToMinConverter(totalTime));
                    slider.setValue(currentFrame * 100 / totalFrames);

                    for (JProgressBar jProgressBar : StorageManager.getInstance().getMainPanel().getJProgressBars()) {
                        int change = new Random().nextInt(21) - 10;
//                        System.out.println(change);
                        if (jProgressBar.getValue() + change > 100 || jProgressBar.getValue() + change < 0) {
                            change = -change;
                        }
                        jProgressBar.setBackground(Color.BLACK);

                        jProgressBar.setValue(jProgressBar.getValue() + change);
                        if (jProgressBar.getValue() <=33)
                            jProgressBar.setForeground(Color.GREEN);
                        else if(jProgressBar.getValue()<=66)
                            jProgressBar.setForeground(Color.YELLOW);
                        else
                            jProgressBar.setForeground(Color.RED);
                    }
                    if (slider.getValue() == 100) {
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

    void changeVolume(float f) {

        player.setVol(f);

    }


}
