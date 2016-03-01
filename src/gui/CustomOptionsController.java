package gui;

import exceptions.NodeAlreadyExistsException;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class CustomOptionsController extends HBox {
    private Map<String, Node> nodes;

    public CustomOptionsController() {
        this.nodes = new TreeMap<>();
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("CustomOption.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
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
        svg.prefWidth(getNodeHeight());
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
        iv.fitHeightProperty().setValue(getNodeHeight());
        sp.getChildren().add(iv);
        addNode(name, sp);
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

    private double getNodeHeight() {
        return this.getHeight() * 0.95;
    }
}