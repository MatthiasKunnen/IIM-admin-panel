package gui.controls.calendar;

import java.time.LocalDateTime;
import java.util.Map;
import javafx.scene.Node;

public interface CalendarAddOn {
    
    Map<LocalDateTime,Node> getNodes();
}
