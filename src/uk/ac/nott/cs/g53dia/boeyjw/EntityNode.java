package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.Cell;
import uk.ac.nott.cs.g53dia.library.Point;

public class EntityNode extends CoreEntity {
    private int gscore;
    private int hscore;
    private int fscore;
    private float weight;


    private int fuelConsumption;

    private EntityNode parent;

    EntityNode(Cell entity, Coordinates coord, long firstVisited, Point position) {
        super(entity, coord, firstVisited, position);
        this.weight = 1; // No weight
        this.gscore = Integer.MAX_VALUE;
        this.hscore = Integer.MAX_VALUE;
        this.fscore = Integer.MAX_VALUE;
        this.fuelConsumption = 0;
        this.parent = null;
    }

    EntityNode(Cell entity, int x, int y, long firstVisited) {
        this(entity, new Coordinates(x, y), firstVisited, null);
    }

    public int getGscore() {
        return gscore;
    }

    public void setGscore(int gscore) {
        this.gscore = gscore;
    }

    public int getHscore() {
        return hscore;
    }

    public void setHscore(int hscore) {
        this.hscore = hscore;
    }

    public int getFscore() {
        return fscore;
    }

    public void calculateFscore() {
        this.fscore = (int) Math.abs((hscore + gscore) * weight);
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public EntityNode getParent() {
        return parent;
    }

    public void setParent(EntityNode parent) {
        this.parent = parent;
    }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(int fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }
}
