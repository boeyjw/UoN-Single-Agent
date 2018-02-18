package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.boeyjw.boeyjwhold.Coordinates;
import uk.ac.nott.cs.g53dia.library.*;

public class Entity {
    public static final int FUELPUMP = 0;
    public static final int WELL = 1;
    public static final int STATION = 2;
    public static final int EXPLORER = 3;

    private Object entity;
    private Point p;
    private int entityType;
    private long lastVisited;
    private boolean hasTask;

    // Future
    private Coordinates realCoord;
    private Coordinates nearestFuelPumpCoord;
    private int distanceToNearestFuelPump;
    private int priority;

    public Entity(Object entity) {
        hasTask = false;
        if(entity instanceof FuelPump) {
            entityType = FUELPUMP;
            this.entity = (FuelPump) entity;
            p = ((FuelPump) entity).getPoint();
        }
        else if(entity instanceof Well) {
            entityType = WELL;
            this.entity = (Well) entity;
            p = ((Well) entity).getPoint();
        }
        else if(entity instanceof Station) {
            entityType = STATION;
            this.entity = (Station) entity;
            p = ((Station) entity).getPoint();
            hasTask = ((Station) entity).getTask() != null;
        }
        else if(entity instanceof Explorer) {
            entityType = EXPLORER;
            this.entity = (Explorer) entity;
            p = null;
        }
    }

    public Entity(Object entity, long lastVisited) {
        this(entity);
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

    public Task getTask() {
        update();
        return hasTask ? ((Station) entity).getTask() : null;
    }

    public Point getPoint() {
        return p;
    }

    public int getExploringDirection() {
        if(entity instanceof Explorer)
            return ((Explorer) entity).getDirection();
        return -1;
    }

    public int getEntityType() {
        return entityType;
    }

    public Object getEntity() {
        return entity;
    }
}
