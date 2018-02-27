package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.Cell;
import uk.ac.nott.cs.g53dia.library.Tanker;

import java.util.*;

public class Interceptor extends Mapper {
    private CoreEntity lastClosestWellSeen;
    private CoreEntity lastClosestFuelPumpSeen;

    public Interceptor() {
        lastClosestWellSeen = null;
        lastClosestFuelPumpSeen = null;
    }

    public void intercept(Deque<Cell> moves, Tanker t, long timestep) {
        boolean needDispose = false;
        boolean needRefuel = false;
        if(!super.acceptableWasteLevel(t.getWasteLevel())) {
            if(!moves.isEmpty() && !checkTwoSteps(moves)) {
                needDispose = true;
            }
            else if(moves.isEmpty()) {
                needDispose = true;
            }
        }
        else if(!moves.isEmpty() && EntityChecker.isWell(moves.peekFirst())) {
            moves.removeFirst();
        }
        if(!super.acceptableFuelLevel(t.getFuelLevel(), Math.toIntExact(timestep - lastClosestFuelPumpSeen.getLastVisited()) + Tanker.VIEW_RANGE)) {
            if(!moves.isEmpty() && !EntityChecker.isFuelPump(moves.peekFirst())) {
                needRefuel = true;
            }
            else if(moves.isEmpty()) {
                needRefuel = true;
            }
        }
        if(needDispose) {
            moves.push(lastClosestWellSeen.getEntity());
        }
        if(needRefuel) {
            moves.push(lastClosestFuelPumpSeen.getEntity());
        }
        if(moves.isEmpty())
            Explorer.explorerMode = true;
        if(needDispose || needRefuel)
            System.out.println("INTERCEPTED");
    }
    private boolean checkTwoSteps(Deque<Cell> moves) {
        int i = 0;
        for(Cell m : moves) {
            if(i >= 2)
                break;
            if(EntityChecker.isWell(m))
                return true;
            else
                i++;
        }
        return false;
    }

    public void setLastClosestSeen(CoreEntity well, CoreEntity fuelPump) {
        if(well != null) {
            lastClosestWellSeen = well;
        }
        if(fuelPump != null) {
            lastClosestFuelPumpSeen = fuelPump;
        }
    }
}
