package gui.controls.calendar;

import java.time.LocalDate;

import javafx.collections.ObservableMap;
import javafx.scene.Node;

public interface CalendarAddOn {
    
    ObservableMap<LocalDate, Node> getNodes();
}
