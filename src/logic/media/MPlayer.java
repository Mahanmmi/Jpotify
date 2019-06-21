package logic.media;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import java.io.*;
import java.nio.file.Files;

/*public class MPlayer{
    AdvancedPlayer player;
    private static int pausedOnFrame =0;
    private byte[] decrypted = null;
    private long audioLength;
    private AudioInputStream stream;




    private InputStream bytesToStream(byte[] in) {
        InputStream is = new ByteArrayInputStream(in);
        return is;
    }
*/
  /*  public MPlayer() {
         here file is encrypted to variable byte[] decrypted and then:
        File file=new File("1.mp3");
        try {
            this.decrypted = Files.readAllBytes(file.toPath());
           // InputStream is = bytesToStream(decrypted);
        }
        catch (Exception e){
            System.out.println("df");
        }
        InputStream is = bytesToStream(decrypted);
        try {
        stream = AudioSystem.getAudioInputStream(is);
        audioLength = stream.getFrameLength();

        player = new AdvancedPlayer(stream);
        player.setPlayBackListener(new PlaybackListener() {
            @Override
            public void playbackFinished(PlaybackEvent event) {
                System.err.println(event.getFrame());
                pausedOnFrame = event.getFrame();
            }

        });
    }catch (Exception e)
        {
            System.out.println("e");
        }
    }
    public void play() throws Exception {
        Thread th = new Thread() {
            public void run() {
                try {
                    player.play(MPlayer.pausedOnFrame, Integer.MAX_VALUE);
                } catch (Exception e) {
                    System.out.println("in play method");
                }
            }
        };
        th.start();
    }

    public void fastforward()throws Exception {
        pausemusic();
        long nextFrame = (long) (pausedOnFrame+0.02*audioLength);
        if (nextFrame < audioLength)
            play();
    }
    public void rewind() throws Exception {
        pausemusic();
        long nextFrame = (long) (pausedOnFrame-0.02*audioLength);
        if (nextFrame > 0)
            play();
    }


    public void pausemusic() throws LineUnavailableException  {

        player.stop();
    }
    public void stopmusic() throws LineUnavailableException {
        player.stop();
        pausedOnFrame = 0;
    }


public static void main(String[]args)throws Exception{
        MPlayer mPlayer=new MPlayer();
        mPlayer.play();
        for(int i=0; i<99999999;i++){}
        System.out.println("asas");
      //  mPlayer.pausemusic();



        }
}*/
import java.net.MalformedURLException;
import java.net.URL;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class unisound implements Runnable {

    static AdvancedPlayer mp3_player = null;
    static FileInputStream mp3_in = null;
    private int pausedOnFrame = 0;

    private boolean looped = false;
    private boolean stopped = true;
    private String file = "";
    private Thread thread;

    private PlaybackListener pb = new PlaybackListener() {

        @Override
        public  void playbackFinished(PlaybackEvent event) {

            thread.interrupt();

            if( looped ){
                play( file, looped );
            }else{
                stopped = true;
            }

        }

    };



    public  void play( String sFile, boolean bLoop ) {

        file = sFile;
        looped = bLoop;

        try {
            mp3_in=new FileInputStream(file);
            mp3_player = new AdvancedPlayer(mp3_in);
            mp3_player.setPlayBackListener(pb);

            thread = new Thread( new unisound() );
            thread.start();

            stopped = false;

            //(new Thread( new unisound())).start();

        } catch (MalformedURLException ex) {
            System.out.println("1");
        } catch (IOException e) {
        } catch (JavaLayerException e) {
        } catch (NullPointerException ex) {
        }

    }

    public  void isplaying(){

    }

    public  void stop() {
        if(stopped) return;
        System.out.println("Stop music playback");
        looped = false;
        mp3_player.stop();
    }

    @Override
    public  void run() {
        // TODO Auto-generated method stub

        try {
            mp3_player.play();
        } catch (JavaLayerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}