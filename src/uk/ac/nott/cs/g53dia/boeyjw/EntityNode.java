package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.Cell;

public class EntityNode extends CoreEntity {
    private int gscore;
    private int hscore;
    private int fscore;
    private float weight;


    private EntityNode parent;

    EntityNode(Cell entity, Coordinates coord, long firstVisited) {
        super(entity, coord, firstVisited);
        this.weight = 1; // No weight
        this.gscore = Integer.MAX_VALUE;
        this.hscore = Integer.MAX_VALUE;
        this.fscore = Integer.MAX_VALUE;
        this.parent = null;
    }

    EntityNode(Cell entity, int x, int y, long firstVisited) {
        this(entity, new Coordinates(x, y), firstVisited);
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
}
