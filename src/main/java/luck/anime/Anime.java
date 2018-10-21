package luck.anime;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Anime {
    private boolean loaded;
    private String title;
    private String href;
    private String description;
    private String imageUrl;
    private String tags;
    private String info;
    private List<Episode> episodes;

    public Anime(String title, String href) {
        this.title = title;
        this.href = href;
        this.loaded = false;
    }

    public Anime(String title, String imageUrl, String tags, String description, String info) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.tags = tags;
        this.description = description;
        this.info = info;
        this.loaded = true;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        if (!loaded) load();
        return imageUrl;
    }

    public String getTags() {
        if (!loaded) load();
        return tags;
    }

    public String getDescription() {
        if (!loaded) load();
        return description;
    }

    public String getInfo() {
        if (!loaded) load();
        return info;
    }

    @Override
    public String toString() {
        return title;
    }

    public void load() {

        Document link = null;
        try {
            link = API.getPage(this.href);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageUrl = link.getElementsByClass("info-aside-img").get(0).attr("src");
        description = link.getElementsByClass("info-top").get(0).getElementsByTag("p").text();
        tags = link.getElementsByClass("tags").get(0).getElementsByTag("ul").text();
        Document episodes = null;
        try {
            episodes = API.getPage(this.href + "/all-episodes");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.episodes = new ArrayList<>();
        for (Element element : episodes.select(".page-anime-all-episodes section:not(.ads) table > tbody > tr")) {
            final Elements td = element.getElementsByTag("td");
            List<String> langs = new ArrayList<>();
            if (td.size() < 5) {
                System.out.println(td);
                continue;
            }
            for (Element span : td.get(3).getElementsByTag("span")) {
                langs.addAll(span.classNames().stream().filter(n -> n.startsWith("flag-icon-")).map(s -> s.substring("flag-icon-".length())).collect(Collectors.toList()));
            }
            String viewLink = "";
            for (Element a : td.get(5).select("a")) {
                if (a.attr("href").contains("/view/")) {
                    viewLink = a.attr("href");
                    break;
                }
            }
            this.episodes.add(new Episode(td.get(0).text(), td.get(1).text(), langs, td.get(4).text(), viewLink));
        }
        this.loaded = true;
    }

    public List<Episode> getEpisodes() {
        if (!loaded) load();
        return episodes;
    }
}
//        tags=link.select(".info-top-table-highlight > tr:nth-child(1) > td:nth-child(2) > ul:nth-child(1) > li:nth-child(2)").text(); // To jest do pobierania pojedynczego tagu, czyli tak jak by sie przydalo aby robic ladne custom celle.
