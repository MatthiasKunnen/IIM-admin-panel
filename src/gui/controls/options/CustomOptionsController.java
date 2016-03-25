package gui.controls.options;

import exceptions.NodeAlreadyExistsException;
import gui.GuiHelper;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CustomOptionsController extends HBox {
    private Map<String, Node> nodes;
    private DoubleProperty iconHeight;
    private Set<SVGPath> svgPaths;

    public CustomOptionsController() {
        this.nodes = new TreeMap<>();
        this.svgPaths = new HashSet<>();
        try {
            GuiHelper.loadFXML("CustomOption.fxml", this);
            iconHeight = prefHeightProperty();
            iconHeight.multiply(0.95);
            heightProperty().addListener((observable, oldValue, newValue) -> {
                svgPaths.forEach(svg->fixSVGScaling(svg, (Pane) svg.getParent()));
            });
        } catch (IOException ex) {
            throw new RuntimeException("Cannot load CustomOptionsController", ex);
        }
    }

    public void addNode(String name, Node node) {
        if (nodes.containsKey(name))
            throw new NodeAlreadyExistsException(String.format("Node with name '%s' already exists!", name));
        this.getChildren().add(node);
        this.nodes.put(name, node);
        node.setCursor(Cursor.HAND);
    }

    public void addSVGPath(String name, SVGPath svg) {
        StackPane sp = new StackPane();
        //svg.scaleYProperty().bind(iconHeight.divide(100));
        //svg.scaleXProperty().bind(iconHeight.divide(100));
        svgPaths.add(svg);
        fixSVGScaling(svg, sp);
        sp.getChildren().add(svg);
        addNode(name, sp);
    }

    public void addSVGPath(String name, String content) {
        SVGPath svg = new SVGPath();
        svg.setContent(content);
        addSVGPath(name, svg);
    }

    public void addImage(String name, Image image) {
        StackPane sp = new StackPane();
        ImageView iv = new ImageView(image);
        iv.setPreserveRatio(true);
        sp.getChildren().add(iv);
        addNode(name, sp);
        iv.fitHeightProperty().bind(iconHeight);
    }

    public void addExistingSVG(String key) {
        addExistingSVG(key, key);
    }

    public void addExistingSVG(String name, String key) {
        addSVGPath(name, GuiHelper.getSVGContent(key));
    }

    public void bind(String name, EventType event, EventHandler handler) {
        nodes.get(name).addEventHandler(event, handler);
    }

    private void fixSVGScaling(SVGPath svg, Pane pane){
        Platform.runLater(() -> {
            double svgH = svg.getBoundsInParent().getHeight(),
                    spH = pane.getBoundsInParent().getHeight(),
                    diffH = spH / svgH,
                    spW = pane.getBoundsInParent().getWidth(),
                    diffW = (spW * diffH - spW) / 2;
            svg.setScaleX(diffH);
            svg.setScaleY(diffH);
            Insets oldInsets = pane.getInsets();
            if (oldInsets == null) {
                setMargin(pane, new Insets(0, diffW, 0, diffW));
            } else {
                setMargin(pane, new Insets(oldInsets.getTop(), oldInsets.getRight() + diffW, oldInsets.getBottom(), oldInsets.getLeft() + diffW));
            }
        });
    }
}