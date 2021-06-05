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
        super(name, healthPool, attackPoints, defensePoints, abilityName);
        this.manaPool = manaPool;
        this.currentMana = manaPool/4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = hitsCount;
        this.abilityRange = abilityRange;
    }

    // getters & setters

    public Integer getManaPool(){
            return manaPool;
        }

    public Integer getCurrentMana(){
        return currentMana;
    }

    public Integer getSpellPower(){ return spellPower;}

    public Integer getManaCost(){ return manaCost;}

    protected void increaseManaPool(Integer increaseVal){
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
    public void Interact(Unit tile) {

    }

        @Override
        public void castAbility(List<Enemy> enemies) {

            if(getCurrentMana() < getManaCost()) {
                msgCallback.call(getName() + " tried to cast " + abilityName + ", but he is missing " + (getManaCost()-getCurrentMana())  + " mana.");
                return;
            }

            msgCallback.call(getName() + " cast " + abilityName);
            // filter enemies in range
            List<Enemy> enemiesInRange = new ArrayList<>();
            for (Enemy enemy: enemies) {
                if (Range(enemy) < abilityRange) {
                    enemiesInRange.add(enemy);
                }
            }
            int hits = 0;
            while(enemiesInRange.size() != 0 & hits < hitsCount) {
                // choose a random enemy
                int randomEnemy = (int) ( Math.random() * enemiesInRange.size());
                Enemy enemy = enemiesInRange.get(randomEnemy);
                // deal damage
                enemy.dealDamage(getSpellPower());
                hits++;
                if(enemy.health.getCurrentHP() == 0){
                    enemiesInRange.remove(enemy);
                }
            }
            decreaseMana(getManaCost());
        }

        public void lvlUp(){
            super.lvlUp();
            increaseManaPool(MANA_POOL_BONUS * level);
            regenerateMana((int)(getManaPool() * MANA_REGENERATION_BONUS));
            increaseSpellPower(SPELL_POWER_BONUS * level);
        }

        @Override
        public void turn(List<Unit> enemies) {
            regenerateMana(1 * level);
        }

        public String description(){
            return super.description() + "\tMana: " + getCurrentMana() + "/" + getManaPool() +  "\tSpell Power: " + getSpellPower();
        }

}


