package gui;

import game_board.BoardNode;
import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BoardPart extends JButton {

    private int row;
    private int column;
    private BoardNode boardNode;

    public BoardPart(int row, int column){
        this.row = row;
        this.column = column;
        this.boardNode = new BoardNode();

        Insets margin = new Insets(0,0,0,0);
        this.setMargin(margin);
        ImageIcon icon = new ImageIcon(new BufferedImage(1, 41, BufferedImage.TYPE_INT_ARGB));
        this.setIcon(icon);

        setDefaultColor();

        this.setText(findText());
    }

    public void setDefaultColor(){
        if ((column % 2 == 1 && row % 2 == 1) || (column % 2 == 0 && row % 2 == 0)) {
            this.setBackground(Color.gray);
        } else {
            this.setBackground(Color.lightGray);
        }
        this.setText(this.findText());
    }

    public String findText(){

        if(boardNode.getPieceIn() == null) return "                   ";

        StringBuilder sb = new StringBuilder();
        if(boardNode.getPieceIn() instanceof Bishop) sb.append("     B ");
        if(boardNode.getPieceIn() instanceof Golden) sb.append("     G ");
        if(boardNode.getPieceIn() instanceof King) sb.append("     K ");
        if(boardNode.getPieceIn() instanceof Lance) sb.append("     L ");
        if(boardNode.getPieceIn() instanceof Pawn) sb.append("     P ");
        if(boardNode.getPieceIn() instanceof Silver) sb.append("     S ");

        if(boardNode.getPieceIn().isPlayerBlack()) sb.append("black       ");
        else sb.append("white       ");

        return sb.toString();
    }

    public Piece getPieceIn() {
        return boardNode.getPieceIn();
    }

    public void setPieceIn(Piece pieceIn) {
        this.boardNode.setPieceIn(pieceIn);
        this.setText(findText());
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public BoardNode getBoardNode() {
        return boardNode;
    }

    public void setBoardNode(BoardNode boardNode) {
        this.boardNode = boardNode;
        this.setText(findText());
    }

    public String getPlace(){
        return  column + "" + row;
    }

    public String getPieceSymbol(){

        if(boardNode.getPieceIn() == null) return "null";
        return boardNode.getPieceIn().toString() ;
    }
}
