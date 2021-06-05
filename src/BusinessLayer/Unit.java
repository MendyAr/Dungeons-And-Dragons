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

    //initializers
    public void init(Position position, List<Unit> enemies){
        super.init(position);
        this.enemies = enemies;
    }

    public void init(Position position, List<Unit> enemies, OnDeathCallback deathCallback, MessageCallback msgCallback, MoveCallback moveCallback){
        init(position, enemies);
        this.deathCallback = deathCallback;
        this.msgCallback = msgCallback;
        this.moveCallback = moveCallback;
    }

    public String getName() {
        return name;
    }

    public String getAttack() {
        return "Attack: " + attackPoints;
    }

    public String getDefense() {
        return "Defense: " + defensePoints;
    }

    //methods

    public String description() {
        return String.format("%s\t\t%s\t%s\t%s", getName(), health.toString(), getAttack(), getDefense());
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
            onKill(attacker);
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
    public abstract void onKill(Unit killer);
    public abstract void onEnemyKill(Enemy kill);
    public abstract void onPlayerKill(Player kill);
    public abstract void onDeath();
}
