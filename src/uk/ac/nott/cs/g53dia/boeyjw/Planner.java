package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.Cell;
import uk.ac.nott.cs.g53dia.library.Tanker;

import javax.swing.text.html.parser.Entity;
import java.util.*;

public class Planner extends Mapper {
    public static boolean noPathFound = false;

    private Deque<Cell> moves;
    private Random r;

    Planner(Random r) {
        moves = new ArrayDeque<>();
        this.r = r;
    }

    public void reinitialise() {
        moves.clear();
        noPathFound = false;
    }

    /**
     * TODO: If source == destination, make a loop while traversing multiple stations and wells completing task
     * TODO: If source != destination, do normal A* pathfinding while completing waste dumping task
     * @param currentCell
     * @param destination
     * @return
     */
    public Deque<Cell> plan(Hashtable<String, List<CoreEntity>> map, Tanker t, EntityNode currentCell, EntityNode destination) {
        noPathFound = false;
        // If there are no station with tasked, explore
        if(map.get("taskedStation").isEmpty())
            return null;

        int estFuelLevel = t.getFuelLevel();
        int estWasteLevel = t.getWasteLevel();
        boolean evaluateAtLeastOnce = false;
        String nextMove = "";

        // A* algorithm
        List<EntityNode> openList = new ArrayList<>();
        List<EntityNode> closedList = new ArrayList<>();
        List<EntityNode> plannedTaskedStation = new ArrayList<>();

        currentCell.setGscore(0);
        currentCell.setHscore(currentCell.getEntity().equals(destination.getEntity()) ?
                Tanker.MAX_FUEL - t.getFuelLevel() : Calculation.modifiedManhattenDistance(currentCell.getCoord(), destination.getCoord()));
        currentCell.calculateFscore();
        openList.add(currentCell);

        while(!openList.isEmpty()) {
            EntityNode current = getEntityLowestFScore(openList);
            if(current.getEntity().equals(destination.getEntity()) && evaluateAtLeastOnce)
                return reconstruct_path(destination);
            closedList.add(openList.remove(openList.indexOf(current)));

            if(!super.acceptableFuelLevel(estFuelLevel, getClosestFuelPump(map.get("fuel"), current)))
                nextMove = "fuel";
            else if(!super.acceptableWasteLevel(estWasteLevel))
                nextMove = "well";
            else
                nextMove = "taskedStation";

            openList.addAll(calculateGscore(map.get(nextMove).iterator(), current));



            evaluateAtLeastOnce = true;
        }

        noPathFound = true;
        return null;
    }

    private List<EntityNode> calculateGscore(Iterator neighbours, EntityNode current) {
        List<EntityNode> gscore_entities = new ArrayList<>();

        while (neighbours.hasNext()) {
            EntityNode e = (EntityNode) neighbours.next();
            e.setGscore(current.getGscore() + Calculation.modifiedManhattenDistance(current.getCoord(), e.getCoord()));
            e.setParent(current);
            gscore_entities.add(e);
        }

        return gscore_entities;
    }

    private int getClosestFuelPump(List<CoreEntity> fuelPumps, EntityNode current) {
        if(fuelPumps.size() == 1)
            return Calculation.modifiedManhattenDistance(current.getCoord(), fuelPumps.get(0).getCoord());

        int min = 0;
        int argmin = 0;
        int[] dist = new int[fuelPumps.size()];
        for(int i = 0; i < fuelPumps.size(); i++) {
            dist[i] = Calculation.modifiedManhattenDistance(current.getCoord(), fuelPumps.get(i).getCoord());
            if(i == 0)
                min = dist[i];
            else if(dist[i] > min) {
                argmin = i;
                min = dist[i];
            }
        }

        return dist[argmin];
    }

    private Deque<Cell> reconstruct_path(EntityNode destination) {
        EntityNode node = destination;
        moves.add(destination.getEntity());
        while(node.getParent() != null) {
            node = node.getParent();
            moves.add(node.getEntity());
        }

        return moves;
    }

    private EntityNode getEntityLowestFScore(List<EntityNode> openList) {

    }
}
