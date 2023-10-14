package gui;

import game_board.GameBoard;
import runner.Runner;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame implements ActionListener {

    private static GameFrame gameFrame;

    public static GameFrame getInstance(){
        if(gameFrame == null)
            gameFrame = new GameFrame("Generals!");
        return gameFrame;
    }

    public GameFrame(String title) {
        this.setTitle(title);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.add(GeneralsBoard.getInstance().getGui());

        this.pack();
        this.setMinimumSize(this.getMinimumSize());
        this.setVisible(true);

        BoardPart[][] board = GeneralsBoard.getInstance().getMainBoard();
        for(int i = 1; i <= 5; i++)
            for (int j =1; j <= 5; j++){
                board[i][j].addActionListener(this);
                board[i][j].setActionCommand( board[i][j].getRow() + " " + board[i][j].getColumn());
            }

        setResizable(false);
        refresh();
    }

    public void refresh(){
        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GeneralsBoard main = GeneralsBoard.getInstance();

        if(Runner.ended) return;

        if(main.getSource() == null){
            BoardPart source = (BoardPart) (e.getSource());
            if(source.getPieceIn() == null) return;
            if(source.getPieceIn().isPlayerBlack() != Runner.blackTurns) return;

            GeneralsBoard.getInstance().setSource(source);
            GeneralsBoard.getInstance().threatBoardColor();
            return;
        } else {
            BoardPart source = main.getSource();
            BoardPart destination = (BoardPart) (e.getSource());

            Runner.playGame(source.getPieceSymbol() + " " + source.getPlace()+ " " + destination.getPlace());
            main.setSource(null);
            main.defaultBoardColor();
        }
        GameBoard.getInstance().printBoard();
    }
}
