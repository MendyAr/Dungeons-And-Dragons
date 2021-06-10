package BusinessLayer.Tiles.Classes;
import BusinessLayer.Tiles.Player;
import BusinessLayer.Tiles.Unit;
import BusinessLayer.util.Resource;

import java.util.List;
import java.util.stream.Collectors;

public class Rogue extends Player {

    // fields

    protected static final String abilityName = "Fan of Knives";
    protected static final int R_ATTACK_BONUS = 3;
    private static final Integer energyPool = 100;

    private final Integer abilityCost;

    private final Resource energy;


    // constructor

    public Rogue(String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer abilityCost) {
        super(name, healthPool, attackPoints, defensePoints);
        this.energy = new Resource(energyPool);
        this.abilityCost = abilityCost;
    }

    // getters & setters

    public Resource getEnergy() {
        return energy;
    }

    public static Integer getEnergyPool() {
        return energyPool;
    }

    public Integer getAbilityCost() {
        return abilityCost;
    }

    public String getEnergyString() {
        return String.format("Energy: %s", energy.toString());
    }

    // methods

    @Override
    public void turn() {
        super.turn();
        energy.addAmount(10);
    }

    @Override
    public void castAbility() {

        if (energy.getCurrent() < getAbilityCost()) {
            msgCallback.call(getName() + " tried to cast " + abilityName + ", but missing " + (getAbilityCost() - energy.getCurrent()) + " energy.");
            return;
        }

        msgCallback.call(getName() + " cast " + abilityName);
        // filter enemies in range
        List<Unit> enemiesInRange =  enemies.stream().filter(e -> range(e)<2).collect(Collectors.toList());

        attackRoll = attackPoints;
        enemiesInRange.forEach(e -> e.dealDamage(this));

        energy.subAmount(getAbilityCost());
    }

    public void lvlUp() {
        super.lvlUp();
        energy.init();
        increaseAtt(R_ATTACK_BONUS * level);
    }

    public String description() {
        return String.format("%s\t%s", super.description(), getEnergyString());
    }
}