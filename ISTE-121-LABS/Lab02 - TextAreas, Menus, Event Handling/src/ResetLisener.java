import javafx.event.ActionEvent;
import javafx.event.EventHandler;
/**
 * Counter 2 / part 2
 * 
 * @author Klara Makek
 * @version 30.1.2023.
 * ISTE 121 - LAB
 */
public class ResetLisener implements EventHandler<ActionEvent> {

    //parameths
    private Counter2 counter;

    // constructor
    public ResetLisener(Counter2 counter) {
        this.counter = counter;
    }

    // the action event
    @Override
    public void handle(ActionEvent event) {
        counter.doReset();

    }

}
