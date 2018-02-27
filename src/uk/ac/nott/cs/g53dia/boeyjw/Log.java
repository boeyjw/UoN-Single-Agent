package uk.ac.nott.cs.g53dia.boeyjw;

import java.util.List;

public class Log {
    private boolean doLog;

    Log(boolean doLog) {
        this.doLog = doLog;
    }

    Log() {
        this(false);
    }

    public void d(String p) {
        if(doLog)
            System.out.println(p);
    }

    public void dc(String p) {
        if(doLog)
            System.out.print(p);
    }
}
