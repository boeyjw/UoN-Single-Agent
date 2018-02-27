package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.Cell;
import uk.ac.nott.cs.g53dia.library.Tanker;

import java.util.*;

public class Interceptor extends Mapper {
    private CoreEntity lastClosestWellSeen;
    private CoreEntity lastClosestFuelPumpSeen;

    private Log l;

    public Interceptor() {
        lastClosestWellSeen = null;
        lastClosestFuelPumpSeen = null;
        l = new Log(true);
    }

    public void intercept(Deque<Cell> moves, Tanker t, long timestep, List<CoreEntity> taskedStation) {
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
        if(needDispose || needRefuel && !taskedStation.isEmpty()) {
            int taskedIndex = getIdenticalStation(taskedStation, moves.peekFirst());
            l.d("Tasked Index: " + taskedIndex);
            if(taskedIndex != Integer.MIN_VALUE) {
                int dist = Tanker.VIEW_RANGE + Math.toIntExact(timestep - (needDispose ? lastClosestWellSeen.getLastVisited() : lastClosestFuelPumpSeen.getLastVisited())) +
                        Calculation.modifiedManhattenDistance(new Coordinates(Tanker.VIEW_RANGE, Tanker.VIEW_RANGE), taskedStation.get(taskedIndex).getCoord());
                l.d("Distance: " + dist);
                if(!super.acceptableFuelLevel(100 - dist, dist)) {
                    moves.removeFirst();
                }
            }
            l.d("INTERCEPTED");
        }
        if(needDispose) {
            moves.push(lastClosestWellSeen.getEntity());
        }
        if(needRefuel) {
            moves.push(lastClosestFuelPumpSeen.getEntity());
        }
        if(moves.isEmpty())
            Explorer.explorerMode = true;
    }

    private int getIdenticalStation(List<CoreEntity> taskedStation, Cell station) {
        if(!EntityChecker.isStation(station))
            return Integer.MIN_VALUE;
        int i = 0;
        for(CoreEntity s : taskedStation) {
            if(s.getEntity().equals(station)) {
                return i;
            }
            i++;
        }

        return Integer.MIN_VALUE;
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
