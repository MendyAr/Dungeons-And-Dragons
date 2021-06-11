package BusinessLayer.Tiles;

public class Empty extends Tile{

    //constructors
    public Empty(){
        super('.');
    }

    @Override
    public void interact(Unit unit) {
        unit.visited(this);
    }
}
