package main;

import domain.DomainController;
import gui.LoginScreenController;
import gui.controls.GuiHelper;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new LoginScreenController(new DomainController(), stage));
        stage.setTitle("Login - IIM");
        stage.setScene(scene);
        GuiHelper.setIcon(stage);
        stage.show();
    }

    public static void main(String... args) {
        Application.launch(Main.class, args);
    }
}