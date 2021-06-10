package BusinessLayer.Tiles.Classes;
import BusinessLayer.Tiles.Enemy;
import BusinessLayer.Tiles.Player;
import BusinessLayer.Tiles.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Warrior extends Player {

    // fields

    protected static final String abilityName = "Avenger’s Shield";
    protected static final int W_HEALTH_BONUS = 5;
    protected static final int W_ATTACK_BONUS = 2;
    protected static final int W_DEFENSE_BONUS = 1;

    private final Integer abilityCooldown;
    private Integer remainingCooldown;

    // constructor

    public Warrior(String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer abilityCooldown) {
        super(name, healthPool, attackPoints, defensePoints);
        this.abilityCooldown = abilityCooldown;
        this.remainingCooldown = 0;
    }

    // getters & setters

    public Integer getAbilityCooldown() {
        return abilityCooldown;
    }

    public Integer getRemainingCooldown() {
        return remainingCooldown;
    }

    protected void decCooldown() {
        remainingCooldown = Math.min(0, getRemainingCooldown() - 1);
    }

    protected void resetCooldown() {
        remainingCooldown = 0;
    }

    // methods

    @Override
    public void turn() {
        super.turn();
        decCooldown();
    }

    @Override
    public void castAbility() {

        if (getRemainingCooldown() > 0) {
            msgCallback.call(String.format("%s tried to cast %s, but there is a cooldown: %d.", getName() ,abilityName, abilityCooldown));
            return;
        }

        msgCallback.call(getName() + " cast " + abilityName);
        // filter enemies in range
        List<Unit> enemiesInRange = enemies.stream().filter(e -> range(e) < 3).collect(Collectors.toList());
                /*new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (range(enemy) < 3) {
                enemiesInRange.add(enemy);
            }
        }

                 */
        if (enemiesInRange.size() != 0) {
            // choose a random enemy
            int randomEnemy = rng.generate(0, enemiesInRange.size()-1);
            Unit enemy = enemiesInRange.get(randomEnemy);
            // deal damage
            attackRoll = (int) (health.getMaxHP() * 0.1);
            enemy.dealDamage(this);
        }

        // heal Warrior
        health.addHP(10 * defensePoints);
        remainingCooldown = abilityCooldown;
    }

    public void lvlUp() {
        super.lvlUp();
        resetCooldown();
        health.increasePool(W_HEALTH_BONUS * level);
        increaseAtt(W_ATTACK_BONUS * level);
        increaseDef(W_DEFENSE_BONUS * level);

    }

    public String getCooldownString() {
        return String.format("Cooldown: %d/%d", getRemainingCooldown(), getAbilityCooldown());
    }

    public String description() {
        return String.format("%s\t%s", super.description(), getCooldownString());
    }
}