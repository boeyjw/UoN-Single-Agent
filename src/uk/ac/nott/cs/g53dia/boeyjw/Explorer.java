package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.MoveAction;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.Stack;

/**
 * Handles exploration of the tanker
 */
public class Explorer extends Mapper {
    public static boolean explorerMode = false;
    private HashMap<Integer, Integer> crossDirectionMovement;
    private int direction;
    private long startExplorerTimestep;

    public Explorer(Random r) {
        crossDirectionMovement = new HashMap<>();
        init();
        direction = r.nextInt(Threshold.TOTAL_DIRECTION_BOUND.getThresh());
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

    public int getDirection() {
        return direction;
    }

    public void updateDirection() {
        direction = crossDirectionMovement.get(direction);
    }

    public int getAndUpdateDirection() {
        int dir = direction;
        updateDirection();
        return dir;
    }

    public long getStartExplorerTimestep() {
        return startExplorerTimestep;
    }

    public void setStartExplorerTimestep(long startExplorerTimestep) {
        this.startExplorerTimestep = startExplorerTimestep;
    }
}
