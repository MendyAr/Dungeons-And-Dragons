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

    protected String getLevel(){
       return String.format("Level: %d",level);
    }

    protected String getExperience(){
        return String.format("Experience: %d/%d", experience, (level * REQ_EXP));
    }

    protected void addExperience(Integer value){
        experience += value;
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
        return String.format("%s\t%s\t%s", super.description(), getLevel() ,getExperience());
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
        setTileChar('X');
        msgCallback.call("You lost, Looser!");
        deathCallback.death();
    }

    abstract public void castAbility(List<Enemy> enemies);


}
