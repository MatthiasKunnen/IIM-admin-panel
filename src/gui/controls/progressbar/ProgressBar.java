package gui.controls.progressbar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.sun.javafx.tk.Toolkit;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.*;

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
        this.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">

    public void createBar(Color color) {
        createBar(color, "");
    }

    public void createBar(Color color, String text) {
        Bar newBar = new Bar(this, color, text);
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
        private Pane parent;

        public Bar(Pane parent, Color color, String text) {
            this.parent = parent;
            this.label = new Label(text);
            this.label.setTextOverrun(OverrunStyle.CLIP);
            this.getChildren().add(label);
            this.setMaxWidth(Double.MAX_VALUE);
            this.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
            this.setAlignment(Pos.CENTER);
            this.enforceWidth(0, 0);

            Platform.runLater(this::hideIfNecessary);
            this.widthProperty().addListener((observable, oldValue, newValue) -> hideIfNecessary());
            this.label.widthProperty().addListener((observable, oldValue, newValue) -> hideIfNecessary());
        }

        public void hideIfNecessary() {
            if (parent != null) {
                ObservableList<Node> nodes = parent.getChildren();
                if (Toolkit.getToolkit().getFontLoader().computeStringWidth(this.label.getText(), this.label.getFont()) < parent.getWidth() * percentage) {
                    if (!nodes.contains(this))
                        nodes.add(this);
                } else {
                    if (nodes.contains(this))
                        nodes.remove(this);
                }
            }
        }

        public void enforceWidth(double width, double percentage) {
            this.prefWidthProperty().set(width);
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
