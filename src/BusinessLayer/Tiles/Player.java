package BusinessLayer.Tiles;

import BusinessLayer.util.Position;

import java.util.List;

abstract public class Player extends Unit{

    //fields

    public static final char playerTile = '@';
    protected static final int REQ_EXP = 50;
    protected static final int HEALTH_BONUS = 10;
    protected static final int ATTACK_BONUS = 4;
    protected static final int DEFENSE_BONUS = 1;

    protected final String abilityName = null;
    protected Integer level;
    protected Integer experience;
    protected List<Enemy> enemies;


    //constructors

    public Player(String name, Integer healthPool, Integer attackPoints, Integer defensePoints) {
        super(playerTile, name, healthPool, attackPoints, defensePoints);
        this.enemies = enemies;
        level = 1;
        experience = 0;
    }

    //methods

    @Override
    public void turn() {
        getAction();
    }

    public void getAction(){ inputProvider.getAction(this); }

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

    protected String getLevelString(){
       return String.format("Level: %d",level);
    }

    protected String getExperienceString(){
        return String.format("Experience: %d/%d", experience, (level * REQ_EXP));
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

    public String description(){
        return String.format("%s\t%s\t%s", super.description(), getLevelString() ,getExperienceString());
    }

    @Override
    public void onKill(Unit killer) {
        killer.onPlayerKill(this);
    }

    @Override
    public void onEnemyKill(Enemy kill) {
        Integer experience = kill.getExperienceValue();
        msgCallback.call(String.format("%s died. %s gained %d experience.", kill.getName(), getName(), experience));
        addExperience(experience);
    }

    @Override
    public void onPlayerKill(Player kill) {
        Integer experience = kill.experience;
        msgCallback.call(String.format("%s died. %s gained %d experience.", kill.getName(), getName(), experience));
        addExperience(experience);
    }

    public void onDeath() {
        msgCallback.call("You lost, Looser!");
        deathCallback.death();
    }

    @Override
    public String toString(){ return health.getCurrentHP() > 0 ? super.toString() : "X"; }

    abstract public void castAbility();

}
