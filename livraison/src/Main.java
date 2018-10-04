package src;

/**
 * @author Zaizafoun Sami et Jacqueline Martin
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

    /**
     * Main method of the program. Opens the ".fxml" file.
     */

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("MediaPlayer");
        stage.getIcons().add(new Image("file:cover/index.png"));

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        launch(args);
    }

}
