package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.FuelPump;
import uk.ac.nott.cs.g53dia.library.Station;
import uk.ac.nott.cs.g53dia.library.Well;

public class Entity {
    public static final int FUELPUMP = 0;
    public static final int WELL = 1;
    public static final int STATION = 2;

    private Object entity;
    private int entityType;
    private long lastVisited;
    private boolean hasTask;

    // Future
    private Coordinates realCoord;
    private Coordinates nearestFuelPumpCoord;

    public Entity(Object entity, long lastVisited) {
        this.entity = entity;
        hasTask = false;
        if(entity instanceof FuelPump)
            entityType = FUELPUMP;
        else if(entity instanceof Well)
            entityType = WELL;
        else if(entity instanceof Station) {
            entityType = STATION;
            hasTask = ((Station) entity).getTask() != null;
        }

        this.lastVisited = lastVisited;
    }

    public long getLastVisited() {
        return lastVisited;
    }

    public void update() {
        if(entity instanceof Station)
            hasTask = ((Station) entity).getTask() != null;
    }

    public void update(long currentTimestep, Object currentEntity) {
        if(currentEntity.equals(entity))
            lastVisited = currentTimestep;
        update();
    }

    public boolean checkHasTask() {
        return hasTask;
    }
}
