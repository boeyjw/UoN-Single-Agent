package uk.ac.nott.cs.g53dia.boeyjw.boeyjwhold;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates() {
        this(-1, 0-1);
    }

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void modifyCoordinates(int x, int y) {
        if(x > 0)
            this.x = x;
        if(y > 0)
            this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean isValidCoordinate() {
        return this.x > 0 && this.y > 0;
    }

    public void clearCoordinate() {
        this.x = -1;
        this.y = -1;
    }
}
