package BusinessLayer.Board;

import BusinessLayer.Tiles.*;
import BusinessLayer.Tiles.Classes.Mage;
import BusinessLayer.Tiles.Classes.Rogue;
import BusinessLayer.Tiles.Classes.Warrior;
import BusinessLayer.Tiles.Enemies.Monster;
import BusinessLayer.Tiles.Enemies.Trap;
import BusinessLayer.CallBacks.MessageCallback;
import BusinessLayer.util.LevelsComparator;
import BusinessLayer.util.Supplier;
import BusinessLayer.util.TrueRNG;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BoardInitializer {

    private static Scanner reader = new Scanner(System.in);

    //fields
    private final Board board;
    private final Iterator<File> levelsFiles;
    private final MessageCallback messageCallback;

    private final Map<Character, Supplier<Enemy>> enemyFactory;
    private final List<Supplier<Player>> playerFactory;

    private List<Tile> tiles;
    private List<Player> players;
    private List<Enemy> enemies;

    //constructor
    public BoardInitializer(Board board, String levelsDirName, MessageCallback messageCallback) {
        this.board = board;
        File levelsDir = new File(levelsDirName);
        if (!levelsDir.exists())
            throw new IllegalArgumentException("Invalid directory given");

        List<File> listOfLevels = Arrays.stream(Objects.requireNonNull(levelsDir.listFiles())).filter(file -> file.getName().matches("^level\\d+.txt")).filter(File::isFile).collect(Collectors.toList());
        if (listOfLevels.isEmpty())
            throw new NoSuchElementException("Levels directory has no level files");
        listOfLevels.sort(new LevelsComparator());
        checkLevelsLegality(listOfLevels);

        this.levelsFiles = listOfLevels.iterator();
        this.messageCallback = messageCallback;
        this.enemyFactory = createEnemeyFactory();
        this.playerFactory = createPlayerFactory();
    }


    public boolean buildNext() {
        if (!levelsFiles.hasNext())
            return false;
        tiles = new ArrayList<>();
        enemies = new ArrayList<>();
        if (players == null)
            buildPlayers();

        File level = levelsFiles.next();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(level));
        } catch (FileNotFoundException e) {
            messageCallback.call("Previously scanned level file is now missing");
            System.exit(127);
        }
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            messageCallback.call("Couldn't read file");
            System.exit(127);
        }
        for (int pY = 0; line!= null; pY++) {
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
                            throw new IllegalArgumentException(String.format("Level %s has illegal character: '%c'", level.getName(), tileChar));
                        tile = enemyFactory.get(tileChar).get();
                        ((Enemy)tile).init(position, players, () -> board.onEnemyDeath((Enemy) tile), messageCallback, (pos) -> board.move((Enemy) tile, pos), TrueRNG.getInstance());
                        break;
                }
            }
            try {
                line = br.readLine();
            } catch (IOException e) {
                messageCallback.call("Couldn't read file");
                System.exit(127);
            }
        }
        board.init(players, enemies, tiles);
        return true;
    }

    private void buildPlayers(){
        int numOfPlayers = 1;
        /*
        messageCallback.call("How many players?");
        numOfPlayers = reader.nextInt();
         */ //for multiplayer option
        players = new ArrayList<>(numOfPlayers);
        for (; numOfPlayers>0; numOfPlayers--){
            players.add(getPlayer());
        }
    }

    private Player getPlayer(){
        messageCallback.call("Select Player: ");
        Iterator<String> possiblePlayers = playerFactory.stream().map(Supplier::get).map(Player::description).iterator();
        for (int i = 1; possiblePlayers.hasNext(); i++)
            messageCallback.call(String.format("%d. %s", i, possiblePlayers.next()));
        Integer input = null;
        while (input == null){
            try {
                input = Integer.valueOf(reader.next().trim());
                if (input < 1 || input > playerFactory.size())
                    input = null;
            }
            catch (Exception ignored) {
            }
        }
        return playerFactory.get(input-1).get();
    }

    private void checkLevelsLegality(List<File> listOfLevels) {
        List<String> levelNames = listOfLevels.stream().map(File::getName).collect(Collectors.toList());
        for (int i = 1; i <= levelNames.size(); i++) {
            if (!levelNames.contains(String.format("level%d.txt", i)))
                throw new IllegalArgumentException(String.format("'level%d.txt missing in a group of %d levels", i, levelNames.size()));
        }
    }

    private Map<Character, Supplier<Enemy>> createEnemeyFactory(){
        List<Supplier<Enemy>> enemyList = Arrays.asList(
                () -> new Monster('s', "Lannister Solider", 80, 8, 3, 25, 3),
                () -> new Monster('k', "Lannister Knight", 200, 14, 8, 50, 4),
                () -> new Monster('q', "Queen's Guard", 400, 20, 15, 100, 5),
                () -> new Monster('z', "Wright", 600, 30, 15, 100, 3),
                () -> new Monster('b', "Bear-Wright", 1000, 75, 30, 250, 4),
                () -> new Monster('g', "Giant-Wright", 1500, 100, 40, 500, 5),
                () -> new Monster('w', "White Walker", 2000, 150, 50, 1000, 6),
                () -> new Monster('M', "The Mountain", 1000, 60, 25, 500, 6),
                () -> new Monster('C', "Queen Cersei", 100, 10, 10, 1000, 1),
                () -> new Monster('K', "Night's King", 5000, 300, 150, 5000, 8),
                () -> new Trap('B', "Bonus Trap", 1, 1, 1, 250, 1, 5),
                () -> new Trap('Q', "Queen's Trap", 250, 50, 10, 100, 3, 7),
                () -> new Trap('D', "Death Trap", 500, 100, 20, 250, 1, 10)
        );
        return enemyList.stream().collect(Collectors.toMap(s -> s.get().getTileChar(), Function.identity()));
    }

    private List<Supplier<Player>> createPlayerFactory(){
        List<Supplier<Player>> playerList = Arrays.asList(
                () -> new Warrior("Jon Snow", 300, 30, 4, 3),
                () -> new Warrior("The Hound", 400, 20, 6, 5),
                () -> new Mage("Melisandre", 100, 5, 1, 300, 30, 15, 5, 6),
                () -> new Mage("Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4),
                () -> new Rogue("Arya Stark", 150, 40, 2, 20),
                () -> new Rogue("Bronn", 250, 35, 3, 50)
        );
        return playerList;
    }
}