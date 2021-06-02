package BusinessLayer;

abstract public class Unit extends Tile{

    //fields

    protected final String name;
    protected Health health;
    protected Integer attackPoints;
    protected Integer defensePoints;

    //constructor

    public Unit(Integer px, Integer py, String name, Integer healthPool, Integer attackPoints, Integer defensePoints) {
        super(px, py);
        this.name = name;
        this.health = new Health(healthPool);
        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
    }

    //methods

    public String getName() {
        return name;
    }

    public String description() {
        return name + "\t\t" + health.toString() + "\tAttack: " + attackPoints + "\tDefense: " + defensePoints;
    }

    private Integer attack() {
        Integer attackRoll = (int)(Math.random() * (attackPoints + 1));
        return attackRoll;
    }

    private void defend(Integer attackRoll){
        Integer defenseRoll = (int)(Math.random() * (defensePoints + 1));
        health.subHP(Math.max(attackRoll-defenseRoll, 0));
        if (health.getCurrentHP() == 0)
            onDeath();
    }

    public void combat(Unit rival){
        rival.defend(attack());
    }

    public abstract void onDeath();
}
