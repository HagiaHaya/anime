package luck.anime;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

public class DownloadUtils {
    public static void shedule(String videoURL, File file) {
        try {
            HttpsURLConnection urlConnection = API.getConnection(videoURL);


            final int contentLength = urlConnection.getContentLength();
            final InputStream inputStream = urlConnection.getInputStream();
            final FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int n = 0;
            int downloaded = 0;
            while((n = inputStream.read(buffer)) > 0){
                downloaded += n;
                fileOutputStream.write(buffer, 0, n);
                System.out.println("readed " + convertToStringRepresentation(downloaded) + "/" + convertToStringRepresentation(contentLength) + " bytes " + (downloaded/(contentLength/100)) + "%");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static final long K = 1024;
    private static final long M = K * K;
    private static final long G = M * K;
    private static final long T = G * K;
    public static String convertToStringRepresentation(final long value){
        final long[] dividers = new long[] { T, G, M, K, 1 };
        final String[] units = new String[] { "TB", "GB", "MB", "KB", "B" };
        if(value < 1)
            throw new IllegalArgumentException("Invalid file size: " + value);
        String result = null;
        for(int i = 0; i < dividers.length; i++){
            final long divider = dividers[i];
            if(value >= divider){
                result = format(value, divider, units[i]);
                break;
            }
        }
        return result;
    }

    private static String format(final long value,
                                 final long divider,
                                 final String unit){
        final double result =
                divider > 1 ? (double) value / (double) divider : (double) value;
        return new DecimalFormat("#,##0.#").format(result) + " " + unit;
    }
}
