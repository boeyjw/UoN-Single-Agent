package uk.ac.nott.cs.g53dia.boeyjw;

import java.util.ArrayList;
import java.util.Stack;

public interface ReactiveLayer {
    String toString();
    Stack<Entity> decision(ArrayList<Entity> entities);
}
