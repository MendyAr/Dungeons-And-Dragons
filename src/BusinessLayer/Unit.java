package BusinessLayer;

abstract public class Unit extends Tile{

    //fields

    protected final String name;
    protected Integer healthPool;
    protected Integer healthAmount;
    protected Integer attackPoints;
    protected Integer defensePoints;

    //constructor

    public Unit(Integer px, Integer py, String name, Integer healthPool, Integer attackPoints, Integer defensePoints) {
        super(px, py);
        this.name = name;
        this.healthPool = healthPool;
        healthAmount = healthPool;
        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
    }

    //methods

    public String getName() {
        return name;
    }

    public String description() {
        return name + "\t\tHealth: " + healthAmount + '/' + healthPool + "\tAttack: " + attackPoints + "\tDefense: " + defensePoints;
    }
}
