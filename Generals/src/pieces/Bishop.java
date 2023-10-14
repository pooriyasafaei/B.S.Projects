package pieces;

import game_board.BoardNode;
import game_board.GameBoard;

public class Bishop extends Piece{

    public Bishop(int row, int column, boolean playerBlack) {
        super(row, column, playerBlack);
    }

    @Override
    public boolean move(int i, int j) {

        if(!super.move(i, j)) return false;
        GameBoard main = GameBoard.getInstance();
        BoardNode[][] gameBoard = main.getMainBoard();

        int current_i = this.getRow(), current_j = this.getColumn();

        if (!isUpgraded()){
            if(current_i + current_j != i + j && current_j - current_i != j - i) return false;

            if(isPlayerBlack()){
                if(i >= current_i) return false;

                if(j > current_j)
                    for (int t = 1; current_j + t < j; t++)
                        if(gameBoard[current_i - t][current_j + t].getPieceIn() != null) return false;

                if(j < current_j)
                    for (int t = 1; current_j - t > j; t++)
                        if(gameBoard[current_i - t][current_j - t].getPieceIn() != null) return false;
            }

            if(!isPlayerBlack()){
                if(i <= current_i) return false;

                if(j > current_j)
                    for (int t = 1; current_j + t < j; t++)
                        if(gameBoard[current_i + t][current_j + t].getPieceIn() != null) return false;

                if(j < current_j)
                    for (int t = 1; current_j - t > j; t++)
                        if(gameBoard[current_i + t][current_j - t].getPieceIn() != null) return false;
            }
        }

        if(isUpgraded()){

            if (i == current_i)
                return j == current_j + 1 || j == current_j - 1;

            if(j == current_j){
                return i == current_i + 1 || i == current_i - 1;

            } else {
                if(current_i + current_j != i + j && current_j - current_i != j - i) return false;

                if(i > current_i && j > current_j)
                    for(int t = 1; current_i + t < i; t++)
                        if(gameBoard[current_i + t][current_j + t].getPieceIn() != null) return false;

                if(i > current_i && j < current_j)
                    for(int t = 1; current_i + t < i; t++)
                        if(gameBoard[current_i + t][current_j - t].getPieceIn() != null) return false;

                if(i < current_i && j > current_j)
                    for(int t = 1; current_i + t < i; t++)
                        if(gameBoard[current_i - t][current_j + t].getPieceIn() != null) return false;

                if(i < current_i && j < current_j)
                    for(int t = 1; current_i + t < i; t++)
                        if(gameBoard[current_i - t][current_j - t].getPieceIn() != null) return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        if(isPlayerBlack()) return "b";
        return "B";
    }
}
