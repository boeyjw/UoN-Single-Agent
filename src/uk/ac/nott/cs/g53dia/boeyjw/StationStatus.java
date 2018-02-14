package uk.ac.nott.cs.g53dia.boeyjw;

import com.sun.istack.internal.NotNull;
import uk.ac.nott.cs.g53dia.library.Point;
import uk.ac.nott.cs.g53dia.library.Station;
import uk.ac.nott.cs.g53dia.library.Task;

public class StationStatus {
    private Station s;
    private Point p;
    private long lastVisited;
    private boolean hasPendingTask;

    public StationStatus(Station s, long lastVisited) {
        this.s = s;
        this.p = s.getPoint();
        this.lastVisited = lastVisited;
        hasPendingTask = s.getTask() != null;
    }

    public Task getTask() {
        return s.getTask();
    }

    public long getLastVisited() {
        return lastVisited;
    }

    public boolean hasTask() {
        return hasPendingTask;
    }

    public void updateStationStatus() {
        hasPendingTask = s.getTask() != null;
    }
}
