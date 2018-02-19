package uk.ac.nott.cs.g53dia.boeyjw;

import java.util.ArrayDeque;

public class Coordinates extends TwoTuple {
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

    public TwoTuple manhattenAbsolute(Coordinates target) {
        return new TwoTuple(Math.abs(target.getX() - this.getX()), Math.abs(target.getY() - this.getY()));
    }

    public ArrayDeque<Integer> planMoveTo(Coordinates source, Coordinates target) {
        ArrayDeque<Integer> moves = new ArrayDeque<>();
        TwoTuple preManhatten = source.manhattenAbsolute(target);

        int diagonal = preManhatten.getMin();
        int straight = preManhatten.getMax() - preManhatten.getMin();
        int bearing = Calculation.targetBearing(source, target);

        if(diagonal != 0) {
            moves.add(Calculation.targetBearing(source, target));
            moves.add(diagonal);
        }
        if(straight != 0) {
            moves.add(Calculation.targetBearing((Coordinates) source.simpleOperation(diagonal, null, "+"), target));
            moves.add(straight);
        }

        return moves;
    }
}
