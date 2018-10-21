package luck.anime.providers;

import luck.anime.PlayerInfo;

import java.io.File;

public interface StreamProvider {
    public MediaInfo getMedia(String iframeUrl);

    void download(String url, File file);
}
