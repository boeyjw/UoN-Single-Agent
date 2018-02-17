package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import static uk.ac.nott.cs.g53dia.library.MoveAction.NORTH;

/**
 * A simple example Tanker
 * 
 * @author Julian Zappala
 */
/*
 * Copyright (c) 2011 Julian Zappala
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public class DemoTanker extends Tanker {
    private final int REFUEL_THRESHOLD = 52;
    private ArrayList<Entity> entities;

    public DemoTanker() {
        this(new Random());
    }

    public DemoTanker(Random r) {
	    this.r = r;
	    entities = new ArrayList<>();
    }

    /*
     * The following is a simple demonstration of how to write a
     * tanker. The code below is very stupid and simply moves the
     * tanker randomly until the fuel tank is half full, at which
     * point it returns to a fuel pump to refuel.
     */
    public Action senseAndAct(Cell[][] view, long timestep) {
        scanView(view, timestep);
        return null;
    }

    private void scanView(Cell[][] view, long timestep) {
        entities.clear();
        for(int x = 0; x < VIEW_RANGE * 2 + 1; x++) {
            for(int y = 0; y < VIEW_RANGE * 2 + 1; y++) {
                if(!(view[x][y] instanceof EmptyCell))
                    entities.add(new Entity(view[x][y], timestep));
            }
        }
    }
}
