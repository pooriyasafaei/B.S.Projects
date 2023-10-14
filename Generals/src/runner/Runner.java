package runner;

import game_board.BoardNode;
import game_board.GameBoard;
import gui.GeneralsBoard;
import pieces.Piece;

public class Runner {

    public static boolean blackTurns = true;
    public static boolean ended = false;

    public static boolean playGame(String string){

        GameBoard game = GameBoard.getInstance();
        BoardNode[][] gameBoard = game.getMainBoard();

        if(string.equals("0")) return true;


        String[] parts = string.split(" ");
        if(parts.length < 3) return false;


        String turn = parts[0].toLowerCase();
        if((blackTurns && !parts[0].equals(turn)) || (!blackTurns && parts[0].equals(turn))) return false;


        Piece piece = null;

        int current_i = Integer.parseInt((parts[1].substring(1, 2)));
        int current_j = Integer.parseInt((parts[1].substring(0, 1)));

        if(current_i < 0 || current_i > 5 || current_j < 0 || current_j > 5 ) return false;


        int i = Integer.parseInt((parts[2].substring(1, 2)));
        int j = Integer.parseInt((parts[2].substring(0, 1)));

        if(current_i == 0 && current_j == 0){

            if(i > 5 || j > 5 || i < 1 || j < 1) return false;

            if(blackTurns){
                for(Piece out: game.getBlackOut()){
                    if(game.isSameType(turn, out)){
                        piece = out;
                        break;
                    }
                }
            }

            if(!blackTurns){
                for(Piece out: game.getWhiteOut()){
                    if(game.isSameType(turn, out)){
                        piece = out;
                        break;
                    }
                }
            }

            if(piece == null) return false;


        } else {

            piece = gameBoard[current_i][current_j].getPieceIn();
            if(piece == null) return false;


            if(blackTurns != piece.isPlayerBlack()) return false;


            if(!game.isSameType(turn, piece)) return false;

        }

        if(current_i != 0 && current_j != 0) {
            if (!piece.move(i, j)) {
                return false;
            }
            else  piece.transfer(i, j);
        } else {
            if(gameBoard[i][j].getPieceIn() != null) return false;
            piece.transfer(i, j);
        }

        blackTurns = !blackTurns;
        ended = !(game.showWinner().equals("continue"));
        if (ended) return true;
        GeneralsBoard.getInstance().defaultBoardColor();
        return ended;
    }
}
