package uk.ac.nott.cs.g53dia.boeyjw.boeyjwhold;

import java.util.Hashtable;
import java.util.Stack;

public interface ReactiveLayer {
    Stack<Entity> decision(Hashtable<String, Stack<Entity>> entities);
    String toString();
}
