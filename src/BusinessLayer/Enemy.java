package BusinessLayer;

abstract public class Enemy extends Unit{

    //fields
    protected Integer experienceValue;

    //constructors
    public Enemy(char tileChar, String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer experienceValue) {
        super(tileChar, name, healthPool, attackPoints, defensePoints);
        this.experienceValue = experienceValue;
    }

    //methods

    public String description(){
        return super.description() + "\tExperience Value: " + experienceValue;
    }

    public Integer getExperienceValue(){
        return experienceValue;
    }
}
