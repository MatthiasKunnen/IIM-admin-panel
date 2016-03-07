package gui;

import domain.Curricular;
import domain.DomainController;
import domain.Firm;
import domain.TargetGroup;
import exceptions.InvalidEmailException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;

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
    private Tab tReservations;

    @FXML
    private Tab tOptions;

    private DomainController dc;
    private Stage stage;

    public TabsController(DomainController dc, Stage theStage) {
        this.stage = theStage;
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
        ReservationOverviewController rc = new ReservationOverviewController(dc);
        tReservations.setContent(rc);

        HBox overview = new HBox();
        TempController<Firm> tcFirm = createFirmOverview();
        TempController<Curricular> tcCurricular = createCurricularOverview();
        TempController<TargetGroup> tcTargetGroup = createTargetGroupOverview();

        tcCurricular.getListViewMaxHeightProperty().bind(tcFirm.getListViewHeightProperty());
        tcTargetGroup.getListViewMaxHeightProperty().bind(tcFirm.getListViewHeightProperty());

        overview.getChildren().add(tcFirm);
        overview.getChildren().add(tcCurricular);
        overview.getChildren().add(tcTargetGroup);

        tAdd.setContent(overview);
    }

    private TempController<TargetGroup> createTargetGroupOverview() {
        TempController<TargetGroup> tcTargetGroup = new TempController<>(
                "Doelgroepen",
                TargetGroup::getName,
                dc.getTargetGroups());

        tcTargetGroup.addManagedCustomTextField("name", new ManagedCustomTextFieldBuilder<TargetGroup>()
                .setConverter(TargetGroup::getName)
                .setPromptText("Naam")
                .addErrorPredicate(String::isEmpty, "Naam moet ingevuld worden!")
                .addErrorPredicate(name -> !tcTargetGroup.statusIsSaving() && dc.getTargetGroups().stream().anyMatch(t -> t.getName().equalsIgnoreCase(name)), "Doelgroep bestaat al!")
                .get());

        tcTargetGroup.setOnSave((TargetGroup t) -> {
            t.setName(tcTargetGroup.getValue("name"));
            dc.updateTargetGroup(t);
            return true;
        });

        tcTargetGroup.setOnAdd(() -> {
            TargetGroup tg = new TargetGroup();
            tg.setName(tcTargetGroup.getValue("name"));
            dc.addTargetGroup(tg);
            return true;
        });

        tcTargetGroup.setOnDelete((TargetGroup tg) -> {
            dc.removeTargetGroup(tg);
            return true;
        });

        return tcTargetGroup;
    }

    private TempController<Curricular> createCurricularOverview() {
        TempController<Curricular> tcCurricular = new TempController<>(
                "Leergebieden",
                Curricular::getName,
                dc.getCurricular());

        tcCurricular.addManagedCustomTextField("name", new ManagedCustomTextFieldBuilder<Curricular>()
                .setConverter(Curricular::getName)
                .setPromptText("Naam")
                .addErrorPredicate(String::isEmpty, "Naam moet ingevuld worden!")
                .addErrorPredicate(name -> !tcCurricular.statusIsSaving() && dc.getCurricular().stream().anyMatch(c -> c.getName().equalsIgnoreCase(name)), "Leergebied bestaat al!")
                .get());

        tcCurricular.setOnSave((Curricular c) -> {
            c.setName(tcCurricular.getValue("name"));
            dc.updateCurricular(c);
            return true;
        });

        tcCurricular.setOnAdd(() -> {
            dc.addCurricular(new Curricular(tcCurricular.getValue("name")));
            return true;
        });

        tcCurricular.setOnDelete((Curricular c) -> {
            dc.removeCurricular(c);
            return true;
        });

        return tcCurricular;
    }

    private TempController<Firm> createFirmOverview() {
        TempController<Firm> tcFirm = new TempController<>("Firma's",
                (Firm f) -> String.format("%s (%s)", f.getName(), f.getEmail()),
                dc.getFirms());

        tcFirm.addManagedCustomTextField("name", new ManagedCustomTextFieldBuilder<Firm>()
                .setConverter(Firm::getName)
                .setPromptText("Naam")
                .addErrorPredicate(String::isEmpty, "Naam moet ingevuld worden!")
                .addErrorPredicate(name -> !tcFirm.statusIsSaving() && dc.getFirms().stream().anyMatch(f -> f.getName().equalsIgnoreCase(name)), "Firmanaam bestaat al!")
                .get());
        tcFirm.addManagedCustomTextField("email", new ManagedCustomTextFieldBuilder<Firm>()
                .setConverter(Firm::getEmail)
                .setPromptText("E-mail")
                .addErrorPredicate(String::isEmpty, "E-mail moet ingevuld worden!")
                .addErrorPredicate(email -> !EmailValidator.getInstance().isValid(email), "E-mail is niet in een correct formaat.")
                .addErrorPredicate(email -> !tcFirm.statusIsSaving() && dc.getFirms().stream().anyMatch(f -> f.getEmail().equalsIgnoreCase(email)), "E-mail is al geregistreerd in een andere firma!")
                .get());
        tcFirm.addManagedCustomTextField("phone_number", new ManagedCustomTextFieldBuilder<Firm>()
                .setConverter(Firm::getPhoneNumber)
                .setPromptText("Telefoonnummer")
                .addWarningPredicate(String::isEmpty, "Telefoonnummer is niet ingevuld.")
                .get());

        tcFirm.setOnAdd(() -> {
            Firm newFirm = new Firm();
            try {
                newFirm.setEmail(tcFirm.getValue("email"));
                newFirm.setName(tcFirm.getValue("name"));
                newFirm.setPhoneNumber(tcFirm.getValue("phone_number"));
                dc.addFirm(newFirm);
            } catch (InvalidEmailException ex) {
                return false;
            }
            return true;
        });

        tcFirm.setOnSave((Firm f) -> {
            f.setName(tcFirm.getValue("name"));
            try {
                f.setEmail(tcFirm.getValue("email"));
            } catch (InvalidEmailException e) {
                return false;
            }
            f.setPhoneNumber(tcFirm.getValue("phone_number"));
            dc.updateFirm(f);
            return true;
        });

        tcFirm.setOnDelete((Firm f) -> {
            dc.removeFirm(f);
            return true;
        });

        return tcFirm;
    }
}
