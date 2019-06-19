package logic.media;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class StorageManager {
    private static StorageManager ourInstance = new StorageManager();
    private static File mediaAddresses;
    private static File mediaDataAddresses;
    private static ArrayList<Media> mediaArrayList = new ArrayList<>();
    private static HashMap<String,MediaData> mediaData = new HashMap<>();

    public static StorageManager getInstance() {
        return ourInstance;
    }

    private StorageManager() {
        mediaAddresses = new File("./data/mediaAddresses.bin");
        mediaDataAddresses = new File("./data/mediaDataAddresses.bin");
        load();
    }

    private static void load() {
        try {
            Scanner scanner = new Scanner(new FileReader(mediaAddresses));
            while (scanner.hasNextLine()){
                mediaArrayList.add(new Media(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            return;
        }
        try {
            FileInputStream inputStream = new FileInputStream(mediaDataAddresses);

        } catch (FileNotFoundException e){
            //Ignore
        }
    }

    private static void

    public static ArrayList<Media> getMediaArrayList() {
        return mediaArrayList;
    }
}
