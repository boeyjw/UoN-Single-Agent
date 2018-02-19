package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.MoveAction;

public class Calculation {
    public static double calculateEuclideanDistance(Coordinates source, Coordinates target, boolean doSqrt) {
        return doSqrt ? Math.sqrt(Math.pow((target.getX() - source.getX()), 2) + Math.pow((target.getY() - source.getY()), 2)) :
                Math.pow((target.getX() - source.getX()), 2) + Math.pow((target.getY() - source.getY()), 2);
    }

    public static boolean isPerfectSquare(long value) {
        long sqrt = (long) Math.sqrt(value);
        return value == sqrt * sqrt;
    }

    public static int targetBearing(Coordinates source, Coordinates target) {
        int bearing = Integer.MIN_VALUE;
        int xdiff = target.getX() - source.getX();
        int ydiff = target.getY() - source.getY();

        if(xdiff == 0 && ydiff != 0) {
            bearing = ydiff > 0 ? MoveAction.NORTH : MoveAction.SOUTH;
        }
        else if(xdiff != 0 && ydiff == 0) {
            bearing = xdiff >0 ? MoveAction.EAST : MoveAction.WEST;
        }
        else {
            if(xdiff < 0 && ydiff > 0)
                bearing = MoveAction.NORTHEAST;
            else if(xdiff > 0 && ydiff > 0)
                bearing = MoveAction.NORTHWEST;
            else if(xdiff < 0 && ydiff < 0)
                bearing = MoveAction.SOUTHEAST;
            else if(xdiff > 0 && ydiff < 0)
                bearing = MoveAction.SOUTHWEST;
        }

        return bearing;
    }

    public static String directionToString(int direction) {
        String dirStr[] = {
                "NORTH", "SOUTH", "EAST", "WEST", "NORTHEAST",
                "NORTHWEST", "SOUTHEAST", "SOUTHWEST"
        };
        return dirStr[direction];
    }

    public static long getClosestValue(long[] arr, long value) {
        int low = 0;
        int high = arr.length - 1;

        if(high < 0)
            return Integer.MIN_VALUE;

        while(low < high) {
            int mid = (low + high) / 2;
            long diff1 = Math.abs(arr[mid] - value);
            long diff2 = Math.abs(arr[mid + 1] - value);

            if(diff2 <= diff1)
                low = mid + 1;
            else
                high = mid;
        }

        return arr[high];
    }

    public static int modifiedManhattenDistance(Coordinates source, Coordinates target) {
        return Math.max(Math.abs(target.getX() - source.getX()), Math.abs(target.getY() - source.getY()));
    }

    public long[] generatePerfectSquareListFromRange(int min, int max) {
        long[] perfSqr = new long[max - min + 1];
        int c = 0;

        for(int i = min; i <= max; i++) {
            if((Math.pow(i + 1, 2) - Math.pow(i, 2)) == ((2 * i) + 1)) {
                perfSqr[c++] = i;
            }
        }

        return perfSqr;
    }
}
