package uk.ac.nott.cs.g53dia.boeyjw;

import java.util.ArrayDeque;

public abstract class Mapper {

    protected boolean acceptableFuelLevel(int estFuelLevel, int distToFuelPump) {
        return estFuelLevel > distToFuelPump + Threshold.REFUEL_ERROR_MARGIN.getThresh();
    }

    protected boolean acceptableWasteLevel(int estWasteLevel) {
        return Threshold.HIGHEST_WASTE.hitThresh(estWasteLevel);
    }

    protected ArrayDeque<Integer> planMoveTo(Coordinates source, Coordinates target) {
        ArrayDeque<Integer> moves = new ArrayDeque<>();
        TwoNumberTuple preManhatten = source.manhattenAbsolute(target);

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
