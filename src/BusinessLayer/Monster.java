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
    public void turn(List<Unit> players) {
        Unit closest = findClosestTarget(players);
        if (getPosition().range(closest.getPosition()) < visionRange) {
            Integer dx = getPosition().getPositionX() - closest.getPosition().getPositionX();
            Integer dy = getPosition().getPositionY() - closest.getPosition().getPositionY();
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    //moveLeft
                } else {
                    //moveRight
                }
            } else {
                if (dy > 0) {
                    //moveUp
                } else {
                    //moveDown
                }
            }
        } else {
            //randomAction
        }
    }

    @Override
    public void visited(Enemy enemy) {
    }

    @Override
    public void visited(Player player) {
        combat(player);
    }

    @Override
    public void interact(Unit unit) {
        unit.visited(this);
    }
}
