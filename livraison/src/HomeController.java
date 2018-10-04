package src;

/**
 * @author Zaizafoun Sami et Jacqueline Martin
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

/**
 * Initializes all the .fxml attributes and methods
 */
public class HomeController implements Initializable {
    @FXML TabPane tab;
    private int track = 0;

    private MediaProp mediaProp;
    private File[] file;
    private ObservableList <Media> song;

    private MediaPlayer mediaPlayer;
    private final HashMap<String,Integer> playingCounter = new HashMap<>(); // Favorit counter

    // Import PlayList
    /*--------------------------------------*/
    final ObservableList<PlayList> data = FXCollections.observableArrayList();
    @FXML private TableView<PlayList> Plist; // PlayList
    @FXML private TableColumn<PlayList, String> tTitle; //Table Title
    @FXML private TableColumn<PlayList, String> tArtist; //Table Artist
    @FXML private TableColumn<PlayList, String> tAlbum; //Table Album
    @FXML private TableColumn<PlayList, String> tDuration; //Table Duration
    /*--------------------------------------*/

    // Import PlayList favorits
    /*--------------------------------------*/
    final ObservableList<PlayList> dataFav = FXCollections.observableArrayList();
    @FXML private TableView<PlayList> PlistFav; // favorits PlayList
    @FXML private TableColumn<PlayList, String> tTitleFav; //Table Title favorit
    @FXML private TableColumn<PlayList, String> tArtistFav; //Table Artist favorit
    @FXML private TableColumn<PlayList, String> tAlbumFav; //Table Album favorit
    @FXML private TableColumn<PlayList, String> tDurationFav; //Table Duration favorit
    /*--------------------------------------*/

    private ArrayList<String> titleName;
    private ArrayList<String> artistName;
    private ArrayList<String> albumName;

    private int click=0; // Play/Pause click (logo display)
    private int mClick =0; //Mouse click counter
    private Object save; // Save clicked on song for counter

    @FXML Slider seekSlider;
    private Duration dur; // Current duration

    private Boolean mloop= false; // Music loop test

    @FXML Slider volumeSlider;
    private int clickv=0; //Volume click
    private int saveVolu=0; // Counter to avoid volume reset
    private double volumeSaved; // Save previous volume (before mute)
    private double vol; //volume according to the slider's location

    // .fxml imports
    /*--------------------------------------*/
    @FXML private Label title;
    @FXML private Label artist;
    @FXML private ImageView cover;
    @FXML private TextArea lyrics;

    @FXML private Label volIcon;
    @FXML private Label pl; //play
    @FXML private Label pa; //pause
    @FXML private Label timeLine;
    @FXML private Label lLoop; //label loop
    /*--------------------------------------*/

    /**
     * Initialize the program.
     * @param url URL
     * @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tab.getSelectionModel().select(1);
        mediaProp = new MediaProp();
        file = mediaProp.openFile();
        song = mediaProp.openMedia(track);

        for(int i=0; i<file.length;i++){
            mediaProp.getMeta(i);
        }
        listSet();
        start();
        timeLine();

        // Auto refresh when the program is launched.
        int setUp=0;
        if (setUp==0){
            tab.setOnMouseMoved(e -> {
                try {
                    refresh();
                } catch (Exception ex) {
                  System.out.println("Program not ready!");
                }
            });
            setUp++;
        }
    }

    /**
     * Open the media file.
     */
    public void start(){
        mediaPlayer = new MediaPlayer(song.get(track));
        songCounter();
        imageRec();
        volume();
    }

    /**
     * Creates a HashMap of the songs and how many times they've been played.
     * @return A HashMap of the songs and how many times they've been played.
     */
    public HashMap<Integer,Integer> ListCounter(){
    	HashMap<Integer,Integer> listCounter = new HashMap<>();
    	for(int i=0; i<file.length; i++){
            String testName = file[i].getName();
            if(playingCounter.containsKey(testName)){
            	listCounter.put(i,playingCounter.get(testName));
            }else{
            	listCounter.put(i,0);
            }
    	}
    	return listCounter;
    }

    /**
     * Counts how many times a song has been played.
     */
    public void songCounter(){
        PlayList selectedUnit = Plist.getSelectionModel().getSelectedItem();
        String CounterName = file[track].getName();
        if(playingCounter.containsKey(CounterName)){
            playingCounter.put(CounterName,playingCounter.get(CounterName)+1);
        }else{
            playingCounter.put(CounterName,1);
        }
        if(playingCounter.get(file[track].getName())>=3){
            if(!dataFav.contains(selectedUnit)){
                dataFav.add(selectedUnit);
            }
        }
    }

