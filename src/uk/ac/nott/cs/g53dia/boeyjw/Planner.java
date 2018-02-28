package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.Cell;
import uk.ac.nott.cs.g53dia.library.Tanker;

import java.util.*;

public class Planner extends Mapper {
    public static boolean NEED_FUEL = false;
    public static boolean NEED_WELL = false;
    public static boolean NO_PATH_FOUND = false;

    private final int MAX_STATION_VISIT;

    private Log l;

    Planner() {
        MAX_STATION_VISIT = 3;
        l = new Log(true);
    }

    public Deque<EntityNode> plan(Hashtable<String, List<CoreEntity>> entities,
                                  int currentFuelLevel, int currentWasteLevel, EntityNode current) {
        Deque<EntityNode> plannedMoves = new ArrayDeque<>();
        int estFuelLevel = currentFuelLevel;
        int estWasteLevel = currentWasteLevel;
        int stationVisitCounter = 0;
        boolean hasPlan = false;
        String nextMove = "";
        List<CoreEntity> taskedStations = entities.get("taskedStation");
        boolean noFeasibleNodes = false;

        current.setGscore(0);
        current.setFuelConsumption(0);
        plannedMoves.add(current);

        while(true) {
            EntityNode currentMove = plannedMoves.peekLast();
            if(!entities.get("fuel").isEmpty()) {
                if(!super.acceptableFuelLevel(estFuelLevel, super.getClosestEntityDistanceTo(entities.get("fuel"), currentMove)) || noFeasibleNodes) {
                    nextMove = "fuel";
                }
                else {
                    nextMove = "completed";
                }
            }
            else if(!entities.get("well").isEmpty()) {
                if(stationVisitCounter >= MAX_STATION_VISIT || !super.acceptableWasteLevel(estWasteLevel)) {
                    nextMove = "well";
                }
                else {
                    nextMove = "completed";
                }
            }
            else if(!taskedStations.isEmpty()){
                nextMove = "taskedStation";
            }
            else {
                nextMove = "completed";
            }
            l.d(nextMove);
            if(nextMove.equalsIgnoreCase("completed")) {
                if(verifyPlan(plannedMoves, currentFuelLevel, current)) {
                    break;
                }
                else {
                    System.out.println("Planning failed");
                    NO_PATH_FOUND = true;
                    plannedMoves.clear();
                    break;
                }
            }
            else {
                hasPlan = true;
                List<EntityNode> gscored_nodes = calculateGscore(nextMove.equalsIgnoreCase("taskedStation") ?
                        taskedStations.iterator() : entities.get(nextMove).iterator(), current);
                // Has fuel pumps in view and next move is not fuel pump
                if(!nextMove.equalsIgnoreCase("fuel")) {
                    int argmin = Integer.MAX_VALUE;
                    if(!entities.get("fuel").isEmpty()) {
                        boolean[] feasibleNodes = getFeasibleNodes(gscored_nodes, entities.get("fuel"), estFuelLevel);
                        argmin = getArgminDistance(gscored_nodes, feasibleNodes);
                    }
                    else {
                        argmin = getArgminDistance(gscored_nodes);
                    }
                    if(argmin != Integer.MAX_VALUE) {
                        if(nextMove.equalsIgnoreCase("taskedStation")) {
                            stationVisitCounter++;
                            taskedStations.remove(argmin);
                        }
                        else {
                            stationVisitCounter = 0;
                            estWasteLevel = 0;
                        }
                        estFuelLevel -= gscored_nodes.get(argmin).getFuelConsumption();
                        plannedMoves.add(gscored_nodes.remove(argmin));
                    }
                    else {
                        noFeasibleNodes = true;
                    }
                }
                // next move is fuel pump
                else {
                    noFeasibleNodes = false;
                    int argmin = getArgminDistance(gscored_nodes);
                    plannedMoves.add(gscored_nodes.remove(argmin));
                    estFuelLevel = 100;
                }
            }
        }

        if(!hasPlan) {
            plannedMoves.clear();
        }
        return plannedMoves;
    }

