package util;
import BusinessLayer.Tile;

import java.util.Comparator;

public class TileComparator implements Comparator<Tile> {

    @Override
    public int compare(Tile T1, Tile T2) {
        return T1.getPosition().compareTo(T2.getPosition());
    }
}
