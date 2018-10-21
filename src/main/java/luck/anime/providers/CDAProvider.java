package luck.anime.providers;

import luck.anime.API;
import luck.anime.CDAPlayerData;
import luck.anime.DownloadUtils;
import luck.anime.PlayerInfo;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class CDAProvider implements StreamProvider {
    @Override
    public MediaInfo getMedia(String iframeUrl) {
        System.out.println(iframeUrl);
        final String[] split = iframeUrl.split("/");
        String player_data = null;
        try {
            player_data = Jsoup.parse(new URL(iframeUrl), 5000).getElementById("mediaplayer" + split[split.length - 1]).attr("player_data");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final CDAPlayerData cdaPlayerData = API.getGson().fromJson(player_data, CDAPlayerData.class);
        try {
            System.out.println("Playing: " + URLDecoder.decode(cdaPlayerData.getVideo().getTitle(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Thumb url: " + cdaPlayerData.getVideo().getThumb());

        return new MediaInfo(cdaPlayerData.getVideo().getFile());
    }

    @Override
    public void download(String url, File file) {
        final MediaInfo media = getMedia(url);
        DownloadUtils.shedule(media.getVideoURL(), file);
    }
}
