package gui;

import game_board.BoardNode;
import game_board.GameBoard;
import runner.Runner;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;

public class GeneralsBoard implements ActionListener {

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private BoardPart[][] mainBoard = new BoardPart[6][6];
    private JPanel mainPanel;
    private JLabel message = new JLabel("black's turn");
    private static GeneralsBoard guiBoard;

    private BoardPart source = null;

    public static GeneralsBoard getInstance(){
        if( guiBoard == null)
            guiBoard = new GeneralsBoard();
        return guiBoard;
    }

    public GeneralsBoard() {
        rebuild();
    }


    public void rebuild(){
        //gui setting up
        gui.setLayout(new BorderLayout());

        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);

        JButton reset = new JButton("Reset");
        tools.add(reset);

        reset.addActionListener(this);
        reset.setActionCommand("reset");

        tools.addSeparator();
        tools.add(message);

        gui.add(new JLabel(""), BorderLayout.LINE_START);

        mainPanel = new JPanel(new GridLayout(0, 6));
        mainPanel.setBorder(new LineBorder(Color.BLACK));
        gui.add(mainPanel, BorderLayout.CENTER);
        gui.add(HitPieces.getInstance(), BorderLayout.EAST);

        BoardNode[][] logicalBoard = GameBoard.getInstance().getMainBoard();
        // create the board squares
        Insets buttonMargin = new Insets(0,0,0,0);
        for (int i = 0; i < mainBoard.length; i++) {
            for (int j = 0; j < mainBoard[i].length; j++) {
                mainBoard[i][j] = new BoardPart(i , j);
                mainBoard[i][j].setBoardNode(logicalBoard[i][j]);
            }
        }

        //fill the main board
        mainPanel.add(new JLabel(""));
        // fill the top row
        for (Integer i = 1; i <= 5; i++) {
            mainPanel.add(new JLabel((i).toString(), SwingConstants.CENTER));
        }
        // fill the first piece row
        for (int i = 5; i > 0; i--) {
            for (int j = 1; j <= 5; j++) {
                switch (j) {
                    case 1:
                        mainPanel.add(new JLabel("" + i , SwingConstants.CENTER));
                    default:
                        mainPanel.add(mainBoard[i][j]);
                }
            }
        }

    }

    public void defaultBoardColor(){
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                mainBoard[i][j].setDefaultColor();
            }
        }
        HitPieces.getInstance().rebuild();

        if(Runner.ended) {
            message.setText(GameBoard.getInstance().showWinner());
            return;
        }
        if(Runner.blackTurns) message.setText("black's turn");
        else message.setText("white's turn");
    }

    public void threatBoardColor(){
        if(source.getPlace().equals("00")){
            for (int i = 1; i <= 5; i++)
                for (int j = 1; j <= 5; j++) {
                    if(mainBoard[i][j].getPieceIn() == null){
                        mainBoard[i][j].setBackground(Color.green);
                    }
                }
            return;
        }


        for (int i = 1; i <= 5; i++)
            for (int j = 1; j <= 5; j++) {
                if(source.getPieceIn().move(i, j)){
                    if (mainBoard[i][j].getPieceIn() == null)
                        mainBoard[i][j].setBackground(Color.green);
                    else mainBoard[i][j].setBackground(Color.red);
                }
            }
    }

    public final JComponent getMainPanel() {
        return mainPanel;
    }

    public final JComponent getGui() {
        return gui;
    }

    public JLabel getMessage() {
        return message;
    }

    public void setMessage(JLabel message) {
        this.message = message;
    }

    public BoardPart[][] getMainBoard() {
        return mainBoard;
    }

    public void setMainBoard(BoardPart[][] mainBoard) {
        this.mainBoard = mainBoard;
    }

    public BoardPart getSource() {
        return source;
    }

    public void setSource(BoardPart source) {
        this.source = source;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("reset"))
        {
            GameBoard.getInstance().getWhiteOut().clear();
            GameBoard.getInstance().getBlackOut().clear();
            HitPieces.getInstance().rebuild();

            GameBoard.getInstance().startPosition();
            this.defaultBoardColor();
            GameFrame.getInstance().revalidate();

            Runner.blackTurns = true;
            Runner.ended = false;
        }
    }
}