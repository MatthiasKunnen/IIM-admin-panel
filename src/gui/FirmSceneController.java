package gui;

import domain.DomainController;
import domain.Firm;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;

import java.io.IOException;

public class FirmSceneController extends VBox {
    //<editor-fold desc="FXML Variables" defaultstate="collapsed">

    @FXML
    private ListView<Firm> lvFirms;

    @FXML
    private CustomTextField ctfName;

    @FXML
    private CustomTextField ctfEmail;

    @FXML
    private VBox vbInput;
    //</editor-fold>

    //<editor-fold desc="Declarations" defaultstate="collapsed">
    private final DomainController dc;
    private final CustomOptionsController coc;

    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public FirmSceneController(DomainController dc, Stage stage) {
        stage.setResizable(true);
        stage.setTitle("Firma's - IIM");
        this.dc = dc;
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("FirmScene.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
            coc = new CustomOptionsController();
            coc.addImage("add", new Image(this.getClass().getResource("images/plus.png").toExternalForm()));
            coc.addExistingSVG("save", "floppy-o");
            vbInput.getChildren().add(0, coc);
        } catch (IOException ex) {
            throw new RuntimeException("Could not load FirmScene FXML", ex);
        }
        this.lvFirms.setItems(dc.getFirms());
        this.lvFirms.setCellFactory(param -> new ListCell<Firm>() {
            @Override
            protected void updateItem(Firm item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.format("%s (%s)", item.getName(), item.getEmail()));
                }
            }
        });
        this.lvFirms.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ctfName.setText(newValue.getName());
                ctfEmail.setText(newValue.getEmail());
            }
        });
        Platform.runLater(()->{
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());
        });
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">

    //</editor-fold>

    //<editor-fold desc="Private actions" defaultstate="collapsed">

    //</editor-fold>
}
