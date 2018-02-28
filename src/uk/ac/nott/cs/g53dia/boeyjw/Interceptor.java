package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.Cell;
import uk.ac.nott.cs.g53dia.library.Tanker;

import java.util.*;

/**
 * Interrupts agent's current moveset (before or after planning) to satisfy urgent needs.
 */
public class Interceptor extends Mapper {
    private CoreEntity lastClosestWellSeen;
    private CoreEntity lastClosestFuelPumpSeen;

    private Log l;

    public Interceptor() {
        lastClosestWellSeen = null;
        lastClosestFuelPumpSeen = null;
        l = new Log(true);
    }

    /**
     * Reacts to agent environment based on agent's urgent needs (dispose waste && refuel)
     * Directly inserts into moveset using last seen entities {@link uk.ac.nott.cs.g53dia.library.FuelPump} and {@link uk.ac.nott.cs.g53dia.library.Well}
     * @param moves The current moves set, whether planned or unplanned
     * @param t Tanker object to get tanker status
     * @param timestep Current timestep to estimate distance between tanker and last seen entities
     * @param taskedStation List of recently seen tasked stations, used to compare with moveset to check if
     *                      after intercepting, is the next move still feasible with fuel restrictions
     */
    public void intercept(Deque<Cell> moves, Tanker t, long timestep, List<CoreEntity> taskedStation) {
        boolean needDispose = false;
        boolean needRefuel = false;
        if(!super.acceptableWasteLevel(t.getWasteLevel())) {
            if(!moves.isEmpty() && !checkTwoSteps(moves)) {
                needDispose = true;
            }
            else if(moves.size() == 1 && EntityChecker.isWell(moves.peekFirst())) {
                moves.removeFirst();
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
        // If there is a tasked station after interrupt but is no longer feasible to go over to finish the task, remove it
        if(needDispose || needRefuel && !taskedStation.isEmpty() && EntityChecker.isStation(moves.peekFirst())) {
            int taskedIndex = getIdenticalStation(taskedStation, moves.peekFirst());
            l.d("Tasked Index: " + taskedIndex);
            if(taskedIndex != Integer.MIN_VALUE) {
                int dist = Tanker.VIEW_RANGE + Math.toIntExact(timestep - (needDispose && lastClosestWellSeen != null ?
                        lastClosestWellSeen.getLastVisited() : lastClosestFuelPumpSeen.getLastVisited())) +
                        Calculation.modifiedManhattenDistance(Coordinates.getTankerCoordinate(), taskedStation.get(taskedIndex).getCoord());

                l.d("Distance: " + dist);
                if(!super.acceptableFuelLevel(100 - dist, dist)) {
                    moves.removeFirst();
                }
            }
        }
        if(needDispose && lastClosestWellSeen != null) {
            moves.push(lastClosestWellSeen.getEntity());
        }
        if(needRefuel) {
            moves.push(lastClosestFuelPumpSeen.getEntity());
        }
        if(needDispose || needRefuel) {
            l.d("INTERCEPTED: " + (needDispose ? "WELL " : "") + (needRefuel ? "FUEL" : ""));
        }
        if(moves.isEmpty()) {
            Explorer.explorerMode = true;
        }
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

    public void setLastClosestSeen(CoreEntity well, CoreEntity fuelPump, long timestep) {
        if(well != null) {
            lastClosestWellSeen = well;
        }
        if(fuelPump != null) {
            lastClosestFuelPumpSeen = fuelPump;
            if(well == null && lastClosestWellSeen != null) {
                int dist = Tanker.VIEW_RANGE + Math.toIntExact(timestep - lastClosestWellSeen.getLastVisited()) +
                        Calculation.modifiedManhattenDistance(Coordinates.getTankerCoordinate(), lastClosestFuelPumpSeen.getCoord());
                if(!super.acceptableFuelLevel(100 - dist, dist)) {
                    lastClosestWellSeen = null;
                }
            }
        }
    }
}
