package uk.ac.nott.cs.g53dia.boeyjw;

import java.util.ArrayDeque;

public class Coordinates extends TwoNumberTuple {
    public Coordinates(int x, int y) {
        super(x, y);
    }

    public void modifyCoordinates(int x, int y) {
        if(x > 0)
            this.x = x;
        if(y > 0)
            this.y = y;
    }

    public int distanceBetweenCoordinate(Coordinates target) {
        return Calculation.modifiedManhattenDistance(this, target);
    }

    public TwoNumberTuple manhattenAbsolute(Coordinates target) {
        return new TwoNumberTuple(Math.abs(target.getValue(0) - this.getValue(0)),
                Math.abs(target.getValue(1) - this.getValue(1)));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
