package BusinessLayer;

abstract public class Player extends Unit{

    //fields

    protected Integer level;
    protected Integer experience;
    protected final String abilityName;

    //constructors

    public Player(Integer px, Integer py, String name, Integer healthPool, Integer attackPoints, Integer defensePoints, String abilityName) {
        super(px, py, name, healthPool, attackPoints, defensePoints);
        this.abilityName = abilityName;
        level = 1;
        experience = 0;
    }

    //methods

    public String description(){
        return super.description() + "\tLevel: " + level + "\tExperience: " + experience + '/' + (level * 50);
    }
}
