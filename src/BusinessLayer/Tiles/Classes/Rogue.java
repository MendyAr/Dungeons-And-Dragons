package BusinessLayer.Tiles.Classes;
import BusinessLayer.Tiles.Enemy;
import BusinessLayer.Tiles.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Rogue extends Player {

    // fields

    protected static final String abilityName = "Fan of Knives";
    protected static final int R_ATTACK_BONUS = 3;
    private static final Integer energyPool = 100;

    private final Integer abilityCost;

    private Integer energy;


    // constructor

    public Rogue(String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer abilityCost) {
        super(name, healthPool, attackPoints, defensePoints);
        this.energy = energyPool;
        this.abilityCost = abilityCost;
    }

    // getters & setters

    public Integer getEnergy() {
        return energy;
    }

    public static Integer getEnergyPool() {
        return energyPool;
    }

    public Integer getAbilityCost() {
        return abilityCost;
    }

    protected void increaseEnergy(Integer increaseVal) {
        energy = Math.min(energy + increaseVal, energyPool);
    }

    protected void decreaseEnergy(Integer decreaseVal) {
        energy = Math.max(energy - decreaseVal, 0);
    }

    protected void resetEnergy() {
        energy = energyPool;
    }

    // methods

    @Override
    public void turn() {
        super.turn();
        increaseEnergy(10);
    }

    @Override
    public void castAbility() {

        if (getEnergy() < getAbilityCost()) {
            msgCallback.call(getName() + " tried to cast " + abilityName + ", but missing " + (getAbilityCost() - getEnergy()) + " energy.");
            return;
        }

        msgCallback.call(getName() + " cast " + abilityName);
        // filter enemies in range
        List<Enemy> enemiesInRange =  enemies.stream().filter(e -> range(e)<2).collect(Collectors.toList());
                /*new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (range(enemy) < 2) {
                enemiesInRange.add(enemy);
            }
        }

                 */
        attackRoll = attackPoints;
        enemiesInRange.forEach(e -> e.dealDamage(this));
        /*
        for (Enemy enemy : enemiesInRange) {
            enemy.dealDamage(this);
        }
         */

        decreaseEnergy(getAbilityCost());
    }

    public void lvlUp() {
        super.lvlUp();
        resetEnergy();
        increaseAtt(R_ATTACK_BONUS * level);
    }

    public String getEnergyString() {
        return String.format("Energy: %d/%d", getEnergy(), getEnergyPool());
    }

    public String description() {
        return String.format("%s\t%s", super.description(), getEnergyString());
    }
}