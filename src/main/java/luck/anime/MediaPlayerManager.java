package luck.anime;

import com.sun.deploy.panel.ControlPanel;
import com.sun.jna.platform.WindowUtils;
import luck.anime.providers.MediaInfo;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;

import static java.lang.System.exit;
import static java.lang.System.setOut;

public class MediaPlayerManager {
    private static JFrame frame;
    private static EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private static JSlider timeSlider;
    private static JButton pauseButton;
    private static JButton rewindButton;
    private static JButton skipButton;
    private static long lastUpdate = 0;
    private static long videoLength;
    private static ControlsPanel controlsPanel;

    public static void play(PlayerInfo playerInfo, MediaInfo media) {
        SwingUtilities.invokeLater(() -> {
            if (frame == null) {
                frame = new JFrame();
                frame.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                            mediaPlayerComponent.getMediaPlayer().skip(10000);
                        }else if(e.getKeyCode() == KeyEvent.VK_LEFT){
                            mediaPlayerComponent.getMediaPlayer().skip(-10000);
                        }
                    }
                });
                frame.setBounds(100, 100, 600, 400);
                mediaPlayerComponent = new EmbeddedMediaPlayerComponent(){

                    @Override
                    protected Window onGetOverlay() {
                        return (controlsPanel = new ControlsPanel(frame));
                    }
                };
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        mediaPlayerComponent.release();
                        frame = null;
                    }
                });
                mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {


                    @Override
                    public void finished(MediaPlayer mediaPlayer) {
                        exit(0);
                    }

                    @Override
                    public void error(MediaPlayer mediaPlayer) {
                        exit(1);
                    }

                    @Override
                    public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
                        super.mediaChanged(mediaPlayer, media, mrl);
                    }

                    @Override
                    public void playing(MediaPlayer mediaPlayer) {
                        videoLength = mediaPlayer.getMediaMeta().getLength();
                        timeSlider.setMaximum((int) mediaPlayer.getMediaMeta().getLength());
                    }

                    @Override
                    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                        if (Math.abs(newTime - lastUpdate) > 1000) {
                            lastUpdate = newTime;
                        }
                    }
                });

                mediaPlayerComponent.setSize(600, 400); // Size is needed here, as there is no layout in lp
                frame.setContentPane(mediaPlayerComponent);




                /*JPanel controlsPane = new JPanel();
                pauseButton = new JButton("Pause");
                controlsPane.add(pauseButton);
                rewindButton = new JButton("Rewind");
                controlsPane.add(rewindButton);
                skipButton = new JButton("Skip");
                controlsPane.add(skipButton);
                timeSlider = new JSlider(JSlider.HORIZONTAL,
                        0, 1, 0);
                timeSlider.addChangeListener(e -> mediaPlayerComponent.getMediaPlayer().setTime(timeSlider.getValue()));
                controlsPane.add(timeSlider);


                pauseButton.addActionListener(e -> mediaPlayerComponent.getMediaPlayer().pause());

                rewindButton.addActionListener(e -> mediaPlayerComponent.getMediaPlayer().skip(-10000));

                skipButton.addActionListener(e -> mediaPlayerComponent.getMediaPlayer().skip(10000));

                contentPane.add(controlsPane, BorderLayout.SOUTH);

                frame.setContentPane(contentPane);*/
                frame.setVisible(true);
                mediaPlayerComponent.getMediaPlayer().enableOverlay(true);
            }
            frame.setTitle("Odtwarzanie: " + playerInfo.getTitle() + " #" + playerInfo.getEpisodeNo());

            mediaPlayerComponent.getMediaPlayer().playMedia(media.getVideoURL());
            //mediaPlayerComponent.getMediaPlayer().playMedia("https://oload.cloud/stream/kuk_A6jsano~1538826792~149.156.0.0~8HeF5NGY?mime=true");
        });
    }

    public static void main(String[] args) {
        play(null, null);
    }

    public static long getLastUpdate() {
        return lastUpdate;
    }

    public static long getVideoLength() {
        return videoLength;
    }
}
