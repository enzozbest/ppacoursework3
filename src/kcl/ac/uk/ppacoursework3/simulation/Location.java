package src.kcl.ac.uk.ppacoursework3.simulation;

/**
 * Represent a location in a rectangular grid.
 *
 * @author David J. Barnes and Michael Kölling, Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.02.12
 */

public record Location(int row, int col) {

    /**
     * Represent a row and column.
     *
     * @param row The row.
     * @param col The column.
     */
    public Location {
    }

    /**
     * Implement content equality.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Location other) {
            return row == other.row() && col == other.col();
        } else {
            return false;
        }
    }

    /**
     * Return a string of the form row,column
     *
     * @return A string representation of the location.
     */
    public String toString() {
        return row + "," + col;
    }

    /**
     * Use the top 16 bits for the row value and the bottom for
     * the column. Except for very big grids, this should give a
     * unique hash code for each (row, col) pair.
     *
     * @return A hashcode for the location.
     */
    public int hashCode() {
        return (row << 16) + col;
    }

    /**
     * @return The row.
     */
    @Override
    public int row() {
        return row;
    }

    /**
     * @return The column.
     */
    @Override
    public int col() {
        return col;
    }
}
