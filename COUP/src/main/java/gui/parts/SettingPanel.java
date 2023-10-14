package gui.parts;

import gui.MainFrame;
import logic.config.Config;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SettingPanel extends JPanel implements ActionListener {
    private JLabel playerNameLabel;
    private JTextField playerNameField;
    private JLabel player2Label;
    private JLabel player3Label;
    private JLabel player4Label;
    private JComboBox player2Type;
    private JComboBox player3Type;
    private JComboBox player4Type;
    private JComboBox playerCard1;
    private JComboBox playerCard2;
    private JComboBox player2Card1;
    private JComboBox player2Card2;
    private JComboBox player3Card1;
    private JComboBox player3Card2;
    private JComboBox player4Card1;
    private JComboBox player4Card2;
    private JButton saveButton;

    private MainFrame game;
    private JFrame frame = new JFrame ("Settings");

    public SettingPanel(MainFrame game) {
        this.game = game;

        //construct preComponents
        String[] player2TypeItems = {"Bot Type", "CautiousAssassinAndroid", "CouperAndroid", "ExtortionistAndroid", "ParanoidAndroid"};
        String[] player3TypeItems = {"Bot Type", "CautiousAssassinAndroid", "CouperAndroid", "ExtortionistAndroid", "ParanoidAndroid"};
        String[] player4TypeItems = {"Bot Type", "CautiousAssassinAndroid", "CouperAndroid", "ExtortionistAndroid", "ParanoidAndroid"};
        String[] playerCard1Items = {"Card Type", "ambassador", "assassin", "captain", "contessa", "duke"};
        String[] playerCard2Items = {"Card Type", "ambassador", "assassin", "captain", "contessa", "duke"};
        String[] player2Card1Items = {"Card Type", "ambassador", "assassin", "captain", "contessa", "duke"};
        String[] player2Card2Items = {"Card Type", "ambassador", "assassin", "captain", "contessa", "duke"};
        String[] player3Card1Items = {"Card Type", "ambassador", "assassin", "captain", "contessa", "duke"};
        String[] player3Card2Items = {"Card Type", "ambassador", "assassin", "captain", "contessa", "duke"};
        String[] player4Card1Items = {"Card Type", "ambassador", "assassin", "captain", "contessa", "duke"};
        String[] player4Card2Items = {"Card Type", "ambassador", "assassin", "captain", "contessa", "duke"};

        //construct components
        playerNameLabel = new JLabel ("Enter Player Name:");
        playerNameField = new JTextField (5);
        player2Label = new JLabel ("Player 2:");
        player3Label = new JLabel ("Player 3:");
        player4Label = new JLabel ("Player 4:");
        player2Type = new JComboBox (player2TypeItems);
        player3Type = new JComboBox (player3TypeItems);
        player4Type = new JComboBox (player4TypeItems);
        playerCard1 = new JComboBox (playerCard1Items);
        playerCard2 = new JComboBox (playerCard2Items);
        player2Card1 = new JComboBox (player2Card1Items);
        player2Card2 = new JComboBox (player2Card2Items);
        player3Card1 = new JComboBox (player3Card1Items);
        player3Card2 = new JComboBox (player3Card2Items);
        player4Card1 = new JComboBox (player4Card1Items);
        player4Card2 = new JComboBox (player4Card2Items);

        saveButton = new JButton ("save");
        saveButton.addActionListener(this);

        //set components properties
        playerNameField.setToolTipText ("Enter your player name");

        //adjust size and set layout
        setPreferredSize (new Dimension (699, 392));
        setLayout (null);

        //add components
        add (playerNameLabel);
        add (playerNameField);
        add (player2Label);
        add (player3Label);
        add (player4Label);
        add (player2Type);
        add (player3Type);
        add (player4Type);
        add (playerCard1);
        add (playerCard2);
        add (player2Card1);
        add (player2Card2);
        add (player3Card1);
        add (player3Card2);
        add (player4Card1);
        add (player4Card2);
        add (saveButton);

        //set component bounds (only needed by Absolute Positioning)
        playerNameLabel.setBounds (40, 35, 110, 25);
        playerNameField.setBounds (170, 35, 170, 25);
        player2Label.setBounds (40, 100, 100, 25);
        player3Label.setBounds (40, 180, 100, 25);
        player4Label.setBounds (40, 260, 100, 25);
        player2Type.setBounds (170, 100, 170, 25);
        player3Type.setBounds (170, 180, 170, 25);
        player4Type.setBounds (170, 260, 170, 25);
        playerCard1.setBounds (405, 35, 100, 25);
        playerCard2.setBounds (530, 35, 100, 25);
        player2Card1.setBounds (405, 100, 100, 25);
        player2Card2.setBounds (530, 100, 100, 25);
        player3Card1.setBounds (405, 180, 100, 25);
        player3Card2.setBounds (530, 180, 100, 25);
        player4Card1.setBounds (405, 260, 100, 25);
        player4Card2.setBounds (530, 260, 100, 25);
        saveButton.setBounds (575, 330, 65, 25);

        // Frame
        frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add (this);
        frame.pack();
        frame.setVisible (true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        StringBuilder p1 = new StringBuilder(),
                p2 = new StringBuilder(),
                p3 = new StringBuilder(),
                p4 = new StringBuilder();

        // Names and types
        if(!playerNameField.getText().equals("")) p1.append(playerNameField.getText());
        else p1.append(Config.getInstance().getPlayerInfo().split(":")[0]);

        if(!player2Type.getSelectedItem().equals("Bot Type")) p2.append(player2Type.getSelectedItem());
        else p2.append(Config.getInstance().getPlayer2Info().split(":")[0]);

        if(!player3Type.getSelectedItem().equals("Bot Type")) p3.append(player3Type.getSelectedItem());
        else p3.append(Config.getInstance().getPlayer3Info().split(":")[0]);

        if(!player4Type.getSelectedItem().equals("Bot Type")) p4.append(player4Type.getSelectedItem());
        else p4.append(Config.getInstance().getPlayer4Info().split(":")[0]);

        p1.append(":");
        p2.append(":");
        p3.append(":");
        p4.append(":");

        // Card 1
        if(!playerCard1.getSelectedItem().equals("Card Type")) p1.append(playerCard1.getSelectedItem());
        else p1.append(Config.getInstance().getPlayerInfo().split(":")[1].split(",")[0]);

        if(!player2Card1.getSelectedItem().equals("Card Type")) p2.append(player2Card1.getSelectedItem());
        else p2.append(Config.getInstance().getPlayer2Info().split(":")[1].split(",")[0]);

        if(!player3Card1.getSelectedItem().equals("Card Type")) p3.append(player3Card1.getSelectedItem());
        else p3.append(Config.getInstance().getPlayer3Info().split(":")[1].split(",")[0]);

        if(!player4Card1.getSelectedItem().equals("Card Type")) p4.append(player4Card1.getSelectedItem());
        else p4.append(Config.getInstance().getPlayer4Info().split(":")[1].split(",")[0]);

        p1.append(",");
        p2.append(",");
        p3.append(",");
        p4.append(",");

        // Card 2
        if(!playerCard2.getSelectedItem().equals("Card Type")) p1.append(playerCard2.getSelectedItem());
        else p1.append(Config.getInstance().getPlayerInfo().split(":")[1].split(",")[1]);

        if(!player2Card2.getSelectedItem().equals("Card Type")) p2.append(player2Card2.getSelectedItem());
        else p2.append(Config.getInstance().getPlayer2Info().split(":")[1].split(",")[1]);

        if(!player3Card2.getSelectedItem().equals("Card Type")) p3.append(player3Card2.getSelectedItem());
        else p3.append(Config.getInstance().getPlayer3Info().split(":")[1].split(",")[1]);

        if(!player4Card2.getSelectedItem().equals("Card Type")) p4.append(player4Card2.getSelectedItem());
        else p4.append(Config.getInstance().getPlayer4Info().split(":")[1].split(",")[1]);

        p1.append(",");
        p2.append(",");
        p3.append(",");
        p4.append(",");

        // Players Numbers
        p1.append(0);
        p2.append(1);
        p3.append(2);
        p4.append(3);

        Config.getInstance().setPlayerInfo(p1.toString());
        Config.getInstance().setPlayer2Info(p2.toString());
        Config.getInstance().setPlayer3Info(p3.toString());
        Config.getInstance().setPlayer4Info(p4.toString());

        Config.saveConfig();
        game.reset();

        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}