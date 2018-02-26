package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.*;

import java.util.*;

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
    public static boolean doScan = true;

    private Hashtable<String, List<CoreEntity>> entities;
    private MapBuilder mapper;
    private List<CoreEntity> fuelpump, well, station, taskedStation;
    private Deque<Cell> moves;

    private Cell lastFuelPump, lastWell;
    private Explorer explorer;
    private Replanner planner;
    private int explorerDirection;

    public DemoTanker() {
        this(new Random());
    }

    public DemoTanker(Random r) {
	    this.r = r;
	    entities = new Hashtable<>(5);
	    mapper = new MapBuilder();
	    planner = new Replanner();

        fuelpump = new ArrayList<>();
        well = new ArrayList<>();
        station = new ArrayList<>();
        taskedStation = new ArrayList<>();
        moves = new ArrayDeque<>();

        lastFuelPump = null;
        lastWell = null;

        explorer = new Explorer(this.r);
        explorerDirection = explorer.getAndUpdateDirection();
    }

    public Action senseAndAct(Cell[][] view, long timestep) {
        int mapperStatus = 0;
        // Add actual positions of fuel pumps, wells and stations to form an entity map
        if(mapper.addToMap(getCurrentCell(view))) {
            mapperStatus = mapper.addPermanentPositions(new EntityNode(getCurrentCell(view), new Coordinates(VIEW_RANGE, VIEW_RANGE), timestep, getPosition()));
            System.out.println(mapperStatus + "\n" + mapper.toString());
        }

        if(!moves.isEmpty() && getCurrentCell(view).equals(moves.peek())) {
            Cell c = moves.pop();
            if(EntityChecker.isWell(c)) {
                return new DisposeWasteAction();
            }
            else if(EntityChecker.isFuelPump(c)) {
                return new RefuelAction();
            }
            else if(EntityChecker.isStation(c) && EntityChecker.hasTaskStation(getCurrentCell(view))) {
                return new LoadWasteAction(((Station) getCurrentCell(view)).getTask());
            }
        }

        if(EntityChecker.isFuelPump(getCurrentCell(view)))
            lastFuelPump = getCurrentCell(view);
        else if(EntityChecker.isWell(getCurrentCell(view)))
            lastWell = getCurrentCell(view);

        doScan = moves.isEmpty();
        if(doScan || Explorer.explorerMode) {
            spiralScanView(view, timestep);
            if(moves.isEmpty()) {
                if(Threshold.LOWEST_FUEL.hitThresh(getFuelLevel()))
                    moves.push(lastFuelPump);
                Explorer.explorerMode = true;
                explorer.setStartExplorerTimestep(timestep);
            }
            else if(Explorer.explorerMode && !moves.isEmpty()) {
                explorerDirection = explorer.getAndUpdateDirection();
            }
            if(!taskedStation.isEmpty() && Explorer.explorerMode) {
                moves.add(taskedStation.remove(0).getEntity());
            }

            cleanup();
        }

        if(!moves.isEmpty())
            return new MoveTowardsAction(moves.peek().getPoint());
        else if(Explorer.explorerMode) {
            return new MoveAction(explorerDirection);
        }

        // Should never reach here
        return null;
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
                binEntitiesToStack(view[fr][i], fr, i, timestep);
                c++;
            }
            fr++;
            // Right column values
            for(i = fr; i <= lr; i++) {
                binEntitiesToStack(view[i][lc], i, lc, timestep);
                c++;
            }
            lc--;
            if(fr < lr) { // Bottom row values
                for(i = lc; i >= fc; i--) {
                    binEntitiesToStack(view[lr][i], lr, i, timestep);
                    c++;
                }
                lr--;
            }
            if(fc < lc) { // Left column values
                for(i = lr; i >= fr; i--) {
                    binEntitiesToStack(view[i][fc], i, fc, timestep);
                    c++;
                }
                fc++;
            }
        }

        // Add stacks into HashTable to be sent over to the decision function
        entities.put("fuel", fuelpump);
        entities.put("well", well);
        entities.put("station", station);
        entities.put("taskedStation", taskedStation);
    }

    /**
     * Bin each entity into the right stack
     * @param entity The entity viewed by the Tanker's view
     * @param timestep The current timestep in the simulation
     */
    private void binEntitiesToStack(Cell entity, int x, int y, long timestep) {
        if(EntityChecker.isFuelPump(entity))
            fuelpump.add(new EntityNode(entity, x, y, timestep));
        else if(EntityChecker.isStation(entity)) {
            station.add(new EntityNode(entity, x, y, timestep));
            if(((Station) entity).getTask() != null)
                taskedStation.add(new EntityNode(entity, x, y, timestep));
        }
        else if(EntityChecker.isWell(entity))
            well.add(new EntityNode(entity, x, y, timestep));
    }

    private void cleanup() {
        fuelpump.clear();
        well.clear();
        station.clear();
        taskedStation.clear();
        entities.clear();
    }
}
