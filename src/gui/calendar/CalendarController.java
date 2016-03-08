/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.calendar;

import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

/**
 * FXML Controller class
 *
 * @author Evert
 */
public class CalendarController extends VBox {

    @FXML
    private Label lblMaand;
    @FXML
    private Rectangle rectLeft;
    @FXML
    private SVGPath svgLeft;
    @FXML
    private Rectangle rectRight;
    @FXML
    private SVGPath svgRight;
    @FXML
    private GridPane gpDates;

    private LocalDate currentDate;
    private LocalDate selectedDate;

    private DatePane selectedField;

    public CalendarController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Calendar.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        svgRight.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            rectRight.setHeight(newValue.getHeight());
            rectRight.setWidth(newValue.getWidth());
        });
        svgLeft.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            rectLeft.setHeight(newValue.getHeight());
            rectLeft.setWidth(newValue.getWidth());
        });

        rectRight.setOnMouseClicked(event -> {
            selectedDate = selectedDate.plusMonths(1);
            updateDateLabel();
            setDates();
        });
        rectLeft.setOnMouseClicked(event -> {
            selectedDate = selectedDate.minusMonths(1);
            updateDateLabel();
            setDates();
        });

        rectRight.setCursor(Cursor.HAND);
        rectLeft.setCursor(Cursor.HAND);

        currentDate = LocalDate.now();
        selectedDate = LocalDate.now();
        updateDateLabel();

        initializeCalendar();
    }
    
    public LocalDate getSelectedDate(){
        return selectedField.getDate();
    }

    private void updateDateLabel() {
        lblMaand.setText(String.format("%s - %s", selectedDate.getMonth().toString(), selectedDate.getYear()));
    }

    private void initializeCalendar() {
        String[] days = new String[]{"ma", "di", "wo", "do", "vr", "za", "zo"};

        for (int x = 0; x < 7; x++) {
            Label lbl = new Label(days[x]);
            gpDates.add(lbl, x, 0);
            GridPane.setHalignment(lbl, HPos.CENTER);
            GridPane.setValignment(lbl, VPos.CENTER);
        }

        for (int y = 1; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                Label lbl = new Label("00");
                Rectangle rect = new Rectangle();

                DatePane sp = new DatePane(rect, lbl);

                rect.heightProperty().bind(sp.heightProperty());
                rect.widthProperty().bind(sp.widthProperty());
                rect.setOpacity(0);
                rect.setStroke(Color.GRAY);

                sp.setOnMouseClicked(event -> {
                    if (selectedField != null) {
                        selectedField.getrect().setOpacity(0);
                        selectedField.getLabel().setTextFill( selectedField.getDate().getMonth() != selectedDate.getMonth() ? Color.GRAY : Color.BLACK);
                    }
                    rect.setOpacity(1);
                    lbl.setTextFill(Color.WHITE);
                    selectedField = sp;
                });

                sp.getChildren().add(rect);
                sp.getChildren().add(lbl);

                gpDates.add(sp, x, y);
            }
        }

        setDates();
    }

    private void setDates() {
        LocalDate tempDate = selectedDate.minusDays(selectedDate.getDayOfMonth()).minusDays(selectedDate.getDayOfWeek().getValue() - 1);

        for (int y = 1; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                DatePane p = getNodeFromGridPane(x, y);
                p.getLabel().setText(String.format("%2d", tempDate.getDayOfMonth()));
                p.getLabel().setTextFill(tempDate.getMonth() != selectedDate.getMonth()? Color.GRAY : Color.BLACK);
                p.setDate(tempDate);
                tempDate = tempDate.plusDays(1);
            }
        }
    }

    private DatePane getNodeFromGridPane( int col, int row) {
    for (Node node : gpDates.getChildren()) {
        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
            return (DatePane)node;
        }
    }
    return null;
}
    
    class DatePane extends StackPane {

        private Rectangle rect;
        private Label lbl;
        private LocalDate date;

        public DatePane(Rectangle rect, Label lbl) {
            this.rect = rect;
            this.lbl = lbl;
        }

        public Label getLabel() {
            return this.lbl;
        }

        public Rectangle getrect() {
            return this.rect;
        }
        
        public void setDate(LocalDate date){
            this.date = date;
        }
        public LocalDate getDate() {
            return this.date;
        }
    }

}
