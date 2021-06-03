package BusinessLayer;

abstract public class Tile {

    //fields

    protected Character tileChar;
    protected Position position;

    //constructor
    public Tile(char tileChar){
        this.tileChar = tileChar;
    }

    //initializer
    public void initialize(Position position){
        this.position = position;
    }

    //getter
    public char getTile() {
        return tileChar;
    }

    public void setTileChar(Character tileChar) {
        this.tileChar = tileChar;
    }

    public Position getPosition() {
        return position;
    }

    //setters
    public void setPosition(Position position) {
        this.position = position;
    }

    // methods

    //returns the euclidean distance between tiles
    public double Range(Tile tile){
        if(tile == null)
            throw new NullPointerException("Trying to check range to a null tile");

        return this.position.Range(tile.position);
    }

    public String toString(){
        return tileChar.toString();
    }

    public abstract void Interact(Unit unit);
}
