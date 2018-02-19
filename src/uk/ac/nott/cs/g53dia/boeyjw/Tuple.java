package uk.ac.nott.cs.g53dia.boeyjw;

public interface Tuple {
    String PLUS = "+";
    String MINUS = "-";
    String MULTIPLY = "*";
    String DIVIDE = "/";

    int getX();
    int getY();
    int getMin();
    int getMax();
    Tuple simpleOperation(int value, Tuple otherTuple, String operation);
}
