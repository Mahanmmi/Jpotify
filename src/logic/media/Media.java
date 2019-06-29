package logic.media;

import javax.imageio.ImageIO;
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

/**
 * this class get data from file and play,pause,seek and adjusting volume
 *
 */
public class Media implements Showable {
    //Player related fields
    private static PauseablePlayer mainPlayer = null;
    private static Media nowPlaying = null;
    private static boolean isPlaying = false;
    private static boolean isShuffling = false;
    private static boolean isReplaying = false;
    private static Playlist currentPlaylist;
    private String address;
    private String title;
    private String artist;
    private String album;
    private ImageIcon icon;
    private Mp3File mp3File;

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

    /**
     * checks that media is in favorite playList or not
     * @return a boolean that shows is favorite or not
     */
    public boolean isFave() {
        return StorageManager.getInstance().getPlaylistHashMap().get("Favorite").getPlaylistMedia().contains(this);
    }
    /**
     * checks that media is in shared playList or not
     * @return a boolean that shows is shared or not
     */
    public boolean isShared() {
        return StorageManager.getInstance().getPlaylistHashMap().get("Shared").getPlaylistMedia().contains(this);
    }


    public ImageIcon getIcon() {
        return icon;
    }

    public Media(String address) {
        this.address = address;
        try {
            mp3File = new Mp3File(address);
            if (mp3File.hasId3v1Tag()) {
                //this part is for reading data from ID3v1 files

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

            this.artist= id3.substring(33, 63);

            this.album= id3.substring(63, 93);
    }
                 */
                ID3v1 id3v1 = mp3File.getId3v1Tag();
                this.title = id3v1.getTitle();
                this.artist = id3v1.getArtist();
                this.album = id3v1.getAlbum();
            }
            if (mp3File.hasId3v2Tag()) {
                ID3v2 id3v2 = mp3File.getId3v2Tag();
                this.title = id3v2.getTitle();
                this.artist = id3v2.getArtist();
                this.album = id3v2.getAlbum();

                byte[] imageData = id3v2.getAlbumImage();
                if (imageData != null) {
                    System.out.println("imageData is not null");
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
                    icon = new ImageIcon(img);

                }
            }


        } catch (Exception e) {
            System.out.println("s");

        }
        //this part is for reading data from id3v1 files
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

    /**
     * sets volume playing file between -20,40
     * @param volumeSlidPosition that declare volume of file
     */
    public void adjustVolume(float volumeSlidPosition) {
        float volume = (float) (((6 / 10.0) * volumeSlidPosition) - 20);
        if (mainPlayer != null) {
            mainPlayer.changeVolume(volume);
        }
    }

    /**
     * declare that which frame is starting frame to playFile
     * @param MusicSliderPosition that declare percentage of seeking
     * @throws FileNotFoundException
     * @throws JavaLayerException
     */
    public void seekTo(int MusicSliderPosition) throws FileNotFoundException, JavaLayerException {
        int startingFrame = (int) (MusicSliderPosition * mp3File.getFrameCount() / 100.0);
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

    /**
     * this func updates data and GUI after playing a song
     */
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

    /**
     * switches to next media if its shuffling goes random
     * if music finish it goes to next music automatically
     */
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




    // this part is for checking this class and make it easy to check just this part
/*
    public static void main(String[] args) throws InterruptedException,  {
        Media media = new Media("E:/Music/Imagine-Dragons-Digital-128.mp3");

//      new Media("./resources/media/Barobax - Shervin - www.telegram.me~IranSongs.mp3");
//       Media media = new Media("1.mp3");
        media.playFile();
        // mainPlayer.play();
        System.out.println("fffuck");
//        System.out.println("---"+mp3File.getFrameCount());
        Thread.sleep(5000);


        //  mainPlayer.moveSlider(60,  mp3File.getFrameCount());



       media.playFile();

        System.out.println("ghsem");

        Thread.sleep(5000);
        mainPlayer.pause();
        Thread.sleep(2000);
        mainPlayer.resume();
        Thread.sleep(10000);
       // media.setVolume(0.5f);
    }
*/
}
