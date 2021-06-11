package BusinessLayer.Tiles.Classes;
import BusinessLayer.Tiles.Player;
import BusinessLayer.Tiles.Unit;
import BusinessLayer.util.Cooldown;

import java.util.List;
import java.util.stream.Collectors;

public class Warrior extends Player {

    // fields

    protected static final String abilityName = "Avenger's Shield";
    protected static final String abilityResourceName = "Cooldown";
    protected static final int W_HEALTH_BONUS = 5;
    protected static final int W_ATTACK_BONUS = 2;
    protected static final int W_DEFENSE_BONUS = 1;

    // constructor

    public Warrior(String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer abilityCooldown) {
        super(name, healthPool, attackPoints, defensePoints, abilityName, new Cooldown(abilityResourceName, abilityCooldown));
    }

    // methods

    @Override
    public void turn() {
        super.turn();
        if (!abilityUsed)
            abilityResource.subAmount(1);
    }

    @Override
    public void castAbility() {
        if (abilityResource.getCurrent() != 0) {
            msgCallback.call(String.format("%s tried to cast %s, but there is a cooldown: %d.", getName(), abilityName, abilityResource.getCurrent()));
            return;
        }

        abilityUsed = true;
        msgCallback.call(getName() + " cast " + abilityName);
        // filter enemies in range
        List<Unit> enemiesInRange = enemies.stream().filter(e -> range(e) < 3).collect(Collectors.toList());

        if (enemiesInRange.size() != 0) {
            // choose a random enemy
            int randomEnemy = rng.generate(0, enemiesInRange.size() - 1);
            Unit enemy = enemiesInRange.get(randomEnemy);
            // deal damage
            attackRoll = (int) (health.getMax() * 0.1);
            enemy.dealDamage(this);
        }

        // heal Warrior
        health.addAmount(10 * defensePoints);
    }

    public void lvlUp() {
        super.lvlUp();
        getAbilityResource().init();
        getHealth().increasePool(W_HEALTH_BONUS * level);
        increaseAtt(W_ATTACK_BONUS * level);
        increaseDef(W_DEFENSE_BONUS * level);
    }
}