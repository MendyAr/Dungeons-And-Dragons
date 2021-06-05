package BusinessLayer;

import CallBacks.MessageCallback;
import CallBacks.MoveCallback;
import CallBacks.OnDeathCallback;

import java.util.List;

abstract public class Unit extends Tile{

    //fields

    protected final String name;
    protected Health health;
    protected Integer attackPoints;
    protected Integer attackRoll;
    protected Integer defensePoints;
    protected Integer defenseRoll;
    protected List<Unit> enemies;
    protected OnDeathCallback deathCallback;
    protected MessageCallback msgCallback;
    protected MoveCallback moveCallback;

    //constructor

    public Unit(char tileChar, String name, Integer healthPool, Integer attackPoints, Integer defensePoints) {
        super(tileChar);
        this.name = name;
        this.health = new Health(healthPool);
        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
    }

    //methods

    public String getName() {
        return name;
    }

    public String getAttack() {
        return "Attack: " + attackPoints;
    }

    public String getDefense() {
        return "Defense: " + defensePoints;
    }

    public String description() {
        return getName() + "\t\t" + health.toString() + "\t" + getAttack() + "\t" + getDefense();
    }

    protected void increaseAtt(Integer value){
        attackPoints += value;
    }

    protected void increaseDef(Integer value){
        defensePoints += value;
    }

    protected void attack() {
        attackRoll = (int)(Math.random() * (attackPoints + 1));
        msgCallback.call(String.format("%s rolled %d attack points.", getName(), attackRoll));
    }

    protected void defend(){
        defenseRoll= (int)(Math.random() * (defensePoints + 1));
        msgCallback.call(String.format("%s rolled %d defense points.", getName(), defenseRoll));
    }

    protected Integer calcDMG(Integer att, Integer def){
        return Math.max(att-def, 0);
    }

    protected void dealDamage(Unit attacker){
        Integer damage = calcDMG(attacker.attackRoll, defenseRoll);
        msgCallback.call(String.format("%s dealt %d damage to %s.", attacker.getName(), damage, getName()));
        if (health.subHP(damage)){
            attacker.onKill(this);
            onDeath();
        }
    }

    public void combat(Unit defender){
        msgCallback.call(String.format("%s engaged in combat with %s.", getName(), defender.getName()));
        msgCallback.call(description());
        msgCallback.call(defender.description());
        attack();
        defender.defend();
        defender.dealDamage(this);
    }

    public void accept(Tile tile){
        tile.interact(this);
    }

    public void visited(Empty empty) {
        Position tmp = empty.getPosition();
        empty.setPosition(getPosition());
        setPosition(tmp);
    }

    public void visited(Wall wall){}

    public abstract void visited(Enemy enemy);
    public abstract void visited(Player player);

    public void moveLeft(){
        moveCallback.move(new Position(position.getPositionX()-1, position.positionY));
    }

    public void moveUp(){
        moveCallback.move(new Position(position.getPositionX(), position.positionY-1));
    }

    public void moveRight(){
        moveCallback.move(new Position(position.getPositionX()+1, position.positionY));
    }

    public void moveDown(){
        moveCallback.move(new Position(position.getPositionX(), position.positionY+1));
    }

    public void DoNothing(){}
    public abstract void turn();
    public abstract void onKill(Unit kill);
    public abstract void onDeath();
}
