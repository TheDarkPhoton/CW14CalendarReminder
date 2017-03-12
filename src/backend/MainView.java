package backend;

import java.util.Observer;

/**
 * Created by dovydas on 11/03/17.
 */
public interface MainView {
    void setMainController(MainController controller);
    Observer getObserver();
}
