package BusinessLayer;

public class Wall extends Tile{

    //constructors
    public Wall(){
        super('#');
    }

    @Override
    public void interact(Unit unit) {
        unit.visited(this);
    }
}
