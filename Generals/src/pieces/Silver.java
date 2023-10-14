package pieces;

import game_board.BoardNode;
import game_board.GameBoard;

public class Silver extends Piece{

    public Silver(int row, int column, boolean playerBlack) {
        super(row, column, playerBlack);
    }

    @Override
    public boolean move(int i, int j) {

        if(!super.move(i, j)) return false;
        GameBoard main = GameBoard.getInstance();
        BoardNode[][] gameBoard = main.getMainBoard();

        int current_i = this.getRow(), current_j = this.getColumn();

        if(!isUpgraded()){
            if(i > current_i + 1 || i < current_i -1 || j > current_j + 1 || j < current_j - 1) return false;
            if((i == current_i) || (!isPlayerBlack()&&(i == current_i - 1 && j == current_j))
                    ||(isPlayerBlack()&&(i == current_i + 1 && j == current_j)))return false;
        }

        if(isUpgraded()){
            if(i > current_i + 2 || i < current_i - 2 || j > current_j + 2 || j < current_j - 2) return false;

            if (i != current_i && j != current_j){
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
            } else {
                if(i != current_i){
                    if(isPlayerBlack()){
                        if(current_i <= i) return false;
                        for(int t = current_i - 1 ; t > i; t--)
                            if(gameBoard[t][j].getPieceIn() != null) return false;
                    }

                    if(!isPlayerBlack()){
                        if(current_i >= i) return false;
                        for(int t = current_i + 1 ; t < i; t++)
                            if(gameBoard[t][j].getPieceIn() != null) return false;
                    }
                }

                if(j != current_j){

                    if(j < current_j){
                        for(int t = current_j - 1 ; t > j; t--)
                            if(gameBoard[i][t].getPieceIn() != null) return false;
                    }

                    if(j > current_j){

                        for(int t = current_j + 1 ; t < j; t++)
                            if(gameBoard[i][t].getPieceIn() != null) return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        if(isPlayerBlack()) return "s";
        return "S";
    }
}
