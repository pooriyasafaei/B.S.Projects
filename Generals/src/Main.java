import game_board.BoardNode;
import game_board.GameBoard;
import runner.Runner;

import java.util.Scanner;

public class Main {



    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        GameBoard game = GameBoard.getInstance();
        BoardNode[][] gameBoard = game.getMainBoard();

        while (!Runner.ended) {
            Runner.ended = Runner.playGame(scan.nextLine());
            if(Runner.ended) return;
            game.printBoard();
        }


    }


}
