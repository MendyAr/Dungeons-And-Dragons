package BusinessLayer;

import CallBacks.MessageCallback;
import util.LevelsComparator;
import util.Supplier;
import util.TrueRNG;

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
    public BoardInitializer(Board board, String levelsDirName, MessageCallback messageCallback) throws IOException {
        this.board = board;
        File levelsDir = new File(levelsDirName);
        if (!levelsDir.exists())
            throw new IOException("Invalid directory given");

        List<File> listOfLevels = Arrays.stream(levelsDir.listFiles()).filter(file -> file.getName().matches("^level\\d+.txt")).filter(file -> file.isFile()).collect(Collectors.toList());
        if (listOfLevels.isEmpty())
            throw new IOException("Levels directory has no level files");
        listOfLevels.sort(new LevelsComparator());
        checkLevelsLegality(listOfLevels);

        this.levelsFiles = listOfLevels.iterator();
        this.messageCallback = messageCallback;
        this.enemyFactory = createEnemeyFactory();
        this.playerFactory = createPlayerFactory();
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

    private void checkLevelsLegality(List<File> listOfLevels) throws IOException {
        List<String> levelNames = listOfLevels.stream().map(File::getName).collect(Collectors.toList());
        for (int i = 1; i <= levelNames.size(); i++) {
            if (!levelNames.contains(String.format("level%d.txt", i)))
                throw new IOException(String.format("'level%d.txt missing in a group of %d levels", i, levelNames.size()));
        }
    }

    private Map<Character, Supplier<Enemy>> createEnemeyFactory(){
        List<Supplier<Enemy>> enemyList = Arrays.asList(
                () -> new Monster('s', "Lannister Solider", 80, 8, 3, 25, 3)
        );
        return enemyList.stream().collect(Collectors.toMap(s -> s.get().getTileChar(), Function.identity()));
    }

    private List<Supplier<Player>> createPlayerFactory(){
        List<Supplier<Player>> playerList = Arrays.asList(
                () -> new Warrior("Jon Snow", 300, 30, 4, 3)
        );
        return playerList;
    }
}