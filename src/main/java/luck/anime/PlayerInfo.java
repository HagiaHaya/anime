package luck.anime;

//{
//   "player_name":"Cda",
//   "audio_lang":"pl",
//   "sub_lang":null,
//   "max_res":"480p",
//   "subtitle_author":null,
//   "add_date":"2018-07-23T15:57:51.000Z",
//   "source":"cda.pl",
//   "title":{
//      "title":"Darling in the FranXX"
//   },
//   "episode":{
//      "episode_no":24,
//      "episode_time":null
//   },
//   "embded":"<iframe src=\"http://ebd.cda.pl/800x450/24347450b\" width=\"800\" height=\"450\" style=\"border:none;\" scrolling=\"no\" allowfullscreen name=\"v2\"></iframe>"
//}
public class PlayerInfo {
    private String player_name;
    private String audio_lang;
    private String sub_lang;
    private String max_res;
    private String subtitle_author;
    private String add_date;
    private String source;
    private TitlePlayerInfo title;
    private EpisodePlayerInfo episode;
    private String embded;

    private class TitlePlayerInfo {
        private String title;
    }

    private class EpisodePlayerInfo {
        private int episode_no;
        private String episode_time;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public String getAudio_lang() {
        return audio_lang;
    }

    public String getSub_lang() {
        return sub_lang;
    }

    public String getMax_res() {
        return max_res;
    }

    public String getSubtitle_author() {
        return subtitle_author;
    }

    public String getAdd_date() {
        return add_date;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title.title;
    }

    public int getEpisodeNo() {
        return episode.episode_no;
    }

    public String getEpisodeTime() {
        return episode.episode_time;
    }

    public String getEmbded() {
        return embded;
    }
}
