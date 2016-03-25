package gui.calendar;

import java.time.LocalDateTime;
import java.util.Map;
import javafx.scene.Node;

public interface CalendarAddOn {
    
    public Map<LocalDateTime,Node> getNodes();
}
