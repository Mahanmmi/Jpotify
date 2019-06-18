package logic.media;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import java.io.*;
import java.util.Calendar;
import java.util.Map;

public class Media {
    private String address;
    private Mp3File mp3File;
    private String title;
    private String artist;
    private String album;
    private String year;
    private int genre;
    private int time;

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
        try {
            mp3File = new Mp3File(address);
            this.time=(int)mp3File.getLengthInSeconds();

            if(mp3File.hasId3v1Tag()){
                ID3v1 id3v1=mp3File.getId3v1Tag();
                this.title=id3v1.getTitle();
                this.artist=id3v1.getArtist();
                this.album=id3v1.getAlbum();
                this.genre=id3v1.getGenre();
                this.year=id3v1.getYear();
            }
            if(mp3File.hasId3v2Tag()){
                ID3v1 id3v2=mp3File.getId3v2Tag();
                this.title=id3v2.getTitle();
                this.artist=id3v2.getArtist();
                this.album=id3v2.getAlbum();
                this.genre=id3v2.getGenre();
                this.year=id3v2.getYear();
            }

        }catch ( Exception e){
            System.out.println("s");

        }
        System.out.println("artist: "+artist);
        System.out.println("album: "+album);
        System.out.println("title: "+title);
        System.out.println("time: "+ time);
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

//        myByteReader(0, 4);
//        myByteReader(4, 100);
//        myByteReader(104, 100);
//        myByteReader(204, 100);
//        myByteReader(304, 100);
//        myByteReader(404, 100);
*/
    }

    public static void main(String[] args) {
     //   new Media("./resources/media/Imagine-Dragons-Digital-128.mp3");
     //
        //   new Media("./resources/media/Barobax - Shervin - www.telegram.me~IranSongs.mp3");
        new Media("1.mp3");

    }
}
