package gui.controls.calendar;

import java.time.LocalDate;
import java.util.Map;
import javafx.scene.Node;

public interface CalendarAddOn {
    
    Map<LocalDate, Node> getNodes();
}