    public boolean optimisePlan(Deque<Cell> moves, Hashtable<String, List<CoreEntity>> entities, Tanker t) {
        return false;
    }

    private boolean[] getFeasibleNodes(List<EntityNode> gscored, List<CoreEntity> fuelpumps, int estFuelLevel) {
        boolean[] feasibleNodes = new boolean[gscored.size()];
        for(int i = 0; i < gscored.size(); i++) {
            feasibleNodes[i] = super.acceptableFuelLevel(estFuelLevel, gscored.get(i).getFuelConsumption()) &&
                    super.acceptableFuelLevel(estFuelLevel - gscored.get(i).getFuelConsumption(),
                            super.getClosestEntityDistanceTo(fuelpumps, gscored.get(i)));
        }

        return feasibleNodes;
    }

    private int getArgminDistance(List<EntityNode> entities) {
        int argmin = 0;
        int min = entities.get(argmin).getFuelConsumption();

        for(int i = 1; i < entities.size(); i++) {
            if(entities.get(i).getFuelConsumption() < min) {
                argmin = i;
                min = entities.get(i).getFuelConsumption();
            }
        }

        return argmin;
    }

    private int getArgminDistance(List<EntityNode> entities, boolean[] feasibleNodes) {
        int argmin = Integer.MAX_VALUE;
        int min = Integer.MAX_VALUE;

        for(int i = 0; i < entities.size(); i++) {
            if(!feasibleNodes[i]) {
                continue;
            }
            if(entities.get(i).getFuelConsumption() < min) {
                argmin = i;
                min = entities.get(i).getFuelConsumption();
            }
        }

        return argmin;
    }

    private List<EntityNode> downcastToEntityNodeList(Iterator<CoreEntity> entities) {
        List<EntityNode> newEntities = new ArrayList<>();
        while (entities.hasNext()) {
            newEntities.add((EntityNode) entities.next());
        }

        return newEntities;
    }

    private List<EntityNode> calculateGscore(Iterator<CoreEntity> entities, EntityNode current) {
        List<EntityNode> gscore_nodes = new ArrayList<>();

        while(entities.hasNext()) {
            EntityNode e = (EntityNode) entities.next();
            int dist = Calculation.modifiedManhattenDistance(current.getCoord(), e.getCoord());
            e.setGscore(current.getGscore() + dist);
            e.setFuelConsumption(dist);
            e.setParent(current);
            gscore_nodes.add(e);
        }

        return gscore_nodes;
    }

    private boolean verifyPlan(Deque<EntityNode> plannedMoves, int currentFuelLevel, EntityNode current) {
        if(plannedMoves.isEmpty())
            return false;
        else if(!plannedMoves.peekFirst().getEntity().equals(current.getEntity()))
            return false;
        else {
            int stationCounter = 0;
            int estFuelLevel = currentFuelLevel;

            for(Iterator<EntityNode> it = plannedMoves.iterator(); it.hasNext(); ) {
                if(estFuelLevel <= 0) {
                    return false;
                }
                EntityNode e = it.next();

                if(EntityChecker.isFuelPump(e.getEntity())) {
                    estFuelLevel = 100;
                }
                else if(EntityChecker.isStation(e.getEntity())) {
                    if(stationCounter >= MAX_STATION_VISIT) {
                        return false;
                    }
                    stationCounter++;
                    estFuelLevel -= e.getFuelConsumption();
                }
                else if(EntityChecker.isWell(e.getEntity())) {
                    stationCounter = 0;
                    estFuelLevel -= e.getFuelConsumption();
                }
            }
            return true;
        }
    }

    public Deque<Cell> toCellMoves(Deque<EntityNode> plannedMoves) {
        Deque<Cell> moves = new ArrayDeque<>();
        for(Iterator<EntityNode> it = plannedMoves.iterator(); it.hasNext(); ) {
            moves.add(it.next().getEntity());
        }

        return moves;
    }

    public void reset() {
        NEED_FUEL = false;
        NEED_WELL = false;
        NO_PATH_FOUND = false;
    }
}
