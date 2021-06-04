package BusinessLayer;

import java.util.List;

public class Monster extends Enemy{

    //fields
    protected Integer visionRange;

    //constructor
    public Monster(char tileChar, String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer experienceValue, Integer visionRange) {
        super(tileChar, name, healthPool, attackPoints, defensePoints, experienceValue);
        this.visionRange = visionRange;
    }

    //methods
    @Override
    public void Interact(Unit unit) {

    }

    @Override
    public void onDeath() {

    }

    @Override
    public void turn(List<Unit> players) {
        Unit closest = findClosestTarget(players);
        if (getPosition().range(closest.getPosition()) < visionRange){
            Integer dx = getPosition().getPositionX() - closest.getPosition().getPositionX();
            Integer dy = getPosition().getPositionY() - closest.getPosition().getPositionY();
            if (Math.abs(dx) > Math.abs(dy)){
                if (dx > 0){

                }
            }
        }

    }

    protected Unit findClosestTarget(List<Unit> targets){
        Unit closest = targets.get(0);
        for (Unit target: targets){
            if (getPosition().range(target.getPosition()) < getPosition().range(closest.getPosition())){
                closest = target;
            }
        }
        return closest;
    }
}
