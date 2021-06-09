package UI;

import BusinessLayer.Board.Board;
import BusinessLayer.Board.BoardInitializer;

public class GameManager {

    //fields
    private Board board;
    private BoardInitializer boardInitializer;

    //methods
    public void init(String levelsDir){
        board = new Board(this::printMessage, this::levelEnded);
        boardInitializer = new BoardInitializer(board, levelsDir, this::printMessage);
    }

    public void startGame(){
        if (board == null)
            throw new NullPointerException("Initialize the game before starting the game");
        board.play();
    }

    public void printMessage(String msg){
        System.out.println(msg);
    }

    public void levelEnded(boolean ended) { //false if players lost, true if level finished
        if (ended) {
            if (boardInitializer.buildNext())
                board.play();
            else
                gameWon();
        }
        else
            gameOver();
    }

    private void gameWon(){
        printMessage("You won!");
        System.exit(0);
    }

    private void gameOver(){
        printMessage("You Lost!");
        printMessage(board.toString());
        printMessage("Game Over.");
        System.exit(0);
    }

    public static void main(String[] args) {
        GameManager gm = new GameManager();
        gm.init(args[0]);
        gm.startGame();
    }
}
