package gui;
//<editor-fold defaultstate="collapsed" desc="Imports">

import domain.DomainController;
import exceptions.LoginException;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
//</editor-fold>

/**
 * FXML Controller class
 *
 * @author matthias
 */
public class LoginScreenController extends AnchorPane {

    //<editor-fold defaultstate="collapsed" desc="FXML controls">
    @FXML
    private Hyperlink urlCreateAccount;
    @FXML
    private Hyperlink urlForgotPassword;
    @FXML
    private ImageView imgThumbnail;
    @FXML
    private CustomTextField txfUsername;
    @FXML
    private CustomPasswordField txfPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblError;
    @FXML
    private Hyperlink hlLicense;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Class variables">
    private DomainController dc;
    private Timer timer;
    private int secondsRemaining;
    private Service<Void> loginService = new LoginService();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">

    public LoginScreenController(DomainController domaincontroller) {
        this.dc = domaincontroller;
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("LoginScreen.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
            this.getStylesheets().add("/gui/style/form.css");
        } catch (IOException ex) {
        }
        this.txfUsername.requestFocus();
        lblError.textProperty().bind(loginService.messageProperty());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FXML handlers">

    @FXML
    private void retrievePassword(ActionEvent event) {

    }

    @FXML
    private void textfield_KeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            login();
        }
    }

    @FXML
    private void login(ActionEvent event) {
        login();
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private actions">
    private void login() {
        if (!(loginService.isRunning() && loginService.getState() == Worker.State.FAILED)) { //True when: Service is not running and has not failed
            if (timer != null) {    //Cancel any running timer
                timer.cancel();
            }
            this.btnLogin.setDisable(true); //disable the button for as long as we're trying to login the user
            if (loginService.getState() == Worker.State.SUCCEEDED) {  //True: Service exited successfully
                loginService.reset();
                loginService.restart();
            } else {
                loginService.start();
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LoginService">

    private class LoginService extends Service<Void> {

        //Services can run tasks more than once
        @Override
        protected Task<Void> createTask() { //A task runs on another thread. This makes sure that the GUI doesn't freeze while something else is being processed.
            return new Task<Void>() {
                @Override
                public Void call() {    //Call is run on task execution.
                    this.updateMessage("Aanmelden..."); //To avoid direct communication with the JavaFX thread, updateMessage is used. In this case, updateMessage is bound to lblError.
                    String username = txfUsername.getText().trim(), password = txfPassword.getText();  //Get the username (without trailing whitespaces) and password
                    if (username.isEmpty()) { //Check if username is filled in.
                        String error = "Gebruikersnaam is niet ingevuld!";
                        this.updateMessage(error);
                        Platform.runLater(() -> GuiHelper.showError(txfUsername, error));
                    } else if (password.isEmpty()) {
                        String error = "Wachtwoord is niet ingevuld!";
                        this.updateMessage(error);
                        Platform.runLater(() -> {
                            GuiHelper.hideError(txfUsername);
                            GuiHelper.showError(txfPassword, error);
                        });
                    } else {    //The form was filled in correctly
                        Platform.runLater(() -> {
                            GuiHelper.hideError(txfUsername);
                            GuiHelper.hideError(txfPassword);
                        });
                        try {
                            if (secondsRemaining > 0) {
                                return null;
                            }
                            dc.login(username, password);
                            this.updateMessage("Data aan het inladen!");
                            Platform.runLater(() -> {
                                //Run this method when we're back on the GUI thread.
                                OverviewController chooseActionScreen = new OverviewController(dc);
                                Scene scene = new Scene(chooseActionScreen);
                                Stage currentStage = (Stage) getScene().getWindow();
                                currentStage.close();
                                currentStage.setScene(scene);
                                currentStage.setMinWidth(620);
                                currentStage.setMinHeight(463);
                                currentStage.setTitle("Overzicht - IIM");
                                currentStage.setResizable(true);
                                currentStage.show();
                            });
                        } catch (LoginException e) {
                            switch (e.getLoginFailureCause()) {
                                case CREDENTIALS_INCORRECT:
                                    updateMessage("De gebruikersnaam/wachtwoord combinatie blijkt niet te bestaan!");
                                    Platform.runLater(() -> {
                                        txfPassword.clear(); //Clear password field on an unsuccessful login.
                                    });
                                    break;
                                case ACCOUNT_SUSPENDED:
                                    updateMessage("Uw account is geschorst!");
                                    break;
                                case ACCOUNT_LOCKED:
                                    updateMessage("De beveiliging heeft teveel mislukte aanmeldingen gedecteerd en bijgevolg het aanmelden verhinderd.");
                                    timer = new Timer();
                                    timer.scheduleAtFixedRate(new TimerTask() { //We don't want the GUI to be unresponsive. -> run a new task
                                        @Override
                                        public void run() { //Runs every tick.
                                            Platform.runLater(() -> {
                                                if (secondsRemaining == 0) {
                                                    updateMessage("");
                                                    timer.cancel();
                                                    return;
                                                }
                                                updateMessage(String.format("Maximum aantal foutieve aanmeldingen overschreden, probeer opnieuw in %s second%s.", secondsRemaining, secondsRemaining == 1 ? "" : "en"));  //Let the user know that he has to wait some time
                                                secondsRemaining--;
                                            });
                                        }
                                    }, 0, 1000);    //delay: 0ms, period: 1000ms
                                    break;
                            }

                        } /*catch (Exception e) {   //Error thrown when the DB is unreachable.
                            this.updateMessage("U blijkt offline te zijn.\nGelieve uw internetconnectie na te kijken."); //Don't translate, else the user won't get the appropriate message
                            return null; //End method here to skip the clearing of the password field.}
                        }*/
                    }
                    return null;
                }
            };
        }

        @Override
        protected void succeeded() {
            btnLogin.setDisable(false);
        }
    }
    //</editor-fold>
}
