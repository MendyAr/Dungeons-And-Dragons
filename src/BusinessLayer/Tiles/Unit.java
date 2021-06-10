package BusinessLayer.Tiles;

import BusinessLayer.CallBacks.MessageCallback;
import BusinessLayer.CallBacks.MoveCallback;
import BusinessLayer.CallBacks.OnDeathCallback;
import BusinessLayer.util.*;

import java.util.List;

abstract public class Unit extends Tile{

    //fields

    protected final String name;
    protected Resource health;
    protected Integer attackPoints;
    protected Integer attackRoll;
    protected Integer defensePoints;
    protected Integer defenseRoll;
    protected List<? extends Unit> enemies;
    protected OnDeathCallback deathCallback;
    protected MessageCallback msgCallback;
    protected MoveCallback moveCallback;
    protected RandomNumberGenerator rng;

    //constructor

    public Unit(char tileChar, String name, Integer healthPool, Integer attackPoints, Integer defensePoints) {
        super(tileChar);
        this.name = name;
        this.health = new Resource(healthPool);
        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
    }

    //initializers
    public void init(Position position, List<? extends Unit> enemies){
        super.init(position);
        this.enemies = enemies;
    }

    public void init(Position position, List<? extends Unit> enemies, OnDeathCallback deathCallback, MessageCallback msgCallback, MoveCallback moveCallback, RandomNumberGenerator rng){
        init(position, enemies);
        this.deathCallback = deathCallback;
        this.msgCallback = msgCallback;
        this.moveCallback = moveCallback;
        this.rng = rng;
    }

    //getters
    public String getName() {
        return name;
    }

    public Resource getHealth() {
        return health;
    }

    public String getHealthString(){
        return "Health: " + health;
    }

    public String getAttackString() {
        return "Attack: " + attackPoints;
    }

    public String getDefenseString() {
        return "Defense: " + defensePoints;
    }

    // setters
    public void increaseAtt(Integer value){
        attackPoints += value;
    }

    public void increaseDef(Integer value){
        defensePoints += value;
    }

    //methods
    public void swapPositions(Tile tile){
        Position tmp = getPosition();
        setPosition(tile.getPosition());
        tile.setPosition(tmp);
    }

    // combat methods
    public void attack() {
        attackRoll = rng.generate(0, attackPoints);
        msgCallback.call(String.format("%s rolled %d attack points.", getName(), attackRoll));
    }

    public void defend(){
        defenseRoll= rng.generate(0, defensePoints);
        msgCallback.call(String.format("%s rolled %d defense points.", getName(), defenseRoll));
    }

    public Integer calcDMG(Integer att, Integer def){
        return Math.max(att-def, 0);
    }

    public void dealDamage(Unit attacker){
        defend();
        Integer damage = calcDMG(attacker.attackRoll, defenseRoll);
        msgCallback.call(String.format("%s dealt %d damage to %s.", attacker.getName(), damage, getName()));
        if (health.subAmount(damage)){// check if target is not alive
            onKill(attacker);
            onDeath();
        }
    }

    public void combat(Unit defender){
        msgCallback.call(String.format("%s engaged in combat with %s.", getName(), defender.getName()));
        msgCallback.call(description());
        msgCallback.call(defender.description());
        attack();
        defender.dealDamage(this);
    }

    // tile interactions
    public void visited(Empty empty) {
        swapPositions(empty);
    }

    public void visited(Wall wall){}

    public abstract void visited(Enemy enemy);
    public abstract void visited(Player player);

    // actions
    public void moveLeft(){
        moveCallback.move(new Position(position.getPositionX()-1, position.getPositionY()));
    }

    public void moveUp(){
        moveCallback.move(new Position(position.getPositionX(), position.getPositionY()-1));
    }

    public void moveRight(){
        moveCallback.move(new Position(position.getPositionX()+1, position.getPositionY()));
    }

    public void moveDown(){
        moveCallback.move(new Position(position.getPositionX(), position.getPositionY()+1));
    }

    public void doNothing(){}

    public void randomAction(){
        Integer random = rng.generate(0, 4);
        switch (random){
            case 0:
                doNothing();
                break;
            case 1:
                moveUp();
                break;
            case 2:
                moveLeft();
                break;
            case 3:
                moveDown();
                break;
            case 4:
                moveRight();
                break;
        }
    }

    public String description() {
        return String.format("%s\t\t%s\t%s\t%s", getName(), getHealthString(), getAttackString(), getDefenseString());
    }

    public abstract void turn();
    public abstract void onKill(Unit killer);
    public abstract void onEnemyKill(Enemy kill);
    public abstract void onPlayerKill(Player kill);
    public abstract void onDeath();
}
