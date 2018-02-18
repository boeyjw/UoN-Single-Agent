package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.Tanker;

import java.util.ArrayDeque;
import java.util.Stack;

public class Interceptor {

    public Interceptor() {

    }

    public Stack<String> getInterceptorMoves(Tanker t, boolean hasFuelPumpInView, boolean hasWellInView) {
        Stack<String> priorityMoves = new Stack<>();
        boolean reqFuel = false;
        boolean reqWell = false;

        if(Threshold.LOWEST_FUEL.hitThresh(t.getFuelLevel()) && hasFuelPumpInView)
            priorityMoves.push("fuel");
        else
            reqFuel = true;

        if(Threshold.HIGHEST_WASTE.hitThresh(t.getWasteLevel()) && hasWellInView)
            priorityMoves.push("well");
        else
            reqWell = true;

        // @TODO: If no fuel/well in view, use history or exploration to find them

        return priorityMoves;
    }
}
