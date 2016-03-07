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
                .addErrorPredicate(name -> !name.isEmpty(), "Naam moet ingevuld worden!")
                .get());

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
                .addErrorPredicate(name -> !name.isEmpty(), "Naam moet ingevuld worden!")
                .get());

        return tcCurricular;
    }

    private TempController<Firm> createFirmOverview() {
        TempController<Firm> tcFirm = new TempController<>("Firma's",
                (Firm f) -> String.format("%s (%s)", f.getName(), f.getEmail()),
                dc.getFirms());

        tcFirm.addManagedCustomTextField("name", new ManagedCustomTextFieldBuilder<Firm>()
                .setConverter(Firm::getName)
                .setPromptText("Naam")
                .addErrorPredicate(name -> !name.isEmpty(), "Naam moet ingevuld worden!")
                .addErrorPredicate(name -> dc.getFirms().stream().anyMatch(f -> f.getName().equalsIgnoreCase(name)), "Firmanaam bestaat al!")
                .get());
        tcFirm.addManagedCustomTextField("email", new ManagedCustomTextFieldBuilder<Firm>()
                .setConverter(Firm::getEmail)
                .setPromptText("E-mail")
                .addErrorPredicate(email -> !email.isEmpty(), "E-mail moet ingevuld worden!")
                .addErrorPredicate(email -> EmailValidator.getInstance().isValid(email), "E-mail is niet in een correct formaat.")
                .get());
        tcFirm.addManagedCustomTextField("phone_number", new ManagedCustomTextFieldBuilder<Firm>()
                .setConverter(Firm::getPhoneNumber)
                .setPromptText("Telefoonnummer")
                .addWarningPredicate(phoneNumber -> !phoneNumber.isEmpty(), "Telefoonnummer is niet ingevuld.")
                .get());

        tcFirm.setOnAdd(() -> {
            boolean willSave = true;
            String name = tcFirm.getValue("name");
            String email = tcFirm.getValue("email");
            String phone = tcFirm.getValue("phone_number");

            Firm newFirm = new Firm();
            try {
                newFirm.setEmail(email);
                newFirm.setName(name);
                newFirm.setPhoneNumber(phone);
            } catch (InvalidEmailException ex) {
                willSave = false;
            }

            if (willSave) {
                dc.addFirm(newFirm);
            }
            
            return willSave;
        });

        return tcFirm;
    }
}
