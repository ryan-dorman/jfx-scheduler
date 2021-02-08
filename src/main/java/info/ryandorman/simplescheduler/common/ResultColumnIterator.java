package info.ryandorman.simplescheduler.common;

public class ResultColumnIterator {
    private int current;

    public ResultColumnIterator(int current) {
        this.current = current;
    }

    public int next() {
        int old = current;
        current++;
        return old;
    }
}
