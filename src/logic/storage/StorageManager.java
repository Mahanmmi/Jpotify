package logic.storage;

import graphic.MainPanel;
import logic.media.Media;
import logic.media.MediaData;
import logic.playlist.AutoPlayList;
import logic.playlist.Playlist;
import logic.playlist.PlaylistElement;
import logic.playlist.UserPlaylist;

import java.io.*;
import java.util.*;

public class StorageManager {
    private static StorageManager ourInstance = new StorageManager();
    private final File MEDIA_ADDRESSES;
    private final File MEDIA_DATAFILE;

    private ArrayList<Media> mediaArrayList = new ArrayList<>();
    private HashMap<String, Album> albumHashMap = new HashMap<>();
    private HashMap<String, Playlist> playlistHashMap = new HashMap<>();
    private HashMap<String, MediaData> mediaDataHashMap = new HashMap<>();
    private Playlist defaultPlaylist;

    private MainPanel mainPanel;

    public static StorageManager getInstance() {
        return ourInstance;
    }

    private StorageManager() {
        MEDIA_ADDRESSES = new File("./data/MEDIA_ADDRESSES.bin");
        MEDIA_DATAFILE = new File("./data/MEDIA_DATAFILE.bin");
        load();
        generateAlbums();
        generatePlaylists();
        setInitialPlaylist();
        mainPanel = new MainPanel();
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    private void setInitialPlaylist() {
        defaultPlaylist = new AutoPlayList("", mediaArrayList);
        Media.setCurrentPlaylist(defaultPlaylist);
        if (Media.getCurrentPlaylist().getPlaylistMedia().size() != 0) {
            Media.setNowPlaying(Media.getCurrentPlaylist().getPlaylistMedia().get(0));
        }
    }

    private void load() {
        try {
            Scanner scanner = new Scanner(new FileReader(MEDIA_ADDRESSES));
            while (scanner.hasNextLine()) {
                mediaArrayList.add(new Media(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            System.out.println("FNF");
            return;
        }
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(MEDIA_DATAFILE));
            //noinspection unchecked
            mediaDataHashMap = (HashMap<String, MediaData>) inputStream.readObject();
            for (String s : mediaDataHashMap.keySet()) {
                System.out.println("s = " + s + " : " + mediaDataHashMap.get(s));
            }
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

    public void sortMediaArrayList() {
        mediaArrayList.sort((a, b) -> {
            Date aDate = mediaDataHashMap.get(a.getAddress()).getLastPlayed();
            Date bDate = mediaDataHashMap.get(b.getAddress()).getLastPlayed();
            return bDate.compareTo(aDate);
        });
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
            System.out.println("Ridem amoo");
        }
        mediaArrayList.add(new Media(media.getAbsolutePath()));
        sortMediaArrayList();
        mediaDataHashMap.put(media.getAbsolutePath(), new MediaData(media.getAbsolutePath(), new ArrayList<>()));
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

    private void generateAlbums() {
        for (Media savedMedia : mediaArrayList) {
            if (!albumHashMap.containsKey(savedMedia.getAlbumName())) {
                albumHashMap.put(savedMedia.getAlbumName(), new Album(savedMedia.getAlbumName()));

            }
            albumHashMap.get(savedMedia.getAlbumName()).addSong(savedMedia);
        }
    }

    private Media findMediaByAddress(String address) {
        for (Media media : mediaArrayList) {
            if (media.getAddress().equals(address)) {
                return media;
            }
        }
        return null;
    }

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

    public void updateMediaData() {
        for (Map.Entry<String, Playlist> entry : playlistHashMap.entrySet()) {
            String playListName = entry.getKey();
            Playlist playlist = entry.getValue();
            int index = 0;
            for (Media media : playlist.getPlaylistMedia()) {
                String mediaPath = media.getAddress();
                PlaylistElement playlistElement = new PlaylistElement(playListName, mediaPath, index);
                System.out.println(playlistElement);
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

        // PlaylistElement playlistElement=new PlaylistElement(playlistName,media.getAddress(),index);
        //ArrayList<PlaylistElement>playlistElementArrayList =new ArrayList<>();


    }

    public void saveAndQuit() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(MEDIA_DATAFILE));
            outputStream.writeObject(mediaDataHashMap);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ne nashod");
        }
    }


}
