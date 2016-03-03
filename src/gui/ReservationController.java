package gui;

import domain.DomainController;
import domain.Material;
import domain.MaterialIdentifier;
import domain.Reservation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class ReservationController extends AnchorPane {

    //<editor-fold desc="FXMLVariables" defaultstate="collapsed">
    @FXML
    private TextField tfEndTime;

    @FXML
    private TableColumn<MaterialIdentifier, String> tcName;

    @FXML
    private TableColumn<MaterialIdentifier, Boolean> tcActions;

    @FXML
    private TableView<MaterialIdentifier> tv;

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private TextField tfStartTime;

    @FXML
    private TableColumn<MaterialController, Integer> tcId;

    @FXML
    private TableColumn<MaterialIdentifier, String> tcLocation;

    @FXML
    private DatePicker dpEndDate;

    //</editor-fold>
    //<editor-fold desc="Variables" defaultstate="collapsed">
    private DomainController dc;
    private ObservableList<MaterialIdentifier> identifiers;
    private Stage theStage;
    private Reservation reservation;

    //</editor-fold>
    //<editor-fold desc="Constructor" defaultstate="collapsed">    
    public ReservationController(DomainController dc, Stage stage, Reservation reservation) {
        this.identifiers = FXCollections.observableArrayList();
        this.theStage = stage;
        this.dc = dc;
        this.reservation = reservation;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Reservation.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        this.tv.setItems(FXCollections.observableList(reservation.getMaterialIdentifiersList()));
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcName.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getInfo().getName()));
        tcName.setCellFactory(TextFieldTableCell.<MaterialIdentifier>forTableColumn());
        tcLocation.setCellValueFactory(new PropertyValueFactory<>("place"));
        tcLocation.setCellFactory(TextFieldTableCell.<MaterialIdentifier>forTableColumn());
        tcActions.setCellValueFactory(new PropertyValueFactory<>("NONEXISTENT"));
        tcActions.setCellFactory(new Callback<TableColumn<MaterialIdentifier, Boolean>, TableCell<MaterialIdentifier, Boolean>>() {

            @Override
            public TableCell<MaterialIdentifier, Boolean> call(TableColumn<MaterialIdentifier, Boolean> param) {
                return new TableCell<MaterialIdentifier, Boolean>() {
                    private final CustomOptionsController coc = new CustomOptionsController();

                    {
                        //coc.addExistingSVG("tick");
                        coc.addExistingSVG("delete");
                        coc.bind("delete", MouseEvent.MOUSE_CLICKED, event -> {
                            if (getTableRow().getItem() != null) {
                                getTableView().getItems().remove((MaterialIdentifier) getTableRow().getItem());
                            }
                        });
                        

                    }

                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setGraphic(coc);
                        }
                    }
                };
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="Action" defaultstate="collapsed">
    //</editor-fold>
    //<editor-fold desc="FXML actions" defaultstate="collapsed">
    //</editor-fold>
}
