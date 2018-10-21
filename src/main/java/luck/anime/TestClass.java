package luck.anime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestClass {
    public static void main(String[] args) throws IOException {
        String string = API.getHttpContent("https://openload.co/embed/BtZMKqOvIBM/58614ea9650a3.mp4");
        final String regex = "\\w+~\\d+~[\\d\\.]+~\\w+";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()){
            System.out.println(matcher.group());
        }
        if (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));
            for (int i = 1; i <= matcher.groupCount(); i++) {
                System.out.println("Group " + i + ": " + matcher.group(i));
            }
//        System.out.println("https://oload.cloud/stream/" + text + "?mime=true");
    }
}}
