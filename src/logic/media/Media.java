package logic.media;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import java.io.*;
import java.util.Map;

public class Media {
    private String address;
    private File file;
    private String title;
    private String artist;
    private String album;
    private String year;
    private String genre;
    private int time;

    private Object myPropertiesReader(String prop) {
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
    }

    public Media(String address) {
        this.address = address;
        file = new File(address);
        System.out.println(file.getName());
        //duration, title, author, album, date, comment, copyright, mp3.framerate.fps, mp3.length.frames, mp3.vbr.scale, mp3.id3tag.v2
        System.out.println("Title : " + myPropertiesReader("title"));
        System.out.println("Duration : " + myPropertiesReader("duration"));
        System.out.println("Copyright : " + myPropertiesReader("copyright"));
        System.out.println("Author : " + myPropertiesReader("author"));
        System.out.println("Album : " + myPropertiesReader("album"));
        System.out.println("mp3.framerate.fps : " + myPropertiesReader("mp3.framerate.fps"));

//        myByteReader(0, 4);
//        myByteReader(4, 100);
//        myByteReader(104, 100);
//        myByteReader(204, 100);
//        myByteReader(304, 100);
//        myByteReader(404, 100);
    }

    public static void main(String[] args) {
        new Media("./resources/media/Imagine-Dragons-Digital-128.mp3");
        new Media("./resources/media/Barobax - Shervin - www.telegram.me~IranSongs.mp3");
    }
}
