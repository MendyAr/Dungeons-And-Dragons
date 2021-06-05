package BusinessLayer;

import CallBacks.GameOverCallback;

import java.util.List;

public class Board {

    //fields

    private List<Player> player;
    private List<Enemy> enemies;
    private List<Tile> board;
    private TileComparator tileComparator = new TileComparator();
    private GameOverCallback gameOverCallback;

    public void init (List<Player> player, List<Enemy> enemies, List<Tile> board, GameOverCallback gameOverCallback){
        this.player = player;
        this.enemies = enemies;
        this.board = board;
        this.gameOverCallback = gameOverCallback;
    }

    public void init(List<Enemy> enemies, List<Tile> board){
        this.enemies = enemies;
        this.board = board;
    }

    public String toString(){
        enemies.sort(tileComparator);
        String str = "";
        for (Tile tile: board){
            if (tile.getPosition().positionY == 0)
                str += "\n";
            str += tile;
        }
        return str.substring(1);
    }
}
