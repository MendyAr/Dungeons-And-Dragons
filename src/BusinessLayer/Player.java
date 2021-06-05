package BusinessLayer;

import java.util.List;

abstract public class Player extends Unit{

    //fields

    public static final char playerTile = '@';
    protected static final int REQ_EXP = 50;
    protected static final int HEALTH_BONUS = 10;
    protected static final int ATTACK_BONUS = 4;
    protected static final int DEFENSE_BONUS = 1;

    protected Integer level;
    protected Integer experience;
    protected final String abilityName;

    //constructors

    public Player(String name, Integer healthPool, Integer attackPoints, Integer defensePoints, String abilityName) {
        super(playerTile, name, healthPool, attackPoints, defensePoints);
        this.abilityName = abilityName;
        level = 1;
        experience = 0;
    }

    //methods

    protected void addExperience(Integer value){
        experience += value;
        msgCallback.call(getName() + " gained " + value + " experience.");
        while (experience >= level*REQ_EXP)
            lvlUp();
    }

    protected void lvlUp() {
        experience -= REQ_EXP * level;
        level++;
        health.increasePool(HEALTH_BONUS * level);
        increaseAtt(ATTACK_BONUS * level);
        increaseDef(DEFENSE_BONUS * level);
    }

    public String description(){
        return super.description() + "\tLevel: " + level + "\tExperience: " + experience + '/' + (level * REQ_EXP);
    }

    public void onKill(Enemy enemy) {
        addExperience(enemy.getExperienceValue());
    }

    public void onDeath() {
        setTileChar('X');
        msgCallback.call("You lost, Looser!");
        deathCallback.call();
    }

    abstract public void castAbility(List<Enemy> enemies);


}
