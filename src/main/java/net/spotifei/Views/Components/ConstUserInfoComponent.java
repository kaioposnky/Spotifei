
package net.spotifei.Views.Components;

//import
import net.spotifei.Infrastructure.Container.AppContext;
import net.spotifei.Models.User;
import net.spotifei.Views.MainFrame;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ConstUserInfoComponent extends JPanel {
    private final User user;
    private final AppContext appContext;
    private final MainFrame mainframe;

    /**
     * Construtor do componente `ConstUserInfoComponent`.
     * Recebe as informações do usuário, o contexto da aplicação e a referência ao MainFrame.
     *
     * @param user O objeto `User` contendo os dados a serem exibidos.
     * @param appContext O contexto da aplicação.
     * @param mainframe A instância do `MainFrame`.
     */
    public ConstUserInfoComponent(User user, AppContext appContext,
                                  MainFrame mainframe){
        this.user = user;
        this.appContext = appContext;
        this.mainframe = mainframe;
        initComponents();
    }

    /**
     * Método privado para inicializar e configurar os componentes visuais do painel.
     */
    private void initComponents(){
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Color.decode("#121212"));
        setOpaque(true);

        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.X_AXIS));
        boxPanel.setOpaque(false);

        Border borderLine = new MatteBorder(0, 0, 1, 0, new Color(50, 50, 50));
        Border borderInside = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        boxPanel.setBorder(BorderFactory.createCompoundBorder(borderLine, borderInside));

        boxPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        boxPanel.add(getConstUserInfoPanel(), BorderLayout.WEST);
        boxPanel.add(Box.createHorizontalGlue());
        boxPanel.add(Box.createHorizontalStrut(20));

        setAlignmentY(Component.CENTER_ALIGNMENT);
        this.add(boxPanel);
    }

    /**
     * Método privado que constrói o painel contendo as Labels com as informações do usuário.
     *
     * @return Um `JPanel` com as informações do usuário formatadas.
     */
    private JPanel getConstUserInfoPanel() {
        JPanel constUserInfoPanel = new JPanel();
        constUserInfoPanel.setLayout(new BoxLayout(constUserInfoPanel, BoxLayout.X_AXIS));
        constUserInfoPanel.setOpaque(false);

        JPanel infoWrapperPanel = new JPanel();
        infoWrapperPanel.setLayout(new BoxLayout(infoWrapperPanel, BoxLayout.Y_AXIS));
        infoWrapperPanel.setOpaque(false);

        JLabel userName = new JLabel("Nome: " + user.getNome() + " - ");
        userName.setFont(new Font("Arial", Font.BOLD, 14));
        userName.setForeground(Color.white);
        userName.add(Box.createHorizontalStrut(20));
        constUserInfoPanel.add(userName);

        JLabel userSobrenome = new JLabel("Sobrenome: " + user.getSobrenome() + " - ");
        userSobrenome.setFont(new Font("Arial", Font.BOLD, 14));
        userSobrenome.setForeground(Color.white);
        userSobrenome.add(Box.createHorizontalStrut(20));
        constUserInfoPanel.add(userSobrenome);

        JLabel userEmail = new JLabel("Email: " + user.getEmail() + " - ");
        userEmail.setFont(new Font("Arial", Font.BOLD, 14));
        userEmail.setForeground(Color.white);
        userEmail.add(Box.createHorizontalStrut(20));
        constUserInfoPanel.add(userEmail);

        JLabel userTelefone = new JLabel("Telefone: " + user.getTelefone());
        userTelefone.setFont(new Font("Arial", Font.BOLD, 14));
        userTelefone.setForeground(Color.white);
        constUserInfoPanel.add(userTelefone);


        constUserInfoPanel.add(infoWrapperPanel);
        constUserInfoPanel.add(Box.createHorizontalStrut(40));


        return constUserInfoPanel;
    }

}
