package luck.anime;

import com.sun.jna.platform.WindowUtils;

import javax.swing.*;
import java.awt.*;

public class ControlsPanel extends Window {
    public ControlsPanel(JFrame frame) {
        super(frame, WindowUtils.getAlphaCompatibleGraphicsConfiguration());
        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final int width = getWidth();
        final int height = getHeight();
        g2.setColor(Color.WHITE);
        int w = (int) ((width-20)*(((float)MediaPlayerManager.getLastUpdate())/MediaPlayerManager.getVideoLength()));
        g2.fillRect(10+w, 10, width-w-20, 10);
        g2.setColor(Color.BLUE);
        g2.fillRect(10, 10, w, 10);
        repaint();
    }
}
