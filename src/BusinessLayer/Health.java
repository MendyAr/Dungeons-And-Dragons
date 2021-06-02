package BusinessLayer;

public class Health {
    //fields
    private Integer healthPool;
    private Integer healthAmount;

    //constructor
    public Health(Integer healthPool){
        this.healthPool = healthPool;
        init();
    }

    //methods

    public void init(){
        healthAmount = healthPool;
    }

    public Integer getCurrentHP() {
        return healthAmount;
    }

    public Integer getMaxHP() {
        return healthPool;
    }

    public void increasePool(Integer increaseVal){
        healthPool += increaseVal;
        init();
    }

    public void addHP(Integer addition){
        healthAmount = Math.min(healthAmount + addition, healthPool);
    }

    public void subHP(Integer subtraction){
        healthAmount = Math.max(healthAmount - subtraction, 0);
    }

    public String toString(){
        return "Health: " + healthAmount + '/' + healthPool;
    }
}
