package pieces;

import game_board.BoardNode;
import game_board.GameBoard;

public class Lance extends Piece{

    public Lance(int row, int column, boolean playerBlack) {
        super(row, column, playerBlack);
    }
    //TODO
    @Override
    public boolean move(int i, int j) {

        if(!super.move(i, j)) return false;
        GameBoard main = GameBoard.getInstance();
        BoardNode[][] gameBoard = main.getMainBoard();

        int current_i = this.getRow(), current_j = this.getColumn();

        if(!isUpgraded()){
            if (j != current_j) return false;

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

        if(isUpgraded()){

            if((i == current_i && j == current_j) || (i != current_i && j != current_j)) return false;

            if(i != current_i){

                    if(i < current_i)
                    for(int t = current_i - 1 ; t > i; t--)
                        if(gameBoard[t][j].getPieceIn() != null) return false;



                    if(i > current_i)
                    for(int t = current_i + 1 ; t < i; t++)
                        if(gameBoard[t][j].getPieceIn() != null) return false;

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

        return true;
    }

    @Override
    public String toString() {
        if(isPlayerBlack()) return "l";
        return "L";
    }
}
