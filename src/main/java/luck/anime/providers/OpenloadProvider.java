package luck.anime.providers;

import luck.anime.API;
import luck.anime.DownloadUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenloadProvider implements StreamProvider {
    private final String BASE_URL = "https://oload.cloud/";

//    public static void main(String[] args) {
//
//        String s = "bca3e3b13ee02ec366c018877aba8ba55fd23a2dc7bd20d9d71dd63f0981d517937add057047454952027f4e495c35487b5a5c7d015c62535a27545c58430f615b594951024164567541026a5c56525c01616466787d02407a534056027e7a6d577f01";
//
//
//
//    }

    @Override
    public MediaInfo getMedia(String iframeUrl) {
        try {
            System.out.println("iframe url: " + iframeUrl);
            final Document dom = API.getDom(iframeUrl);
            final String httpContent = dom.html();

            System.out.println("thumb: " + Jsoup.parse(httpContent).getElementsByTag("meta").attr("content"));
            final Pattern compile = Pattern.compile("_0x30725e,\\(parseInt\\('([0-9]+)',8\\)-([0-9]+)\\+0x([0-9|a-f]+)-([0-9]+)\\)\\/\\(([0-9]+)-0x([0-9|a-f]+)\\)\\)", Pattern.MULTILINE);
            long key = 0;
            final Matcher matcher = compile.matcher(httpContent);
            if (matcher.find()) {
                //(parseInt(g1,8)-g2+0xg3-g4)/(g5-0xg6))
                key = Long.parseLong(matcher.group(1), 8);
                key -= Long.parseLong(matcher.group(2), 10);
                key += Long.parseLong(matcher.group(3), 16);
                key -= Long.parseLong(matcher.group(4), 10);
                key = key / (Long.parseLong(matcher.group(5), 10) - Long.parseLong(matcher.group(6), 16));
            }
            final Pattern compile2 = Pattern.compile("_1x4bfb36=parseInt\\('([0-7]+)',8\\)-([0-9]+)", Pattern.MULTILINE);
            long key2 = 0;
            final Matcher matcher2 = compile2.matcher(httpContent);
            if (matcher2.find()) {
                key2 = Long.parseLong(matcher2.group(1), 8);
                key2 -= Long.parseLong(matcher2.group(2), 10);
            }
            String token = genToken(
                    dom.getElementById("DtsBlkVFQx").parent().select("p").get(0).text(),
                    key, key2
            );
            return new MediaInfo(BASE_URL + "stream/" + token + "?mime=true");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //'/stream/' + $('#DtsBlkVFQx').text() + '?mime=true';
        //302 redirect
        //"https://thumb.oloadcdn.net/splash/I6_Iv7vnC2U/2slW5Il9sy0.jpg"
        // testowy link = https://api.openload.co/1/file/dlticket?file=BtZMKqOvIBM&login=5f0f7b8d1d7cb134&key=izJf9kYq
        // potem trzeba https://api.openload.co/1/file/dl?file=BtZMKqOvIBM&ticket={ticket}


        return null;
    }

    @Override
    public void download(String url, File file) {

        final MediaInfo media = getMedia(url);
        DownloadUtils.shedule(media.getVideoURL(), file);
    }

    private String genToken(String input, long key, long key2) {
        System.out.println("calculating token for " + input + ", and key " + key + " and key 2 " + key2);
        StringBuilder output = new StringBuilder();
        int _0x41e0ff = 72;
        String _0x439a49a = input.substring(0x0, _0x41e0ff);
        List<Long> ke = new ArrayList<>();
        for (int i = 0x0; i < _0x439a49a.length(); i += 0x8) {
            _0x41e0ff = i * 8;
            String _0x40b427 = _0x439a49a.substring(i, i + 8);
            long _0x577716 = Long.parseLong(_0x40b427, 0x10);
            ke.add(_0x577716);
        }
        List<Long> _0x3d7b02 = new ArrayList<>(ke);
        _0x41e0ff = 72;
        input = input.substring(_0x41e0ff);
        int _0x439a49 = 0x0;
        int _0x145894 = 0x0;
        while (_0x439a49 < input.length()) {
            long _0x5eb93a = 0x40;
            long _0x37c346 = 0x7f;
            long _0x896767 = 0x0;
            long _0x1a873b = 0x0;
            long _0x3d9c8e = 0x0;
            do {
                if (_0x439a49 + 1 >= input.length()) {
                    _0x5eb93a = 0x8f;
                }
                String _0x1fa71e = input.substring(_0x439a49, _0x439a49 + 2);
                _0x439a49 += 2;
                _0x3d9c8e = Long.parseLong(_0x1fa71e, 0x10);
                if (_0x1a873b < 30) {
                    long _0x332549 = _0x3d9c8e & 0x3f;
                    _0x896767 += _0x332549 << _0x1a873b;
                } else {
                    long _0x332549 = _0x3d9c8e & 0x3f;
                    _0x896767 += _0x332549 * Math.pow(0x2, _0x1a873b);
                }
                _0x1a873b += 0x6;

            } while (_0x3d9c8e >= _0x5eb93a);
            long _0x30725e = (_0x896767 ^ _0x3d7b02.get(_0x145894 % 0x9));
            _0x30725e = (_0x30725e ^ key) ^ key2;
            long _0x2de433 = _0x5eb93a * 0x2 + _0x37c346;
            for (int i = 0x0; i < 4; i++) {
                long _0x1a9381 = _0x30725e & _0x2de433;
                long _0x1a0e90 = _0x41e0ff / 9 * i;
                _0x1a9381 = _0x1a9381 >> _0x1a0e90;
                char _0x3fa834 = (char) (_0x1a9381 - 1);
                if (_0x3fa834 != '$') output.append(String.valueOf(_0x3fa834));
                _0x2de433 = _0x2de433 << _0x41e0ff / 0x9;
            }
            _0x145894 += 0x1;

        }
        System.out.println("token: " + output.toString());
        return output.toString();
    }
}