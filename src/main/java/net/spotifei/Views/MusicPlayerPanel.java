package net.spotifei.Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import static net.spotifei.Infrastructure.Logger.LoggerRepository.logInfo;

public class MusicPlayerPanel extends JPanel {

    public MusicPlayerPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 20, 15, 10));
        setBackground(Color.black);

        add(createLeftPanel(), BorderLayout.WEST);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createRightPanel(), BorderLayout.EAST);
    }

    private JPanel createLeftPanel(){
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 20));
        leftPanel.setOpaque(false);

        JPanel songInfoPanel = new JPanel();
        songInfoPanel.setOpaque(false);
        songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));

        JLabel musicTitle = new JLabel("Music Title");
        musicTitle.setFont(new Font("Arial", Font.BOLD, 14));
        musicTitle.setForeground(Color.WHITE);

        JLabel musicArtist = new JLabel("Music Artist");
        musicArtist.setFont(new Font("Arial", Font.PLAIN, 12));
        musicArtist.setForeground(Color.GRAY);

        songInfoPanel.add(musicTitle);
        songInfoPanel.add(musicArtist);

        leftPanel.add(songInfoPanel, BorderLayout.WEST);

        return leftPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());


        JPanel musicProgressPanel = new JPanel();
        musicProgressPanel.setOpaque(false);

        MusicSlider musicProgress = new MusicSlider(0, 100, 20, 400, 20);

        JLabel musicTimeNow = new JLabel("0:00");
        musicTimeNow.setFont(new Font("Arial", Font.PLAIN, 12));
        musicTimeNow.setForeground(Color.GRAY);

        JLabel musicTimeTotal = new JLabel("3:20");
        musicTimeTotal.setFont(new Font("Arial", Font.PLAIN, 12));
        musicTimeTotal.setForeground(Color.GRAY);

        musicProgressPanel.add(musicTimeNow, BorderLayout.WEST);
        musicProgressPanel.add(musicProgress, BorderLayout.CENTER);
        musicProgressPanel.add(musicTimeTotal, BorderLayout.EAST);

        JPanel musicControlPanel = new JPanel();
        musicControlPanel.setOpaque(false);
        musicControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        MusicControlButton btnSkip = new MusicControlButton("<html>&#x23ED;</html>", 16);

        MusicControlButton btnPause = new MusicControlButton("<html>&#x25B6;</html>", 20);

        MusicControlButton btnPrevious = new MusicControlButton("<html>&#x23EE;</html>", 16);

        musicControlPanel.add(btnPrevious, BorderLayout.WEST);
        musicControlPanel.add(btnPause, BorderLayout.CENTER);
        musicControlPanel.add(btnSkip, BorderLayout.EAST);

        centerPanel.add(musicControlPanel, BorderLayout.NORTH);
        centerPanel.add(musicProgressPanel, BorderLayout.SOUTH);

        return centerPanel;
    }

    private JPanel createRightPanel(){
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));

        JPanel muteButtonWrapper = new JPanel();
        muteButtonWrapper.setOpaque(false);
        muteButtonWrapper.setPreferredSize(new Dimension(25, 25));
        muteButtonWrapper.setMinimumSize(new Dimension(25, 25));
        muteButtonWrapper.setMaximumSize(new Dimension(25, 25));
        muteButtonWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        MusicControlButton btnMute = new MusicControlButton("<html> &#x1F50A </html>", 17);
        btnMute.setFocusPainted(false);
        btnMute.setBorder(new EmptyBorder(-7,8,0,0));

        muteButtonWrapper.add(btnMute);

        SpotifyLikeSlider spotifyLikeSlider = new SpotifyLikeSlider(0, 100, 15);

        rightPanel.add(muteButtonWrapper);
        rightPanel.add(spotifyLikeSlider);

        return rightPanel;
    }

    private class MusicControlButton extends JButton {

        private final Color textColor = new Color(179, 179, 179);
        private final Color backgroundColor = Color.BLACK;
        private final Color hoverTextColor = Color.white;

        public MusicControlButton(String text, int fontSize) {
            super("<html><span style='font-size: " + fontSize + "px;'>" + text + "</span></html>");
            this.setFocusPainted(false);
            this.setOpaque(false);
            this.setBorderPainted(false);
            this.setBackground(backgroundColor);
            this.setForeground(textColor);
            this.setContentAreaFilled(false);
            this.addMouseListener(getMouseListener());
        }

        private MouseAdapter getMouseListener() {
            return new MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setForeground(hoverTextColor);
                    setText("<html><span style='font-size: " + (getFont().getSize() + 10) + "px;'>" + getText() + "</span></html>");
                    super.mouseEntered(evt);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setForeground(textColor);
                    super.mouseExited(e);
                }
            };
        }
    }

    private class SpotifyLikeSlider extends JSlider {

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

    private class MusicSlider extends SpotifyLikeSlider {

        public MusicSlider(int min, int max, int value) {
            super(min, max, value);
            addMouseListener(getSeekMouseListener());
        }

        public MusicSlider(int min, int max, int value, int width, int height) {
            super(min, max, value, width, height);
            addMouseListener(getSeekMouseListener());
        }

        private MouseAdapter getSeekMouseListener() {
            return new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    // adicionar logica do seek do audioplayer
                }
            };
        }
    }


}
