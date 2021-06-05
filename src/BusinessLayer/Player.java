package BusinessLayer;

import java.util.List;

abstract public class Player extends Unit{

    //fields

    public static final char playerTile = '@';
    protected static final int REQ_EXP = 50;
    protected static final int HEALTH_BONUS = 10;
    protected static final int ATTACK_BONUS = 4;
    protected static final int DEFENSE_BONUS = 1;

    protected final String abilityName;
    protected Integer level;
    protected Integer experience;
    protected List<Enemy> enemies;


    //constructors

    public Player(String name, Integer healthPool, Integer attackPoints, Integer defensePoints, String abilityName, List<Enemy> enemies) {
        super(playerTile, name, healthPool, attackPoints, defensePoints);
        this.abilityName = abilityName;
        this.enemies = enemies;
        level = 1;
        experience = 0;
    }

    //methods

    @Override
    public void turn() {
        getAction();
    }

    public String getAction(){ return inputProvider.getAction(); }

    @Override
    public void interact(Unit unit) {
        unit.visited(this);
    }

    public void visited(Enemy enemy) { combat(enemy); }

    public void visited(Player player) { combat(player); }

    public void visited(Wall wall) {}

    public void visited(Empty empty) {
        Position tmp = getPosition();
        setPosition(empty.getPosition());
        empty.setPosition(tmp);
    }

    protected void addExperience(Integer value){
        experience += value;
        msgCallback.call(getName() + " gained " + value + " experience.");
        while (experience >= level*REQ_EXP){
            lvlUp();
        }
    }

    protected void lvlUp() {
        experience -= REQ_EXP * level;
        level++;
        health.increasePool(HEALTH_BONUS * level);
        increaseAtt(ATTACK_BONUS * level);
        increaseDef(DEFENSE_BONUS * level);
    }

    @Override
    public void onKill(Unit unit) {
        addExperience(enemy.getExperienceValue());
    }

    public void onDeath() {
        msgCallback.call("You lost, Looser!");
        deathCallback.call();
    }

    public String description(){
        return super.description() + "\tLevel: " + level + "\tExperience: " + experience + '/' + (level * REQ_EXP);
    }

    @Override
    public String toString(){ return health.getCurrentHP() > 0 ? super.toString() : "X"; }

    abstract public void castAbility();

}
