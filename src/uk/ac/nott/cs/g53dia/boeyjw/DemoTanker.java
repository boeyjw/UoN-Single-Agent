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
	private Stack<Coordinates> stations;
	private Stack<Coordinates> well;
	private Stack<Coordinates> fuel;
    private Coordinates persistentMove;

    public DemoTanker() {
        this(new Random());
    }

    public DemoTanker(Random r) {
	    this.r = r;
	    stations = new Stack<>();
	    well = new Stack<>();
	    fuel = new Stack<>();
	    persistentMove = new Coordinates();
    }

    /*
     * The following is a simple demonstration of how to write a
     * tanker. The code below is very stupid and simply moves the
     * tanker randomly until the fuel tank is half full, at which
     * point it returns to a fuel pump to refuel.
     */
    public Action senseAndAct(Cell[][] view, long timestep) {
        storePointOfInterest(view);
        if((getFuelLevel() <= MAX_FUEL/2)) {
            if(!(getCurrentCell(view) instanceof FuelPump)) {
                if(fuel.isEmpty() || fuel.peek().getX() == 0 && fuel.peek().getY() == 0)
                    return new MoveTowardsAction(FUEL_PUMP_LOCATION);
                else {
                    persistentMove.modifyCoordinates(fuel.peek().getX(), fuel.peek().getY());
                    return new MoveTowardsAction(view[persistentMove.getX()][persistentMove.getY()].getPoint());
                }
            }
            else
                return new RefuelAction();
        }
        else {
            if(getWasteLevel() < MAX_WASTE / 4 && !stations.isEmpty())
                persistentMove.modifyCoordinates(stations.peek().getX(), stations.peek().getY());
            else if(!well.isEmpty())
                persistentMove.modifyCoordinates(well.peek().getX(), well.peek().getY());
            if(getWasteLevel() < MAX_WASTE / 4 && !stations.isEmpty() && persistentMove.isValidCoordinate()) {
                if(getCurrentCell(view) instanceof Station) {
                    Task t = ((Station) getCurrentCell(view)).getTask();
                    if(!(t == null))
                        return new LoadWasteAction(t);
                }
                else
                    return new MoveTowardsAction(view[persistentMove.getX()][persistentMove.getY()].getPoint());
            }
            else if(getWasteLevel() > MAX_WASTE / 4 && !well.isEmpty() && persistentMove.isValidCoordinate()) {
                if(getCurrentCell(view) instanceof Well) {
                    return new DisposeWasteAction();
                }
                else
                    return new MoveTowardsAction(view[persistentMove.getX()][persistentMove.getY()].getPoint());
            }
            return new MoveAction(MoveAction.NORTH);
        }
    }

    private void storePointOfInterest(Cell[][] view) {
        stations.empty();
        well.empty();
        fuel.empty();
        for(int r = 0; r < view.length; r++) {
            for(int c = 0; c < view.length; c++) {
                if(view[r][c] instanceof Station) {
                    stations.push(new Coordinates(r, c));
                }
                else if(view[r][c] instanceof Well) {
                    well.push(new Coordinates(r, c));
                }
                else if(view[r][c] instanceof FuelPump) {
                    fuel.push(new Coordinates(r, c));
                }
            }
        }
    }
}
