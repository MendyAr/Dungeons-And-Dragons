package BusinessLayer;

public class Position implements Comparable<Position>{

    //fields
    protected int positionX;
    protected int positionY;

    //constructor
    public Position(int x, int y){
        positionX = x;
        positionY = y;
    }

    //getters
    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    //setters
    protected void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    protected void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    //methods
    //returns the euclidean distance
    public double Range(Position P){
        if(P == null)
            throw new NullPointerException("Trying to check range to a null Position");

        return Math.sqrt(Math.pow(positionX-P.positionX, 2) + Math.pow(positionY-P.positionY, 2));
    }

    @Override
    public int compareTo(Position P) {
        if(P == null)
            throw new NullPointerException("Trying to compare to a null Position");

        if(this.positionY < P.positionY | (this.positionY == P.positionY & this.positionX < P.positionX))
        {
            return -1;
        }
        else if(this.positionY > P.positionY | (this.positionY == P.positionY & this.positionX > P.positionX))
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}
