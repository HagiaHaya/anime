package luck.anime;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class LoginGuiController implements Initializable {
    @FXML
    public ImageView shinden;
    @FXML
    public TextField login;
    @FXML
    public PasswordField password;
    @FXML
    public Button sendData;
    @FXML
    public Text error;
    @FXML
    public Text session;

    @FXML
    public void getData(ActionEvent event) throws IOException {
        final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";
        String loginFormUrl = "https://shinden.pl/main/login";
        String loginActionUrl = "https://shinden.pl/main/0/login";
        String username = login.getText();
        String pwd = password.getText();
        HashMap<String, String> formData = new HashMap<>();
        formData.put("username", username);
        formData.put("password", pwd);
        formData.put("remember", "on");
        Connection.Response loginPage = Jsoup.connect(loginActionUrl)
                .referrer(loginFormUrl)
                .data(formData)
                .method(Connection.Method.POST)
                .userAgent(USER_AGENT)
                .followRedirects(true)
                .execute();
        final Map<String, String> cookies = loginPage.cookies();
        System.out.println(cookies);
        File dir = new File(System.getProperty("user.home").concat("/.SHINDEN"));
        if(!dir.exists()){
            dir.mkdirs();
        }
        final File file = new File(dir, "AUTH.dat");
        final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        cookies.forEach((a,b)-> System.out.println(a + " -> " + b));
        final String sess_shinden = cookies.get("sess_shinden");
        final String cross = cookies.get("cross");
        final String authautologin = cookies.get("autologin");
        final String cfduid = cookies.get("__cfduid");
        oos.writeUTF(sess_shinden);
        oos.writeUTF(cross);
        oos.writeUTF(authautologin);
        oos.writeUTF(cfduid);
        oos.flush();
        oos.close();
        logged(cookies);
    }

    private void logged(Map<String, String> cookies) {

        API.setCookies(cookies);
        if (cookies.size() > 2) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Stage stage = AnimeApplication.getInstance().getPrimaryStage();
            Scene scene = null;
            try {
                scene = new Scene(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(scene);
        } else {
            error.setText("Wprowadzono błędny login lub hasło");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File dir = new File(System.getProperty("user.home").concat("/.SHINDEN"));
        if(!dir.exists()){
            dir.mkdirs();
        }
        final File file = new File(dir, "AUTH.dat");
        if(file.exists()) {
            final ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(new FileInputStream(file));
                String sess_shinden = ois.readUTF();
                String cross = ois.readUTF();
                String authautologin = ois.readUTF();
                String cfduid = ois.readUTF();
                ois.close();
                final HashMap<String, String> cookies = new HashMap<>();
                cookies.put("sess_shinden", sess_shinden);
                cookies.put("cross", cross);
                cookies.put("authautologin", authautologin);
                cookies.put("__cfduid", cfduid);

                if (isLogged(cookies)) {
                    Platform.runLater(()->logged(cookies));
                }else{
                    System.out.println("login failed");
                    session.setText("Sesja wygasła. Zaloguj się ponownie");
                }
            } catch (IOException e) {
                file.delete();
                e.printStackTrace();
            }
        }else{
            System.out.println("no file");
        }
    }

    private boolean isLogged(HashMap<String, String> cookies) {
        if (API.getResponseCode("/user", cookies)==200) {
            return true;
        }
        return false;
    }
}
