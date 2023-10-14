package gui;

import game_board.BoardNode;
import game_board.GameBoard;
import pieces.Piece;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class HitPieces extends JPanel implements ActionListener {

    private final BoardPart[] blackOut = new BoardPart[10];
    private final BoardPart[] whiteOut = new BoardPart[10];

    private JPanel blackPanel = new JPanel(new GridLayout(0, 1));
    private JPanel whitePanel = new JPanel(new GridLayout(0, 1));

    private static HitPieces hitPieces;

    public static HitPieces getInstance(){
        if(hitPieces == null)
            hitPieces = new HitPieces();
        return hitPieces;
    }

    public HitPieces(){
        this.initialize();
    }

    public void initialize(){

        this.removeAll();
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.add(new JLabel(""), BorderLayout.LINE_START);

        blackPanel.add(new JLabel("black's out" , SwingConstants.CENTER));
        whitePanel.add(new JLabel("white's out" , SwingConstants.CENTER));

        for(int i =0; i < 10; i++){
            blackOut[i] = new BoardPart(0, 0);
            blackOut[i].setBoardNode(new BoardNode());
            blackOut[i].setSize(1,24);
            blackOut[i].setBackground(Color.orange);

            blackOut[i].addActionListener(this);
            blackOut[i].setActionCommand("0 0");
            blackPanel.add(blackOut[i]);

            whiteOut[i] = new BoardPart(0, 0);
            whiteOut[i].setBoardNode(new BoardNode());
            whiteOut[i].setSize(1,10);
            whiteOut[i].setBackground(Color.orange);

            whiteOut[i].addActionListener(this);
            whiteOut[i].setActionCommand("0 1");
            whitePanel.add(whiteOut[i]);
        }

        this.add(blackPanel);
        this.add(whitePanel);
        this.revalidate();
    }

    public void rebuild(){


        int i;

        for (i = 0; i < 10; i++){
            blackOut[i].setPieceIn(null);
            whiteOut[i].setPieceIn(null);
        }

        i = 0;

        for (Piece piece: GameBoard.getInstance().getBlackOut()) {
            blackOut[i].setPieceIn(piece);
            i++;
        }

        i = 0;
        for (Piece piece: GameBoard.getInstance().getWhiteOut()) {
            whiteOut[i].setPieceIn(piece);
            i++;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        GameFrame.getInstance().actionPerformed(e);
       }
}
