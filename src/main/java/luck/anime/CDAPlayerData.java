package luck.anime;
//{
//   "id":"mediaplayer24347450b",
//   "ads":{
//      "schedule":[
//         {
//            "enabled":true,
//            "counter":false,
//            "skip":true,
//            "click":true,
//            "key":"",
//            "tag":"https:\/\/www.cda.pl\/xml.php?type=g_embed&get=pool&safe=0&requestUrl=https%3A%2F%2Febd.cda.pl%2F800x450%2F24347450b&ts=1537696517",
//            "tagAdblock":"https:\/\/www.cda.pl\/xml.php?type=g_ad&get=pool&safe=0&requestUrl=https%3A%2F%2Febd.cda.pl%2F800x450%2F24347450b&ts=1537696517",
//            "repeat":1,
//            "time":0,
//            "type":"pool",
//            "displayAs":"prerol",
//            "safe":false
//         }
//      ]
//   },
//   "video":{
//      "id":"24347450b",
//      "file":"http:\/\/vwaw080.cda.pl\/znWhHHbE-WATvaYpWEhbPg\/1537739717\/lqd3ae19d7e8847a4d12d621944cec5b94.mp4",
//      "file_cast":null,
//      "cast_available":true,
//      "manifest":null,
//      "duration":"1440",
//      "durationFull":"00:24:00",
//      "poster":"\/\/static.cda.pl\/v001\/img\/mobile\/poster16x9.png",
//      "type":"plain",
//      "width":1280,
//      "height":720,
//      "content_rating":null,
//      "quality":"lq",
//      "ts":1537696517,
//      "hash":"f5d6e8fcde5e88df888a3d384f4f0c2374117c35",
//      "title":"DARLING%20IN%20THE%20FRANXX%20Odcinek%2024%20Lektor%20pl",
//      "thumb":"\/\/icdn.2cda.pl\/vid\/thumbs\/d3ae19d7e8847a4d12d621944cec5b94-51.jpg_ooooxooooo_1280x720.jpg?v1"
//   },
//   "nextVideo":null,
//   "autoplay":false,
//   "seekTo":0,
//   "premium":false,
//   "api":{
//      "client":"json_client",
//      "ts":"1537696517_48122",
//      "key":"19bd42e43b97a2140d0f0ac071aebc9955bf1da1",
//      "method":""
//   },
//   "user":{
//      "role":"guest"
//   }
//}
public class CDAPlayerData {
    private CDAVideoInfo video;

    public CDAVideoInfo getVideo() {
        return video;
    }

    public static class CDAVideoInfo {
        private String id;
        private String file;
        private String duration;
        private String durationFull;
        private String poster;
        private String quality;
        private String title;
        private String thumb;
        private int width;
        private int height;

        public String getId() {
            return id;
        }

        public String getFile() {
            return file;
        }

        public String getDuration() {
            return duration;
        }

        public String getDurationFull() {
            return durationFull;
        }

        public String getPoster() {
            return poster;
        }

        public String getQuality() {
            return quality;
        }

        public String getTitle() {
            return title;
        }

        public String getThumb() {
            return thumb;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
