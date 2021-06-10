package BusinessLayer.util;

public class Resource {
    //fields
    private int pool;
    private int currAmount;

    //constructor
    public Resource(int pool){
        this.pool = pool;
        init();
    }

    //methods

    public void init(){
        currAmount = pool;
    }

    public int getCurrent() {
        return currAmount;
    }

    public void setCurrent(Integer currAmount) {
        if (currAmount < 0 || currAmount > pool)
            throw new IllegalArgumentException("currAmount can't be lower than 0 or greater than pool = " + pool);
        this.currAmount = currAmount;
    }

    public int getMax() {
        return pool;
    }

    public void increasePool(int increaseVal){
        pool += increaseVal;
    }

    public boolean addAmount(int addition){
        currAmount = Math.min(currAmount + addition, pool);
        return currAmount == pool;
    }

    public boolean subAmount(int subtraction){
        currAmount = Math.max(currAmount - subtraction, 0);
        return currAmount == 0;
    }

    public String toString(){
        return String.format("%d%c%d", currAmount, '/', pool);
    }
}
