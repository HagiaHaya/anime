package luck.anime.providers;

public class MediaInfo {
    private String videoURL;
    private String name;

    public MediaInfo(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoURL() {
        return videoURL;
    }
}
