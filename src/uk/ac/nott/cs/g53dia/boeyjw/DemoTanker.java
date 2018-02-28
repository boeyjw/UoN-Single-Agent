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
    private Deque<EntityNode> plannedMoves;
    private Deque<Cell> history;

    private Cell lastFuelPump, lastWell;
    private Explorer explorer;
    private Planner planner;
    private Interceptor interceptor;
    private int explorerDirection;

    private Log l;
    public DemoTanker() {
        this(new Random());
    }

    public DemoTanker(Random r) {
	    this.r = r;
	    entities = new Hashtable<>(5);
	    mapper = new MapBuilder();
	    planner = new Planner();
	    interceptor = new Interceptor();

        fuelpump = new ArrayList<>();
        well = new ArrayList<>();
        station = new ArrayList<>();
        taskedStation = new ArrayList<>();
        moves = new ArrayDeque<>();
        plannedMoves = new ArrayDeque<>();
        history = new ArrayDeque<>();

        lastFuelPump = null;
        lastWell = null;

        explorer = new Explorer(this.r);
        explorerDirection = explorer.getAndUpdateDirection();

        l = new Log(true);
    }

    public Action senseAndAct(Cell[][] view, long timestep) {
        l.d("Timestep: " + timestep);
        int mapperStatus = 0;
        // Add actual positions of fuel pumps, wells and stations to form an entity map
        if(mapper.addToMap(getCurrentCell(view))) {
            mapperStatus = mapper.addPermanentPositions(new EntityNode(getCurrentCell(view), Coordinates.getTankerCoordinate(), timestep, getPosition()));
        }
        spiralScanView(view, timestep);
        interceptor.setLastClosestSeen(entities.get("well").isEmpty() ? null : entities.get("well").get(entities.get("well").size() - 1),
                entities.get("fuel").isEmpty() ? null : entities.get("fuel").get(entities.get("fuel").size() - 1), timestep);

        if(moves.isEmpty()) {
            if(!EntityChecker.isEmptyCell(getCurrentCell(view))) {
                plannedMoves = planner.plan(entities, getFuelLevel(), getWasteLevel(),
                        new EntityNode(getCurrentCell(view), Coordinates.getTankerCoordinate(), timestep, getPosition()));
            }
            if(plannedMoves.isEmpty()) {
                if(!Explorer.explorerMode) {
                    l.d("EXPLORE");
                    Explorer.explorerMode = true;
                    explorer.setStartExplorerTimestep(timestep);
                }
            }
            else {
                l.d("PLANNED");
                Explorer.explorerMode = false;
                for(EntityNode e : plannedMoves) {
                    l.d(e.getEntity().getClass().getName() + " @ " + e.getEntityHash());
                }
                for(EntityNode e : plannedMoves) {
                    moves.add(e.getEntity());
                }
                plannedMoves.clear();
                explorerDirection = explorer.getAndUpdateDirection();
//                explorerDirection = explorer.getDirectionUsingClusterAttraction(entities, this);
            }
        }
        if(!moves.isEmpty()) {
            Cell c = moves.peekFirst();
            if(EntityChecker.getEntityType(c) == EntityChecker.getEntityType(getCurrentCell(view))) {
                l.d("Current: " + getCurrentCell(view).hashCode());
                if(EntityChecker.isFuelPump(c)) {
                    if(Explorer.explorerMode) {
                        explorerDirection = explorer.getAndUpdateDirection();
//                        explorerDirection = explorer.getDirectionUsingClusterAttraction(entities, this);
                    }
                    history.add(moves.removeFirst());
                    l.d("MOVES: REFUEL" + " => " + c.getClass() + " @ " + c.hashCode());
                    return new RefuelAction();
                }
                else if(EntityChecker.isWell(c)) {
                    history.add(moves.removeFirst());
                    l.d("MOVES: DUMP" + " => " + c.getClass() + " @ " + c.hashCode());
                    return new DisposeWasteAction();
                }
                else if(EntityChecker.isStation(c)) {
                    history.add(moves.removeFirst());
                    if(((Station) getCurrentCell(view)).getTask() != null) {
                        l.d("MOVES: LOAD" + " => " + c.getClass() + " @ " + c.hashCode());
                        return new LoadWasteAction(((Station) getCurrentCell(view)).getTask());
                    }
                }
                else { //Empty cell
                    l.d("MOVES: EMPTY CELL" + " => " + c.getClass() + " @ " + c.hashCode());
                    history.add(moves.removeFirst());
                }
            }
        }

        interceptor.intercept(moves, this, timestep, taskedStation);
        explorer.getPassbyTask(moves, getWasteLevel(), taskedStation);
        cleanup();
        for(Cell c : moves) {
            l.dc(c.getClass().getName() + " @ " + c.hashCode() + ", ");
        }
        l.d("");

        if(!moves.isEmpty()) {
            return new MoveTowardsAction(moves.peek().getPoint());
        }
        else if(Explorer.explorerMode && plannedMoves.isEmpty()) {
            l.d("EXPLORER MODE");
            if(getWasteLevel() < MAX_WASTE && EntityChecker.isStation(getCurrentCell(view)) && ((Station) getCurrentCell(view)).getTask() != null) {
                return new LoadWasteAction(((Station) getCurrentCell(view)).getTask());
            }
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
        lc = Threshold.TOTAL_VIEW_RANGE.getThreshold() - 1;
        lr = Threshold.TOTAL_VIEW_RANGE.getThreshold() - 1;

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
        EntityNode node = new EntityNode(entity, x, y, timestep);
        node.setBearing(Calculation.targetBearing(Coordinates.getTankerCoordinate(), node.getCoord()));
        if(EntityChecker.isFuelPump(entity)) {
            fuelpump.add(node);
        }
        else if(EntityChecker.isStation(entity)) {
            station.add(node);
            if(((Station) entity).getTask() != null) {
                taskedStation.add(node);

            }
        }
        else if(EntityChecker.isWell(entity)) {
            well.add(node);
        }
    }

    private void cleanup() {
        fuelpump.clear();
        well.clear();
        station.clear();
        taskedStation.clear();
        entities.clear();
    }
}
