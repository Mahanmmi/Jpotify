package logic.media;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import graphic.Showable;
import javazoom.jl.decoder.JavaLayerException;
import logic.storage.playlist.Playlist;
import logic.storage.StorageManager;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Media implements Showable {
    //Player related fields
    private static PauseablePlayer mainPlayer = null;
    private static Media nowPlaying = null;
    private static boolean isPlaying = false;
    private static boolean isMute = false;
    private static boolean isShuffling = false;
    private static boolean isReplaying = false;
    private static Playlist currentPlaylist;
    public static Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    public static void setCurrentPlaylist(Playlist currentPlaylist) {
        Media.currentPlaylist = currentPlaylist;
    }

    public static void setNowPlaying(Media nowPlaying) {
        if (mainPlayer != null) {
            mainPlayer.close();
        }
        Media.nowPlaying = nowPlaying;
    }

    public static Media getNowPlaying() {
        return nowPlaying;
    }

    public static boolean isPlaying() {
        return isPlaying;
    }

    public static void setPlaying(boolean isPlaying) {
        Media.isPlaying = isPlaying;
    }

    public static boolean isMute() {
        return isMute;
    }

    public static void setMute(boolean isMute) {
        Media.isMute = isMute;
    }

    public static boolean isShuffling() {
        return isShuffling;
    }

    public static void setShuffling(boolean isShuffling) {
        Media.isShuffling = isShuffling;
    }

    public static boolean isReplaying() {
        return isReplaying;
    }

    public static void setReplaying(boolean isReplaying) {
        Media.isReplaying = isReplaying;
    }


    private String address;
    private String title;
    private String artist;
    private String album;
    private String year;
    private int genre;
    private int time;
    private ImageIcon icon;
    private Mp3File mp3File;

    public boolean isFave() {
        return StorageManager.getInstance().getPlaylistHashMap().get("Favorite").getPlaylistMedia().contains(this);
    }

    public boolean isShared() {
        return StorageManager.getInstance().getPlaylistHashMap().get("Shared").getPlaylistMedia().contains(this);
    }



 /*   private Object myPropertiesReader(String prop) {
        try {
            AudioFileFormat baseFileFormat;
            baseFileFormat = AudioSystem.getAudioFileFormat(file);
            if (baseFileFormat instanceof TAudioFileFormat) {
                Map properties = ((TAudioFileFormat) baseFileFormat).properties();

                return properties.get(prop);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }*/

    public ImageIcon getIcon() {
        return icon;
    }

    public Media(String address) {
        this.address = address;
        try {
            mp3File = new Mp3File(address);
            this.time = (int) mp3File.getLengthInSeconds();
            if (mp3File.hasId3v1Tag()) {


         /*         File file=new File("address");
        byte metadata[]=new byte[128];
        try {

            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(file.length()-128);
            raf.read(metadata,0,128);

        }catch (IOException e){
            System.out.println("cant read file");

        }


        String id3=new String(metadata);
            this.title= id3.substring(3, 33);

            this.artist= id3.substring(33, 62);

            this.album= id3.substring(63, 92);



    }
                 */
                ID3v1 id3v1 = mp3File.getId3v1Tag();
                this.title = id3v1.getTitle();
                this.artist = id3v1.getArtist();
                this.album = id3v1.getAlbum();
                this.genre = id3v1.getGenre();
                this.year = id3v1.getYear();
            }
            if (mp3File.hasId3v2Tag()) {
                ID3v2 id3v2 = mp3File.getId3v2Tag();
                this.title = id3v2.getTitle();
                this.artist = id3v2.getArtist();
                this.album = id3v2.getAlbum();
                this.genre = id3v2.getGenre();
                this.year = id3v2.getYear();

                byte[] imageData = id3v2.getAlbumImage();
                if (imageData != null) {
                    System.out.println("debug:: imageData is not null");
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
                    icon = new ImageIcon(img);

                }
            }


        } catch (Exception e) {
            System.out.println("s");

        }
       /* this.address = address;
        file = new File(address);
        System.out.println(file.getTargetName());
        //duration, title, author, album, date, comment, copyright, mp3.framerate.fps, mp3.length.frames, mp3.vbr.scale, mp3.id3tag.v2
        this.album=(String) myPropertiesReader("album");
        this.artist=(String) myPropertiesReader("author");
        this.time=(int)myPropertiesReader("duration");
        this.title=(String)myPropertiesReader("title");
        this.genre=

        System.out.println("Title : " + myPropertiesReader("title"));
        System.out.println("Duration : " + myPropertiesReader("duration"));
        System.out.println("Copyright : " + myPropertiesReader("copyright"));
        System.out.println("Author : " + myPropertiesReader("author"));
        System.out.println("Album : " + album);
        System.out.println("mp3.framerate.fps : " + myPropertiesReader("mp3.framerate.fps"));

        myByteReader(0, 4);
        myByteReader(4, 100);
        myByteReader(104, 100);
        myByteReader(204, 100);
        myByteReader(304, 100);
        myByteReader(404, 100);
*/
    }
  /*  public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }*/

   /* public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) ((Clip) mp3File).getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }*/
   public void adjustVolume(float volumeSlidPosition){
       float volume=(float)(((6/10.0)*volumeSlidPosition)-20);
       mainPlayer.changeVolume(volume);
   }

    public void seekTo(int MusicsliderPosition) throws FileNotFoundException, JavaLayerException {
        int startingFrame = (int) (MusicsliderPosition * mp3File.getFrameCount() / 100.0);
        mainPlayer.close();
        if (isPlaying) {
            mainPlayer = new PauseablePlayer(new FileInputStream(address), startingFrame, mp3File);
            mainPlayer.play();
        } else {
            mainPlayer = new PauseablePlayer(new FileInputStream(address), startingFrame, mp3File);
        }
    }

    public void pauseFile() {
        mainPlayer.pause();
    }

    public void resumeFile() {
        if (mainPlayer == null) {
            try {
                mainPlayer = new PauseablePlayer(new FileInputStream(address), 0, mp3File);
            } catch (FileNotFoundException | JavaLayerException e) {
                e.printStackTrace();
            }
        }

        mainPlayer.play();
        doPlayingSongUpdates();
    }

    private void doPlayingSongUpdates() {
        StorageManager.getInstance().getMediaDataHashMap().get(address).setLastPlayed(new Date());
        StorageManager.getInstance().sortMediaArrayList();
        StorageManager.getInstance().getMainPanel().setShowcaseContent(new ArrayList<>(getCurrentPlaylist().getPlaylistMedia()));
        if (StorageManager.getInstance().getPlaylistHashMap().get("Shared").getPlaylistMedia().contains(nowPlaying)) {
            if (StorageManager.getInstance().getClient() != null) {
                StorageManager.getInstance().getClient().sendNowPlayingSong(nowPlaying);
            }
        }
    }

    static void goNext() {
        mainPlayer.close();
        if (isReplaying()) {
            nowPlaying.playFile();
            return;
        }
        int next;
        if (Media.isShuffling()) {
            Random random = new Random();
            next = random.nextInt(currentPlaylist.getPlaylistMedia().size());
        } else {
            next = currentPlaylist.getPlaylistMedia().indexOf(Media.getNowPlaying()) + 1;
            if (next >= currentPlaylist.getPlaylistMedia().size()) {
                next = 0;
            }
        }
        setNowPlaying(currentPlaylist.getPlaylistMedia().get(next));
        if (isPlaying()) {
            nowPlaying.playFile();
        }
        StorageManager.getInstance().getMainPanel().updateGUISongDetails();
    }

    public void playFile() {
        if (mainPlayer != null) {
            mainPlayer.close();
        }
        try {
            mainPlayer = new PauseablePlayer(new FileInputStream(address), 0, mp3File);
            nowPlaying = this;
            mainPlayer.play();
            isPlaying = true;
            doPlayingSongUpdates();
        } catch (FileNotFoundException | JavaLayerException e) {
            System.out.println("File not found for playing!");
        }
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getAlbumName() {
        return album;
    }

    @Override
    public void getClicked() {
        if (!currentPlaylist.getPlaylistMedia().contains(this)) {
            setCurrentPlaylist(StorageManager.getInstance().getDefaultPlaylist());
        }
        playFile();
        StorageManager.getInstance().getMainPanel().updateGUISongDetails();
    }

    @Override
    public String toString() {
        return "Media{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                '}';
    }


    public static void main(String[] args) throws InterruptedException, JavaLayerException {
        Media media = new Media("E:/Music/Imagine-Dragons-Digital-128.mp3");

//      new Media("./resources/media/Barobax - Shervin - www.telegram.me~IranSongs.mp3");
//       Media media = new Media("1.mp3");
        media.playFile();
        // mainPlayer.play();
        System.out.println("fffuck");
//        System.out.println("---"+mp3File.getFrameCount());
        Thread.sleep(5000);


        //  mainPlayer.moveSlider(60,  mp3File.getFrameCount());



    /*   media.playFile();

        System.out.println("ghsem");

        Thread.sleep(5000);
        mainPlayer.pause();
        Thread.sleep(2000);
        mainPlayer.resume();
        Thread.sleep(10000);
       // media.setVolume(0.5f);*/
    }
}
