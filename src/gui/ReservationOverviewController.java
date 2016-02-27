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
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DateCell;
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
    private TableColumn<Reservation, MonthDay> tcPickUpDate;
    @FXML
    private TableColumn<Reservation, MonthDay> tcBringBackDate;
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
        tcPickUpDate.setCellValueFactory(new PropertyValueFactory<>("pickUpDate"));
        tcPickUpDate.setCellFactory(col -> new DateCell());
        tcBringBackDate.setCellValueFactory(new PropertyValueFactory<>("bringBackDate"));
        tcBringBackDate.setCellFactory(col -> new DateCell());

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
   
        tvReservations.setItems(dc.getReservations());
    

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
    

    public static class DateCell extends TableCell<Reservation, MonthDay> {
        
        private final DateTimeFormatter formatter ;
        private final DatePicker datePicker ;
        
        public DateCell() {
            
            formatter = DateTimeFormatter.ofPattern("d-MMMM-yyyy") ;
            datePicker = new DatePicker() ;
            
            // Commit edit on Enter and cancel on Escape.
            // Note that the default behavior consumes key events, so we must 
            // register this as an event filter to capture it.
            // Consequently, with Enter, the datePicker's value won't yet have been updated, 
            // so commit will sent the wrong value. So we must update it ourselves from the
            // editor's text value.
//            
//            datePicker.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
//                    datePicker.setValue(datePicker.getConverter().fromString(datePicker.getEditor().getText()));
//                    commitEdit(MonthDay.from(datePicker.getValue()));
//                }
//                if (event.getCode() == KeyCode.ESCAPE) {
//                    cancelEdit();
//                }
//            });
            
            // Modify default mouse behavior on date picker:
            // Don't hide popup on single click, just set date
            // On double-click, hide popup and commit edit for editor
            // Must consume event to prevent default hiding behavior, so
            // must update date picker value ourselves.
            
            // Modify key behavior so that enter on a selected cell commits the edit
            // on that cell's date.
            
//            datePicker.setDayCellFactory(picker -> {
//                DateCell cell = new DateCell();
//                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
//                    datePicker.setValue(cell.getItem());
//                    if (event.getClickCount() == 2) {
//                        datePicker.hide();
//                        commitEdit(MonthDay.from(cell.getItem()));
//                    }
//                    event.consume();
//                });
//                cell.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
//                    if (event.getCode() == KeyCode.ENTER) {
//                        commitEdit(MonthDay.from(datePicker.getValue()));
//                    }
//                });
//                return cell ;
//            });

//            contentDisplayProperty().bind(Bindings.when(editingProperty())
//                    .then(ContentDisplay.GRAPHIC_ONLY)
//                    .otherwise(ContentDisplay.TEXT_ONLY));
        }
        
//        @Override
//        public void updateItem(MonthDay birthday, boolean empty) {
//            super.updateItem(birthday, empty);
//            if (empty) {
//                setText(null);
//                setGraphic(null);
//            } else {
//                setText(formatter.format(birthday));
//                setGraphic(datePicker);
//            }
//        }
        
//        @Override
//        public void startEdit() {
//            super.startEdit();
//            if (!isEmpty()) {
//                datePicker.setValue(getItem().atYear(LocalDate.now().getYear()));
//            }
//        }

    }
}

