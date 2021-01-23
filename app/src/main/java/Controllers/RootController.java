package Controllers;

import javafx.event.Event;
import javafx.scene.control.Button;

public class RootController {
    public Button clickMeButton;
    public void handleButtonClick(Event e) {
        System.out.println(e.getEventType());
        System.out.println("test");
    }
}
