package BusinessLayer;

import java.util.ArrayList;
import java.util.List;

public class Mage extends Player {

    // fields

    protected static final String abilityName = "Blizzard";
    protected static final int MANA_POOL_BONUS = 25;
    protected static final double MANA_REGENERATION_BONUS = 0.25;
    protected static final int SPELL_POWER_BONUS = 10;

    private Integer manaPool;
    private Integer currentMana;
    private Integer spellPower;
    private final Integer manaCost;
    private final Integer hitsCount;
    private final Integer abilityRange;

    // constructor

    public Mage(String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer manaPool, Integer manaCost, Integer spellPower, Integer hitsCount, Integer abilityRange) {
        super(name, healthPool, attackPoints, defensePoints);
        this.manaPool = manaPool;
        this.currentMana = manaPool / 4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = hitsCount;
        this.abilityRange = abilityRange;
    }

    // getters & setters

    public Integer getManaPool() {
        return manaPool;
    }

    public Integer getCurrentMana() {
        return currentMana;
    }

    public Integer getSpellPower() {
        return spellPower;
    }

    public Integer getManaCost() {
        return manaCost;
    }

    protected void increaseManaPool(Integer increaseVal) {
        manaPool = manaPool + increaseVal;
    }

    protected void regenerateMana(Integer regenerationVal) {
        currentMana = Math.max(currentMana + currentMana * regenerationVal, manaPool);
    }

    protected void decreaseMana(Integer decreaseVal) {
        currentMana = Math.min(currentMana - decreaseVal, 0);
    }

    protected void increaseSpellPower(Integer IncreaseVal) {
        spellPower = spellPower + IncreaseVal;
    }

    // methods

    @Override
    public void turn() {
        super.turn();
        regenerateMana(1 * level);
    }

    @Override
    public void castAbility() {

        if (getCurrentMana() < getManaCost()) {
            msgCallback.call(getName() + " tried to cast " + abilityName + ", but missing " + (getManaCost() - getCurrentMana()) + " mana.");
            return;
        }

        msgCallback.call(getName() + " cast " + abilityName);
        // filter enemies in range
        List<Enemy> enemiesInRange = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (range(enemy) < abilityRange) {
                enemiesInRange.add(enemy);
            }
        }
        int hits = 0;
        attackRoll = getSpellPower();
        while (enemiesInRange.size() != 0 & hits < hitsCount) {
            // choose a random enemy
            int randomEnemy = (int) (Math.random() * enemiesInRange.size());
            Enemy enemy = enemiesInRange.get(randomEnemy);
            // deal damage
            enemy.dealDamage(this);
            hits++;
            if (enemy.health.getCurrentHP() == 0) {
                enemiesInRange.remove(enemy);
            }
        }
        decreaseMana(getManaCost());
    }

    public void lvlUp() {
        super.lvlUp();
        increaseManaPool(MANA_POOL_BONUS * level);
        regenerateMana((int) (getManaPool() * MANA_REGENERATION_BONUS));
        increaseSpellPower(SPELL_POWER_BONUS * level);
    }

    public String getManaString(){
        return String.format("Mana: %d/%d", getCurrentMana(), getManaPool());
    }

    public String getSpellPowerString(){
        return String.format("Spell Power: %d", getSpellPower());
    }

    public String description() {
        return String.format("%s\t%s\t%s", super.description(), getManaString(), getSpellPowerString());
    }
}