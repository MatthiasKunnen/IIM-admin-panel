package gui;

import domain.DomainController;
import domain.Reservation;
import domain.User;
import gui.controls.GuiHelper;
import gui.controls.calendar.CalendarController;
import gui.controls.calendar.ReservationAddOn;
import gui.controls.options.CustomOptionsController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


public class ReservationOverviewController extends VBox {

    //<editor-fold desc="FXML variables" defaultstate="collapsed">
    @FXML
    private CheckBox cbShowCompletedReservations;

    @FXML
    private TableView<Reservation> tvReservations;

    @FXML
    private TableColumn<Reservation, Hyperlink> tcBy;

    @FXML
    private TableColumn<Reservation, LocalDateTime> tcUntil;

    @FXML
    private TableColumn<Reservation, CheckBox> tcCompleted;

    @FXML
    private TableColumn<Reservation, Object> tcActions;
    //</editor-fold>

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private DomainController dc;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public ReservationOverviewController(DomainController dc) {
        this.dc = dc;
        try {
            GuiHelper.loadFXML("ReservationOverview.fxml", this);
            ReservationAddOn rao = new ReservationAddOn(dc.getReservations());
            CalendarController cc = new CalendarController(rao);
            getChildren().add(0, cc);
            VBox.setVgrow(cc, Priority.ALWAYS);
            cc.selectedDateProperty().addListener((observable, oldValue, newValue) -> updateReservationsList(newValue));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        tcActions.setCellFactory(new Callback<TableColumn<Reservation, Object>, TableCell<Reservation, Object>>() {
            @Override
            public TableCell<Reservation, Object> call(TableColumn<Reservation, Object> param) {
                return new TableCell<Reservation, Object>() {
                    private final CustomOptionsController coc = new CustomOptionsController();

                    {
                        coc.addExistingSVG("delete");
                        coc.addExistingSVG("list");
                        coc.bind("delete", MouseEvent.MOUSE_CLICKED, event -> {
                            if (getTableRow().getItem() != null) {
                                dc.removeReservation((Reservation) getTableRow().getItem());
                            }
                        });
                        coc.bind("list", MouseEvent.MOUSE_CLICKED, event -> {
                            if (getTableRow().getItem() != null) {
                                openReservation((Reservation) getTableRow().getItem());
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Object item, boolean empty) {
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
        tcBy.setCellValueFactory(param -> {
            User user = param.getValue().getUser();
            Hyperlink h = new Hyperlink(String.format("%s %s", user.getFirstName(), user.getLastName()));
            h.setOnAction(event -> openUserDetails(user));
            return new SimpleObjectProperty<>(h);
        });
        tcCompleted.setCellValueFactory(param -> {
            CheckBox cb = new CheckBox();
            cb.setSelected(param.getValue().isCompleted());
            return new SimpleObjectProperty<>(cb);
        });
        tcUntil.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">
    private void updateReservationsList(LocalDate date){
        tvReservations.setItems(FXCollections.observableList(dc.getReservations().stream()
                .filter(r -> (r.getEndDate().toLocalDate().isEqual(date) || r.getStartDate().toLocalDate().isEqual(date)) && (cbShowCompletedReservations.isSelected() || !r.isCompleted()))
                .collect(Collectors.toList())));
    }
    private void openReservation(Reservation selectedItem) {
        Stage newStage = new Stage(StageStyle.DECORATED);
        newStage.setTitle(String.format("%s %s - IIM", selectedItem.getUser().getEmail(), selectedItem.getStartDate().format(GuiHelper.getDateTimeFormatter())));
        Scene scene = new Scene(new ReservationController(dc, newStage, selectedItem));
        newStage.setScene(scene);
        newStage.show();
    }

    private void openUserDetails(User user){
        Stage newStage = new Stage(StageStyle.DECORATED);
        newStage.setTitle(String.format("Gebruiker: %s %s - IIM", user.getFirstName(), user.getLastName()));
        Scene scene = new Scene(new UserDetailsController(dc, newStage, user));
        newStage.setScene(scene);
        newStage.show();
    }

    //</editor-fold>
}
