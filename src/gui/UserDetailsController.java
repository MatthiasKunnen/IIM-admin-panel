package gui;

import domain.User;
import gui.controls.GuiHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class UserDetailsController extends HBox {

    //<editor-fold desc="FXML Declarations" defaultstate="collapsed">
    @FXML
    private ImageView ivUserPhoto;

    @FXML
    private Label lblName, lblEmail, lblPhoneNumber;

    //</editor-fold>

    //<editor-fold desc="Declarations" defaultstate="collapsed">
    private final Stage stage;
    private final User user;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public UserDetailsController(Stage stage, User user) {
        stage.setTitle(String.format("Gebruiker: %s %s - IIM", user.getFirstName(), user.getLastName()));
        this.stage = stage;
        this.user = user;
        try {
            GuiHelper.loadFXML("UserDetailsScene.fxml", this);
            stage.setResizable(false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        lblName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
        lblEmail.setText(user.getEmail());
        lblPhoneNumber.setText(user.getTelNumber());
    }
    //</editor-fold>
}
