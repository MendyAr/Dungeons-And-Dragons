package BusinessLayer;

import CallBacks.MessageCallback;
import util.LevelsComparator;
import util.Supplier;
import util.TrueRNG;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BoardInitializer {

    private static String PLAYER_OPTIONS = "Select player:\n" +
            "1. Jon Snow             Health: 300/300         Attack: 30              Defense: 4              Level: 1                Experience: 0/50                Cooldown: 0/3\n" +
            "2. The Hound            Health: 400/400         Attack: 20              Defense: 6              Level: 1                Experience: 0/50                Cooldown: 0/5\n" +
            "3. Melisandre           Health: 100/100         Attack: 5               Defense: 1              Level: 1                Experience: 0/50                Mana: 75/300            Spell Power: 15\n" +
            "4. Thoros of Myr                Health: 250/250         Attack: 25              Defense: 4              Level: 1                Experience: 0/50                Mana: 37/150            Spell Power: 20\n" +
            "5. Arya Stark           Health: 150/150         Attack: 40              Defense: 2              Level: 1                Experience: 0/50                Energy: 100/100\n" +
            "6. Bronn                Health: 250/250         Attack: 35              Defense: 3              Level: 1                Experience: 0/50                Energy: 100/100\n" +
            "7. Ygritte              Health: 220/220         Attack: 30              Defense: 2              Level: 1                Experience: 0/50                Arrows: 10              Range: 6";
    private static Map<Character, Supplier<Enemy>> enemyFactory;

    //fields
    private final Board board;
    private final Iterator<File> levelsFiles;
    private final MessageCallback messageCallback;
    private List<Tile> tiles;
    private List<Player> players;
    private List<Enemy> enemies;

    //constructor
    public BoardInitializer(Board board, String levelsDirName, MessageCallback messageCallback) throws IOException {
        this.board = board;
        File levelsDir = new File(levelsDirName);
        if (!levelsDir.exists())
            throw new IOException("Invalid directory given");

        List<File> listOfLevels = Arrays.stream(levelsDir.listFiles()).filter(file -> file.getName().matches("^level\\d+.txt")).filter(file -> file.isFile()).collect(Collectors.toList());
        if (listOfLevels.isEmpty())
            throw new IOException("Levels directory has no level files");
        listOfLevels.sort(new LevelsComparator());


        this.levelsFiles = listOfLevels.iterator();
        this.messageCallback = messageCallback;
    }

    public boolean buildNext() throws IOException {
        if (!levelsFiles.hasNext())
            return false;
        tiles = new ArrayList<>();
        enemies = new ArrayList<>();
        if (players == null)
            buildPlayers();

        File level = levelsFiles.next();
        BufferedReader br = new BufferedReader(new FileReader(level));
        String line;
        for (int pY = 0; (line = br.readLine()) != null; pY++) {
            for (int pX = 0; pX<line.length(); pX++) {
                Position position = new Position(pX, pY);
                Character tileChar = line.charAt(pX);
                Tile tile;
                switch (tileChar){
                    case '.':
                        tile = new Empty();
                        tile.init(position);
                        tiles.add(tile);
                        break;
                    case '#':
                        tile = new Wall();
                        tile.init(position);
                        tiles.add(tile);
                        break;
                    case '@':
                        for (Player player: players){
                            player.init(position, enemies, () -> board.onPlayerDeath(player), messageCallback, (pos) -> board.move(player, pos), TrueRNG.getInstance());
                        }
                        break;
                    default:
                        if (!enemyFactory.containsKey(tileChar))
                            throw new IOException(String.format("Level %s has illegal character: '%c'", level.getName(), tileChar));
                        tile = enemyFactory.get(tileChar).get();
                        ((Enemy)tile).init(position, players, () -> board.onEnemyDeath((Enemy) tile), messageCallback, (pos) -> board.move((Enemy) tile, pos), TrueRNG.getInstance());
                        break;
                }
            }
        }
        board.init(players, enemies, tiles);
        return true;
    }

    private void buildPlayers(){
        int numOfPlayers = 1;
        /*
        messageCallback.call("How many players?");
        numOfPlayers = new Scanner(System.in).nextInt();
         */ //for multiplayer option
        players = new ArrayList<>(numOfPlayers);
        for (; numOfPlayers>0; numOfPlayers--){
            players.add(getPlayer());
        }
    }

    private Player getPlayer(){
        messageCallback.call(PLAYER_OPTIONS);
        return new Warrior("Dimi Demo", 300, 5, 4, 3);
    }
}