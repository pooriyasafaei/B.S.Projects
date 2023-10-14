package gui;

import gui.parts.ActionController;
import gui.parts.Board;
import gui.parts.EventWriter;
import gui.parts.SettingPanel;
import logic.game.BoardGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MainFrame extends JPanel implements ActionListener {
    private final Board board;
    private final EventWriter events = new EventWriter(350, 375);
    private final JMenuBar menuBar;
    private final JLabel systemMessage;
    private final ActionController actionController;

    private String message;

    private BoardGame game;

    JFrame frame = new JFrame ("COUP");

    public MainFrame(BoardGame game)  {

        this.game = game;
        this.board = new Board(game, game.getPlayers()[0]);
        this.actionController = new ActionController(board);

        //construct preComponents
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        resetButton.setActionCommand("reset");
        resetButton.setBackground(Color.LIGHT_GRAY);

        JButton settingButton = new JButton("Settings");
        settingButton.addActionListener(this);
        settingButton.setActionCommand("settings");
        settingButton.setBackground(Color.LIGHT_GRAY);

        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(this);
        helpButton.setActionCommand("help");
        helpButton.setBackground(Color.LIGHT_GRAY);

        //construct components
        menuBar = new JMenuBar();
        menuBar.add (resetButton);
        menuBar.add (settingButton);
        menuBar.add (helpButton);

        systemMessage = new JLabel ("Game is started. please select a card and an action to start");

        //adjust size and set layout
        setPreferredSize (new Dimension (1100, 630));
        setLayout (null);

        //add components
        add (menuBar);
        add (systemMessage);
        add (this.board);
        add (events);
        add (actionController);

        //set component bounds (only needed by Absolute Positioning)
        menuBar.setBounds (0, 0, 210, 25);
        systemMessage.setBounds (210, 0, 540, 25);
        this.board.setBounds(0, 25, 750, 605);
        events.setBounds(750, 0, 350, 375);
        actionController.setBounds(750, 375, 350, 255);

        // borders

        systemMessage.setBorder(BorderFactory.createMatteBorder(
                2, 2, 1, 1, Color.darkGray));
        menuBar.setBorder(BorderFactory.createMatteBorder(
                2, 2, 1, 1, Color.darkGray));

        setBackground(new Color(101, 134, 62));

        start(this);
    }

    public void start(MainFrame panel){
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible (true);
        frame.setResizable(false);
    }

    public void helpFrame() throws IOException {
        JFrame frame = new JFrame ("MyPanel");

        JPanel helpCard = new JPanel();
        JLabel label = new JLabel();
        helpCard.setPreferredSize(new Dimension(720,460));

        helpCard.add(label);
        label.setBounds(0,0,720,460);
        BufferedImage img = null;

        img = ImageIO.read(new File("images\\helpCard.png"));
        Image dimg = img.getScaledInstance(720, 460,
                Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);
        label.setIcon(imageIcon);

        frame.getContentPane().add(helpCard);
        helpCard.setVisible(true);


        frame.pack();
        frame.setVisible (true);
        frame.setResizable(false);
    }

    public void setMessage(String message) {
        this.message = message;
        systemMessage.setText(message);
        revalidate();
        repaint();
    }

    public String getMessage() {
        return message;
    }

    public void noticeEvent(String message){
        events.noticeEvent(message);
        revalidate();
        repaint();
    }

    public ActionController getActionController() {
        return actionController;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if(command.equals("reset")){
            reset();
        }

        if(command.equals("settings")){
            new SettingPanel(this);
        }

        if(command.equals("help")){
            try {
                helpFrame();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }

    }

    public void reset(){
        frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        game.startGame();
    }
}