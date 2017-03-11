package backend;

import backend.entries.EntryType;

/**
 * Created by dovydas on 11/03/17.
 */
public interface MainController {

    Entry makeEntry(String input, EntryType type);
    void setMainView(MainView view);
}
