package BusinessLayer;
import java.util.ArrayList;
import java.util.List;

public class Warrior extends Player{

    // fields

    protected static final String abilityName = "Avenger’s Shield";
    protected static final int W_HEALTH_BONUS = 5;
    protected static final int W_ATTACK_BONUS = 2;
    protected static final int W_DEFENSE_BONUS = 1;

    private final Integer abilityCooldown;
    private Integer remainingCooldown;

    // constructor

    public Warrior(String name, Integer healthPool, Integer attackPoints, Integer defensePoints, List<Enemy> enemies, Integer abilityCooldown) {
        super(name, healthPool, attackPoints, defensePoints, abilityName, enemies);
        this.abilityCooldown = abilityCooldown;
        this.remainingCooldown = 0;
    }

    // getters & setters

    public Integer getAbilityCooldown(){
        return abilityCooldown;
    }

    public Integer getRemainingCooldown(){
        return remainingCooldown;
    }

    protected void decCooldown() {
        remainingCooldown = Math.min(0, getRemainingCooldown() - 1);
    }

    protected void resetCooldown(){
            remainingCooldown = 0;
    }

    // methods

    @Override
    public void castAbility() {

        if(getRemainingCooldown() > 0) {
            msgCallback.call(getName() + " tried to cast " + abilityName + ", but there is a cooldown: " + abilityCooldown + ".");
            return;
        }

        msgCallback.call(getName() + " cast " + abilityName);
        // filter enemies in range
        List<Enemy> enemiesInRange = new ArrayList<>();
        for (Enemy enemy: enemies) {
            if (Range(enemy) < 3) {
                enemiesInRange.add(enemy);
            }
        }
        if(enemiesInRange.size() != 0) {
            // choose a random enemy
            int randomEnemy = (int) ( Math.random() * enemiesInRange.size());
            Enemy enemy = enemiesInRange.get(randomEnemy);
            // deal damage
            attackRoll = (int) (health.getMaxHP() * 0.1);
            enemy.dealDamage(this);
        }

        // heal Warrior
        health.addHP(10 * defensePoints);
        remainingCooldown = abilityCooldown;


    }

    public void lvlUp(){
        super.lvlUp();
        resetCooldown();
        health.increasePool(W_HEALTH_BONUS * level);
        increaseAtt(W_ATTACK_BONUS * level);
        increaseDef(W_DEFENSE_BONUS * level);

    }

    @Override
    public void turn(List<Unit> enemies) {


        decCooldown();
    }

    public String description(){
        return super.description() + "\tCooldown: " + getRemainingCooldown() + "/" + getAbilityCooldown();
    }

}
