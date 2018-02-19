package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.MoveAction;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.Stack;

public class Explorer {
    private HashMap<Integer, Integer> crossDirectionMovement;
    private int direction;

    public Explorer(Random r) {
        crossDirectionMovement = new HashMap<>();
        init();
        direction = r.nextInt(Threshold.TOTAL_DIRECTION_BOUND.getThresh());
    }

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
        direction = crossDirectionMovement.get(direction);
        return direction;
    }
}
