package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.Cell;
import uk.ac.nott.cs.g53dia.library.Station;

public abstract class CoreEntity {
    private Cell entity;
    private int entityHash;
    private Coordinates coord;
    private long lastVisited;
    private boolean hasTask;

    CoreEntity(Cell entity, Coordinates coord, long firstVisit) {
        this.entity = entity;
        this.entityHash = entity.getPoint().hashCode();
        this.coord = coord;
        this.lastVisited = firstVisit;
        this.hasTask = EntityChecker.isStation(entity) && ((Station) entity).getTask() != null;
    }

    CoreEntity(Cell entity, int x, int y, long firstVisit) {
        this(entity, new Coordinates(x, y), firstVisit);
    }

    public Cell getEntity() {
        return entity;
    }

    public boolean isHasTask() {
        return hasTask;
    }

    public long getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(long thisVisit) {
        this.lastVisited = thisVisit;
    }

    public int getEntityHash() {
        return entityHash;
    }

    public Coordinates getCoord() {
        return coord;
    }
}
