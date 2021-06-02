package BusinessLayer;

abstract public class Tile {

    //fields

    protected Integer px;
    protected Integer py;
    protected Character tileChar;

    //constructors

    public Tile(Integer px, Integer py){
        this.px = px;
        this.py = py;
    }

    //methods

    public String toString(){
        return tileChar.toString();
    }
}
