package BusinessLayer;

import java.util.List;

public class Trap extends Enemy{

    //fields
    private Integer visibilityTime;
    private Integer invisibilityTime;
    private Integer tickCount = 0;
    private Boolean visible = true;

    //constructor
    public Trap(char tileChar, String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer experienceValue, Integer visibilityTime, Integer invisibilityTimen) {
        super(tileChar, name, healthPool, attackPoints, defensePoints, experienceValue);
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTimen;
    }

    //methods
    @Override
    public void interact(Unit unit) {
        unit.visited(this);
    }

    @Override
    public void turn() {
        tickCount = (tickCount+1)%(visibilityTime+invisibilityTime);
        visible = tickCount < visibilityTime;
        Unit closest = findClosestTarget(enemies);
        if (getPosition().range(closest.getPosition()) < 2)
            combat(closest);
    }

    @Override
    public void visited(Enemy enemy) {
    }

    @Override
    public void visited(Player player) {
    }

    @Override
    public String toString() {
        if (visible)
            return super.toString();
        return String.valueOf('.');
    }
}
