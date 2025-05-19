package net.spotifei.Views.Components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class SpotifyLikeScrollBarUI extends BasicScrollBarUI {
    private final Color trackColor = Color.decode("#2a2a2a"); // Cor do "trilho" da barra
    private final Color thumbColor = Color.decode("#555555"); // Cor do "polegar" (a parte que você arrasta)
    private final Color thumbHighlightColor = Color.decode("#777777"); // Cor do polegar ao passar o mouse (opcional)

    private JButton emptybutton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        button.setVisible(false);
        return button;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return emptybutton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return emptybutton();
    }

    // barra que se mexe
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(thumbColor);
        if (isDragging) {
            g2d.setColor(thumbHighlightColor);
        }
        g2d.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width - 5, thumbBounds.height - 1, 5, 5);
    }

    // "background da barra que se mexe"
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(trackColor);
        g2d.fillRoundRect(trackBounds.x -1, trackBounds.y, trackBounds.width - 3, trackBounds.height - 1, 5, 5);
    }
}