    /**
     * Set all the metadata to their right place in the Playlist table.
     */
    public void listSet(){
        tTitle.setCellValueFactory(cellData -> cellData.getValue().getTitleP());
        tTitleFav.setCellValueFactory(cellData -> cellData.getValue().getTitleP());

        tArtist.setCellValueFactory(cellData -> cellData.getValue().getArtistP());
        tArtistFav.setCellValueFactory(cellData -> cellData.getValue().getArtistP());

        tAlbum.setCellValueFactory(cellData -> cellData.getValue().getAlbumP());
        tAlbumFav.setCellValueFactory(cellData -> cellData.getValue().getAlbumP());

        tDuration.setCellValueFactory(cellData -> cellData.getValue().getDurationP());
        tDurationFav.setCellValueFactory(cellData -> cellData.getValue().getDurationP());

        Plist.setItems(data);
        PlistFav.setItems(dataFav);
    }

    /**
     * PlayList display
     * @throws IOException Because of lyrics
     */
    public void listDisplay() throws IOException{
        ArrayList<ArrayList<String>> meta = mediaProp.setMeta();

        titleName= meta.get(0);
        artistName= meta.get(1);
        albumName= meta.get(2);

        data.clear();
        for(int i=0;i<file.length;i++){
            String totalTime = mediaProp.getDuration((long) song.get(i).getDuration().toMillis());
            PlayList ele = new PlayList(titleName.get(i),artistName.get(i),albumName.get(i),totalTime);
            data.add(ele);
        }
        metaData();
    }

    /**
     * Make PlayList clickable.
     * @param arg0 Mouse event argument
     * @throws IOException Because of lyrics
     */
    @FXML public void listChoice(MouseEvent arg0) throws IOException {
        Object choix= Plist.getSelectionModel().getSelectedItem().getTitleP().getValue();
        if(save==choix){
            mClick++;
        }

        if (mClick==0){
            save = choix;
        }

        if(mClick == 1){
            click=0;
            if (choix==null){
                System.out.println("Invalid choice");
            }else{
                click=0;
                save="";
                stop(null);

                String fichier = "../"+file[track].getParentFile().getName()+"/"+choix+".mp3";
                final URL resource = getClass().getResource(fichier);
                Media media = new Media(resource.toString());
                mediaPlayer = new MediaPlayer(media);

                if (Plist.getSelectionModel().getFocusedIndex()<=file.length){
                    track=Plist.getSelectionModel().getFocusedIndex();
                }

                play_pause(null);
                songCounter();
                imageRec();
                metaData();
                volume();
            }
            mClick = 0;
        }
    }

    /**
     * Make Favorit PlayList clickable.
     * @param arg0 Mouse event argument
     * @throws IOException Because of lyrics
     */
     @FXML public void listChoiceFav(MouseEvent arg0) throws IOException {
        Object choix= PlistFav.getSelectionModel().getSelectedItem().getTitleP().getValue();
        if(save==choix){
            mClick++;
        }

        if (mClick==0){
            save = choix;
        }

        if(mClick == 1){
            click=0;
            if (choix==null){
                System.out.println("Invalid choice");
            }else{
                click=0;
                stop(null);

                String fichier = "../"+file[track].getParentFile().getName()+"/"+choix+".mp3";
                final URL resource = getClass().getResource(fichier);
                Media media = new Media(resource.toString());
                mediaPlayer = new MediaPlayer(media);

                if (PlistFav.getSelectionModel().getFocusedIndex()<=file.length){
                    track=PlistFav.getSelectionModel().getFocusedIndex();
                }

                play_pause(null);
                imageRec();
                metaData();
                volume();
            }
            mClick = 0;
        }
    }

    /**
     * Add to favorits button.
     * @param event Button click event
     */
    public void addFavorits(ActionEvent event){
        PlayList selectedUnit = Plist.getSelectionModel().getSelectedItem();
        if(!dataFav.contains(selectedUnit)){
            dataFav.add(selectedUnit);
        }
    }

    /**
     * Remove from favorits button.
     * @param event Button click event
     */
    public void removeFavorits(ActionEvent event){
        PlayList selectedUnit = PlistFav.getSelectionModel().getSelectedItem();
        dataFav.remove(selectedUnit);
    }

