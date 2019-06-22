package logic.storage;

import logic.media.Media;
import logic.media.MediaData;
import logic.playlist.Playlist;
import logic.playlist.PlaylistElement;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class StorageManager {
    private static StorageManager ourInstance = new StorageManager();
    private File mediaAddresses;
    private File mediaDataFile;

    private ArrayList<Media> mediaArrayList = new ArrayList<>();
    private HashMap<String, Album> albumHashMap = new HashMap<>();
    private HashMap<String, Playlist> playlistHashMap = new HashMap<>();

    private HashMap<String, MediaData> mediaDataHashMap = new HashMap<>();

    public static StorageManager getInstance() {
        return ourInstance;
    }

    private StorageManager() {
        mediaAddresses = new File("./data/mediaAddresses.bin");
        mediaDataFile = new File("./data/mediaDataFile.bin");
        load();
        generateAlbums();
        generatePlaylists();
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
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(mediaDataFile));
            //noinspection unchecked
            mediaDataHashMap = (HashMap<String, MediaData>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            //Ignore
        }
    }

    public void addDirectory(File directory) {
        if (directory.isFile()) {
            addMedia(directory);
            return;
        }
        if (new File(directory.getAbsolutePath() + ".nomedia").exists()) {
            return;
        }
        if (directory.listFiles() != null) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                addDirectory(file);
            }
        }
    }

    public void addMedia(File media) {
        if (!media.getName().endsWith(".mp3")) {
            return;
        }
        for (Media savedMedia : mediaArrayList) {
            if (savedMedia.getAddress().equals(media.getAbsolutePath())) {
                return;
            }
        }
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(mediaAddresses, true));
            System.out.println(media.getAbsolutePath());
            writer.println(media.getAbsolutePath());
            writer.flush();
        } catch (IOException e) {
            System.out.println("Ridem amoo");
        }
        mediaArrayList.add(new Media(media.getAbsolutePath()));
    }

    public ArrayList<Media> getMediaArrayList() {
        return mediaArrayList;
    }

    public HashMap<String, MediaData> getMediaDataHashMap() {
        return mediaDataHashMap;
    }

    public HashMap<String, Playlist> getPlaylistHashMap() {
        return playlistHashMap;
    }

    private void generateAlbums() {
        for (Media savedMedia : mediaArrayList) {
            if (albumHashMap.containsKey(savedMedia.getAlbumName())) {
                albumHashMap.get(savedMedia.getAlbumName()).addSong(savedMedia);
            } else {
                albumHashMap.put(savedMedia.getAlbumName(), new Album(savedMedia.getAlbumName()));
            }

        }
    }

    private void generatePlaylists(){
        //TODO
    }

    public void updateMediaData(){
        for (Map.Entry<String, Playlist> entry : playlistHashMap.entrySet()) {
            String playListName = entry.getKey();
            Playlist playlist=entry.getValue();
            ArrayList<PlaylistElement>playlistElementArrayList =new ArrayList<>();
            int index=0;
            for (Media media1:playlist.getPlaylistMedia()){
                String mediaPath= media1.getAddress();
                PlaylistElement playlistElement=new PlaylistElement(playListName,mediaPath,index);
                if(mediaDataHashMap.containsKey(mediaPath)){
                    mediaDataHashMap.get(mediaPath).getElements().add(playlistElement);
                }
                else {
                    playlistElementArrayList.add(playlistElement);
                    MediaData mediaData = new MediaData(playlistElementArrayList);
                    mediaDataHashMap.put(mediaPath, mediaData);
                }
                    index++;
           }


        }

       // PlaylistElement playlistElement=new PlaylistElement(playlistName,media.getAddress(),index);
        //ArrayList<PlaylistElement>playlistElementArrayList =new ArrayList<>();


    }

    public void saveAndQuit(){
        //TODO
    }


}
