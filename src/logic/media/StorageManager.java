package logic.media;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class StorageManager {
    private static StorageManager ourInstance = new StorageManager();
    private File mediaAddresses;
    private File mediaDataAddresses;
    private ArrayList<Media> mediaArrayList = new ArrayList<>();
    private HashMap<String, MediaData> mediaData = new HashMap<>();

    public static StorageManager getInstance() {
        return ourInstance;
    }

    private StorageManager() {
        mediaAddresses = new File("./data/mediaAddresses.bin");
        mediaDataAddresses = new File("./data/mediaDataAddresses.bin");
        load();
    }

    private void load() {
        try {
            Scanner scanner = new Scanner(new FileReader(mediaAddresses));
            while (scanner.hasNextLine()) {
                mediaArrayList.add(new Media(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            System.out.println("FNF");
            return;
        }
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(mediaDataAddresses));
            mediaData = (HashMap<String, MediaData>) inputStream.readObject();
            //TODO
        } catch (IOException | ClassNotFoundException e) {
            //Ignore
        }
    }

    public void addMedia(File media){
        for (Media savedMedia : mediaArrayList) {
            if(savedMedia.getAddress().equals(media.getAbsolutePath())){
                return;
            }
        }
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(mediaAddresses,true));
            System.out.println(media.getAbsolutePath());
            writer.println(media.getAbsolutePath());
            writer.flush();
        } catch (IOException e){
            System.out.println("Ridem amoo");
        }
        mediaArrayList.add(new Media(media.getAbsolutePath()));
    }

    public ArrayList<Media> getMediaArrayList() {
        return mediaArrayList;
    }
}
