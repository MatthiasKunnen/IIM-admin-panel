/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import domain.Firm;
import exceptions.InvalidEmailException;
import exceptions.InvalidPhoneNumberException;
import java.beans.EventHandler;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;

/**
 * FXML Controller class
 *
 * @author Evert
 */
public class TabsController extends TabPane {
    
    @FXML
    private Tab tAdd;
    
    @FXML
    private Tab tMaterial;
    
    @FXML
    private Tab tConflicts;
    
    @FXML
    private Tab tOptions;
    
    public TabsController(DomainController dc, Stage theStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Tabs.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
            
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        OverviewController mc = new OverviewController(dc);
        tMaterial.setContent(mc);
        
        HBox FirmOverview = new HBox();
        TempController<Firm> tcFirm = new TempController<>("Firmas", dc.getFirms());
        CustomTextField tfFirmName = new CustomTextField();
        tfFirmName.setPromptText("Naam");
        CustomTextField tfFirmEmail = new CustomTextField();
        tfFirmEmail.setPromptText("E-mail");
        CustomTextField tfFirmPhone = new CustomTextField();
        tfFirmPhone.setPromptText("Telefoon nummer");
        Button btnAdd = new Button("Opslaan");
        Button btnDelete = new Button("Verwijder");
        btnDelete.setDisable(true);
        btnDelete.setVisible(false);
        
        tcFirm.getListView().setCellFactory(param -> new ListCell<Firm>() {
            @Override
            protected void updateItem(Firm item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.format("%s (%s)", item.getName(), item.getEmail()));
                }
            }
        });
        
        tcFirm.getListView().getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)-> {
            if(newValue != null){
            btnDelete.setDisable(false);
            btnDelete.setVisible(true);
            
            tfFirmName.setText(newValue.getName());
            tfFirmPhone.setText(newValue.getPhoneNumber());
            tfFirmEmail.setText(newValue.getEmail());
            }
            
        });
        
        btnDelete.setOnAction((ActionEvent event)-> {
            Firm f =tcFirm.getListView().getSelectionModel().getSelectedItem();
            dc.removeFirm(f);
        });
        
        btnAdd.setOnAction((ActionEvent event) -> {
            String name = tfFirmName.getText();
            String email = tfFirmEmail.getText();
            String phone = tfFirmPhone.getText();
            Firm f = new Firm();
            try {
                f.setEmail(email);
                f.setName(name);
                f.setPhoneNumber(phone);
            } catch (InvalidEmailException | InvalidPhoneNumberException ex) {
            }
            
            dc.addFirm(f);
        });
        
        tcFirm.addHorizontalButton(btnAdd);
        tcFirm.addHorizontalButton(btnDelete);
        tcFirm.addVerticalNode(tfFirmName);
        tcFirm.addVerticalNode(tfFirmEmail);
        tcFirm.addVerticalNode(tfFirmPhone);
        
        
        FirmOverview.getChildren().add(tcFirm);
        tAdd.setContent(FirmOverview);
    }
    
}
