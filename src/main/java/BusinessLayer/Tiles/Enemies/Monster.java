package BusinessLayer.Tiles.Enemies;

import BusinessLayer.Tiles.Enemy;
import BusinessLayer.Tiles.Unit;

public class Monster extends Enemy {

    //fields
    protected Integer visionRange;

    //constructor
    public Monster(char tileChar, String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer experienceValue, Integer visionRange) {
        super(tileChar, name, healthPool, attackPoints, defensePoints, experienceValue);
        this.visionRange = visionRange;
    }

    //methods

    public String getVisionRangeString(){
        return String.format("Vision Range: %d", visionRange);
    }

    @Override
    public void turn() {
        Unit closest = findClosestTarget(enemies);  //enemies in 'Enemy' class context is players
        if (range(closest) < visionRange) {
            Integer dx = getPosition().getPositionX() - closest.getPosition().getPositionX();
            Integer dy = getPosition().getPositionY() - closest.getPosition().getPositionY();
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    moveLeft();
                } else {
                    moveRight();
                }
            } else {
                if (dy > 0) {
                    moveUp();
                } else {
                    moveDown();
                }
            }
        } else {
            randomAction();
        }
    }

    @Override
    public void interact(Unit unit) {
        unit.visited(this);
    }

    @Override
    public String description() {
        return String.format("%s\t%s", super.description(), getVisionRangeString());
    }

}
