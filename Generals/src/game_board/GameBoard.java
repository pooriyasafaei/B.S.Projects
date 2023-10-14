package game_board;
import pieces.*;

import java.util.LinkedList;

public class GameBoard {

    private BoardNode[][] mainBoard;
    private LinkedList<Piece> blackOut;
    private LinkedList<Piece> whiteOut;

    private static GameBoard gameBoard;

    public static GameBoard getInstance(){
        if (gameBoard == null)
            gameBoard = new GameBoard();
        return gameBoard;
    }

    public GameBoard() {
        this.mainBoard = new BoardNode[6][6];
        this.blackOut = new LinkedList<Piece>();
        this.whiteOut = new LinkedList<Piece>();

        int i, j;
        for (i = 0; i < 6; i++) {
            for (j = 0; j < 6; j++) {
                mainBoard[i][j] = new BoardNode();
            }
        }
        startPosition();

    }

    public BoardNode[][] getMainBoard() {
        return mainBoard;
    }

    public void startPosition(){
        int i, j;
        for (i = 0; i < 6; i++) {
            for (j = 0; j < 6; j++) {
                mainBoard[i][j].setPieceIn(null);
            }
        }
        mainBoard[1][1].setPieceIn(new King(1, 1, false));
        mainBoard[1][2].setPieceIn(new Golden(1, 2, false));
        mainBoard[1][3].setPieceIn(new Silver(1, 3, false));
        mainBoard[1][4].setPieceIn(new Bishop(1, 4, false));
        mainBoard[1][5].setPieceIn(new Lance(1, 5, false));
        mainBoard[2][1].setPieceIn(new Pawn(2, 1, false));

        mainBoard[5][5].setPieceIn(new King(5, 5, true));
        mainBoard[5][4].setPieceIn(new Golden(5, 4, true));
        mainBoard[5][3].setPieceIn(new Silver(5, 3, true));
        mainBoard[5][2].setPieceIn(new Bishop(5, 2, true));
        mainBoard[5][1].setPieceIn(new Lance(5, 1, true));
        mainBoard[4][5].setPieceIn(new Pawn(4, 5, true));
    }

    public void setMainBoard(BoardNode[][] mainBoard) {
        this.mainBoard = mainBoard;
    }

    public LinkedList<Piece> getBlackOut() {
        return blackOut;
    }

    public void setBlackOut(LinkedList<Piece> blackOut) {
        this.blackOut = blackOut;
    }

    public LinkedList<Piece> getWhiteOut() {
        return whiteOut;
    }

    public void setWhiteOut(LinkedList<Piece> whiteOut) {
        this.whiteOut = whiteOut;
    }

    public boolean isSameType(String string, Piece piece){

        switch (string){
            case "p": if(piece instanceof Pawn) return true; break;
            case "b": if(piece instanceof Bishop) return true; break;
            case "l": if(piece instanceof Lance) return true; break;
            case "s": if(piece instanceof Silver) return true; break;
            case "g": if(piece instanceof Golden) return true; break;
            case "k": if(piece instanceof King) return true; break;
            default: return false;
        }
        return false;
    }

    public void printBoard(){
        for (int i = 1; i <6; i++) {
            for (int j = 1; j < 6; j++) {
                if (mainBoard[i][j].getPieceIn() == null) System.out.print("-");
                else System.out.print(mainBoard[i][j].getPieceIn());
            }
        }

        System.out.println();
        for(Piece piece: blackOut) System.out.print(piece);
        System.out.println();
        for(Piece piece: whiteOut) System.out.print(piece);
        System.out.println();
    }

    public String showWinner(){
        for (Piece piece: blackOut){
            if(piece instanceof King){
                System.out.println("black wins!");
                return "black wins!";
            }
        }

        for (Piece piece: whiteOut){
            if(piece instanceof King){
                System.out.println("white wins!");
                return "white wins!";
            }
        }
        return "continue";
    }

}
