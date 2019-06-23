package logic.media;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

import com.mpatric.mp3agic.FileWrapper;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import javazoom.jl.decoder.JavaLayerException;

import java.awt.image.BufferedImage;
import java.io.*;

public class Media {
    private static PauseablePlayer mainPlayer = null;
    private String address;
    private static Mp3File mp3File;
    private String title;
    private String artist;
    private String album;
    private String year;
    private int genre;
    private int time;
    private static Boolean isPlaying;


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

    public Media(String address) {
        this.address = address;
        try {
            mp3File = new Mp3File(address);
            this.time = (int) mp3File.getLengthInSeconds();

            if (mp3File.hasId3v1Tag()) {
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
                    ImageIcon icon = new ImageIcon(img);

                }
            }


        } catch (Exception e) {
            System.out.println("s");

        }
        System.out.println("artist: " + artist);
        System.out.println("album: " + album);
        System.out.println("title: " + title);
        System.out.println("time: " + time);
       /* this.address = address;
        file = new File(address);
        System.out.println(file.getName());
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

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) ((Clip) mp3File).getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public void seekTo(int sliderPosition) throws FileNotFoundException, JavaLayerException {
        int startingFrame=(int)(sliderPosition*mp3File.getFrameCount()/100.0);
        mainPlayer.close();
        if(isPlaying){
            mainPlayer = new PauseablePlayer(new FileInputStream(address),startingFrame);
            mainPlayer.play();}
            else
                mainPlayer=new PauseablePlayer(new FileInputStream(address),startingFrame);
    }

    private void playFile() {
        if (mainPlayer != null) {
            mainPlayer.stop();
        }
        try {
            mainPlayer = new PauseablePlayer(new FileInputStream(address), 0);
            mainPlayer.play();
            isPlaying=true;
        } catch (FileNotFoundException | JavaLayerException e){
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
