package src;

/**
 * @author Zaizafoun Sami et Jacqueline Martin
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * PlayList class creates the playlist.
 */
public class PlayList {

    private final StringProperty rTitle;  //rTitle: Ready Title
    private final StringProperty rArtist; //rArtist: Ready Artist
    private final StringProperty rAlbum; //rAlbum: Ready album
    private final StringProperty rDuration; //rDuration :Ready Duration

    /**
     * Assigns all values of a song's metadata.
     * @param sTitle song title
     * @param sArtist song artist
     * @param sAlbum song album
     * @param sDuration song duration
     */
    public PlayList(String sTitle, String sArtist, String sAlbum, String sDuration){
        this.rTitle = new SimpleStringProperty(sTitle);
        this.rArtist = new SimpleStringProperty(sArtist);
        this.rAlbum = new SimpleStringProperty(sAlbum);
        this.rDuration = new SimpleStringProperty(sDuration);
    }

    /**
     * @return The value of rTitle
     */
    public String getTitle(){
        return rTitle.get();
    }

    /**
     * Sets the value of rTitle.
     * @param t sets the value
     */
    public void setTitle(String t){
        rTitle.set(t);
    }

    /**
     * @return The set value of rTitle (Title Property)
     */
    public StringProperty getTitleP(){
        return rTitle;
    }

    /**
     * @return The value of rArtist
     */
    public String getArtist(){
        return rArtist.get();
    }

    /**
     * Sets the value of rArtist.
     * @param a sets the value
     */
    public void setArtist(String a){
        rArtist.set(a);
    }

    /**
     * @return The set value of rArtist (Artist Property)
     */
    public StringProperty getArtistP(){
        return rArtist;
    }

    /**
     * @return The value of rAlbum
     */
    public String getAlbum(){
        return rAlbum.get();
    }

    /**
     * Sets the value of rAlbum.
     * @param al sets the value
     */
    public void setAlbum(String al){
        rAlbum.set(al);
    }

    /**
     * @return The set value of rAlbum (Album Property)
     */
    public StringProperty getAlbumP(){
        return rAlbum;
    }

    /**
     * @return The value of rDuration
     */
    public String getDuration(){
        return rDuration.get();
    }

    /**
     * Sets the value of rDuration.
     * @param d sets the value
     */
    public void setDuration(String d){
        rDuration.set(d);
    }

    /**
     * @return The set value of rDuration (Duration Property)
     */
    public StringProperty getDurationP(){
        return rDuration;
    }
}
