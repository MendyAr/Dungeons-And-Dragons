package BusinessLayer.util;

import java.util.Objects;

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
    public double range(Position P){
        if(P == null)
            throw new NullPointerException("Trying to check range to a null Position");

        return Math.sqrt(Math.pow(positionX-P.positionX, 2) + Math.pow(positionY-P.positionY, 2));
    }

    @Override
    public int compareTo(Position P) {
        if(P == null)
            throw new NullPointerException("Trying to compare to a null Position");

        int yDist = positionY - P.positionY;
        if(yDist != 0)
        {
            return yDist;
        }
        return positionX - P.positionX;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return positionX == position.positionX && positionY == position.positionY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionX, positionY);
    }
}
