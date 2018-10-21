package luck.anime;

public class EpisodeStream {
    private String online_id;
    private String player;
    private String added;
    private String max_res;
    private String lang_subs;
    private String lang_audio;
    private String source;


    public String getPlayer() {
        return player;
    }
    public String getMax_res() {
        return max_res;
    }
    public String getLang_audio() {
        return lang_audio;
    }
    public String getLang_subs() {
        return lang_subs;
    }
    public String getAdded() {
        return added;
    }
    public String getSource() {
        return source;
    }

    public String getOnline_id() {
        return online_id;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("EpisodeStream{");
        sb.append("player='").append(player).append('\'');
        sb.append(", added='").append(added).append('\'');
        sb.append(", max_res='").append(max_res).append('\'');
        sb.append(", lang_subs='").append(lang_subs).append('\'');
        sb.append(", lang_audio='").append(lang_audio).append('\'');
        sb.append(", source='").append(source).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
