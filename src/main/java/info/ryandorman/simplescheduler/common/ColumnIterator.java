package info.ryandorman.simplescheduler.common;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

/**
 * Keeps track of the current data being read from a <code>java.sql.ResultSet</code> or into a
 * <code>java.sql.PreparedStatement</code>.
 */
public class ColumnIterator {
    private int current;

    /**
     * Creates a new class instance
     * @param initialIndex Value to start iterating from
     */
    public ColumnIterator(int initialIndex) {
        this.current = initialIndex;
    }

    /**
     * Reads the current iterator value and increments to the next value to be used
     * @return Current value of iterator
     */
    public int next() {
        int old = current;
        current++;
        return old;
    }
}
