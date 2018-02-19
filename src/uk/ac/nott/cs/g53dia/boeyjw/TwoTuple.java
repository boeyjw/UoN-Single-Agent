package uk.ac.nott.cs.g53dia.boeyjw;

public class TwoTuple implements Tuple {
    protected int x;
    protected int y;

    public TwoTuple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMin() {
        return Math.min(x, y);
    }

    public int getMax() {
        return Math.max(x, y);
    }

    @Override
    public Tuple simpleOperation(int value, Tuple otherTuple, String operation) {
        int x = Integer.MIN_VALUE;
        int y = Integer.MIN_VALUE;

        switch (operation) {
            case PLUS:
                x = this.x + (otherTuple == null ? value : otherTuple.getX());
                y = this.y + (otherTuple == null ? value : otherTuple.getY());
                break;
            case MINUS:
                x = this.x - (otherTuple == null ? value : otherTuple.getX());
                y = this.y - (otherTuple == null ? value : otherTuple.getY());
                break;
            case MULTIPLY:
                x = this.x * (otherTuple == null ? value : otherTuple.getX());
                y = this.y * (otherTuple == null ? value : otherTuple.getY());
                break;
            case DIVIDE:
                x = this.x / (otherTuple == null ? value : otherTuple.getX());
                y = this.y / (otherTuple == null ? value : otherTuple.getY());
                break;
            default:
                throw new IllegalArgumentException("Operation out of scope");
        }

        return new TwoTuple(x, y);
    }
}
