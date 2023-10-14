package pieces;

import game_board.BoardNode;
import game_board.GameBoard;

public class Golden extends Piece{

    public Golden(int row, int column, boolean playerBlack) {
        super(row, column, playerBlack);
    }

    @Override
    public boolean move(int i, int j) {

        if (!super.move(i, j)) return false;
        GameBoard main = GameBoard.getInstance();
        BoardNode[][] gameBoard = main.getMainBoard();

        int current_i = this.getRow(), current_j = this.getColumn();


        if (i > current_i + 1 || i < current_i - 1 || j > current_j + 1 || j < current_j - 1) return false;
        if ((!isPlayerBlack() && (i == current_i - 1 && (j == current_j + 1 || j == current_j - 1)))
                || (isPlayerBlack() && (i == current_i + 1 && (j == current_j + 1 || j == current_j - 1))))
            return false;


        return true;
    }

    @Override
    public String toString() {
        if(isPlayerBlack()) return "g";
        return "G";
    }
}
