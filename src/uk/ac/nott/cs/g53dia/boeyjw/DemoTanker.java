package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.*;

import java.util.*;

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
    private Hashtable<String, Stack<Entity>> entities;
    private Stack<Entity> fuelpump, well, station;
    private TankerManager tm;
    private LinkedList<Entity> moves;

    public DemoTanker() {
        this(new Random());
    }

    public DemoTanker(Random r) {
	    this.r = r;
	    entities = new Hashtable<>(3);
        fuelpump = new Stack<>();
        well = new Stack<>();
        station = new Stack<>();
        tm = new TankerManager(r);
        moves = new LinkedList<>();
    }

    /*
     * The following is a simple demonstration of how to write a
     * tanker. The code below is very stupid and simply moves the
     * tanker randomly until the fuel tank is half full, at which
     * point it returns to a fuel pump to refuel.
     */
    public Action senseAndAct(Cell[][] view, long timestep) {
        if(tm.requiresPlanning() || moves.getFirst().getEntityType() == Entity.EXPLORER) {
            spiralScanView(view, timestep);
            moves = tm.getMoves(this, entities);
            cleanup();
        }
        else if(getCurrentCell(view).equals(moves.getFirst().getEntity())) {
            moves.pollFirst();
        }

        return moves.getFirst().getEntityType() == Entity.EXPLORER ? new MoveAction(moves.getFirst().getExploringDirection()) :
                new MoveTowardsAction(moves.getFirst().getPoint());
    }

    /**
     * Does a scan of the tanker's surrounding view of 40 x 40 + 1 grid blocks and stores each interesting entities
     * into a stack to measure relative closenest to the tanker
     * @param view Tanker's current view
     * @param timestep The current timestep fo the simulation
     */
    private void spiralScanView(Cell[][] view, long timestep) {
        int fr, lc, lr, fc, i;
        /*
        fr - First row
        lc - Last column
        lr - Last row
        fc - First column
        i - for loop iterator
         */
        int c = 0;
        fr = fc = 0;
        lc = Threshold.TOTAL_VIEW_RANGE.getThresh() - 1;
        lr = Threshold.TOTAL_VIEW_RANGE.getThresh() - 1;

        while(c < Threshold.TOTAL_VIEW_RANGE.getTotalViewGridLength()) {
            // Top row values
            for(i = fc; i <= lc; i++) {
                binEntitiesToStack(view[fr][i], timestep);
                c++;
            }
            fr++;
            // Right column values
            for(i = fr; i <= lr; i++) {
                binEntitiesToStack(view[i][lc], timestep);
                c++;
            }
            lc--;
            if(fr < lr) { // Bottom row values
                for(i = lc; i >= fc; i--) {
                    binEntitiesToStack(view[lr][i], timestep);
                    c++;
                }
                lr--;
            }
            if(fc < lc) { // Left column values
                for(i = lr; i >= fr; i--) {
                    binEntitiesToStack(view[i][fc], timestep);
                    c++;
                }
                fc++;
            }
        }

        // Add stacks into HashTable to be sent over to the decision function
        entities.put("fuel", fuelpump);
        entities.put("well", well);
        entities.put("station", station);
    }

    /**
     * Bin each entity into the right stack
     * @param entity The entity viewed by the Tanker's view
     * @param timestep The current timestep in the simulation
     */
    private void binEntitiesToStack(Object entity, long timestep) {
        if(entity instanceof FuelPump)
            fuelpump.push(new Entity(entity, timestep));
        else if(entity instanceof Well)
            well.push(new Entity(entity, timestep));
        else if(entity instanceof Station)
            station.push(new Entity(entity, timestep));
    }

    private void cleanup() {
        fuelpump.clear();
        well.clear();
        station.clear();
        entities.clear();
    }
}
