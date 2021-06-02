package BusinessLayer;

abstract public class Enemy extends Unit{

    //fields
    protected Integer experienceValue;

    //constructors
    public Enemy(Integer px, Integer py, String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer experienceValue) {
        super(px, py, name, healthPool, attackPoints, defensePoints);
        this.experienceValue = experienceValue;
    }

    //methods

    public String description(){
        return super.description() + "\tExperience Value: " + experienceValue;
    }
}
