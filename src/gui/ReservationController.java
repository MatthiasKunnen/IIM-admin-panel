package gui;

import domain.*;
import gui.controls.GuiHelper;
import gui.controls.options.CustomOptionsController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationController extends VBox {

    //<editor-fold desc="FXML Variables" defaultstate="collapsed">
    @FXML
    private TextField tfEndTime;

    @FXML
    private TableColumn<Material, String> tcName;

    @FXML
    private TableColumn<Material, Boolean> tcActions;

    @FXML
    private TableView<Material> tv;

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private TextField tfStartTime;

    @FXML
    private TableColumn<Material, Integer> tcCount;

    @FXML
    private DatePicker dpEndDate;

    @FXML
    private Label lblUser;

    @FXML
    private HBox hbOptions;

    //</editor-fold>

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private DomainController dc;
    private ObservableList<MaterialIdentifier> identifiers;
    private Stage theStage;
    private Reservation reservation;
    private ObservableMap<Material, List<ReservationDetail>> simplifiedItems;

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

        lblUser.setText(String.format("%s %s", reservation.getUser().getFirstName(), reservation.getUser().getLastName()));
        lblUser.setOnMouseClicked((event) -> openUserDetails(reservation.getUser()));

        updateSimplifiedItems();

        //if (Boolean.valueOf(dc.getSettingData(Setting.Key.KEEP_HISTORY, "false"))) {
        //this.tv.setItems(FXCollections.observableList(reservation.getReservationDetails()));
        //}else{
        tcCount.setCellValueFactory(cellData -> new SimpleIntegerProperty(simplifiedItems.get(cellData.getValue()).size()).asObject());
        //}
        tcName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        tcActions.setCellFactory(new Callback<TableColumn<Material, Boolean>, TableCell<Material, Boolean>>() {

            @Override
            public TableCell<Material, Boolean> call(TableColumn<Material, Boolean> param) {
                return new TableCell<Material, Boolean>() {
                    private final CustomOptionsController coc = new CustomOptionsController();

                    {
                        coc.addExistingSVG("delete");
                        coc.bind("delete", MouseEvent.MOUSE_CLICKED, event -> {
                            if (getTableRow().getItem() != null) {
                                Material material = (Material) getTableRow().getItem();
                                reservation.getReservationDetails().removeAll(simplifiedItems.get(material));
                                simplifiedItems.get(material).clear();
                                getTableView().getItems().remove(material);
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

    public ReservationController(DomainController dc, Stage newStage, User user) {
        this(dc, newStage, new Reservation(user, LocalDateTime.now(), LocalDateTime.now().plusDays(7)));
        Button btnAddMaterials = new Button("Voeg materialen toe");
        btnAddMaterials.setOnAction(event -> {
            Stage materialPickerStage = new Stage(StageStyle.DECORATED);
            MaterialPickerController mpc = new MaterialPickerController(materialPickerStage, dc.getMaterials(), user.getType());
            materialPickerStage.setScene(new Scene(mpc));
            materialPickerStage.setTitle("Voeg nieuwe materialen toe.");
            materialPickerStage.showAndWait();
            mpc.selectedMaterials().entrySet().stream()
                    .filter(p -> p.getValue().get() > 0)
                    .forEach(p -> reservation.addAllReservationsDetails(dc.createNewReservationDetailsReservation(reservation, p.getKey(), p.getValue().get())));
            updateSimplifiedItems();
        });
        hbOptions.getChildren().add(btnAddMaterials);
    }
    //</editor-fold>

    private void updateSimplifiedItems() {
        simplifiedItems = FXCollections.observableMap(reservation.getReservationDetails().stream()
                .collect(Collectors.groupingBy(r -> r.getMaterialIdentifier().getInfo())));
        this.tv.setItems(FXCollections.observableList(simplifiedItems.keySet().stream().collect(Collectors.toList())));
    }

    private void openUserDetails(User user) {
        Stage newStage = new Stage(StageStyle.DECORATED);
        Scene scene = new Scene(new UserDetailsController(newStage, user));
        newStage.setScene(scene);
        newStage.show();
    }
}