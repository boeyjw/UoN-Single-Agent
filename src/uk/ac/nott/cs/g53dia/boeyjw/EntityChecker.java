package uk.ac.nott.cs.g53dia.boeyjw;

import uk.ac.nott.cs.g53dia.library.*;

/**
 * Handles entity checking.
 * Utility class to avoid overcrowding of code with redundant checks
 */
public class EntityChecker {
    public static final int FUELPUMP = 0;
    public static final int WELL = 1;
    public static final int STATION = 2;
    public static final int EMPTYCELL = 4;

    /**
     * Checks if given entity is a {@link Station}
     * @param entity The cell to be checked
     * @return True if its a station, false otherwise
     */
    public static boolean isStation(Cell entity) {
        return entity instanceof Station;
    }

    /**
     * Checks if given entity is a {@link Well}
     * @param entity The cell to be checked
     * @return True if its a well, false otherwise
     */
    public static boolean isWell(Cell entity) {
        return entity instanceof Well;
    }

    /**
     * Checks if given entity is a {@link FuelPump}
     * @param entity The cell to be checked
     * @return True if its a fuel pump, false otherwise
     */
    public static boolean isFuelPump(Cell entity) {
        return entity instanceof FuelPump;
    }

    /**
     * Checks if given entity is a {@link EmptyCell}
     * @param entity The cell to be checked
     * @return True if its an empty cell, false otherwise
     */
    public static boolean isEmptyCell(Cell entity) {
        return entity instanceof EmptyCell;
    }

    public static boolean hasTaskStation(Cell station) {
        return isStation(station) && ((Station) station).getTask() != null;
    }

    /**
     * Converts entity cell into a string representation
     * @param entity The cell to be stringified, pass null if using the int constants to represent
     *               the entity type.
     * @param entityConstantType The entity constant type in int to be passed
     * @return Entity type in string
     */
    public static String entityToString(Cell entity, int entityConstantType) {
        if(isStation(entity) || entityConstantType == STATION)
            return "Station";
        else if(isWell(entity) || entityConstantType == WELL)
            return "Well";
        else if(isFuelPump(entity) || entityConstantType == FUELPUMP)
            return "Fuel Pump";
        else if(isEmptyCell(entity) || entityConstantType == EMPTYCELL)
            return "Empty Cell";
        else
            return "INVALID";
    }

    public static int getEntityType(Cell entity) {
        if(isEmptyCell(entity))
            return EntityChecker.EMPTYCELL;
        else if(isFuelPump(entity))
            return EntityChecker.FUELPUMP;
        else if(isStation(entity))
            return EntityChecker.STATION;
        else if(isWell(entity))
            return EntityChecker.WELL;
        else
            throw new IllegalArgumentException("Invalid entity type");
    }
}
