package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.Cell;
import uk.ac.nott.cs.g53dia.library.Tanker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class Interceptor extends Mapper {
    private Cell lastClosestWell;
    private Cell lastClosestFuelPump;

    public Interceptor() {
        lastClosestWell = null;
        lastClosestFuelPump = null;
    }

    public boolean intercept(List<Cell> moves, Tanker t) {
        return false;
    }
}
