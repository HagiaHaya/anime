package luck.anime;

import com.sun.jna.NativeLibrary;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.discovery.NativeDiscoveryStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.io.File;

public class AnimeApplication extends Application {
    private static AnimeApplication instance;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public static AnimeApplication getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        this.primaryStage = primaryStage;
        Scene login = new Scene(FXMLLoader.load(this.getClass().getResource("/login.fxml")), 1280, 720);
        primaryStage.setScene(login);
        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
