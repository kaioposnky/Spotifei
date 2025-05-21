package net.spotifei.Views.Components;

//imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SpotifyLikeButton extends JButton {

    private final Color textColor = new Color(179, 179, 179);
    private final Color backgroundColor = Color.BLACK;
    private final Color hoverTextColor = Color.white;
    private final int fontSize;

    /**
     * Construtor do `SpotifyLikeButton`.
     *
     * @param text O texto a ser exibido no botão.
     * @param fontSize O tamanho da fonte para o texto do botão.
     */
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

    /**
     * Sobrescreve o método `setText` para aplicar o tamanho da fonte.
     *
     * @param text O novo texto para o botão.
     */
    @Override
    public void setText(String text) {
        super.setText("<html><span style='font-size: " + this.fontSize + "px;'>" + text + "</span></html>");
        this.revalidate();
        this.repaint();
    }

    /**
     * Sobrecarga do método `setText` que permite alterar o texto e o tamanho da fonte.
     *
     * @param text O novo texto para o botão.
     * @param fontSize O novo tamanho da fonte para o texto do botão.
     */
    public void setText(String text, int fontSize) {
        super.setText("<html><span style='font-size: " + fontSize + "px;'>" + text + "</span></html>");
        this.revalidate();
        this.repaint();
    }

    /**
     * Cria e retorna um `MouseAdapter` que implementa o efeito de hover.
     *
     * @return Um `MouseAdapter` para lidar com eventos de mouse.
     */
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

