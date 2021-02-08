package info.ryandorman.simplescheduler.common;

/**
 * Wrapper class that keeps track of the current column being read from a result set <code>ResultSet</code>. It can be
 * used to easily map results from multiple tables into multple classes by keeping track of which columns have been read
 * by index.
 */
public class ResultColumnIterator {
    private int current;

    /**
     * Create a new class instance to read a <code>ResultSet</code> with
     * @param initialIndex The index to start from
     */
    public ResultColumnIterator(int initialIndex) {
        this.current = initialIndex;
    }

    /**
     * Easily read the current column index from the <code>ResultSet</code> and increment to the next index to be used
     * @return Current column index to be read
     */
    public int next() {
        int old = current;
        current++;
        return old;
    }
}
