package src;

/**
 * @author Zaizafoun Sami et Jacqueline Martin
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

/**
 * MediaProp class takes care of all the properties of a media file.
 */

public class MediaProp{
    private File[] resources;

    private final ObservableList<Media> mediaList = FXCollections.observableArrayList();

    private final ArrayList<String> allTitles;
    private final ArrayList<String> allArtists;
    private final ArrayList<String> allAlbums;
    private final ArrayList <ArrayList<String>> allSongsInfo;

    private Image image;

    private final static String songLyricsURL = "http://www.songlyrics.com";

    public MediaProp() {
        this.allTitles= new ArrayList<>();
        this.allArtists= new ArrayList<>();
        this.allAlbums= new ArrayList<>();
        this.allSongsInfo = new ArrayList<>();
    }

    /**
     * Opens the folder and lists all the files in it.
     * @return The list of all the files.
     */
    public File[] openFile(){
        File audio = new File(getClass().getResource("../audio/").getPath());
        resources = audio.listFiles();
        return resources;
    }

    /**
     * Opens the media file and adds it into mediaList
     * @param track track number
     * @return List of all media files
     */
    public ObservableList<Media> openMedia(int track){
        for (File f: resources){
            String fichier = "../"+resources[track].getParentFile().getName()+"/"+resources[track].getName();
            final URL resource = getClass().getResource(fichier);
            Media media = new Media(resource.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaList.addAll(media);
            track++;
        }
        return mediaList;
    }

    /**
     * Gets the total duration of all media files.
     * @param milliseconds song duration in milliseconds
     * @return totalTime
     */
    public String getDuration(long milliseconds){
        String totalTime = "";
        String secondsString;

        //Convert total duration into time
        int hours = (int) (milliseconds/(1000*60*60));
        int minutes = (int) (milliseconds%(1000*60*60))/(1000*60);
        int seconds = (int) ((milliseconds%(1000*60*60))%(1000*60)/1000);

        // Add hours if there
        if (hours == 0) {
            totalTime = hours + ":";
        }

        if (seconds == 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        totalTime = totalTime + minutes + ":" + secondsString;

        // return timer string
        return totalTime;
    }

    /**
     * Recovers the metadata of all media files and adds it to a specified list.
     * @param track track number
     */
    public void getMeta(int track){
        mediaList.get(track).getMetadata().addListener((MapChangeListener.Change<? extends String, ? extends Object> ch) -> {
            String key = ch.getKey();
            Object value = ch.getValueAdded();
            if (ch.wasAdded()) {
                switch (key) {
                    case "title":
                        allTitles.add(value.toString());
                        break;
                    case "artist":
                        allArtists.add(value.toString());
                        break;
                    case "album":
                        allAlbums.add(value.toString());
                        break;
                    default:
                        break;
                }
            }
            allSongsInfo.clear();
            allSongsInfo.add(allTitles);
            allSongsInfo.add(allArtists);
            allSongsInfo.add(allAlbums);
        });

    }

    /**
     * Returns the list of all the metadata.
     * @return A list of all the songs information
     */
    public ArrayList<ArrayList<String>> setMeta(){
        return allSongsInfo;
    }

    /**
     * Recovers the album's image if existent.
     * @param track track number
     * @return Album image
     */
    public Image affImage(int track){
        String imfichier = resources[track].getName();
        String imName = imfichier.substring(0, imfichier.lastIndexOf("."));

        File imDossier = new File("cover/");
        File[] imFichier = imDossier.listFiles();

        for (File f : imFichier) {
            if (f.getName().equals(imName +".png")) {
                File imAddresse = new File("cover/" + imName + ".png");
                image = new Image(imAddresse.toURI().toString());
                break;
            }
            image = new Image("cover/note.png");
        }
        return image;
    }

    /**
     * Recovers the lyrics online.
     * @param band artist name
     * @param songTitle song title
     * @return Song lyrics
     * @throws IOException Because of lyrics
     */
    public static List<String> getSongLyrics(String band, String songTitle) throws IOException {
        List<String> lyrics= new ArrayList<>();

        try{
            Document doc = Jsoup.connect(songLyricsURL+ "/"+band.replace(" ", "-").toLowerCase()+"/"+songTitle.replace(" ", "-").toLowerCase()+"-lyrics/")
                    .userAgent("Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5").get();

            String title = doc.title();
            Element p = doc.select("p.songLyricsV14").get(0);

            p.childNodes().stream().filter((e) -> (e instanceof TextNode)).forEachOrdered((e) -> {
                lyrics.add(((TextNode)e).getWholeText());
            });
        }

        catch(IOException e){
            lyrics.add("No internet connection");
        }

        return lyrics;
    }
}
