package luck.anime;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoGuiController implements Initializable {
    @FXML
    public MediaView video;
    @FXML
    public ImageView back;

    private static String urlToPlay;


    public void initialize(URL location, ResourceBundle resources) {
        if(urlToPlay != null) {
            //urlToPlay = new File("/run/media/DANE/ANIME/kiminonawa.mp4").toURI().toString();
            System.out.println("video url: " + urlToPlay);

//            final Media media = new Media(urlToPlay);
            final Media media = new Media("https://openload.co/embed/BtZMKqOvIBM/58614ea9650a3.mp4?mime=true");
            MediaPlayer videoMedia = new MediaPlayer(media);
            videoMedia.setAutoPlay(true);
            video.setMediaPlayer(videoMedia);
        }
    }
    public void handleBackAction(MouseEvent event){

    }

    public static String getUrlToPlay() {
        return urlToPlay;
    }

    public static void setUrlToPlay(String urlToPlay) {
        VideoGuiController.urlToPlay = urlToPlay;
    }
}
