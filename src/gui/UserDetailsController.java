package gui;

import domain.DomainController;
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
    private Label lblName;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblPhoneNumber;

    //</editor-fold>

    //<editor-fold desc="Declarations" defaultstate="collapsed">
    private final DomainController dc;
    private final Stage stage;
    private final User user;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public UserDetailsController(DomainController dc, Stage stage, User user) {
        this.dc = dc;
        this.stage = stage;
        this.user = user;
        try {
            GuiHelper.loadFXML("UserDetailsScene.fxml", this);
            stage.setResizable(false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        lblName.setText(String.format("%s %s",user.getFirstName(), user.getLastName()));
        lblEmail.setText(user.getEmail());
        lblPhoneNumber.setText(user.getTelNumber());
    }
    //</editor-fold>
}
