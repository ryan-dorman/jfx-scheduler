package info.ryandorman.simplescheduler.common;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

/**
 * Helps keep track of the current column being read from a result set <code>ResultSet</code> or into a <code>Prepared
 * Statement</code>.
 */
public class ColumnIterator {
    private int current;

    /**
     * Create a new class instance
     * @param initialIndex Value to start iterating from
     */
    public ColumnIterator(int initialIndex) {
        this.current = initialIndex;
    }

    /**
     * Easily read the current iterator value and increment to the next value to be used
     * @return Current value of iterator
     */
    public int next() {
        int old = current;
        current++;
        return old;
    }
}
