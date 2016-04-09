package gui;

import domain.Material;
import domain.MaterialIdentifier;
import domain.User;
import domain.Visibility;
import gui.controls.GuiHelper;
import gui.controls.searchfield.SearchField;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MaterialPickerScene extends VBox {

    //<editor-fold desc="FXML controls" defaultstate="collapsed">
    @FXML
    private SearchField sfSearch;

    @FXML
    private TableView<Material> tvMaterialen;

    @FXML
    private TableColumn<Material, String> tcName;

    @FXML
    private TableColumn<Material, String> tcDescription;

    @FXML
    private TableColumn<Material, Integer> tcAvailable;

    @FXML
    private TableColumn<Material, Integer> tcAdd;

    //</editor-fold>

    //<editor-fold desc="Declarations" defaultstate="collapsed">
    private ObservableMap<Material, ReadOnlyIntegerWrapper> selectedMaterials;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public MaterialPickerScene(Stage stage, ObservableList<Material> materials, User.Type userType) {
        if (userType == null)
            throw new RuntimeException("UserType cannot be null");

        try {
            GuiHelper.loadFXML("MaterialPickerScene.fxml", this);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        selectedMaterials = FXCollections.observableHashMap();
        materials.addListener((ListChangeListener<Material>) c -> c.getRemoved().forEach(m -> selectedMaterials.remove(m)));
        materials.forEach(m -> selectedMaterials.put(m, new ReadOnlyIntegerWrapper(0)));

        this.tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.tcAvailable.setCellValueFactory(param -> {
            Predicate<MaterialIdentifier> filterPredicate = userType == User.Type.STUDENT ?
                    mi -> mi.getVisibility() == Visibility.Student :
                    mi -> mi.getVisibility() == Visibility.Docent || mi.getVisibility() == Visibility.Student;
            return new SimpleIntegerProperty(Math.toIntExact(param.getValue().getIdentifiers().stream()
                    .filter(filterPredicate)
                    .count())).asObject();
        });
        this.tcAdd.setCellValueFactory(param -> selectedMaterials.get(param.getValue()).asObject());
        this.tcAdd.setCellFactory(new Callback<TableColumn<Material, Integer>, TableCell<Material, Integer>>() {
            @Override
            public TableCell<Material, Integer> call(TableColumn<Material, Integer> param) {
                return new TableCell<Material, Integer>() {
                    private final HBox content;
                    private final SimpleIntegerProperty counter = new SimpleIntegerProperty(0);

                    {
                        ImageView ivUp = new ImageView(new Image(getClass().getResource("/gui/images/plus.png").toExternalForm())),
                                ivDown = new ImageView(new Image(getClass().getResource("/gui/images/min.png").toExternalForm()));

                        ivUp.setPreserveRatio(true);
                        ivDown.setPreserveRatio(true);

                        ivUp.setCursor(Cursor.HAND);
                        ivDown.setCursor(Cursor.HAND);

                        ivUp.setOnMouseClicked(event -> {
                            if (counter.get() < tcAvailable.getCellData(getIndex())) {
                                counter.setValue(counter.get() + 1);
                            }
                        });
                        ivDown.setOnMouseClicked(event -> {
                            if (counter.get() > 0)
                                counter.setValue(counter.get() - 1);
                        });

                        Label text = new Label("0");
                        text.textProperty().bind(counter.asString());
                        content = new HBox(ivDown, text, ivUp);

                        ivUp.setFitHeight(32);
                        ivDown.setFitHeight(32);

                        content.setSpacing(10.0);
                        content.setAlignment(Pos.CENTER);
                    }

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setGraphic(content);
                            if (getTableRow() != null && getTableRow().getItem() != null) {
                                counter.bindBidirectional(selectedMaterials.get((Material) getTableRow().getItem()));
                            }
                        }
                    }
                };
            }
        });

        FilteredList<Material> filteredList = new FilteredList<>(materials);
        SortedList<Material> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(this.tvMaterialen.comparatorProperty());

        sfSearch.textProperty().addListener(event ->
                filteredList.setPredicate(m ->
                        Arrays.asList(sfSearch.getText().split(" ")).stream()
                                .allMatch(s -> m.getName().contains(s) || m.getDescription().contains(s))));

        this.tvMaterialen.setItems(sortedList);
    }

    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">
    public ObservableMap<Material, ReadOnlyIntegerProperty> selectedMaterials() {
        return FXCollections.observableMap(this.selectedMaterials.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getReadOnlyProperty())));
    }
    //</editor-fold>
}