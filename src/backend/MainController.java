package backend;

import backend.entries.EntryType;

/**
 * Created by dovydas on 11/03/17.
 */
public interface MainController {
    Entry makeEntry(String input, EntryType type);
    Entry editEntry(String input, Entry entry);
}
