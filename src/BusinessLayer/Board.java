package BusinessLayer;

import java.util.List;

public class Board {

    //fields

    private Player player;
    private List<Enemy> enemies;
    private List<Tile> board;
    private TileComparator tileComparator = new TileComparator();

    public void init (Player player, List<Enemy> enemies, List<Tile> board){
        this.player = player;
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
