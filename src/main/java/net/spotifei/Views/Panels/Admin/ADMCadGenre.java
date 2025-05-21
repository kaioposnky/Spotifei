package net.spotifei.Views.Panels.Admin;

//imports
import net.spotifei.Controller.GenreController;
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import java.awt.*;

public class ADMCadGenre extends JPanel {
    private final MainFrame mainframe;
    private final AppContext appContext;
    private final GenreController genreController;
    private JTextField genreTextField;

    /**
     * Construtor da classe `ADMCadGenre`.
     *
     * @param mainframe A instância da janela principal.
     * @param appContext O contexto da aplicação.
     */
    public ADMCadGenre(MainFrame mainframe, AppContext appContext) {
        this.mainframe = mainframe;
        this.appContext = appContext;
        this.genreController = appContext.getGenreController(this, mainframe);
        initComponents();
    }

    /**
     * Método privado para inicializar e configurar os componentes visuais do painel.
     */
    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Cadastre um novo gênero");
        title.setFont(new java.awt.Font("Segoe UI Black", 1, 36));
        title.setAlignmentX(CENTER_ALIGNMENT);

        JPanel genreCreationPanel = new JPanel();
        genreCreationPanel.setLayout(new BoxLayout(genreCreationPanel, BoxLayout.X_AXIS));
        genreCreationPanel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel genreNameLabel = new JLabel("Nome do gênero:");
        genreNameLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        genreNameLabel.setAlignmentX(CENTER_ALIGNMENT);

        genreTextField = new JTextField();
        genreTextField.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        genreTextField.setPreferredSize(new Dimension(200, 30));
        genreTextField.setMaximumSize(new Dimension(250, 30));

        genreCreationPanel.add(genreNameLabel);
        genreCreationPanel.add(genreTextField);

        JButton bt_cadastrar = new JButton("Cadastrar");
        bt_cadastrar.setFont(new java.awt.Font("Segoe UI Black", 1, 18));
        bt_cadastrar.setBackground(new Color(0, 109, 170));
        bt_cadastrar.setForeground(new Color(0,0,0));
        bt_cadastrar.setAlignmentX(CENTER_ALIGNMENT);
        bt_cadastrar.addActionListener(event -> handleGenreCreate());

        JButton bt_voltar = new JButton("Voltar");
        bt_voltar.setBackground(new java.awt.Color(0, 0, 0));
        bt_voltar.setFont(new java.awt.Font("Segoe UI", 1, 18));
        bt_voltar.setForeground(new java.awt.Color(250, 250, 250));
        bt_voltar.setAlignmentX(CENTER_ALIGNMENT);
        bt_voltar.addActionListener(event -> handleGoBack());

        add(title);
        add(Box.createVerticalStrut(200));
        add(genreCreationPanel);
        add(Box.createVerticalStrut(20));
        add(bt_cadastrar);
        add(Box.createVerticalStrut(200));
        add(bt_voltar);
    }

    /**
     * Método acionado pelo botão "Cadastrar".
     */
    private void handleGenreCreate(){
        genreController.createGenre();
    }

    /**
     * Método acionado pelo botão "Voltar".
     */
    private void handleGoBack(){
        mainframe.setPanel(MainFrame.ADMHOME_PANEL);
    }

    /**
     * Retorna a instância do `JTextField` usado para inserir o nome do gênero.
     *
     * @return O `JTextField` do nome do gênero.
     */
    public JTextField getGenreTextField() {
        return genreTextField;
    }
}
