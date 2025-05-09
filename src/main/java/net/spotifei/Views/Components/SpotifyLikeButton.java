package net.spotifei.Views.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SpotifyLikeButton extends JButton {

    private final Color textColor = new Color(179, 179, 179);
    private final Color backgroundColor = Color.BLACK;
    private final Color hoverTextColor = Color.white;
    private final int fontSize;

    public SpotifyLikeButton(String text, int fontSize) {
        super("<html><span style='font-size: " + fontSize + "px;'>" + text + "</span></html>");
        this.setFocusPainted(false);
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.setBackground(backgroundColor);
        this.setForeground(textColor);
        this.setContentAreaFilled(false);
        this.addMouseListener(getMouseListener());
        this.fontSize = fontSize;
    }

    @Override
    public void setText(String text) {
        super.setText("<html><span style='font-size: " + this.fontSize + "px;'>" + text + "</span></html>");
        this.revalidate();
        this.repaint();
    }

    public void setText(String text, int fontSize) {
        super.setText("<html><span style='font-size: " + fontSize + "px;'>" + text + "</span></html>");
        this.revalidate();
        this.repaint();
    }

    private MouseAdapter getMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setForeground(hoverTextColor);
//                setText("<html><span style='font-size: " + (getFont().getSize() + 10) + "px;'>" + getText() + "</span></html>");
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

