package BusinessLayer.Tiles.Classes;

import BusinessLayer.Tiles.Player;
import BusinessLayer.Tiles.Unit;
import BusinessLayer.util.Resource;

import java.util.List;
import java.util.stream.Collectors;

public class Mage extends Player {

    // fields

    protected static final String abilityName = "Blizzard";
    protected static final int MANA_POOL_BONUS = 25;
    protected static final double MANA_REGENERATION_BONUS = 0.25;
    protected static final int SPELL_POWER_BONUS = 10;

    private final Resource mana;
    private Integer spellPower;
    private final Integer manaCost;
    private final Integer hitsCount;
    private final Integer abilityRange;

    // constructor

    public Mage(String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer manaPool, Integer manaCost, Integer spellPower, Integer hitsCount, Integer abilityRange) {
        super(name, healthPool, attackPoints, defensePoints);
        this.mana = new Resource(manaPool);
        this.mana.setCurrent(manaPool / 4);
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = hitsCount;
        this.abilityRange = abilityRange;
    }

    // getters & setters

    public Integer getSpellPower() {
        return spellPower;
    }

    public Integer getManaCost() {
        return manaCost;
    }

    protected void increaseSpellPower(Integer increaseVal) {
        spellPower +=  increaseVal;
    }

    public String getManaString(){
        return String.format("Mana: %s", mana.toString());
    }

    public String getSpellPowerString(){
        return String.format("Spell Power: %d", getSpellPower());
    }

    // methods

    @Override
    public void turn() {
        super.turn();
        mana.addAmount(level);
    }

    @Override
    public void castAbility() {

        if (mana.getCurrent() < getManaCost()) {
            msgCallback.call(getName() + " tried to cast " + abilityName + ", but missing " + (getManaCost() - mana.getCurrent()) + " mana.");
            return;
        }

        msgCallback.call(getName() + " cast " + abilityName);
        // filter enemies in range
        List<Unit> enemiesInRange = enemies.stream().filter(e -> range(e)<abilityRange).collect(Collectors.toList());

        int hits = 0;
        attackRoll = getSpellPower();
        while (enemiesInRange.size() != 0 & hits < hitsCount) {
            // choose a random enemy
            int randomEnemy = rng.generate(0, enemiesInRange.size()-1);
            Unit enemy = enemiesInRange.get(randomEnemy);
            // deal damage
            enemy.dealDamage(this);
            hits++;
            if (enemy.getHealth().getCurrent() == 0) {
                enemiesInRange.remove(enemy);
            }
        }
        mana.subAmount(getManaCost());
    }

    public void lvlUp() {
        super.lvlUp();
        mana.increasePool(MANA_POOL_BONUS * level);
        mana.increasePool((int) (mana.getMax() * MANA_REGENERATION_BONUS));
        increaseSpellPower(SPELL_POWER_BONUS * level);
    }

    public String description() {
        return String.format("%s\t%s\t%s", super.description(), getManaString(), getSpellPowerString());
    }
}