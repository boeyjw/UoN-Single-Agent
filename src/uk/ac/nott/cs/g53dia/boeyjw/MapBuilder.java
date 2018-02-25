package uk.ac.nott.cs.g53dia.boeyjw;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import java.util.Deque;
import java.util.Hashtable;
import java.util.List;

public class MapBuilder extends Mapper {
    public static final int ADD = 1;
    public static final int REPLACE = 2;
    public static final int REJECTED = -1;

    private Hashtable<Integer, Hashtable<String, Deque<CoreEntity>>> globalmap;

    MapBuilder() {
        globalmap = new Hashtable<>();
    }

    public int addLocalMap(Hashtable<String, Deque<CoreEntity>> localmap) {
        int status = REJECTED;
        if(localmap.get("fuel").isEmpty() || (localmap.get("well").isEmpty() && localmap.get("station").isEmpty()))
            return status;

        status = globalmap.containsKey(localmap.get("fuel").peek().getEntityHash()) ? REPLACE : ADD;
        globalmap.put(localmap.get("fuel").peek().getEntityHash(), new Hashtable<>(localmap));

        return status;
    }
}
