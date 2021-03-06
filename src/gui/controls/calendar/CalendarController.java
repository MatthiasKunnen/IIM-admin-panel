package gui.controls.calendar;

import gui.controls.GuiHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javafx.scene.layout.Priority.ALWAYS;

public class CalendarController extends VBox {

    //<editor-fold description="FXML controls" defaultstate="collapsed">
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
    //</editor-fold>

    private LocalDate currentDate;
    private LocalDate selectedMonth;
    private SimpleObjectProperty<LocalDate> selectedDate;

    private DateLayout standardLayout;
    private DateLayout currentDateLayout;
    private DateLayout otherMonthLayout;

    private static String[] DAYS = new String[]{"ma", "di", "wo", "do", "vr", "za", "zo"};
    private DatePane[][] calendar = new DatePane[7][7];

    private List<CalendarAddOn> addOns = new ArrayList<>();

    public CalendarController() {
        selectedDate = new SimpleObjectProperty<>(LocalDate.now());

        try {
            GuiHelper.loadFXML("Calendar.fxml", this);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        standardLayout = new DateLayout();
        standardLayout.setStandardLayout(Color.BLACK, null);
        standardLayout.setSelectedLayout(Color.WHITE, Color.GRAY);
        standardLayout.setHoverLayout(Color.BLACK, Color.LIGHTGRAY);

        currentDateLayout = new DateLayout();
        currentDateLayout.setStandardLayout(Color.BLACK, Color.LIGHTBLUE);
        currentDateLayout.setSelectedLayout(Color.WHITE, Color.GRAY);
        currentDateLayout.setHoverLayout(Color.BLACK, Color.LIGHTGRAY);

        otherMonthLayout = new DateLayout();
        otherMonthLayout.setStandardLayout(Color.GRAY, null);
        otherMonthLayout.setSelectedLayout(Color.WHITE, Color.GRAY);
        otherMonthLayout.setHoverLayout(Color.BLACK, Color.LIGHTGRAY);

        svgRight.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            rectRight.setHeight(newValue.getHeight());
            rectRight.setWidth(newValue.getWidth());
        });
        svgLeft.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            rectLeft.setHeight(newValue.getHeight());
            rectLeft.setWidth(newValue.getWidth());
        });

        rectRight.setOnMouseClicked(event -> {
            selectedMonth = selectedMonth.plusMonths(1);
            updateDateLabel();
            setDates();
        });
        rectLeft.setOnMouseClicked(event -> {
            selectedMonth = selectedMonth.minusMonths(1);
            updateDateLabel();
            setDates();
        });

        rectRight.setCursor(Cursor.HAND);
        rectLeft.setCursor(Cursor.HAND);

        currentDate = LocalDate.now();
        selectedMonth = LocalDate.now();
        updateDateLabel();

        initializeCalendar();
    }

    public void addAddOn(CalendarAddOn addOn) {
        this.addOns.add(addOn);
        updateAddOn(addOn);
    }

    private void updateAddOns() {
        addOns.forEach(this::updateAddOn);
    }

    public void updateAddOn(CalendarAddOn addOn) {
        if (addOns.contains(addOn)) {
            addOn.getNodes().entrySet().forEach(e -> {
                DatePane dp = findDatePane(e.getKey());
                if (dp != null) {
                    dp.addAddOnNode(e.getValue());
                }
            });
        }
    }

    public SimpleObjectProperty<LocalDate> selectedDateProperty() {
        return selectedDate;
    }

    private void initializeCalendar() {

        for (int x = 0; x < 7; x++) {
            Label lbl = new Label(DAYS[x]);
            HBox hb = new HBox(lbl);
            hb.setAlignment(Pos.CENTER);
            VBox vb = new VBox(hb);
            vb.setAlignment(Pos.CENTER);
            gpDates.add(vb, x, 0);
            GridPane.setHalignment(lbl, HPos.CENTER);
            GridPane.setValignment(lbl, VPos.CENTER);
        }

        for (int y = 1; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                DatePane sp = new DatePane();
                calendar[y][x] = sp;

                sp.setOnMouseClicked(event -> setSelection(sp.getDate()));
                sp.setOnMouseEntered(event -> {
                    if (!sp.getDate().isEqual(this.selectedDate.getValue())) {
                        sp.hover();
                    }
                });
                sp.setOnMouseExited(event -> {
                    if (!sp.getDate().isEqual(this.selectedDate.getValue())) {
                        sp.clear();
                    }
                });

                GridPane.setHgrow(sp, ALWAYS);
                GridPane.setVgrow(sp, ALWAYS);

                gpDates.add(sp, x, y);
            }
        }

        setDates();
    }

    private void updateDateLabel() {
        lblMaand.setText(String.format("%s - %s", selectedMonth.getMonth().toString(), selectedMonth.getYear()));
    }

    private void setDates() {
        LocalDate tempDate = selectedMonth.minusDays(selectedMonth.getDayOfMonth()).minusDays(selectedMonth.getDayOfWeek().getValue() + 1);

        for (int y = 1; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                DatePane p = calendar[y][x];
                p.clearAddOnNodes();
                p.getLabel().setText(String.format("%2d", tempDate.getDayOfMonth()));
                p.setLayout(tempDate.getMonth() != selectedMonth.getMonth() ? otherMonthLayout : standardLayout);
                if (tempDate.atStartOfDay().equals(currentDate.atStartOfDay())) {
                    p.setLayout(currentDateLayout);
                }
                p.clear();
                p.setDate(tempDate);
                tempDate = tempDate.plusDays(1);
            }
        }

        setSelection(this.selectedDate.getValue());
        updateAddOns();
    }

    private void setSelection(LocalDate ld) {
        DatePane newPane = findDatePane(ld);
        DatePane oldPane = findDatePane(this.selectedDate.getValue());

        if (oldPane != null) {
            oldPane.clear();
        }
        if (newPane != null) {
            newPane.select();
            this.selectedDate.setValue(newPane.getDate());
        }
    }

    private DatePane findDatePane(LocalDate ld) {
        return ld == null ? null : Arrays.stream(calendar)
                .flatMap(Arrays::stream)
                .filter(dp -> dp != null && dp.getDate().isEqual(ld))
                .findAny()
                .orElse(null);
    }

    class DatePane extends VBox {

        private Label lbl;
        private HBox hb;
        private LocalDate date;
        private List<Node> addOnNodes;

        private DateLayout dl;

        public DatePane() {
            addOnNodes = new ArrayList<>();
            lbl = new Label("00");
            hb = new HBox(lbl);
            hb.setAlignment(Pos.CENTER);
            this.getChildren().add(hb);
            this.setAlignment(Pos.CENTER);
        }

        public void select() {
            if (dl != null) {
                dl.giveSelectedLayout(this);
            }
        }

        public void clear() {
            if (dl != null) {
                dl.giveStandardLayout(this);
            }
        }

        public void hover() {
            if (dl != null) {
                dl.giveHoverLayout(this);
            }
        }

        public void setLayout(DateLayout dl) {
            this.dl = dl;
        }

        public void addAddOnNode(Node addOnNode) {
            this.addOnNodes.add(addOnNode);
            this.getChildren().add(addOnNode);
        }

        public Label getLabel() {
            return this.lbl;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public LocalDate getDate() {
            return this.date;
        }

        public void clearAddOnNodes() {
            addOnNodes.forEach(n -> getChildren().remove(n));
            addOnNodes.clear();
        }
    }

    class DateLayout {

        private Color textColor;
        private Color backgroundColor;

        private Color textColorSelected;
        private Color backgroundColorSelected;

        private Color textColorHover;
        private Color backgroundColorHover;

        public DateLayout() {
            textColor = Color.BLACK;
            textColorHover = Color.BLACK;
            textColorSelected = Color.BLACK;
        }

        public void giveStandardLayout(DatePane dp) {
            dp.getLabel().setTextFill(textColor);
            if (backgroundColor != null) {
                dp.setStyle("-fx-background-color: " + colorToHex(backgroundColor));
            } else {
                dp.setStyle("-fx-background-color: none");
            }

        }

        public void giveSelectedLayout(DatePane dp) {
            dp.getLabel().setTextFill(textColorSelected);
            if (backgroundColorSelected != null) {
                dp.setStyle("-fx-background-color: " + colorToHex(backgroundColorSelected));
            } else {
                dp.setStyle("-fx-background-color: none");
            }
        }

        public void giveHoverLayout(DatePane dp) {
            dp.getLabel().setTextFill(textColorHover);
            if (backgroundColorSelected != null) {
                dp.setStyle("-fx-background-color: " + colorToHex(backgroundColorHover));
            } else {
                dp.setStyle("-fx-background-color: none");
            }
        }

        public void setStandardLayout(Color textColor, Color backgroundColor) {
            this.textColor = textColor;
            this.backgroundColor = backgroundColor;
        }

        public void setSelectedLayout(Color textColor, Color backgroundColor) {
            this.textColorSelected = textColor;
            this.backgroundColorSelected = backgroundColor;
        }

        public void setHoverLayout(Color textColor, Color backgroundColor) {
            this.textColorHover = textColor;
            this.backgroundColorHover = backgroundColor;
        }

        private String colorToHex(Color p) {
            return String.format("#%02X%02X%02X",
                    (int) (p.getRed() * 255),
                    (int) (p.getGreen() * 255),
                    (int) (p.getBlue() * 255));
        }
    }

}
