package BusinessLayer.Tiles;

import BusinessLayer.util.Position;

abstract public class Tile {

    //fields

    protected Character tileChar;
    protected Position position;

    //constructor
    public Tile(char tileChar){
        this.tileChar = tileChar;
    }

    //initializer
    public void init(Position position){
        setPosition(position);
    }

    //getter

    public Character getTileChar() {
        return tileChar;
    }

    public Position getPosition() {
        return position;
    }

    //setters
    public void setPosition(Position position) {
        this.position = position;
    }

    public void setTileChar(Character tileChar) {
        this.tileChar = tileChar;
    }

    // methods

    //returns the euclidean distance between tiles
    public double range(Tile tile){
        if(tile == null)
            throw new NullPointerException("Trying to check range to a null tile");
        return this.position.range(tile.position);
    }

    public String toString(){
        return tileChar.toString();
    }

    public abstract void interact(Unit unit);
}
