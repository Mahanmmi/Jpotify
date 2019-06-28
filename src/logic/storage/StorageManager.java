package logic.storage;

import graphic.MainPanel;
import logic.media.Media;
import logic.media.MediaData;
import logic.network.client.Client;
import logic.storage.playlist.AutoPlayList;
import logic.storage.playlist.Playlist;
import logic.storage.playlist.PlaylistElement;
import logic.storage.playlist.UserPlaylist;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * StorageManager class is main part of our program
 * implementing Singleton class
 */
public class StorageManager {
    // static variable single_instance of type Singleton
    private static StorageManager ourInstance = new StorageManager();
    private final File MEDIA_ADDRESSES;
    private final File MEDIA_DATAFILE;

    private ArrayList<Media> mediaArrayList = new ArrayList<>();
    private HashMap<String, Album> albumHashMap = new HashMap<>();
    private HashMap<String, Playlist> playlistHashMap = new HashMap<>();
    private HashMap<String, MediaData> mediaDataHashMap = new HashMap<>();
    //playList that we start our program
    private Playlist defaultPlaylist;
    private Client client;

    private MainPanel mainPanel;

    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public static StorageManager getInstance() {
        return ourInstance;
    }

    /**
     * create our MEDIA_ADDRESSES and MEDIA_DATAFILE file
     * call als functions that we need to load our data and connecting to server
     */
    private StorageManager() {
        //this directory contain our data for save and load
        File dataDirectory = new File("./data");
        if(!dataDirectory.exists()){
            try {
                Files.createDirectory(dataDirectory.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MEDIA_ADDRESSES = new File("./data/MEDIA_ADDRESSES.bin");
        MEDIA_DATAFILE = new File("./data/MEDIA_DATAFILE.bin");
        load();
        generateAlbums();
        generatePlaylists();
        setInitialPlaylist();
        connectToServer();
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public Client getClient() {
        return client;
    }

    /**
     * this function connect a client to our server
     */
    private void connectToServer() {
        try {
            client = new Client();
            client.initStreams();
            new Thread(client).start();
        } catch (IOException e) {
            System.out.println("Cannot init client");
        }
    }

    /**
     * this func create our defaultPlayList
     */
    private void setInitialPlaylist() {
        defaultPlaylist = new AutoPlayList("", mediaArrayList);
        Media.setCurrentPlaylist(defaultPlaylist);
        if (Media.getCurrentPlaylist().getPlaylistMedia().size() != 0) {
            Media.setNowPlaying(Media.getCurrentPlaylist().getPlaylistMedia().get(0));
        }
    }

    /**
     *  after running our program to load our data
     * we create mediaArrayList that contain all our media
     * we create mediaDAtaHashMap that contain all our media`s data
     * and at the end sort our media
     */
    private void load() {
        try {
            Scanner scanner = new Scanner(new FileReader(MEDIA_ADDRESSES));
            while (scanner.hasNextLine()) {
                String address = scanner.nextLine();
                if(new File(address).exists()) {
                    mediaArrayList.add(new Media(address));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("FNF");
            return;
        }
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(MEDIA_DATAFILE));
            //noinspection unchecked
            mediaDataHashMap = (HashMap<String, MediaData>) inputStream.readObject();
//            for (String s : mediaDataHashMap.keySet()) {
//                System.out.println("s = " + s + " : " + mediaDataHashMap.get(s));
//            }
        } catch (IOException | ClassNotFoundException e) {
            //Ignore
        }

        System.out.println("HEY");
        for (Media media : mediaArrayList) {
            if (!mediaDataHashMap.containsKey(media.getAddress())) {
                mediaDataHashMap.put(media.getAddress(), new MediaData(media.getAddress(), new ArrayList<>()));
            }
        }
        sortMediaArrayList();
    }

    /**
     *  sort our MediaArrayList
     * its synchronized to prevent adding any media during sorting
     */
    public synchronized void sortMediaArrayList() {
        mediaArrayList.sort((a, b) -> {
            Date aDate = mediaDataHashMap.get(a.getAddress()).getLastPlayed();
            Date bDate = mediaDataHashMap.get(b.getAddress()).getLastPlayed();
            return bDate.compareTo(aDate);
        });
    }

    /**
     *  add all song of a directory to our library
     * @param directory the directory that we want all song it contains.
     */
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

    /**
     * add a media to our library
     * @param media that media we want add to library
     */
    public synchronized void addMedia(File media) {
        if (!media.getName().endsWith(".mp3") || !media.exists()) {
            return;
        }
        if (new File(media.getParent() + "/.nomedia").exists()) {
            return;
        }
        for (Media savedMedia : mediaArrayList) {
            if (savedMedia.getAddress().equals(media.getAbsolutePath())) {
                return;
            }
        }
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(MEDIA_ADDRESSES, true));
            System.out.println(media.getAbsolutePath());
            writer.println(media.getAbsolutePath());
            writer.flush();
        } catch (IOException e) {
            System.out.println("couldnt write address to file");
        }
        mediaArrayList.add(new Media(media.getAbsolutePath()));
        mediaDataHashMap.put(media.getAbsolutePath(), new MediaData(media.getAbsolutePath(), new ArrayList<>()));
        sortMediaArrayList();
        generateAlbums();
        mainPanel.updateGUISongDetails();
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

    public Playlist getDefaultPlaylist() {
        return defaultPlaylist;
    }

    public HashMap<String, Album> getAlbumHashMap() {
        return albumHashMap;
    }

    /**
     * create albums for our media
     */
    private synchronized void generateAlbums() {
        albumHashMap = new HashMap<>();
        for (Media savedMedia : mediaArrayList) {
            if (!albumHashMap.containsKey(savedMedia.getAlbumName())) {
                albumHashMap.put(savedMedia.getAlbumName(), new Album(savedMedia.getAlbumName()));

            }
            albumHashMap.get(savedMedia.getAlbumName()).addSong(savedMedia);
        }
    }

    /**
     * find a media by address
     * @param address this is the path of our media
     * @return media that we looking for.
     */
    private Media findMediaByAddress(String address) {
        for (Media media : mediaArrayList) {
            if (media.getAddress().equals(address)) {
                return media;
            }
        }
        return null;
    }

    /**
     * generate play list from mediaDataHashMap and using mediaData and
     * add media to playList if exists our create playList and it first media and
     * sort our playList
     */
    private void generatePlaylists() {
        for (String address : mediaDataHashMap.keySet()) {
            Media media = findMediaByAddress(address);
            if (media == null) {
                continue;
            }
            ArrayList<PlaylistElement> playlistElements = mediaDataHashMap.get(address).getElements();
            for (PlaylistElement playlistElement : playlistElements) {
                String playlistName = playlistElement.getPlaylistName();
                Playlist playlist;
                if (!playlistHashMap.containsKey(playlistName)) {
                    if (!playlistName.equals("Shared") && !playlistName.equals("Favorite")) {
                        playlist = new UserPlaylist(playlistName, new ArrayList<>());
                    } else {
                        playlist = new AutoPlayList(playlistName);
                    }
                    playlistHashMap.put(playlistName, playlist);
                } else {
                    playlist = playlistHashMap.get(playlistName);
                }
                playlist.addMedia(media);
            }
        }

        if (!playlistHashMap.containsKey("Shared")) {
            playlistHashMap.put("Shared", new AutoPlayList("Shared"));
        }
        if (!playlistHashMap.containsKey("Favorite")) {
            playlistHashMap.put("Favorite", new AutoPlayList("Favorite"));
        }

        for (Playlist playlist : playlistHashMap.values()) {
            playlist.getPlaylistMedia().sort((a, b) -> {
                PlaylistElement aElement = null;
                for (PlaylistElement element : mediaDataHashMap.get(a.getAddress()).getElements()) {
                    if (element.getPlaylistName().equals(playlist.getName())) {
                        aElement = element;
                        break;
                    }
                }
                PlaylistElement bElement = null;
                for (PlaylistElement element : mediaDataHashMap.get(b.getAddress()).getElements()) {
                    if (element.getPlaylistName().equals(playlist.getName())) {
                        bElement = element;
                        break;
                    }
                }
                if (aElement == null) {
                    return 1;
                }
                if (bElement == null) {
                    return -1;
                }
                return Integer.compare(aElement.getIndex(), bElement.getIndex());
            });
        }
    }

    /**
     * apply changes to our mediaDataHashMap
     */
    public synchronized void updateMediaData() {
        for (Map.Entry<String, Playlist> entry : playlistHashMap.entrySet()) {
            String playListName = entry.getKey();
            Playlist playlist = entry.getValue();
            int index = 0;
            for (Media media : playlist.getPlaylistMedia()) {
                String mediaPath = media.getAddress();
                PlaylistElement playlistElement = new PlaylistElement(playListName, mediaPath, index);
//                System.out.println(playlistElement);
                if (mediaDataHashMap.containsKey(mediaPath)) {
                    if (!mediaDataHashMap.get(mediaPath).getElements().contains(playlistElement)) {
                        mediaDataHashMap.get(mediaPath).getElements().add(playlistElement);
                    }
                } else {
                    ArrayList<PlaylistElement> playlistElementArrayList = new ArrayList<>();
                    playlistElementArrayList.add(playlistElement);
                    MediaData mediaData = new MediaData(mediaPath, playlistElementArrayList);
                    mediaDataHashMap.put(mediaPath, mediaData);
                }
                index++;
            }
        }
        for (MediaData mediaData : mediaDataHashMap.values()) {
            for (int i = 0; i < mediaData.getElements().size(); i++) {
                PlaylistElement element = mediaData.getElements().get(i);
                Media media = findMediaByAddress(element.getSongAddress());
                if (!playlistHashMap.get(element.getPlaylistName()).getPlaylistMedia().contains(media)) {
                    mediaData.getElements().remove(i);
                    i--;
                }
            }
        }
    }

    /**
     * its for last part of our program that save all thing we need for reloading
     */
    public synchronized void saveAndQuit() {
        System.out.println(client);
        if (client != null) {
            try {
                client.sendCloseSocket();
            } catch (Exception e){
                System.out.println("can't send close socket");
            }
        }
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(MEDIA_DATAFILE));
            outputStream.writeObject(mediaDataHashMap);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("can't write mediaDataHashMap to MEDIA_DATAFILE");
        }
    }


}
