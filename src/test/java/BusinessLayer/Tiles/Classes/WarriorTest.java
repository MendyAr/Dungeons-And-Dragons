package BusinessLayer.Tiles.Classes;

import BusinessLayer.Tiles.Enemies.Monster;
import BusinessLayer.Tiles.Enemies.Trap;
import BusinessLayer.Tiles.Enemy;
import BusinessLayer.Tiles.Player;
import BusinessLayer.util.DeterministicRNG;
import BusinessLayer.util.Position;
import BusinessLayer.util.PredeterminedInputProvider;
import BusinessLayer.util.UserAction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WarriorTest {

    static Warrior warrior1;
    static List<Player> players;
    static Enemy monster1;
    static Enemy monsterNotInRange;
    static Enemy trap1;
    static List<Enemy> enemies;

    @BeforeAll
    static void setUp() {

        players = new ArrayList<>();
        enemies = new ArrayList<>();

        warrior1 = new Warrior("w1", 100,20,5,3);
        warrior1.init(new Position(1,1), enemies, WarriorTest::doNothing, WarriorTest::doNothing,WarriorTest::doNothing, DeterministicRNG.getInstance(0), PredeterminedInputProvider.getInputProvider(UserAction.DO_NOTHING));

        players.add(warrior1);

        monster1 = new Monster('M', "m1", 100, 50, 3, 20,3);
        monster1.init(new Position(1,2), players, WarriorTest::doNothing, WarriorTest::doNothing,WarriorTest::doNothing, DeterministicRNG.getInstance(2));
        monsterNotInRange = new Monster('M', "m2", 80, 50, 3, 20,3);
        monsterNotInRange.init(new Position(5,5), players, WarriorTest::doNothing, WarriorTest::doNothing,WarriorTest::doNothing, DeterministicRNG.getInstance(1));
        trap1 = new Trap('T', "t1", 50, 15, 3, 30,2, 4);
        trap1.init(new Position(2,3), players, WarriorTest::doNothing, WarriorTest::doNothing,WarriorTest::doNothing, DeterministicRNG.getInstance(1));

        enemies.add(monster1);
        enemies.add(monsterNotInRange);
        enemies.add(trap1);
    }

    @Test
    void turn() {
        assertEquals(0, warrior1.getAbilityResource().getCurrent());
        warrior1.turn();
        assertEquals(0, warrior1.getAbilityResource().getCurrent());
        warrior1.castAbility();
        assertEquals(3, warrior1.getAbilityResource().getCurrent());
        warrior1.turn();
        assertEquals(2, warrior1.getAbilityResource().getCurrent());
    }

    @Test
    void castAbility() {
        warrior1.getHealth().subAmount(60);
        assertEquals(40, warrior1.getHealth().getCurrent());
        warrior1.castAbility();
        assertEquals(3, warrior1.getAbilityResource().getCurrent());
        assertEquals(92, monster1.getHealth().getCurrent());
        assertEquals(80, monsterNotInRange.getHealth().getCurrent());
        assertEquals(90, warrior1.getHealth().getCurrent());
    }

    @Test
    void lvlUp() {
        warrior1.castAbility();
        warrior1.lvlUp();
        assertEquals(0, warrior1.getAbilityResource().getCurrent());
        assertEquals(30 + 100, warrior1.getHealth().getCurrent());
        assertEquals(12 + 20, warrior1.getAttack());
        assertEquals(4 + 5, warrior1.getDefence());
    }

    static void doNothing(){}
    static void doNothing(String str){}
    static void doNothing(Position pos){}
}