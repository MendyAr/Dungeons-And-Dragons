package BusinessLayer;

import CallBacks.LevelOverCallback;
import CallBacks.MessageCallback;
import util.TileComparator;

import java.util.List;
import java.util.Optional;

public class Board {

    //fields

    private List<Player> players;
    private List<Enemy> enemies;
    private List<Tile> board;
    private TileComparator tileComparator = new TileComparator();
    private MessageCallback messageCallback;
    private LevelOverCallback levelOverCallback;

    public Board(MessageCallback messageCallback, LevelOverCallback levelOverCallback){
        this.messageCallback = messageCallback;
        this.levelOverCallback = levelOverCallback;
    }

    public void init (List<Player> players, List<Enemy> enemies, List<Tile> board){
        this.players = players;
        init(enemies, board);
    }

    public void init(List<Enemy> enemies, List<Tile> board){
        this.enemies = enemies;
        this.board = board;
    }

    public void play(){
        while (!enemies.isEmpty() && players.isEmpty()){
            for (Player player: players){
                player.turn();
            }
            for (Enemy enemy: enemies){
                enemy.turn();
            }
        }
    }

    public String toString(){
        board.sort(tileComparator);
        String str = board.stream().map(t -> t.getPosition().getPositionY() == 0 ? "\n" + t.toString() : t.toString()).toString();
        /*
        for (Tile tile: board){
            if (tile.getPosition().positionY == 0)
                str += "\n";
            str += tile;
        }
         */
        str += players.stream().map(p -> "\n" + p.description()).toString();
        /*
        for (Player player: players)
            str += "\n" + player.description();
         */
        return str.substring(1);
    }

    public void move(Unit unit, Position position) {
        Optional<Tile> oT = board.stream().filter(t -> t.position.equals(position)).findFirst();
        if (oT.isPresent())
            oT.get().interact(unit);
    }

    public void onPlayerDeath(Player player){
        players.remove(player);
        if (players.isEmpty())
            levelOverCallback.levelOver(false);
    }

    public void onEnemyDeath(Enemy enemy){
        enemies.remove(enemy);
        if (enemies.isEmpty())
            levelOverCallback.levelOver(true);
    }
}
