/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.Curricular;
import domain.DomainController;
import domain.Firm;
import domain.TargetGroup;
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

    DomainController dc;

    public TabsController(DomainController dc, Stage theStage) {
        this.dc = dc;

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

        HBox overview = new HBox();
        TempController<Firm> tcFirm = createFirmOverview();
        TempController<Curricular> tcCurricular = createCurricularOverview();
        TempController<TargetGroup> tcTargetGroup = createTargetGroupOverview();

        tcCurricular.getListView().maxHeightProperty().bind(tcFirm.getListView().heightProperty());

        overview.getChildren().add(tcFirm);
        overview.getChildren().add(tcCurricular);
        overview.getChildren().add(tcTargetGroup);

        tAdd.setContent(overview);
    }

    private TempController<TargetGroup> createTargetGroupOverview() {
        TempController<TargetGroup> tcTargetGroup = new TempController<>("Doelgroepen", dc.getTargetGroups());
        CustomTextField tfName = new CustomTextField();
        tfName.setPromptText("Naam");
        Button btnAdd = new Button("Opslaan");
        Button btnDelete = new Button("Verwijder");
        Button btnCancel = new Button("Annuleer");

        btnCancel.setDisable(true);
        btnCancel.setVisible(false);
        btnDelete.setDisable(true);
        btnDelete.setVisible(false);

        tcTargetGroup.getListView().setCellFactory(param -> new ListCell<TargetGroup>() {
            @Override
            protected void updateItem(TargetGroup item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.format("%s", item.getName()));
                }
            }
        });

        tcTargetGroup.getListView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnDelete.setDisable(false);
                btnDelete.setVisible(true);

                btnCancel.setDisable(false);
                btnCancel.setVisible(true);

                tfName.setText(newValue.getName());
            }

        });

        btnDelete.setOnAction((ActionEvent event) -> {
            TargetGroup tg = tcTargetGroup.getListView().getSelectionModel().getSelectedItem();
            dc.removeTargetGroup(tg);

            tfName.clear();

            btnDelete.setDisable(true);
            btnDelete.setVisible(false);

            btnCancel.setDisable(true);
            btnCancel.setVisible(false);

            tcTargetGroup.getListView().getSelectionModel().clearSelection();
        });

        btnCancel.setOnAction((ActionEvent event) -> {
            tfName.clear();

            btnDelete.setDisable(true);
            btnDelete.setVisible(false);

            btnCancel.setDisable(true);
            btnCancel.setVisible(false);

            tcTargetGroup.getListView().getSelectionModel().clearSelection();
        });

        btnAdd.setOnAction((ActionEvent event) -> {

            if (tfName.getText().isEmpty()) {
                GuiHelper.showError(tfName, "Een Doelgroep heeft een naam nodig!");
            }
            String name = tfName.getText();

            TargetGroup tg = tcTargetGroup.getListView().getSelectionModel().isEmpty() ? new TargetGroup() : tcTargetGroup.getListView().getSelectionModel().getSelectedItem();

            tg.setName(name);

            if (tcTargetGroup.getListView().getSelectionModel().isEmpty()) {
                dc.addTargetGroup(tg);
            } else {

                dc.updateTargetGroup(tg);
            }

            tfName.clear();
        });

        tcTargetGroup.addHorizontalButton(btnCancel);
        tcTargetGroup.addHorizontalButton(btnAdd);
        tcTargetGroup.addHorizontalButton(btnDelete);

        tcTargetGroup.addVerticalNode(tfName);

        return tcTargetGroup;
    }

    private TempController<Curricular> createCurricularOverview() {
        TempController<Curricular> tcCurricular = new TempController<>("Leergebieden", dc.getCurriculars());
        CustomTextField tfName = new CustomTextField();
        tfName.setPromptText("Naam");
        Button btnAdd = new Button("Opslaan");
        Button btnDelete = new Button("Verwijder");
        Button btnCancel = new Button("Annuleer");

        btnCancel.setDisable(true);
        btnCancel.setVisible(false);
        btnDelete.setDisable(true);
        btnDelete.setVisible(false);

        tcCurricular.getListView().setCellFactory(param -> new ListCell<Curricular>() {
            @Override
            protected void updateItem(Curricular item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.format("%s", item.getName()));
                }
            }
        });

        tcCurricular.getListView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnDelete.setDisable(false);
                btnDelete.setVisible(true);

                btnCancel.setDisable(false);
                btnCancel.setVisible(true);

                tfName.setText(newValue.getName());
            }

        });

        btnDelete.setOnAction((ActionEvent event) -> {
            Curricular c = tcCurricular.getListView().getSelectionModel().getSelectedItem();
            dc.removeCurricular(c);

            tfName.clear();

            btnDelete.setDisable(true);
            btnDelete.setVisible(false);

            btnCancel.setDisable(true);
            btnCancel.setVisible(false);

            tcCurricular.getListView().getSelectionModel().clearSelection();
        });

        btnCancel.setOnAction((ActionEvent event) -> {
            tfName.clear();

            btnDelete.setDisable(true);
            btnDelete.setVisible(false);

            btnCancel.setDisable(true);
            btnCancel.setVisible(false);

            tcCurricular.getListView().getSelectionModel().clearSelection();
        });

        btnAdd.setOnAction((ActionEvent event) -> {

            if (tfName.getText().isEmpty()) {
                GuiHelper.showError(tfName, "Een leergebied heeft een naam nodig!");
            }
            String name = tfName.getText();

            Curricular c = tcCurricular.getListView().getSelectionModel().isEmpty() ? new Curricular(tfName.getText()) : tcCurricular.getListView().getSelectionModel().getSelectedItem();

            if (tcCurricular.getListView().getSelectionModel().isEmpty()) {
                dc.addCurricular(c);
            } else {
                c.setName(name);
                dc.updateCurricular(c);
            }

            tfName.clear();
        });

        tcCurricular.addHorizontalButton(btnCancel);
        tcCurricular.addHorizontalButton(btnAdd);
        tcCurricular.addHorizontalButton(btnDelete);

        tcCurricular.addVerticalNode(tfName);

        return tcCurricular;
    }

    private TempController<Firm> createFirmOverview() {
        TempController<Firm> tcFirm = new TempController<>("Firmas", dc.getFirms());
        CustomTextField tfFirmName = new CustomTextField();
        tfFirmName.setPromptText("Naam");
        CustomTextField tfFirmEmail = new CustomTextField();
        tfFirmEmail.setPromptText("E-mail");
        CustomTextField tfFirmPhone = new CustomTextField();
        tfFirmPhone.setPromptText("Telefoon nummer");
        Button btnFirmAdd = new Button("Opslaan");
        Button btnFirmDelete = new Button("Verwijder");
        Button btnFirmCancel = new Button("Annuleer");

        btnFirmCancel.setDisable(true);
        btnFirmCancel.setVisible(false);
        btnFirmDelete.setDisable(true);
        btnFirmDelete.setVisible(false);

        tcFirm.getListView().setCellFactory(param -> new ListCell<Firm>() {
            @Override
            protected void updateItem(Firm item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.format("%s (%s)", item.getName(), item.getEmail()));
                }
            }
        });

        tcFirm.getListView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnFirmDelete.setDisable(false);
                btnFirmDelete.setVisible(true);

                btnFirmCancel.setDisable(false);
                btnFirmCancel.setVisible(true);

                tfFirmName.setText(newValue.getName());
                tfFirmPhone.setText(newValue.getPhoneNumber());
                tfFirmEmail.setText(newValue.getEmail());
            }

        });

        btnFirmDelete.setOnAction((ActionEvent event) -> {
            Firm f = tcFirm.getListView().getSelectionModel().getSelectedItem();
            dc.removeFirm(f);

            tfFirmEmail.clear();
            tfFirmName.clear();
            tfFirmPhone.clear();

            btnFirmDelete.setDisable(true);
            btnFirmDelete.setVisible(false);

            btnFirmCancel.setDisable(true);
            btnFirmCancel.setVisible(false);

            tcFirm.getListView().getSelectionModel().clearSelection();
        });

        btnFirmCancel.setOnAction((ActionEvent event) -> {
            tfFirmEmail.clear();
            tfFirmName.clear();
            tfFirmPhone.clear();

            btnFirmDelete.setDisable(true);
            btnFirmDelete.setVisible(false);

            btnFirmCancel.setDisable(true);
            btnFirmCancel.setVisible(false);

            tcFirm.getListView().getSelectionModel().clearSelection();
        });

        btnFirmAdd.setOnAction((ActionEvent event) -> {

            if (tfFirmName.getText().isEmpty()) {
                GuiHelper.showError(tfFirmName, "Een bedrijf heeft een naam nodig!");
            }
            String name = tfFirmName.getText();

            String email = tfFirmEmail.getText();
            String phone = tfFirmPhone.getText();

            Firm f = tcFirm.getListView().getSelectionModel().isEmpty() ? new Firm() : tcFirm.getListView().getSelectionModel().getSelectedItem();

            try {
                f.setEmail(email);
                f.setName(name);
                f.setPhoneNumber(phone);
            } catch (InvalidPhoneNumberException ex) {
                GuiHelper.showError(tfFirmPhone, ex.getMessage());
            } catch (InvalidEmailException ex) {
                GuiHelper.showError(tfFirmEmail, ex.getMessage());
            }

            if (tcFirm.getListView().getSelectionModel().isEmpty()) {
                dc.addFirm(f);
            } else {
                dc.updateFirm(f);
            }

            tfFirmEmail.clear();
            tfFirmName.clear();
            tfFirmPhone.clear();
        });

        tcFirm.addHorizontalButton(btnFirmCancel);
        tcFirm.addHorizontalButton(btnFirmAdd);
        tcFirm.addHorizontalButton(btnFirmDelete);

        tcFirm.addVerticalNode(tfFirmName);
        tcFirm.addVerticalNode(tfFirmEmail);
        tcFirm.addVerticalNode(tfFirmPhone);

        return tcFirm;
    }

}
