import backend.Processor;
import frontend.MainWindow;

/**
 * Created by dovydas on 11/03/17.
 */
public class App {

    public static void main(String[] args) {
        new Processor(new MainWindow());
    }
}
