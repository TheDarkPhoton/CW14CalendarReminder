package backend;

import java.util.Observable;

/**
 * Created by dovydas on 11/03/17.
 */
public class Entry extends Observable {

    private String _raw;

    public Entry(String raw) {
        _raw = raw;
    }

    @Override
    public String toString() {
        return _raw;
    }
}
