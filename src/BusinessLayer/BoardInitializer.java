package BusinessLayer;

import CallBacks.MessageCallback;

import java.util.*;

public class BoardInitializer {

    private static String PLAYER_OPTIONS = "Select player:\n" +
            "1. Jon Snow             Health: 300/300         Attack: 30              Defense: 4              Level: 1                Experience: 0/50                Cooldown: 0/3\n" +
            "2. The Hound            Health: 400/400         Attack: 20              Defense: 6              Level: 1                Experience: 0/50                Cooldown: 0/5\n" +
            "3. Melisandre           Health: 100/100         Attack: 5               Defense: 1              Level: 1                Experience: 0/50                Mana: 75/300            Spell Power: 15\n" +
            "4. Thoros of Myr                Health: 250/250         Attack: 25              Defense: 4              Level: 1                Experience: 0/50                Mana: 37/150            Spell Power: 20\n" +
            "5. Arya Stark           Health: 150/150         Attack: 40              Defense: 2              Level: 1                Experience: 0/50                Energy: 100/100\n" +
            "6. Bronn                Health: 250/250         Attack: 35              Defense: 3              Level: 1                Experience: 0/50                Energy: 100/100\n" +
            "7. Ygritte              Health: 220/220         Attack: 30              Defense: 2              Level: 1                Experience: 0/50                Arrows: 10              Range: 6";

    //fields
    private final Board board;
    private final Iterator<String> levelsFiles;
    private final MessageCallback messageCallback;
    private List<Tile> tiles;
    private List<Player> players;
    private List<Enemy> enemies;


    //constructor
    public BoardInitializer(Board board, String levelsDir, MessageCallback messageCallback){
        this.board = board;
        LinkedList<String> files = new LinkedList<>();

        this.levelsFiles = files.iterator();
        this.messageCallback = messageCallback;
    }

    public boolean buildNext(){
        if (!levelsFiles.hasNext())
            return false;
        tiles = new ArrayList<>();
        enemies = new ArrayList<>();
        if (players == null)
            buildPlayers();

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