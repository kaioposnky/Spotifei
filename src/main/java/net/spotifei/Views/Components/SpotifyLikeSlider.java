package net.spotifei.Views.Components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class SpotifyLikeSlider extends JSlider {

    public SpotifyLikeSlider(int min, int max, int value) {
        super(min, max, value);
        this.setPreferredSize(new Dimension(100, 15));
        this.setOpaque(false);
        this.setFocusable(false);
        this.setUI(new SpotifyLikeSliderUI(this));
    }

    public SpotifyLikeSlider(int min, int max, int value, int width, int height) {
        super(min, max, value);
        this.setPreferredSize(new Dimension(width, height));
        this.setOpaque(false);
        this.setFocusable(false);
        this.setUI(new SpotifyLikeSliderUI(this));
    }

    private static class SpotifyLikeSliderUI extends BasicSliderUI {

        private final Color trackColor = new Color(77, 77, 77);
        private final Color filledColor = Color.white;
        private final Color hoveredFilledColor = new Color(30, 215, 96);
        private boolean isMouseHovering = false;

        public SpotifyLikeSliderUI(JSlider b) {
            super(b);
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);

            MouseAdapter hoverListener = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isMouseHovering = true;
                    slider.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isMouseHovering = false;
                    if (!slider.getValueIsAdjusting()) {
                        slider.repaint();
                    }
                }
            };
            slider.addMouseListener(hoverListener);
        }

        @Override // socorro onde eu fui me meter
        public void paintTrack(Graphics g) { // repintar a linha do slider
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

            int trackY = trackRect.y + (trackRect.height / 2) - 4; // centraliza a posição y

            // pintar a linha de background
            g2d.setColor(trackColor);
            // essa classe faz a mágica de criar uma barra com as bordas circulares, melhor nao saber como
            RoundRectangle2D slideBackground = new RoundRectangle2D.Float(
                    trackRect.x, trackY, trackRect.width, 5, 4, 4);
            g2d.fill(slideBackground);

            // pega o valor do slider preenchido e subtrai pela coordenada X
            int amountFilled = xPositionForValue(slider.getValue()) - trackRect.x;

            // pintar a linha preenchida
            g2d.setColor(isMouseHovering || slider.getValueIsAdjusting()? hoveredFilledColor : filledColor);
            RoundRectangle2D slideFilled = new RoundRectangle2D.Float(
                    trackRect.x, trackY, amountFilled, 5, 4, 4);
            g2d.fill(slideFilled);
        }

        @Override
        public void paintThumb(Graphics g){ // repintar o "thumb" do slider
            if (!isMouseHovering && !slider.getValueIsAdjusting()) return;

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

            int ovalX = thumbRect.x + (thumbRect.width - 10) / 2;
            int ovalY = thumbRect.y + 1 + (thumbRect.height - 10) / 2;

            g2d.setColor(Color.WHITE);
            // NÃO AUMENTA PARA MAIS DE 10 ERROS DE VAZAMENTO DE PIXEL VÃO OCORRER
            g2d.fillOval(ovalX, ovalY - 3, 10, 10);
        }
    }
}
