package gui;

import domain.DomainController;
import domain.MaterialIdentifier;
import domain.Reservation;
import domain.ReservationDetail;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class ReservationController extends AnchorPane {

    //<editor-fold desc="FXML Variables" defaultstate="collapsed">
    @FXML
    private TextField tfEndTime;

    @FXML
    private TableColumn<ReservationDetail, String> tcName;

    @FXML
    private TableColumn<ReservationDetail, Boolean> tcActions;

    @FXML
    private TableView<ReservationDetail> tv;

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private TextField tfStartTime;

    @FXML
    private TableColumn<ReservationDetail, Integer> tcId;

    @FXML
    private TableColumn<ReservationDetail, String> tcLocation;

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

        try {
            GuiHelper.loadFXML("Reservation.fxml", this);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        dpStartDate.setValue(reservation.getStartDate().toLocalDate());
        dpEndDate.setValue(reservation.getEndDate().toLocalDate());
        tfStartTime.setText(reservation.getStartDate().toLocalTime().format(GuiHelper.getTimeFormatter()));
        tfEndTime.setText(reservation.getEndDate().toLocalTime().format(GuiHelper.getTimeFormatter()));

        this.tv.setItems(FXCollections.observableList(reservation.getReservationDetails()));
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMaterialIdentifier().getInfo().getName()));
        tcName.setCellFactory(TextFieldTableCell.<ReservationDetail>forTableColumn());
        tcLocation.setCellValueFactory(cellData -> cellData.getValue().getMaterialIdentifier().getPlaceProperty());
        tcLocation.setCellFactory(TextFieldTableCell.<ReservationDetail>forTableColumn());
        tcActions.setCellFactory(new Callback<TableColumn<ReservationDetail, Boolean>, TableCell<ReservationDetail, Boolean>>() {

            @Override
            public TableCell<ReservationDetail, Boolean> call(TableColumn<ReservationDetail, Boolean> param) {
                return new TableCell<ReservationDetail, Boolean>() {
                    private final CustomOptionsController coc = new CustomOptionsController();

                    {
                        //coc.addExistingSVG("tick");
                        coc.addExistingSVG("delete");
                        coc.bind("delete", MouseEvent.MOUSE_CLICKED, event -> {
                            if (getTableRow().getItem() != null) {
                                getTableView().getItems().remove((ReservationDetail) getTableRow().getItem());
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

    //<editor-fold desc="FXML actions" defaultstate="collapsed">
    //</editor-fold>
}
