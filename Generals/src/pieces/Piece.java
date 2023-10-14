package pieces;

import game_board.BoardNode;
import game_board.GameBoard;
import gui.GeneralsBoard;

public class Piece {
    private int row;
    private int column;
    private boolean playerBlack;
    private boolean upgraded;

    public Piece(int row, int column, boolean playerBlack) {
        this.row = row;
        this.column = column;
        this.playerBlack = playerBlack;
        this.upgraded = false;
    }

    public boolean move(int i, int j){

        if(i > 5 || j > 5 || i < 1 || j < 1) return false;

        GameBoard main = GameBoard.getInstance();
        BoardNode[][] gameBoard = main.getMainBoard();

        if(i == this.row && j == this.column) return false;

        Piece piece = gameBoard[i][j].getPieceIn();
        if(piece == null) return true;

        return piece.playerBlack != this.playerBlack;
    }

    public void transfer(int i , int j){

        GameBoard main = GameBoard.getInstance();
        BoardNode[][] gameBoard = main.getMainBoard();

        Piece current = gameBoard[i][j].getPieceIn();

        if(current != null){
            current.row =0;
            current.column = 0;
            if(current.isPlayerBlack())
                main.getWhiteOut().add(current);
            else main.getBlackOut().add(current);
            current.playerBlack = !current.playerBlack;
            current.upgraded = false;
        }

        if(column != 0 && row != 0){
            if(isPlayerBlack())
                if (i <= 2)
                    upgraded = true;

            if(!isPlayerBlack())
                if(i >= 4)
                    upgraded = true;
        }

        gameBoard[i][j].setPieceIn(this);
        gameBoard[row][column].setPieceIn(null);
        this.setRow(i);
        this.setColumn(j);
        GeneralsBoard.getInstance().defaultBoardColor();

        main.getBlackOut().remove(this);
        main.getWhiteOut().remove(this);
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

    public boolean isPlayerBlack() {
        return playerBlack;
    }

    public void setPlayerBlack(boolean playerBlack) {
        this.playerBlack = playerBlack;
    }

    public boolean isUpgraded() {
        return upgraded;
    }

    public void setUpgraded(boolean upgraded) {
        this.upgraded = upgraded;
    }

}