    /**
     * Responsible for the current time elapsed and the song seeker advancement.
     */
    public void timeLine(){
            // Seeker advancement.
            seekSlider.valueProperty().addListener((Observable ov) -> {
                mediaPlayer.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
                    double valueTime=100;
                    valueTime*=(int)newValue.toSeconds();
                    valueTime/=(double)mediaPlayer.getMedia().getDuration().toSeconds();
                    seekSlider.setValue(valueTime);
                });

                // Current time elapsed
                dur = mediaPlayer.getCurrentTime();
                Integer seconde = (int) Math.round(dur.toSeconds());
                Integer min= (int)(Math.round(seconde/60));
                Integer sec = (int) (Math.round(seconde%60));

                String minS;
                String secS;

                if(min<10){
                    minS = "0"+Integer.toString(min);
                }else{
                    minS = Integer.toString(min);
                }
                if(sec<10){
                    secS = "0"+Integer.toString(sec);
                }else{
                    secS = Integer.toString(sec);
                }

                timeLine.setText(minS + ":" + secS);

                mediaPlayer.setOnEndOfMedia(() -> {
                    try{
                       Thread.sleep(1000);
                    }
                    catch(InterruptedException e){}

                    if(mloop){
                            restart(null);
                    }else{
                        try {
                            next(null);
                        } catch (IOException ex) {}
                    }
                });
            });

        }

    /**
     * Play/Pause button.
     * @param event Button click event
     */
    public void play_pause(ActionEvent event){
        if(click==0){
           mediaPlayer.play();
           seekSlider.setValue(0.1);
        }else{
            mediaPlayer.pause();
        }
        logo();
    }

    /**
     * Next button.
     * @param event Button click event
     * @throws IOException Because of lyrics
     */
    public void next(ActionEvent event) throws IOException{
        click =0;
        stop(null);
        if(track == file.length-1){
            track= 0;
            Plist.getSelectionModel().select(track);
        }else{
            track++;
            Plist.getSelectionModel().select(track);
        }
        start();
        metaData();
        play_pause(null);
    }

    /**
     * Previous button.
     * @param event Button click event
     * @throws IOException Because of lyrics
     */
    public void previous(ActionEvent event) throws IOException{
        click =0;
        stop(null);
        if(track==0){
            track= file.length-1;
            Plist.getSelectionModel().select(track);
        }else{
            track--;
            Plist.getSelectionModel().select(track);
        }
        start();
        metaData();
        play_pause(null);
    }

    /**
     * Advance song property.
     */
    public void avance(){
        mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(seekSlider.getValue()/100.0));
    }

    /**
     * Stop button.
     * @param event Button click event
     */
    public void stop(ActionEvent event){
        click = 1;
        mediaPlayer.stop();
        logo();
    }

    /**
     * Replay song button.
     * @param event Button click event
     */
    public void restart(ActionEvent event){
        click=0;
        mediaPlayer.seek(mediaPlayer.getStartTime());
        play_pause(null);
    }

    /**
     * Loop song property.
     * @param event Button click event
     */
    public void loop(ActionEvent event){
        if (mloop){
            mloop=false;
            lLoop.setText("Off");
        }else{
            mloop=true;
            lLoop.setText("On");
        }
    }

    /**
     * Volume property.
     */
    public void volume(){
        if(saveVolu==0){
            volumeSlider.setValue(mediaPlayer.getVolume()*100);
            volumeSlider.valueProperty().addListener((Observable observable) -> {
                vol = volumeSlider.getValue()/100;
                mediaPlayer.setVolume(vol);
                iconVolume();
                saveVolu++;
            });

        }else{
            mediaPlayer.setVolume(vol);
            iconVolume();
        }
    }

    /**
     * Mute button.
     * @param event Button click event
     */
    public void mute(ActionEvent event){
        if (clickv == 0){
            volumeSaved = volumeSlider.getValue();
            volume();
            volumeSlider.setValue(0);
            clickv++;
        }else{
            if (volumeSlider.getValue()!= 0){
                clickv--;
                mute(null);

            }else{
                volumeSlider.setValue(volumeSaved);
                clickv--;
            }
        }
        iconVolume();
    }

    /**
     * Title/Artist update
     * @throws IOException Because of lyrics
     */
    public void metaData() throws IOException{
        title.setText(titleName.get(track));
        artist.setText(artistName.get(track));
        paroles();
    }

    /**
     * Image update
     */
    public void imageRec(){
        final Circle clip = new Circle(133, 133, 130);
        cover.setClip(clip);
        cover.setImage(mediaProp.affImage(track));
    }

    /**
     * Lyrics update.
     * @throws IOException Because of lyrics
     */
    public void paroles() throws IOException{
        lyrics.setText(MediaProp.getSongLyrics(artistName.get(track), titleName.get(track)).toString());
    }

    /**
     * Refresh playlist property.
     * @throws IOException Because of lyrics
     */
    public void refresh() throws IOException{
        for(int i=0; i<file.length;i++){
            mediaProp.getMeta(i);
        }
        listDisplay();
        Plist.getSelectionModel().selectFirst();
    }

    /**
     * Update play/pause button symbol.
     */
    public void logo(){
        if (click ==0){
            pl.setOpacity(0);
            pa.setOpacity(100);
            click++;
        }else{
            pa.setOpacity(0);
            pl.setOpacity(100);
            click--;
        }
    }

    /**
     * Update volume symbol.
     */
    public void iconVolume(){
        if(vol >=0.66){
            volIcon.setText("\uD83D\uDD0A");
        }
        else if(vol >= 0.33){
            volIcon.setText("\uD83D\uDD09");
        }
        else if(vol > 0.0){
            volIcon.setText("\uD83D\uDD08");
        }else{
            volIcon.setText("\uD83D\uDD07");
        }
    }
}
