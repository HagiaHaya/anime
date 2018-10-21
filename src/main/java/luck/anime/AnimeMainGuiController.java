package luck.anime;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import luck.anime.providers.*;
import luck.anime.providers.StreamProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.discovery.NativeDiscoveryStrategy;
import uk.co.caprica.vlcj.discovery.linux.DefaultLinuxNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.discovery.mac.DefaultMacNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.discovery.windows.DefaultWindowsNativeDiscoveryStrategy;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class AnimeMainGuiController implements Initializable {
    @FXML
    public ListView<String> firstLetterList;
    @FXML
    public ListView<Anime> seriesList;
    @FXML
    public ImageView animeCover;
    @FXML
    public Text animeTags;
    @FXML
    public Text animeDescription;
    @FXML
    public Text tagi;
    @FXML
    public Button prevStep;
    @FXML
    public Button nextStep;
    @FXML
    public TextField search;
    @FXML
    public TableView<Episode> episodesTable;
    @FXML
    public TableView<EpisodeStream> streamsTable;
    @FXML
    public Text abc;
    @FXML
    public Button connectedAs;

    private Executor executor = Executors.newSingleThreadExecutor();
    private ObservableList<Anime> animes = FXCollections.observableArrayList();
    private FilteredList<Anime> allAnimes = new FilteredList<>(animes, a -> true);
    private String currentLetter;

    private EpisodeStep episodeStep = EpisodeStep.CHOOSE_EPISODE;
    private static final Map<String, StreamProvider> hostStreamProviders = new HashMap<>();

    static {
        //hostStreamProviders.put("Cda", new CDAProvider());
        hostStreamProviders.put("Openload", new OpenloadProvider());
    }

    private Anime selectedAnime;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        seriesList.setItems(allAnimes);
        System.out.println("A");
        search.setPromptText("Wyszukaj");
        Document dom = null;
        try {
            dom = API.getPage("series");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert dom != null;
        final Elements elementsByClass = dom.getElementsByClass("letter-list");
        final List<String> letters = elementsByClass.get(0).getElementsByTag("a").stream().map(Element::text).collect(Collectors.toList());
        firstLetterList.getItems().addAll(letters);
        firstLetterList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<String>) c -> {
            while (c.next()) {
                for (String s : c.getAddedSubList()) {
                    this.currentLetter = s;
                    executor.execute(() -> {
                        AnimeMainGuiController.this.selectLetter(s);
                    });
                }
            }
        });
        seriesList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Anime>) c -> {
            while (c.next()) {
                for (Anime s : c.getAddedSubList()) {
                    this.selectedAnime = s;
                    if (s.getDescription() != null) {
                        animeDescription.setText(s.getDescription());
                    }
                    if (s.getTags() != null) {
                        animeTags.setText(s.getTags());
                    }
                    if (s.getImageUrl() != null) {
                        animeCover.setImage(new Image(Objects.requireNonNull(API.getHttpStream(s.getImageUrl()))));
                    }
                    if (s.getEpisodes() != null) {
                        episodesTable.getItems().clear();
                        episodesTable.getItems().addAll(s.getEpisodes());
                    }
                }
            }
            episodeStep = EpisodeStep.CHOOSE_EPISODE;
            tagi.setVisible(true);
            prevStep.setVisible(true);
            nextStep.setVisible(true);
            episodesTable.setVisible(true);
            nextStep.setDisable(true);
            prevStep.setDisable(true);
        });
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.length() == 0) {
                allAnimes.setPredicate(a -> true);
            } else {
                allAnimes.setPredicate(a -> a.getTitle().toLowerCase().contains(newValue.toLowerCase()));
            }
        });

        episodesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nextStep.setDisable(false);
            }
        });
        nextStep.setOnAction(event -> {
            switch (episodeStep) {
                case CHOOSE_EPISODE:
                    episodesTable.setVisible(false);
                    streamsTable.getItems().clear();
                    streamsTable.getItems().addAll(episodesTable.getSelectionModel().getSelectedItem().getStreams());
                    streamsTable.setVisible(true);
                    prevStep.setDisable(false);
                    nextStep.setDisable(true);
                    episodeStep = EpisodeStep.CHOOSE_STREAM;
                    break;
            }
        });
        prevStep.setOnAction(event -> {
            switch (episodeStep) {
                case CHOOSE_STREAM:
                    streamsTable.getItems().clear();
                    episodesTable.setVisible(true);
                    streamsTable.setVisible(false);
                    prevStep.setDisable(true);
                    nextStep.setDisable(false);
                    episodeStep = EpisodeStep.CHOOSE_EPISODE;
                    break;
            }
        });
    }
    public void riseAgainstShinden(MouseEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/account.fxml"));
        Stage stage = AnimeApplication.getInstance().getPrimaryStage();
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
    }

    public void streamClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            final EpisodeStream selectedItem = streamsTable.getSelectionModel().getSelectedItem();


            PlayerInfo playerInfo = getPlayerInfo(this.episodesTable.getSelectionModel().getSelectedItem(), selectedItem);
            final String url = Jsoup.parse(playerInfo.getEmbded()).select("iframe").get(0).attr("src");

            if (hostStreamProviders.containsKey(playerInfo.getPlayer_name())) {
                new NativeDiscovery(new DefaultLinuxNativeDiscoveryStrategy(),
                        new DefaultWindowsNativeDiscoveryStrategy(),
                        new DefaultMacNativeDiscoveryStrategy(),
                        new NativeDiscoveryStrategy() {
                            @Override
                            public boolean supported() {
                                return true;
                            }

                            @Override
                            public String discover() {
                                return new File("dll").getAbsolutePath();
                            }

                            @Override
                            public void onFound(String path) {
                            }
                        }).discover();
                final MediaInfo media = hostStreamProviders.get(playerInfo.getPlayer_name()).getMedia(url);
                //VideoGuiController.setUrlToPlay(media.getVideoURL());
                MediaPlayerManager.play(playerInfo, media);
/*                Platform.runLater(()->{
                    FXMLLoader loader = new FXMLLoader(VideoGuiController.class.getResource("/video.fxml"));
                    Stage stage = AnimeApplication.getInstance().getPrimaryStage();
                    Scene scene = null;
                    try {
                        scene = new Scene(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.setScene(scene);
                });*/
            } else {
                System.out.println("Player not found => " + selectedItem.getPlayer());
            }
            System.out.println(selectedItem.getSource());
        }
    }

    private PlayerInfo getPlayerInfo(Episode episode, EpisodeStream selectedItem) {
        String page = API.getHttpContent("https://shinden.pl/" + episode.getViewLink(), true);
        String basic = null, service = null;
        for (String s : page.split("\n")) {
            s = s.trim();
            if (s.contains("_Storage.basic")) {
                basic = s.substring(s.indexOf("_Storage.basic") + "_Storage.basic =  '".length(), s.indexOf("';"));
            }
            if (s.contains("_Storage.XHRService")) {
                service = s.substring(s.indexOf("_Storage.XHRService") + "_Storage.XHRService = '".length(), s.indexOf("';"));
            }
        }
        long endTime = System.currentTimeMillis() + Integer.parseInt(API.xhr("https:" + service + "/" + selectedItem.getOnline_id() + "/player_load?auth=" + basic).replace("\n", "").trim()) * 1000;
        while (System.currentTimeMillis() < endTime) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        final Document parse = Jsoup.parse(API.xhr("https:" + service + "/" + selectedItem.getOnline_id() + "/player_show?auth=" + basic + "&width=765"));
        final String value = parse.select("input[name=json]").get(0).attr("value");
        final PlayerInfo playerInfo = API.getGson().fromJson(value, PlayerInfo.class);
        return playerInfo;
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (prevStep.isArmed()) {
        }
    }

    @FXML
    private void handleYourMother(MouseEvent event) {
        if (event.getClickCount() >= 1) {
            episodesTable.setVisible(true);
            streamsTable.setVisible(false);
            prevStep.setVisible(true);
            nextStep.setVisible(true);
            abc.setText("Zalogowano jako:");
            try {
                connectedAs.setText(API.getDom("https://shinden.pl/").getElementsByClass("notif-total-notif").text());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectLetter(String letter) {
        Platform.runLater(() -> animes.clear());
        Document dom = null;
        int page = 1;
        boolean nextPage = false;
        do {
            if (!currentLetter.equalsIgnoreCase(letter)) {
                return;
            }
            int tries = 0;
            while (tries < 5) {
                try {
                    dom = API.getPage("series?sort_by=desc&sort_order=asc&letter=" + letter + "&page=" + page);
                    break;
                } catch (Exception e) {
                    System.out.println("error at try #" + (tries + 1));
                    e.printStackTrace();
                }
                tries++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nextPage = dom.getElementsByClass("pagination-next").get(0).getElementsByTag("a").size() > 0;
            final ArrayList<Anime> currentPageAnime = new ArrayList<>();
            for (Element row : dom.getElementsByClass("title-table").get(0).getElementsByTag("article").get(0).getElementsByClass("div-row")) {
                final String title = row.getElementsByClass("desc-col").get(0).select("h3 a").text();
                currentPageAnime.add(new Anime(title, row.getElementsByClass("desc-col").get(0).getElementsByTag("h3").get(0).getElementsByTag("a").attr("href")));
            }
//            System.out.println("loaded page " + page);
            Platform.runLater(() -> animes.addAll(currentPageAnime));
            page++;
        } while (nextPage);
    }

    public void downloadAll(MouseEvent mouseEvent) {
        System.out.println("Downloading all episodes");
        if(this.selectedAnime == null){
            return;
        }
        System.out.println(this.selectedAnime);
        for (int i = this.selectedAnime.getEpisodes().size() - 1; i >= 0; i--) {
            Episode episode = this.selectedAnime.getEpisodes().get(i);
            System.out.println("checking episode "  + episode.getTitle() + " " + episode.getId());
            for (EpisodeStream episodeStream : episode.getStreams().stream()
                    .sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getMax_res().replace("p", "")))).collect(Collectors.toList())) {
                final PlayerInfo playerInfo = getPlayerInfo(episode, episodeStream);
                System.out.println("trying " + playerInfo.getPlayer_name() + " for downloading " + playerInfo.getTitle() + " #" + playerInfo.getEpisodeNo());
                if(!hostStreamProviders.containsKey(playerInfo.getPlayer_name())){
                    continue;
                }
                final File file = new File("anime/" + playerInfo.getTitle().replace(" ", "_") + "/");
                file.mkdirs();
                final String[] split = playerInfo.getSource().split("/");
                final String url = Jsoup.parse(playerInfo.getEmbded()).select("iframe").get(0).attr("src");

                hostStreamProviders.get(playerInfo.getPlayer_name()).download(url, new File(file, playerInfo.getEpisodeNo() + ".mp4"));
                break;
            }
        }
    }
}

    /*
    TO DO:
     */
