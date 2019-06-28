package logic.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class LyricsGatherer {

    private final static String songLyricsURL = "http://www.songlyrics.com";

    public static List<String> getSongLyrics(String songArtist, String songTitle) throws IOException {
        List<String> lyrics = new ArrayList<>();

        Document doc = Jsoup.connect(songLyricsURL + "/" + songArtist.replace(" ", "-").toLowerCase() + "/" + songTitle.replace(" ", "-").toLowerCase() + "-lyrics/").get();
        String title = doc.title();
        System.out.println(title);
        Element p = doc.select("p.songLyricsV14").get(0);
        for (Node e : p.childNodes()) {
            if (e instanceof TextNode) {
                lyrics.add(((TextNode) e).getWholeText());
            }
        }
        return lyrics;
    }

}
