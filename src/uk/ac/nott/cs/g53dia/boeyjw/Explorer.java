package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.Cell;
import uk.ac.nott.cs.g53dia.library.MoveAction;

import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Handles exploration of the tanker
 */
public class Explorer extends Mapper {
    public static boolean explorerMode = false;
    private HashMap<Integer, Integer> crossDirectionMovement;
    private int direction;
    private long startExplorerTimestep;

    Explorer(Random r) {
        crossDirectionMovement = new HashMap<>();
        init();
        direction = r.nextInt(Threshold.TOTAL_DIRECTION_BOUND.getThreshold());
        startExplorerTimestep = Integer.MIN_VALUE;
    }

    /**
     * HashTable of cross directions to explore.
     * If the entire HasThTable is traversed, the tanker essentially moved all 8 directions
     */
    private void init() {
        crossDirectionMovement.put(MoveAction.NORTH, MoveAction.EAST);
        crossDirectionMovement.put(MoveAction.EAST, MoveAction.WEST);
        crossDirectionMovement.put(MoveAction.WEST, MoveAction.SOUTH);
        crossDirectionMovement.put(MoveAction.SOUTH, MoveAction.NORTHEAST);
        crossDirectionMovement.put(MoveAction.NORTHEAST, MoveAction.NORTHWEST);
        crossDirectionMovement.put(MoveAction.NORTHWEST, MoveAction.SOUTHEAST);
        crossDirectionMovement.put(MoveAction.SOUTHEAST, MoveAction.SOUTHWEST);
        crossDirectionMovement.put(MoveAction.SOUTHWEST, MoveAction.NORTH);
    }

    public int getAndUpdateDirection() {
        int dir = direction;
        direction = crossDirectionMovement.get(direction);
        return dir;
    }

    public long getStartExplorerTimestep() {
        return startExplorerTimestep;
    }

    public void setStartExplorerTimestep(long startExplorerTimestep) {
        this.startExplorerTimestep = startExplorerTimestep;
    }

    public void getPassbyTask(Deque<Cell> moves, int wasteLevel, List<CoreEntity> station) {
        if(explorerMode && !station.isEmpty() && moves.isEmpty() && super.acceptableWasteLevel(wasteLevel)) {
            moves.addLast(station.get(0).getEntity());
        }
    }
}
