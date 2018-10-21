package luck.anime;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class Episode {
    private final int id;
    private final String title;
    private final List<String> langs;
    private final String date;
    private String viewLink;
    private List<EpisodeStream> streams = null;

    public Episode(String id, String title, List<String> langs, String date, String viewLink) {
        this.id = Integer.parseInt(id);
        this.title = title;
        this.langs = langs;
        this.date = date;
        this.viewLink = viewLink;
    }

    @Override
    public String toString() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLangs() {
        return langs;
    }

    public String getDate() {
        return date;
    }

    public String getViewLink() {
        return viewLink;
    }

    public List<EpisodeStream> getStreams() {
        if (this.streams == null) {
            this.streams = new ArrayList();
            try {
                final Document page = API.getPage(viewLink);
                for (Element element : page.select(".episode-player-list table tbody tr")) {
                    final EpisodeStream a = API.getGson().fromJson(element.getElementsByClass("ep-buttons").get(0).getElementsByTag("a").attr("data-episode"), EpisodeStream.class);
                    this.streams.add(a);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.streams;
    }
}
