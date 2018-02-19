package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.Cell;
import uk.ac.nott.cs.g53dia.library.Station;

public class Entity {
    private Cell entity;
    private Coordinates coord;

    private long firstVisited;
    private long lastVisited;

    private boolean hasTask;

    public Entity(Cell entity, Coordinates coord, long firstVisited) {
        this.entity = entity;
        this.coord = coord;
        lastVisited = this.firstVisited = firstVisited;
        this.hasTask = getTaskStatus();
    }

    public Entity(Cell entity, int x, int y, long firstVisited) {
        this(entity, new Coordinates(x, y), firstVisited);
    }

    public Cell getEntity() {
        return entity;
    }

    public Coordinates getCoord() {
        return coord;
    }

    public long getFirstVisited() {
        return firstVisited;
    }

    public long getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(long lastVisited) {
        this.lastVisited = lastVisited;
    }

    public boolean getTaskStatus() {
        hasTask = entity instanceof Station && ((Station) entity).getTask() != null;
        return hasTask;
    }
}
