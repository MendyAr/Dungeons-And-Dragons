package BusinessLayer.Tiles;

import BusinessLayer.CallBacks.MessageCallback;
import BusinessLayer.CallBacks.MoveCallback;
import BusinessLayer.CallBacks.OnDeathCallback;
import BusinessLayer.util.InputProvider;
import BusinessLayer.util.Position;
import BusinessLayer.util.RandomNumberGenerator;
import BusinessLayer.util.Resource;

import java.util.List;

abstract public class Player extends Unit {

    //fields

    public static final char playerTile = '@';
    protected static final int REQ_EXP = 50;
    protected static final int HEALTH_BONUS = 10;
    protected static final int ATTACK_BONUS = 4;
    protected static final int DEFENSE_BONUS = 1;

    protected Integer level;
    protected Integer experience;
    protected String abilityName;
    protected Resource abilityResource;
    protected boolean abilityUsed;
    protected InputProvider inputProvider;


    //constructors

    public Player(String name, Integer healthPool, Integer attackPoints, Integer defensePoints, String abilityName, Resource abilityResource) {
        super(playerTile, name, healthPool, attackPoints, defensePoints);
        level = 1;
        experience = 0;
        this.abilityName = abilityName;
        this.abilityResource = abilityResource;
    }

    // initialization - adding InputProvider initialization
    public void init(Position position, List<? extends Unit> enemies, OnDeathCallback deathCallback, MessageCallback msgCallback, MoveCallback moveCallback, RandomNumberGenerator rng, InputProvider inputProvider) {
        super.init(position, enemies, deathCallback, msgCallback, moveCallback, rng);
        this.inputProvider = inputProvider;
    }

    //getters

    public Resource getAbilityResource() {
        return abilityResource;
    }

    protected String getLevelString() {
        return String.format("Level: %d", level);
    }

    protected String getExperienceString() {
        return String.format("Experience: %d/%d", experience, (level * REQ_EXP));
    }

    //methods

    @Override
    public void turn() {
        abilityUsed = false;
        getAction();
    }

    public void getAction() {
        inputProvider.getAction(this);
    }

    @Override
    public void interact(Unit unit) {
        unit.visited(this);
    }

    public void visited(Enemy enemy) {
        combat(enemy);
    }

    public void visited(Player player) {
        combat(player);
    }

    protected void addExperience(Integer value) {
        experience += value;
        msgCallback.call(getName() + " gained " + value + " experience.");
        while (experience >= level * REQ_EXP) {
            lvlUp();
        }
    }

    protected void lvlUp() {
        experience -= REQ_EXP * level;
        level++;
        health.increasePool(HEALTH_BONUS * level);
        health.init();
        increaseAtt(ATTACK_BONUS * level);
        increaseDef(DEFENSE_BONUS * level);
    }

    @Override
    public void combat(Unit defender) {
        super.combat(defender);
    }

    @Override
    public void onKill(Unit killer) {
        killer.onPlayerKill(this);
    }

    @Override
    public void onEnemyKill(Enemy kill) {
        Integer experience = kill.getExperienceValue();
        if (!abilityUsed)
            swapPositions(kill);
        msgCallback.call(String.format("%s died.", kill.getName()));
        addExperience(experience);
    }

    @Override
    public void onPlayerKill(Player kill) {
        Integer experience = kill.experience;
        msgCallback.call(String.format("%s died. %s gained %d experience.", kill.getName(), getName(), experience));
        addExperience(experience);
    }

    public void onDeath() {
        deathCallback.death();
    }

    public String description() {
        return String.format("%s\t%s\t%s\t%s", super.description(), getLevelString(), getExperienceString(), abilityResource.toString());
    }

    @Override
    public String toString() {
        return health.getCurrent() > 0 ? super.toString() : "X";
    }

    abstract public void castAbility();
}