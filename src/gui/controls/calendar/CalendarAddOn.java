/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controls.calendar;

import java.time.LocalDate;
import java.util.HashMap;
import javafx.scene.Node;

/**
 *
 * @author Evert
 */
public interface CalendarAddOn {
    
    public HashMap<LocalDate,Node> getNodes();
}
