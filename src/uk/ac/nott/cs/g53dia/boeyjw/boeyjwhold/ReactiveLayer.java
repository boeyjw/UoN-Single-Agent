package uk.ac.nott.cs.g53dia.boeyjw.boeyjwhold;

import uk.ac.nott.cs.g53dia.boeyjw.Entity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public interface ReactiveLayer {
    Stack<Entity> decision(Hashtable<String, Stack<Entity>> entities);
    String toString();
}
