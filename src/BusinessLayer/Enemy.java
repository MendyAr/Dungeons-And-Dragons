package BusinessLayer;

import java.util.List;

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

    @Override
    public void onKill(Unit kill) {
        msgCallback.call(String.format("%s was killed by %s.", kill.getName(), getName()));
    }

    @Override
    public void onDeath() {
        deathCallback.death();
    }

    protected Unit findClosestTarget(List<Unit> targets){
        Unit closest = targets.get(0);
        Double minRange = range(closest);
        for (Unit target: targets){
            Double newRange = range(target);
            if (range(target) < minRange){
                closest = target;
                minRange = newRange;
            }
        }
        return closest;
    }
}
