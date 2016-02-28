/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import domain.Material;
import domain.Reservation;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ReservationOverviewController extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private ImageView ivAddButton;


    private DomainController dc;
    @FXML
    private TableColumn<Reservation, Integer> tcId;
    @FXML
    private TableColumn<Reservation, String> tcReservedFor;
    @FXML
    private TableColumn<Reservation, LocalDate> tcPickUpDate;
    @FXML
    private TableColumn<Reservation, LocalDate> tcBringBackDate;
    @FXML
    private TableView<Reservation> tvReservations;

    public ReservationOverviewController(DomainController dc) {
        this.dc = dc;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReservationOverview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        ivAddButton.setImage(new Image(getClass().getResource("/gui/images/material-add.png").toExternalForm()));
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcReservedFor.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        tcPickUpDate.setCellValueFactory(col->col.getValue().getPickUpDateProperty());
        tcBringBackDate.setCellValueFactory(col->col.getValue().getBringBackDateProperty());
        
        this.tvReservations.setItems(dc.getReservations());


//        tcActions.setCellFactory(new Callback<TableColumn<Material, Boolean>, TableCell<Material, Boolean>>() {
//
//            @Override
//            public TableCell<Material, Boolean> call(TableColumn<Material, Boolean> param) {
//                return new TableCell<Material, Boolean>() {
//                    private final IdentifierOptionsController ioc = new IdentifierOptionsController();
//
//                    @Override
//                    protected void updateItem(Boolean item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setGraphic(null);
//                            setText(null);
//                        } else {
//                            setGraphic(ioc);
//                            EventHandler filter = new EventHandler<MouseEvent>() {
//                                @Override
//                                public void handle(MouseEvent event) {
//                                    Material m = (Material) getTableRow().getItem();
//                                    dc.removeMaterial(m);
//                                    
//                                }
//
//                            };
//
//                            ioc.getNodeByName("delete").addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
//                        }
//
//                    }
//
//                };
//            }
//        });
               

//        tvReservations.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//            @Override
//            public void handle(MouseEvent event) {
//                if (event.getClickCount() >= 2) {
//                    Stage newStage = new Stage(StageStyle.DECORATED);
//                    Reservation theMaterial = tvReservations.getSelectionModel().getSelectedItem();
//                    //MaterialController mc = new MaterialController(dc, newStage, theMaterial);
//                    newStage.setTitle(theMaterial.getName() + " - IIM");
//                    openMaterialScreen(mc, newStage);
//                }
//            }
//
//        });
    }

    @FXML
    private void addReservation(MouseEvent event) {
        Stage newStage = new Stage(StageStyle.DECORATED);
        newStage.setTitle("Nieuw Materiaal - IIM");
        //MaterialController mc = new MaterialController(dc, newStage);

        //openMaterialScreen(mc, newStage);
    }
    


    
}

