package BusinessLayer.BoardParts;

import BusinessLayer.Tiles.*;
import BusinessLayer.CallBacks.LevelOverCallback;
import BusinessLayer.CallBacks.MessageCallback;
import BusinessLayer.util.Position;
import BusinessLayer.util.TileComparator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Board {

    //fields

    private final TileComparator tileComparator = new TileComparator();
    private final MessageCallback messageCallback;
    private final LevelOverCallback levelOverCallback;
    private List<Player> players;
    private List<Enemy> enemies;
    private List<Tile> board;

    public Board(MessageCallback messageCallback, LevelOverCallback levelOverCallback){
        this.messageCallback = messageCallback;
        this.levelOverCallback = levelOverCallback;
    }

    public void init (List<Player> players, List<Enemy> enemies, List<Tile> board){
        this.players = players;
        this.enemies = enemies;
        this.board = board;
    }

    public void play(){
        while (!enemies.isEmpty() && !players.isEmpty()){
            for (Player player: players){
                messageCallback.call(toString());
                player.turn();
            }
            for (Enemy enemy: enemies){
                enemy.turn();
            }
        }
    }

    public String toString(){
        String str = board.stream().sorted(tileComparator)
                .map(t -> t.getPosition().getPositionX() == 0 ? "\n" + t.toString() : t.toString())
                .collect(Collectors.joining(""));

        /*
        for (Tile tile: board){
            if (tile.getPosition().getPositionY() == 0)
                str.append("\n");
            str.append(tile);
        }

         */

        str += players.stream().map(p -> "\n" + p.description())
                .collect(Collectors.joining(""));

        /*
        for (Player player: players)
            str.append("\n").append(player.description());


         */
        return str.substring(1);
    }

    // interact a unit and a position given by it
    public void move(Unit unit, Position position) {
        Optional<Tile> oT = board.stream().filter(t -> t.getPosition().equals(position)).findFirst();
        if (oT.isPresent())
            oT.get().interact(unit);
    }

    public void onPlayerDeath(Player player){
        players.remove(player);
        if (players.isEmpty())
            levelOverCallback.levelOver(false);
        board.remove(player);
        Empty replacement = new Empty();
        replacement.init(player.getPosition());
        board.add(replacement);
    }

    public void onEnemyDeath(Enemy enemy){
        enemies.remove(enemy);
        board.remove(enemy);
        Empty replacement = new Empty();
        replacement.init(enemy.getPosition());
        board.add(replacement);
        if (enemies.isEmpty())
            levelOverCallback.levelOver(true);
    }
}
