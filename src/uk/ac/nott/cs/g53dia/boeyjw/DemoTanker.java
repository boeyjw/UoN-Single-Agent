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
    private Queue<Point> fuel, well;
    private Stack<Station> station;
    private int lastDir;
    private int goDir;
    private Point lockon;
    private Point stationLockon;
    private boolean mutex_lockon;
    private int gotoStationAttempt;
    public DemoTanker() {
        this(new Random());
    }

    public DemoTanker(Random r) {
	    this.r = r;
        fuel = new ArrayDeque<>();
        well = new ArrayDeque<>();
        station = new Stack<>();
        lastDir = MoveAction.NORTH;
        goDir = lastDir % 7;
        lockon = null;
        stationLockon = null;
        gotoStationAttempt = 0;
        mutex_lockon = false;
    }

    /*
     * The following is a simple demonstration of how to write a
     * tanker. The code below is very stupid and simply moves the
     * tanker randomly until the fuel tank is half full, at which
     * point it returns to a fuel pump to refuel.
     */
    public Action senseAndAct(Cell[][] view, long timestep) {
        scanView(view);
        if(getFuelLevel() < 52) {
            if(getCurrentCell(view) instanceof FuelPump) {
                lastDir = goDir;
                if(lastDir == 0)
                    goDir = 7;
                else
                    goDir = (lastDir * 2) % 7;
                lockon = null;
                fuel.clear();
                return new RefuelAction();
            }
            else {
                if(lockon == null || (!lockon.equals(fuel.peek()) && !lockon.equals(FUEL_PUMP_LOCATION)))
                    lockon = fuel.isEmpty() ? FUEL_PUMP_LOCATION : fuel.peek();
                System.out.println("Moving to refuel @ " + lockon.toString());
                return new MoveTowardsAction(lockon);
            }
        }
        else if(getWasteLevel() < MAX_WASTE * 0.9 && !station.isEmpty()) {
            System.out.println("Move to station @ " + station.peek().toString());
            if(getCurrentCell(view) instanceof Station && ((Station) getCurrentCell(view)).getTask() != null && getCurrentCell(view).equals(station.peek())) {
                station.pop();
                return new LoadWasteAction(((Station) getCurrentCell(view)).getTask());
            }
            else if(!station.isEmpty()) {
                if(stationLockon != null && stationLockon.equals(station.peek().getPoint())) {
                    gotoStationAttempt++;
                }
                else {
                    gotoStationAttempt = 0;
                    stationLockon = station.peek().getPoint();
                }
                if(gotoStationAttempt > 5) {
                    station.pop();
                    gotoStationAttempt = 0;
                }
                return new MoveTowardsAction(stationLockon);
            }
        }
        else if(getWasteLevel() > MAX_WASTE * 0.9 && !well.isEmpty()) {
            if(lockon == null || !lockon.equals(well.peek()))
                lockon = well.peek();
            System.out.println("Move to well @ " + lockon.toString());
            if(getCurrentCell(view) instanceof Well) {
                well.clear();
                station.empty();
                lockon = null;
                return new DisposeWasteAction();
            }
            else
                return new MoveTowardsAction(lockon);
        }
        else if(getWasteLevel() > MAX_WASTE / 2 && getCurrentCell(view) instanceof Well) {
            System.out.println("Random dump @ " + getCurrentCell(view).toString());
            return new DisposeWasteAction();
        }
        else if(getWasteLevel() < MAX_WASTE && getCurrentCell(view) instanceof Station && ((Station) getCurrentCell(view)).getTask() != null) {
            System.out.println("Random load @ " + getCurrentCell(view).toString());
            return new LoadWasteAction(((Station) getCurrentCell(view)).getTask());
        }
        System.out.println("Random walk: " + goDir);
        return new MoveAction(goDir);
    }

    private void scanView(Cell[][] view) {
        for(int x = 0; x < view.length; x++) {
            for(int y = 0; y < view.length; y++) {
                if(view[x][y] instanceof FuelPump) {
                    fuel.add(view[x][y].getPoint());
                }
                else if(view[x][y] instanceof Station && ((Station) view[x][y]).getTask() != null) {
                    if(station.isEmpty())
                        station.push((Station) view[x][y]);
                    else if(station.search((Station) view[x][y]) == -1)
                        station.push((Station) view[x][y]);
                }
                else if(view[x][y] instanceof Well) {
                    well.add(view[x][y].getPoint());
                }
            }
        }
    }
}
