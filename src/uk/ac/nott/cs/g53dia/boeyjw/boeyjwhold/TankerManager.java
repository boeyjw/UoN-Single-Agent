package uk.ac.nott.cs.g53dia.boeyjw.boeyjwhold;

import uk.ac.nott.cs.g53dia.boeyjw.Explorer;
import uk.ac.nott.cs.g53dia.library.Tanker;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class TankerManager {
    private LinkedList<Entity> moves;
    private Stack<String> interceptor;
    private Entity explorer;
    private Interceptor ic;
    private boolean explorerMode;

    public TankerManager(Random r) {
        moves = new LinkedList<>();
        explorer = new Entity(new Explorer(r));
        ic = new Interceptor();
        interceptor = new Stack<>();
        explorerMode = false;
    }

    public LinkedList<Entity> getMoves(Tanker t, Hashtable<String, Stack<Entity>> scannedView) {
        interceptor = ic.getInterceptorMoves(t, !scannedView.get("fuel").isEmpty(), !scannedView.get("well").isEmpty());
        if(!interceptor.isEmpty()) {
            if(interceptor.peek().equalsIgnoreCase("fuel") && !scannedView.get(interceptor.peek()).isEmpty()) {
                moves.add(scannedView.get(interceptor.pop()).pop());
            }
            else if(interceptor.peek().equalsIgnoreCase("well") && !scannedView.get(interceptor.peek()).isEmpty()) {
                moves.add(scannedView.get(interceptor.pop()).pop());
            }
        }
        planRoute(t, scannedView);

        if(explorerMode && moves.size() > 1) {
            moves.remove(explorer);
        }
        return moves;
    }

    private void planRoute(Tanker t, Hashtable<String, Stack<Entity>> scannedView) {
        if(!scannedView.get("station").isEmpty()) {
            for(Entity s : scannedView.get("station")) {
                if(s.checkHasTask())
                    moves.add(s);
            }
        }
        else {
            moves.add(explorer);
            explorerMode = true;
        }
    }

    public boolean requiresPlanning() {
        return moves.isEmpty();
    }
}
