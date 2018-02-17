package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.MoveAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Explorer implements ReactiveLayer {
    private HashMap<Integer, Integer> crossDirectionMovement;

    public Explorer() {
        crossDirectionMovement = new HashMap<>();
        init();
    }

    private void init() {
        crossDirectionMovement.put(MoveAction.NORTH, MoveAction.SOUTH);
        crossDirectionMovement.put(MoveAction.EAST, MoveAction.WEST);
        crossDirectionMovement.put(MoveAction.WEST, MoveAction.EAST);
        crossDirectionMovement.put(MoveAction.SOUTH, MoveAction.NORTH);
        crossDirectionMovement.put(MoveAction.NORTHEAST, MoveAction.NORTHWEST);
        crossDirectionMovement.put(MoveAction.NORTHWEST, MoveAction.NORTHEAST);
    }
    @Override
    public Stack<Entity> decision(ArrayList<Entity> entities) {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
