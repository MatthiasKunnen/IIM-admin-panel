package gui.controls.progressbar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ProgressBar extends HBox {

    //<editor-fold desc="Declarations" defaultstate="collapsed">
    private final List<Bar> bars;

    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public ProgressBar() {
        this.bars = new ArrayList<>();
        this.setAlignment(Pos.TOP_LEFT);
        this.widthProperty().addListener((observable, oldValue, newValue)
                -> bars.forEach(b -> setBar(b, newValue.doubleValue())));
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">

    public void createBar(Color color) {
        createBar(color, "");
    }

    public void createBar(Color color, String text) {
        Bar newBar = new Bar(color, text);
        bars.add(newBar);
        this.getChildren().add(newBar);
    }

    public void setBar(int barIndex, String text) {
        bars.get(barIndex).setText(text);
    }

    public void setBar(int barIndex, double percentage) {
        bars.get(barIndex).enforceWidth(getWidth() * percentage, percentage);
    }

    public void setBar(int barIndex, double percentage, String text) {
        setBar(barIndex, text);
        setBar(barIndex, percentage);
    }
    //</editor-fold>

    //<editor-fold desc="Private Actions" defaultstate="collapsed">
    private void setBar(Bar bar, double baseHeight) {
        bar.enforceWidth(baseHeight * bar.getPercentage(), bar.getPercentage());
    }
    //</editor-fold>

    //<editor-fold desc="Class Bar" defaultstate="collapsed">
    final class Bar extends HBox {

        private final Label label;
        private double percentage;

        public Bar(Color color, String text) {
            this.label = new Label(text);
            this.label.setTextOverrun(OverrunStyle.CLIP);
            this.getChildren().add(label);
            this.setMaxWidth(Double.MAX_VALUE);
            this.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
            this.setAlignment(Pos.CENTER);
            this.enforceWidth(0, 0);

            this.widthProperty().addListener((observable, oldValue, newValue) -> label.setVisible(newValue.doubleValue() > label.getWidth()));
            this.label.widthProperty().addListener((observable, oldValue, newValue) -> label.setVisible(newValue.doubleValue() < getWidth()));
            
        }

        public void enforceWidth(double width, double percentage) {
            //this.minWidthProperty().set(width);
            this.prefWidthProperty().set(width);
            //this.maxWidthProperty().set(width);
            this.percentage = percentage;
        }

        public double getPercentage() {
            return this.percentage;
        }

        public void setText(String text) {
            this.label.setText(text);
        }
    }
    //</editor-fold>
}
