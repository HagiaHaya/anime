package luck.anime;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class API {

    private static final Gson GSON = new Gson();
    private static Map<String, String> cookies = new HashMap<>();

    public static Document getPage(String endpoint) throws Exception {
        if(endpoint.startsWith("/")){
            endpoint = endpoint.substring(1);
        }
        return getDom("https://shinden.pl/" + endpoint);
        //dziala !!!
    }

    public static Document getDom(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("accept-language", "pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("cache-control", "no-cache")
                .header("pragma", "no-cache")
                .header("upgrade-insecure-requests", "1")
                .cookies(cookies)
                .get();
    }

    public static String getHttpContent(String url) {
        return getHttpContent(url, false);
    }
    public static String getHttpContent(String url, boolean newLines) {
        try {
            final HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestProperty("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            urlConnection.setRequestProperty("Cookie", cookies.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("; ")));
            urlConnection.connect();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                sb.append(inputLine);
                if(newLines){
                    sb.append('\n');
                }
            }
            in.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static InputStream getHttpStream(String endpoint) {
        try {
            final HttpsURLConnection urlConnection = (HttpsURLConnection) new URL("https://shinden.pl" + endpoint).openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestProperty("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            urlConnection.connect();
            return urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpsURLConnection getConnection(String url) {
        try {
            final HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestProperty("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 302){
                String newLoc = urlConnection.getHeaderField("Location");
                urlConnection.disconnect();
                return getConnection(newLoc);
            }
            return urlConnection;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setCookies(Map<String, String> cookies) {
        API.cookies = cookies;
    }

    public static Gson getGson() {
        return GSON;
    }

    public static int getResponseCode(String endpoint, Map<String, String> cookies) {
        try {
            //Cookie: name=value; name2=value2; name3=value3
            final HttpsURLConnection urlConnection = (HttpsURLConnection) new URL("https://shinden.pl" + endpoint).openConnection();
            urlConnection.setRequestProperty("Cookie", cookies.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("; ")));
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestProperty("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            return urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String xhr(String url) {
        try {
            final HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
            urlConnection.setRequestProperty("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            urlConnection.setRequestProperty("Cookie", cookies.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("; ")));
            urlConnection.setRequestProperty("origin", "https://shinden.pl");
            urlConnection.setRequestProperty("pragma", "no-cache");
            urlConnection.setRequestProperty("accept-language", "pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7");
            urlConnection.setRequestProperty("accept", "/");
            urlConnection.setRequestProperty("cache-control", "no-cache");
            urlConnection.setRequestProperty("authority", new URL(url).getHost());
            urlConnection.connect();
            if(urlConnection.getHeaderField("set-cookie") != null) {
                for (String s : urlConnection.getHeaderField("set-cookie").split("; ")) {
                    if (s.contains("=")) {
                        final String[] split = s.split("=");
                        System.out.println("new cookie " + s);
                        cookies.put(split[0], split[1]);
                    }
                }
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                sb.append(inputLine);
                sb.append('\n');
            }
            in.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }
}