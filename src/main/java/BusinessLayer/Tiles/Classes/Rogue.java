package BusinessLayer.Tiles.Classes;
import BusinessLayer.Tiles.Player;
import BusinessLayer.Tiles.Unit;
import BusinessLayer.util.Resource;

import java.util.List;
import java.util.stream.Collectors;

public class Rogue extends Player {

    // fields

    protected static final String abilityName = "Fan of Knives";
    protected static final String abilityResourceName = "Energy";
    protected static final int R_ATTACK_BONUS = 3;
    private static final Integer energyPool = 100;

    private final Integer abilityCost;

    // constructor

    public Rogue(String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer abilityCost) {
        super(name, healthPool, attackPoints, defensePoints, abilityName, new Resource(abilityResourceName, energyPool));
        this.abilityCost = abilityCost;
    }

    // getters & setters

    public Integer getAbilityCost() {
        return abilityCost;
    }

    // methods

    @Override
    public void turn() {
        super.turn();
        abilityResource.addAmount(10);
    }

    @Override
    public void castAbility() {
        if (getAbilityResource().getCurrent() < getAbilityCost()) {
            msgCallback.call(getName() + " tried to cast " + abilityName + ", but missing " + (getAbilityCost() - getAbilityResource().getCurrent()) + " energy.");
            return;
        }

        abilityUsed = true;
        msgCallback.call(getName() + " cast " + abilityName);
        // filter enemies in range
        List<Unit> enemiesInRange =  enemies.stream().filter(e -> range(e)<2).collect(Collectors.toList());

        attackRoll = attackPoints;
        enemiesInRange.forEach(e -> e.dealDamage(this));

        getAbilityResource().subAmount(getAbilityCost());
    }

    public void lvlUp() {
        super.lvlUp();
        getAbilityResource().init();
        increaseAtt(R_ATTACK_BONUS * level);
    }
}